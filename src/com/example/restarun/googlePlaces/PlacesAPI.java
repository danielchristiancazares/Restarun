package com.example.restarun.googlePlaces;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

public class PlacesAPI {

	private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/search/json?&sensor=false&key=AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE";

	public ArrayList<Place> findPlaces(Location pLocation, String pType) {

		String urlString = buildURL(pLocation.getLatitude(),
				pLocation.getLongitude(), pType);

		try {
			String json = getJSON(urlString);

			JSONObject object = new JSONObject(json);
			JSONArray array = object.getJSONArray("results");

			ArrayList<Place> placesList = new ArrayList<Place>();

			for (int i = 0; i < array.length(); i++) {
				Place place = Place.parseJSON((JSONObject) array.get(i));
				placesList.add(place);
			}

			return placesList;

		} catch (JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private String buildURL(double latitude, double longitude, String placeType) {
		StringBuilder urlString = new StringBuilder(BASE_URL);

		urlString.append("&location=");
		urlString.append(Double.toString(latitude));
		urlString.append(",");
		urlString.append(Double.toString(longitude));
		urlString.append("&radius=1000");
		if (!placeType.equals("")) {
			urlString.append("&types=" + placeType);
		}
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
			Log.e("error", "InterruptedException: " + BASE_URL);
			e.printStackTrace();
		}
		return myRequest.getResponse();
	}



	private class httpRequest extends AsyncTask<Void, Void, Void> {

		private String m_URL;
		private String m_serverResponse;

		public httpRequest(String givenURL) {
			this.m_URL = givenURL;
		}

		public String getResponse() {
			return m_serverResponse;
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpURLConnection conn = null;
			StringBuilder serverResponse = new StringBuilder();

			try {
				URL url = new URL(m_URL);
				conn = (HttpURLConnection) url.openConnection();

				InputStreamReader in = new InputStreamReader(
						conn.getInputStream());

				int read;
				char[] buff = new char[1024];
				while ((read = in.read(buff)) != -1) {
					serverResponse.append(buff, 0, read);
				}
				m_serverResponse = serverResponse.toString();
			} catch (IOException e) {
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
