package com.team_htbr.a1617proj1bloeddonatie_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	//LocationNotification ln = new LocationNotification();
	private Location userLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Rode Kruis");

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

		

		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocationListener ln = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				Toast.makeText(MainActivity.this, Double.toString(location.getLatitude()), Toast.LENGTH_SHORT).show();
				getLocation(location);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		} else {
			lm.requestLocationUpdates("gps", 5000, 0, ln);
		}
	}

	private void createNotification() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);
		//Set sound of notification
		Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.mipmap.ic_launcher)
			.setContentTitle("Bloeddonatie")
			.setContentText("random message")
			.setAutoCancel(true)
			.setSound(notificationSound)
			.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0 /*ID of notification*/, notifiBuilder.build());
	}

	private void getLocation(Location location) {
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("locations_geo_test");

		GeoFire geoFire = new GeoFire(ref);
		GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 2);
		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
			@Override
			public void onKeyEntered(String s, GeoLocation geoLocation) {

				createNotification();
			}

			@Override
			public void onKeyExited(String s) {
				Toast.makeText(MainActivity.this, "onKeyExited", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onKeyMoved(String s, GeoLocation geoLocation) {
				Toast.makeText(MainActivity.this, "onKeyMoved", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onGeoQueryReady() {
				Toast.makeText(MainActivity.this, "onGeoQueryReady", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onGeoQueryError(DatabaseError databaseError) {
				Toast.makeText(MainActivity.this, "onGeoQueryError", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void testing() {

	}
}
