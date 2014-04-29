package com.example.restarun.googlePlaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;

public class PlacesAPI {

	private static String apiKey = "AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE";

	public ArrayList<Place> findPlaces(double latitude, double longitude,
			String placeType) {

		String urlString = makeUrl(latitude, longitude, placeType);

		try {
			String json = getJSON(urlString);

			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("results");

			ArrayList<Place> arrayList = new ArrayList<Place>();
			for (int i = 0; i < array.length(); i++) {
				try {
					Place place = Place
							.jsonToPontoReferencia((JSONObject) array.get(i));
					arrayList.add(place);
				} catch (Exception e) {
				}
			}
			return arrayList;
		} catch (JSONException ex) {
			Log.i("SEVERE ERROR", "somethingsomething");
		}
		return null;
	}

	// https://maps.googleapis.com/maps/api/place/search/json?location=LAT,LONG&radius=NUMBER&types=PLACETYPE&sensor=false&key=APIKEY
	public String makeUrl(double latitude, double longitude, String place) {
		StringBuilder urlString = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/search/json?");

		if (place.equals("")) {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=1000");
			// urlString.append("&types="+place);
			urlString.append("&sensor=false&key=" + apiKey);
		} else {
			urlString.append("&location=");
			urlString.append(Double.toString(latitude));
			urlString.append(",");
			urlString.append(Double.toString(longitude));
			urlString.append("&radius=1000");
			urlString.append("&types=" + place);
			urlString.append("&sensor=false&key=" + apiKey);
		}

		return urlString.toString();
	}

	protected String getJSON(String url) {
		return getUrlContents(url);
	}

	private String getUrlContents(String theUrl) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		StringBuilder content = new StringBuilder();
		try {
			URL url = new URL(theUrl);
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(urlConnection.getInputStream()), 8);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

}
