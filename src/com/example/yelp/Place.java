package com.example.yelp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;

import android.net.Uri;

public class Place {
	private String m_name;
	private String m_address;
	private String m_category;
	private String m_sortableCat;
	
	private double m_rating;
	private double m_distance;
	
	private Uri m_photoURI;


	public Place(String pName, double pRating, String pAddress, double pDistance, String pCat, String pSortCat) {
		this.m_address = pAddress;
		this.m_rating = pRating;
		this.m_name = pName;
		this.m_distance = pDistance;
		this.m_category = pCat;
		this.m_sortableCat = pSortCat;
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
	
	public String getCategory() {
		return m_category;
	}

	public String getSortCat() {
		return m_sortableCat;
	}
	
	public Uri getPhoto() {
		return m_photoURI;
	}

	public double getDistance() {
		return m_distance;
	}

	public static Comparator<Place> ratingComparator = new Comparator<Place>() {

		public int compare(Place firstPlace, Place secondPlace) {
			double firstRating = firstPlace.getRating();
			double secondRating = secondPlace.getRating();

			return firstRating < secondRating ? 1 : -1;

		}
	};

	/* Comparator for sorting the list by roll no */
	public static Comparator<Place> distanceComparator = new Comparator<Place>() {

		public int compare(Place firstPlace, Place secondPlace) {

			double firstDist = firstPlace.getDistance();
			double secondDist = secondPlace.getDistance();

			return firstDist > secondDist ? 1 : -1;

		}
	};

}
