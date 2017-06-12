package com.team_htbr.a1617proj1bloeddonatie_app;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	public static final String TAG = "MainActivity";
	public static Location currentLocation;
	private ActionBarDrawerToggle mToggle;


	private GoogleApiClient googleApiClient = null;
	private List<com.team_htbr.a1617proj1bloeddonatie_app.Location> locationsList;
	private List<Geofence> geofences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Bloeddonatie");

		DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

		mDrawerLayout.addDrawerListener(mToggle);
		mToggle.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		geofences = new ArrayList<>();
		locationsList = new ArrayList<>();


		requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 1234);

		connectToGoogleApi();

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
	}

	private void checkDataBase() {
		DatabaseReference fireBaseDataBase = FirebaseDatabase.getInstance().getReference();
		DatabaseReference locationsDataBase = fireBaseDataBase.child("locations");

		locationsDataBase.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
					locationsList.add(dataSnapshot.getValue(com.team_htbr.a1617proj1bloeddonatie_app.Location.class));
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

		Button btnMaps = (Button) findViewById(R.id.GoogleMap);
		btnMaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, MapsActivity.class));
			}
		});

		Button btnDonorTest = (Button) findViewById(R.id.donorTest);
		btnDonorTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, DonorTestActivity.class));
			}
		});


		Button btnBloodtype = (Button) findViewById(R.id.Bloodtype);
		btnBloodtype.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(MainActivity.this, SubscribeBloodtypeActivity.class));
			}
		});

		connectToGoogleApi();

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startLocationMoitoring();
			}
		}, 5000);
	}

	public void redirectMaps(MenuItem item){
		startActivity(new Intent(MainActivity.this, MapsActivity.class));
	}

	public void redirectDonorTest(MenuItem item){
		startActivity(new Intent(MainActivity.this, DonorTestActivity.class));
	}

	public void redirectBloodType(MenuItem item){
		startActivity(new Intent(MainActivity.this, SubscribeBloodtypeActivity.class));
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
		if (googleApiClient.isConnected()) {
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
	}

	public void startGeofenceMonitoring() {
		if (googleApiClient.isConnected()) {
			for (com.team_htbr.a1617proj1bloeddonatie_app.Location location: locationsList) {
				geofences.add(new Geofence.Builder()
					.setRequestId(location.getName())
					.setCircularRegion(location.getLat(), location.getLng(), 1000)
					.setExpirationDuration(Geofence.NEVER_EXPIRE)
					.setNotificationResponsiveness(5000)
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
	}

	public static LatLng getMyLocation() {
		if (currentLocation == null){
			return null;
		}
		else return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		/*int id = item.getItemId();

		if (id == R.id.nav_bloedtype) {
			Intent intent1 = new Intent(this,SubscribeBloodtypeActivity.class);
			this.startActivity(intent1);
			return true;

		}/* else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}*/

		if(mToggle.onOptionsItemSelected(item)){
			return true;
		}
		return super.onOptionsItemSelected(item);
  }

	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1234: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					startLocationMoitoring();
					checkDataBase();
				} else {
					return;
				}
			}
			default: {
				return;
			}
		}
	}
}


