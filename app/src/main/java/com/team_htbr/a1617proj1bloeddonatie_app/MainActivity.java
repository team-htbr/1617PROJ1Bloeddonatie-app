package com.team_htbr.a1617proj1bloeddonatie_app;

import android.content.Intent;
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
		// Subscribing to a blood type
		//FirebaseMessaging.getInstance().subscribeToTopic("blood-AB");
		// Uncomment this line to effectively unsubscribe from topic
		// FirebaseMessaging.getInstance().unsubscribeFromTopic("blood-AB");

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

		LatLng userLocatie = new LatLng(51.046414, 3.714425);

		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bloeddonatie-bd78c").child("locations_geo_test");

		GeoFire geoFire = new GeoFire(ref);
		GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(userLocatie.latitude, userLocatie.longitude), 100);
		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
			@Override
			public void onKeyEntered(String s, GeoLocation geoLocation) {
				Toast.makeText(MainActivity.this, "onKeyEntered", Toast.LENGTH_SHORT).show();
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
}
