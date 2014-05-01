package com.example.restarun;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.googlePlaces.Place;
import com.example.restarun.googlePlaces.PlacesAPI;

public class SearchFragment extends Fragment {
	
	private View searchView;

	private Button quickSearchButton;
	private Button advancedSearchButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		searchView = inflater.inflate(R.layout.fragment_search, container,
				false);

		quickSearchButton = (Button) searchView.findViewById(R.id.quickSearch);
		quickSearchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final FragmentManager fragmentManager = getFragmentManager();

				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				

				QuickSearchFragment quickSearchFrag = new QuickSearchFragment();
				fragmentTransaction.replace(R.id.container, quickSearchFrag);
				fragmentTransaction.commit();
		}});

		return searchView;
	}
}
