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
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ResultCallback<Status> {

	public static final String TAG = "MainActivity";
	public static final int REACH = 1000;

	GoogleApiClient googleApiClient = null;
	PendingIntent pendingIntent = null;
	ArrayList<Geofence> geofences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Rode Kruis");

		geofences = new ArrayList<>();

		Button btnShowToken = (Button) findViewById(R.id.button_show_token);
		btnShowToken.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Get the token
				String token = FirebaseInstanceId.getInstance().getToken();
				Log.d(TAG, "Token: " + token);
				Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
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

		DatabaseReference fireBaseDataBase = FirebaseDatabase.getInstance().getReference();
		DatabaseReference locationsDataBase = fireBaseDataBase.child("locations_test");

		locationsDataBase.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
					geofences.add(new Geofence.Builder()
						.setRequestId(snapshot.child("name").getValue().toString())
						.setCircularRegion(
							(double) snapshot.child("lat").getValue(),
							(double) snapshot.child("lng").getValue(),
							REACH
						)
						.setExpirationDuration(Geofence.NEVER_EXPIRE)
						.setNotificationResponsiveness(1000)
						.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
						.build());
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.d(TAG, "database error - 0" + databaseError.getMessage());
			}
		});

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		LocationServices.GeofencingApi.addGeofences(
			googleApiClient,
			getGeofencingRequest(),
			getGeofencePendingIntent()
		).setResultCallback(this);
	}

	protected void onStart() {
		googleApiClient.connect();
		super.onStart();
	}

	protected void onStop() {
		googleApiClient.disconnect();
		super.onStop();
	}

	// old


	// new

	@NonNull
	private GeofencingRequest getGeofencingRequest() {
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		builder.addGeofences(geofences);
		return builder.build();
	}

	private PendingIntent getGeofencePendingIntent() {
		// Reuse the PendingIntent if we already have it.
		if ( pendingIntent != null) {
			return pendingIntent;
		}
		Intent intent = new Intent(this, GeofenceService.class);
		// We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
		// calling addGeofences() and removeGeofences().
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private void stopGeo() {
		LocationServices.GeofencingApi.removeGeofences(
			googleApiClient,
			// This is the same pending intent that was used in addGeofences().
			getGeofencePendingIntent()
		).setResultCallback( this); // Result processed in onResult().
	}

	@Override
	public void onResult(@NonNull Status status) {
		if (status.isSuccess()) {
			Log.d(TAG, "succecfully added gefence");
		} else {
			Log.d(TAG, "Failed to add geofence + " + status.getStatus());
		}
	}
}


