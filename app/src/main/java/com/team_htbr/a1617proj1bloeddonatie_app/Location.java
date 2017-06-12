package com.team_htbr.a1617proj1bloeddonatie_app;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by bjorn on 22-4-2017.
 */

public class Location {

	private String id;
	private String name;
	private String street;
	private String streetNumber;
	private String city;
	private Boolean isMobile;
	private double lat;
	private double lng;
	private String startDate;
	private String endDate;
	private MarkerOptions marker;

	public Location(String id, String name, String street, String streetNumber, String city, Boolean isMobile, double lat, double lng, String startDate, String endDate) {
		this.id = id;
		this.name = name;
		this.street = street;
		this.streetNumber = streetNumber;
		this.city = city;
		this.isMobile = isMobile;
		this.lat = lat;
		this.lng = lng;
		this.startDate = startDate;
		this.endDate = endDate;

		this.marker = new MarkerOptions();
	}

	public Location() {
		this.marker = new MarkerOptions();
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setIsMobile(String isMobile) {
		this.isMobile = Boolean.parseBoolean(isMobile);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setMarker(MarkerOptions marker) {
		this.marker = marker;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getId() {

		return id;
	}

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public String getCity() {
		return city;
	}

	public Boolean getIsMobile() {
		return isMobile;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public LatLng getCoordinates() {
		return new LatLng(lat, lng);
	}

	public String getAddress() {
		return street + " " + streetNumber + ", " + city;
	}

	public String getDates() {
		return "\n" + startDate + " - " + endDate;
	}

	public MarkerOptions getMarker() {
		BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_red);
		BitmapDescriptor markerIconMobile = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_mobile_red);

		this.marker.position(this.getCoordinates());
		this.marker.title(this.name);
		this.marker.snippet(this.getAddress() + this.getDates());

		if(this.isMobile == null) {
			this.marker.icon(markerIcon);
		} else {
			if(this.isMobile == true) {
				this.marker.icon(markerIconMobile);
			} else {
				this.marker.icon(markerIcon);
			}
		}


		return this.marker;
	}
}
