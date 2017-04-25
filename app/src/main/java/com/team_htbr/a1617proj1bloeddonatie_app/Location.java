package com.team_htbr.a1617proj1bloeddonatie_app;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by bjorn on 22-4-2017.
 */

public class Location {

	private String id;
	private String title;
	private String address;
	private Boolean isMoblie;
	private double lat;
	private double lng;

	public Location() {

	}

	public Location(String id, String title, String address, Boolean isMoblie, double LAT, double LNG) {
		this.id = id;
		this.title =title;
		this.address = address;
		this.isMoblie = isMoblie;
		this.lat = LAT;
		this.lng = LNG;
	}

	public String getTitle() {
		return title;
	}

	public String getAddress() {
		return address;
	}

	public LatLng getCoordinates() {
		LatLng coordinate = new LatLng(lat, lng);
		return coordinate;
	}
}
