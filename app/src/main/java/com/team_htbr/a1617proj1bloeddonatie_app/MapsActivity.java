package com.team_htbr.a1617proj1bloeddonatie_app;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private HashMap<String, Marker> markers;
	public static final String TAG = "MapsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);


	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {

		mMap = googleMap;
		markers = new HashMap<String, Marker>();

		// Add a marker in Brussel and move the camera
		LatLng currentLocation = MainActivity.getMyLocation();
		LatLng brussel = new LatLng(51.045810, 3.714156);
		if (currentLocation == null) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(brussel, 9));
		} else {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
		}


		//enable search my location
		if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
			mMap.setMyLocationEnabled(true);
		}

		//get database from firebase
		DatabaseReference fireBaseDataBase = FirebaseDatabase.getInstance().getReference();
		DatabaseReference markersDataBase = fireBaseDataBase.child("locations_test");

		//handels changes in the database
		markersDataBase.addChildEventListener(new ChildEventListener() {
			@Override
			//adds new markers
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Location addedLocation = dataSnapshot.getValue(Location.class);
				Marker addedMarker = addMarker(addedLocation);
				markers.put(dataSnapshot.getKey(), addedMarker);
			}

			@Override
			//replaces markers who have changed in the database
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				if (markers.containsKey(dataSnapshot.getKey())) {
					Marker oldMarker = markers.get(dataSnapshot.getKey());
					oldMarker.remove();
				}
				Location changedLocation = dataSnapshot.getValue(Location.class);
				Marker changedMarker = addMarker(changedLocation);
				markers.put(dataSnapshot.getKey(), changedMarker);
			}

			@Override
			//remove markers that have been removed in database
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				if (markers.containsKey(dataSnapshot.getKey())) {
					Marker deletedMarker = markers.get(dataSnapshot.getKey());
					deletedMarker.remove();
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Log.d(TAG, "codency fix");
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Toast.makeText(MapsActivity.this, "database failed", Toast.LENGTH_SHORT);
			}
		});

		//move camera to marker on click
		mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
				return false;
			}
		});

		//creating custom info window
		mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				View v = getLayoutInflater().inflate(R.layout.info_window, null);

				TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
				TextView tvAddress = (TextView) v.findViewById(R.id.tv_address);

				tvTitle.setText(marker.getTitle());
				tvAddress.setText(marker.getSnippet());

				return v;
			}
		});
	}

	private Marker addMarker(Location location) {
		Marker marker = mMap.addMarker(location.getMarker());
		return marker;
	}
}
