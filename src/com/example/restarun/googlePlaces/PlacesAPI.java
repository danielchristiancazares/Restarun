package com.example.restarun.googlePlaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

public class PlacesAPI {

	private static String apiKey = "AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE";
	public String myString = "https://maps.googleapis.com/maps/api/place/search/json?";

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
			Log.e("test", "JSONException: " + urlString);
			ex.printStackTrace();
		}
		return null;
	}

	// https://maps.googleapis.com/maps/api/place/search/json?location=32.8762142,-117.2354577&radius=1000&types=restaurant&sensor=false&key=AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE
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
		httpRequest myRequest = new httpRequest(url);
		myRequest.execute();
		try {
			while (myRequest.getResponse() == null) {
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			Log.i("debug", "InterruptedException: " + myString);
			e.printStackTrace();
		}
		return myRequest.getResponse();
	}

	private class httpRequest extends AsyncTask<Void, Void, Void> {

		String myUrl;
		String myResponse;

		public httpRequest(String newString) {
			myUrl = newString;
		}

		public String getResponse() {
			return myResponse;
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();

			try {
				URL url = new URL(myUrl);
				conn = (HttpURLConnection) url.openConnection();

				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
				myResponse = jsonResults.toString();
			} catch (MalformedURLException e) {
				Log.e("debug", "MalformedURLException: " + myResponse);
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				Log.e("debug", "IOException: " + myResponse);
				e.printStackTrace();
				return null;
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			return null;
		}
	}
}
