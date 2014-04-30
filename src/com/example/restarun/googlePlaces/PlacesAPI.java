package com.example.restarun.googlePlaces;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class PlacesAPI {

	private static String m_googleKey = "AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE";
	private static String m_baseURL = "https://maps.googleapis.com/maps/api/place/search/json?";

	public ArrayList<Place> findPlaces(double latitude, double longitude,
			String placeType) {

		String urlString = buildURL(latitude, longitude, placeType);

		try {
			String json = getJSON(urlString);

			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("results");

			ArrayList<Place> arrayList = new ArrayList<Place>();
			for (int i = 0; i < array.length(); i++) {
				Place place = Place.parseJSON((JSONObject) array.get(i));
				arrayList.add(place);
			}
			return arrayList;

		} catch (JSONException ex) {
			Log.e("error", "JSONException: " + urlString);
			ex.printStackTrace();
		}
		return null;
	}

	// https://maps.googleapis.com/maps/api/place/search/json?location=32.8762142,-117.2354577&radius=1000&types=restaurant&sensor=false&key=AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE
	private String buildURL(double latitude, double longitude, String place) {
		StringBuilder urlString = new StringBuilder(m_baseURL);

		urlString.append("&location=");
		urlString.append(Double.toString(latitude));
		urlString.append(",");
		urlString.append(Double.toString(longitude));
		urlString.append("&radius=1000");
		if (!place.equals("")) {
			urlString.append("&types=" + place);
		}
		urlString.append("&sensor=false&key=" + m_googleKey);

		return urlString.toString();
	}

	private String getJSON(String url) {
		httpRequest myRequest = new httpRequest(url);
		myRequest.execute();
		try {
			while (myRequest.getResponse() == null) {
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			Log.e("error", "InterruptedException: " + m_baseURL);
			e.printStackTrace();
		}
		return myRequest.getResponse();
	}

	private class httpRequest extends AsyncTask<Void, Void, Void> {

		private String m_URL;
		private String m_servResponse;

		public httpRequest(String givenURL) {
			this.m_URL = givenURL;
		}

		public String getResponse() {
			return m_servResponse;
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpURLConnection conn = null;
			StringBuilder jsonResults = new StringBuilder();

			try {
				URL url = new URL(m_URL);
				conn = (HttpURLConnection) url.openConnection();

				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					jsonResults.append(buff, 0, read);
				}
				m_servResponse = jsonResults.toString();
			} catch (MalformedURLException e) {
				Log.e("error", "MalformedURLException: " + m_servResponse);
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				Log.e("error", "IOException: " + m_servResponse);
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
