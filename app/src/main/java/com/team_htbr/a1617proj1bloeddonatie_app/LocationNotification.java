package com.team_htbr.a1617proj1bloeddonatie_app;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by bjorn on 3-5-2017.
 */

public class LocationNotification {

	public void getLocations() {
		LatLng userLocatie = new LatLng(51.046414, 3.714425);

		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bloeddonatie-bd78c/locations_geo_test");
		GeoFire geoFire = new GeoFire(ref);

		GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(userLocatie.latitude, userLocatie.longitude), 2);

		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
			@Override
			public void onKeyEntered(String s, GeoLocation geoLocation) {
				
			}

			@Override
			public void onKeyExited(String s) {

			}

			@Override
			public void onKeyMoved(String s, GeoLocation geoLocation) {

			}

			@Override
			public void onGeoQueryReady() {

			}

			@Override
			public void onGeoQueryError(DatabaseError databaseError) {

			}
		});
	}
}
