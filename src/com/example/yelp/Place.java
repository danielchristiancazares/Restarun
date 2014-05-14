package com.example.yelp;

import java.net.URI;

import android.net.Uri;

public class Place {
	private String m_name, m_address;
	private double m_rating, m_distance;
	private Uri m_photoURI;

	public Place(String pName, double pRating, String pAddress, double pDistance) {
		this.m_address = pAddress;
		this.m_rating = pRating;
		this.m_name = pName;
		this.m_distance = pDistance;
	}

	public String getName() {
		return m_name;
	}

	public double getRating() {
		return m_rating;
	}

	public String getAddress() {
		return m_address;
	}

	public Uri getPhoto() {
		return m_photoURI;
	}

	public double getDistance() {
		return m_distance;
	}

}
