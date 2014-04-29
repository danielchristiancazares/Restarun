package com.example.restarun;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import com.example.restarun.googlePlaces.Place;
import com.example.restarun.googlePlaces.PlacesAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchFragment extends Fragment {

	private View searchView;

	private String[] places;
	private URL url;
	private HttpURLConnection connection = null;

	private double latitude = 32.8762142;
	private double longitude = -117.2354577;
	private EditText latitudeText;
	private EditText longitudeText;
	private Button quickSearchButton;
	private TextView writeText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		searchView = inflater.inflate(R.layout.fragment_search, container,
				false);

		longitudeText = (EditText) searchView.findViewById(R.id.latNum);
		longitudeText = (EditText) searchView.findViewById(R.id.longNum);
		writeText = (TextView) searchView.findViewById(R.id.textView1);
		quickSearchButton = (Button) searchView.findViewById(R.id.quickSearch);
		quickSearchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<Place> myPlaces = new PlacesAPI().findPlaces(latitude,
						longitude, "restaurant");

				StringBuilder myList = new StringBuilder("" + myPlaces.size()
						+ " places found.\n");

				for (int i = 0; i < myPlaces.size(); i++) {
					myList.append(myPlaces.get(i).getName() + "\n");
				}

				writeText.setText(myList.toString());
			}
		});

		/*
		 * StringBuilder restaurantList = new StringBuilder("" + myPlaces.size()
		 * + " places found.\n");
		 * 
		 * for (int i = 0; i < myPlaces.size(); i++) {
		 * restaurantList.append(myPlaces.get(i).getName() + "\n"); }
		 * 
		 * writeText.setText(restaurantList.toString());
		 */
		return searchView;
	}
}
