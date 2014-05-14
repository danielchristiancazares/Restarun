package com.example.yelp;

/*
 * Consumer Key 	kQt6c0n37McCFys-eTKRHw
 * Consumer Secret 	fRgOT-RxIHvB_z_7JAmP-we4gEc
 * Token 			g0wYS5bxLJalAOUNNHgfMyCJq2QJnrbN
 * Token Secret 	XOLETYDWykubOZoSdwNIVcw7WVs
 * 
 */

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author danielcazares
 * @description Our Yelp class inherits functionality from AsyncTask in order
 *              for us to run any web-related requests in the background.
 */
public class Yelp extends AsyncTask<Void, Void, Void> {
	OAuthService service;
	Token accessToken;
	String m_Response = null;
	String m_Term;
	double m_Latitude, m_Longitude;

	/**
	 * These are our API keys given to us by the Yelp developer website. This is
	 * used when we request data from Yelp. It lets them know that we are
	 * registered and that our request is genuine.
	 */
	static private final String YELP_CONSUMER_KEY = "kQt6c0n37McCFys-eTKRHw";
	static private final String YELP_CONSUMER_SECRET = "fRgOT-RxIHvB_z_7JAmP-we4gEc";
	static private final String YELP_TOKEN = "g0wYS5bxLJalAOUNNHgfMyCJq2QJnrbN";
	static private final String YELP_TOKEN_SECRET = "XOLETYDWykubOZoSdwNIVcw7WVs";

	/** Yelp's constructor takes in the search term, and */
	public Yelp(String pTerm, double pLat, double pLong) {
		this.service = new ServiceBuilder().provider(YelpAuth.class)
				.apiKey(YELP_CONSUMER_KEY).apiSecret(YELP_CONSUMER_SECRET)
				.build();
		this.m_Latitude = pLat;
		this.m_Longitude = pLong;
		this.accessToken = new Token(YELP_TOKEN, YELP_TOKEN_SECRET);
	}

	public String getResponse() {
		return m_Response;
	}

	@Override
	protected Void doInBackground(Void... params) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", "restaurant");
		request.addQuerystringParameter("ll", m_Latitude + "," + m_Longitude);
		// request.addQuerystringParameter("limit", "1");
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		this.m_Response = response.getBody();
		return null;
	}

	public ArrayList<Place> parseBusiness() throws JSONException {
		/** Store the response as a JSON Object */
		JSONObject m_JSONResponse = new JSONObject(m_Response);
		/** The entry we want is the list of businesses. */
		JSONArray businesses = m_JSONResponse.getJSONArray("businesses");
		ArrayList<Place> foundPlaces = new ArrayList<Place>();

		for (int i = 0; i < businesses.length(); ++i) {
			String m_Name = businesses.getJSONObject(i).get("name").toString();
			String m_MobileURL = businesses.getJSONObject(i).get("mobile_url")
					.toString();
			Float m_Rating = Float.parseFloat(businesses.getJSONObject(i)
					.get("rating").toString());
			Place newPlace = new Place(m_Name, m_Rating, m_MobileURL);
			Log.d("DEBUG", m_Name);
			foundPlaces.add(newPlace);
		}
		return foundPlaces;
	}
}
