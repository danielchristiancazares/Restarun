package com.example.yelp;

import java.util.ArrayList;

import org.json.JSONException;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class YelpAPI {
	Yelp m_Yelp;
	ArrayList<Place> m_Places = new ArrayList<Place>();
	/**
	 * So, when getPlaces() is called it takes in the Location object and the
	 * search term.
	 */
	public ArrayList<Place> getPlaces(Location pLoc, String searchTerm) {

		/** Here we create a Yelp object. */
		m_Yelp = new Yelp(searchTerm, pLoc.getLatitude(), pLoc.getLongitude());

		m_Yelp.execute();
		/** We create a String to store the response we get from Yelp. */

		/** We create a YelpParser object. */
		while (m_Yelp.getResponse() == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		/** For this particular YelpParser object, we store the response */
		Log.d("DEBUG", m_Yelp.getResponse());

		try {
			m_Places = m_Yelp.parseBusiness();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return m_Places;

	}
}
