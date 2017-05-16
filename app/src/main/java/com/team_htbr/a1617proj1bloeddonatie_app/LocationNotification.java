//package com.team_htbr.a1617proj1bloeddonatie_app;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.*;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//
///**
// * Created by bjorn on 9-5-2017.
// */
//
//public class LocationNotification {
//
//	public void checkLocations() {
//		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//			// TODO: Consider calling
//			//    ActivityCompat#requestPermissions
//			// here to request the missing permissions, and then overriding
//			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//			//                                          int[] grantResults)
//			// to handle the case where the user grants the permission. See the documentation
//			// for ActivityCompat#requestPermissions for more details.
//			locationManager.requestLocationUpdates("gps", 5000, 0, this);
//		}
//
//	}
//
//	@Override
//	public void onLocationChanged(Location location) {
//
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//
//	}
//
//	@Override
//	public void onProviderDisabled(String provider) {
//
//	}
//
//	@Nullable
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//
//}
