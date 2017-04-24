package com.team_htbr.a1617proj1bloeddonatie_app;

/**
 * Created by bjorn on 22-4-2017.
 */

public class Location {

	private String id;
	private String name;
	private String streetName;
	private int streetNumber;
	private String city;
	private Boolean isMoblie;
	private double lat;
	private double lng;

	public Location() {

	}

	public Location(String id, String name, String streetName, int streetnumber, String City, Boolean isMoblie, double LAT, double LNG) {
		this.id = id;
		this.name = name;
		this.streetName = streetName;
		this.streetNumber = streetnumber;
		this.city = City;
		this.isMoblie = isMoblie;
		this.lat = LAT;
		this.lng = LNG;
	}

	public Location(double LAT, double LNG, String Name) {
		this.lat = LAT;
		this.lng = LNG;
		this.name= Name;
	}

	public Location(String name, String streetName, int streetNumber, String city, double lat, double lng) {
		this.name = name;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.city = city;
		this.lat = lat;
		this.lng = lng;
	}

	public double getLAT() {
		return lat;
	}

	public double getLNG() {
		return lng;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		if (streetNumber != 0 && streetName != null && city != null) {
			return streetName + " " + streetNumber + ",  " + city;
		}
		else {
			return "no info available";
		}
	}
}
