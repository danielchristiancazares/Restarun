package com.example.yelp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class YelpAPI {

	/**
	 * So, when getPlaces() is called it takes in the Location object and the
	 * search term.
	 * 
	 * @throws JSONException
	 */
	public ArrayList<Place> getPlaces(Location pLoc, String searchTerm) {

		/** Here we create a Yelp object to send our Yelp API call. */
		Yelp m_Yelp = new Yelp(searchTerm, pLoc.getLatitude(),
				pLoc.getLongitude());
		m_Yelp.execute();

		/** We create a String to store the response we get from Yelp. */
		String m_Response = null;
		try {
			m_Response = m_Yelp.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** Store the response as a JSON Object */
		JSONObject m_JSONResponse;
		ArrayList<Place> foundPlaces = new ArrayList<Place>();

		try {
			m_JSONResponse = new JSONObject(m_Response);

			/** The entry we want is the list of businesses. */
			JSONArray businesses = m_JSONResponse.getJSONArray("businesses");

			for (int i = 0; i < businesses.length(); ++i) {
				/** Store an entire business object first */
				JSONObject m_business = businesses.getJSONObject(i);

				JSONObject m_location = m_business.getJSONObject("location");
				JSONArray m_displayAddress = m_location
						.getJSONArray("display_address");
				StringBuilder m_AddressString = new StringBuilder();
				for (int j = 0; j < m_displayAddress.length(); ++j) {
					m_AddressString.append(m_displayAddress.get(j).toString()
							+ "\n");
				}
				JSONArray m_CatArray = m_business.getJSONArray("categories");
				JSONArray m_categories = m_CatArray.getJSONArray(0);
				String m_Category = m_categories.get(0).toString();
				String m_SortableCat = m_categories.get(1).toString();
				

				String m_Name = m_business.get("name").toString();
				String m_Address = m_AddressString.toString();
				double m_Rating = Float.parseFloat(m_business.get("rating")
						.toString());
				double m_Distance = Double.parseDouble(m_business.get(
						"distance").toString()) / 1609.34;
				Place newPlace = new Place(m_Name, m_Rating, m_Address,
						m_Distance, m_Category, m_SortableCat);
				Log.d("DEBUG", m_Name);
				foundPlaces.add(newPlace);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return foundPlaces;

	}
}
