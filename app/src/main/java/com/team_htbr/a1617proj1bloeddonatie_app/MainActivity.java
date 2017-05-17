package com.team_htbr.a1617proj1bloeddonatie_app;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity  {

	public static final String TAG = "MainActivity";
	public static final int REACH = 1000;
	public static Location currentLocation;

	private GoogleApiClient googleApiClient = null;
	private HashMap<String, com.team_htbr.a1617proj1bloeddonatie_app.Location> locationsList;
	private ArrayList<Geofence> geofences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Rode Kruis");
		geofences = new ArrayList<>();
		locationsList = new HashMap<>();

		DatabaseReference fireBaseDataBase = FirebaseDatabase.getInstance().getReference();
		DatabaseReference locationsDataBase = fireBaseDataBase.child("locations_test");

		locationsDataBase.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				googleApiClient.connect();
				locationsList.put(s, dataSnapshot.getValue(com.team_htbr.a1617proj1bloeddonatie_app.Location.class));
				startLocationMoitoring();
				startGeofenceMonitoring();
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Log.d(TAG, "bla bla");
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Log.d(TAG, "bla bla");
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Log.d(TAG, "bla bla");
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.d(TAG, "bla bla");
			}
		});

		Button btnBloodtype = (Button) findViewById(R.id.Bloodtype);
		btnBloodtype.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, SubscribeBloodtypeActivity.class));
			}
		});

		Button btnDonorTest = (Button) findViewById(R.id.donorTest);
		btnDonorTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, DonorTestActivity.class));
			}
		});

		Button btnMaps = (Button) findViewById(R.id.GoogleMap);
		btnMaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, MapsActivity.class));
			}
		});
		connectToGoogleApi();

	}

	private void connectToGoogleApi() {
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
					@Override
					public void onConnected(@Nullable Bundle bundle) {
						Log.d(TAG, "connected to googleapiclient");
					}

					@Override
					public void onConnectionSuspended(int i) {
						Log.d(TAG, "suspended connection to googleapiclient");
					}
				})
				.addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(@NonNull ConnectionResult result) {
						Log.d(TAG, "failed to cennect - " + result.getErrorMessage());
					}
				})
				.addApi(LocationServices.API)
				.build();
		}
	}

	protected void onStart() {
		super.onStart();
		googleApiClient.reconnect();
	}

	protected void onStop() {
		super.onStop();
		googleApiClient.disconnect();
	}

	private void startLocationMoitoring() {
		LocationRequest locationRequest = LocationRequest.create()
			.setInterval(10000)
			.setFastestInterval(5000)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				Log.d(TAG, "location update");
				currentLocation = location;
			}
		});
	}

	public void startGeofenceMonitoring() {

		for (Map.Entry<String, com.team_htbr.a1617proj1bloeddonatie_app.Location> entry : locationsList.entrySet()) {
			geofences.add(new Geofence.Builder()
				.setRequestId(entry.getValue().getName())
				.setCircularRegion(entry.getValue().getLat(), entry.getValue().getLng(), 20000)
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				.setNotificationResponsiveness(1000)
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
				.build());
		}

		GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
			.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
			.addGeofences(geofences).build();


		Intent intent = new Intent(this, GeofenceService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		if (!googleApiClient.isConnected()) {
			Log.d(TAG, "no connection");
		} else {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			LocationServices.GeofencingApi.addGeofences(googleApiClient, geofencingRequest, pendingIntent)
				.setResultCallback(new ResultCallback<Status>() {
					@Override
					public void onResult(@NonNull Status status) {
						if (status.isSuccess()) {
							Log.d(TAG, "succesful add");
						} else {
							Log.d(TAG, "Failed to add");
						}
					}
				});
		}
	}

//	private void stopGeo() {
//		LocationServices.GeofencingApi.removeGeofences(
//			googleApiClient,
//			// This is the same pending intent that was used in addGeofences().
//			getGeofencePendingIntent()
//		).setResultCallback( this); // Result processed in onResult().
//	}

	public static LatLng getMyLocation() {
		if (currentLocation == null){
			return null;
		}
		else return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
	}
}


