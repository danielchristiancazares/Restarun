package com.example.restarun.SearchActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.restarun.R;
import com.example.restarun.ProfileActivity.ProfileActivity;
import com.example.restarun.User.User;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class SearchActivity extends ActionBarActivity {

	private User mUser = User.getInstance();
	private SearchList list = SearchList.getInstance();

	private ViewInfoFragment viewInfoFragment;
	private ViewPager mPager;
	private boolean viewFlag = false;

	public void getInfo(View view) {
		/* Find the current restaurant selected */
		viewFlag = true;
		int currentPos = mPager.getCurrentItem();
		ArrayList<Place> mPlaces = list.getPlaces();
		Place currentPlace = mPlaces.get(currentPos);

		/* Display the information fragment */
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		// TODO: Fix function call/member access.
		viewInfoFragment = new ViewInfoFragment(currentPlace);

		try {
			viewInfoFragment.m_bitmap = new DownloadImage().execute(
					currentPlace.m_googleAddress).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		transaction.add(R.id.container, viewInfoFragment).addToBackStack(null);
		transaction.commit();
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void getDirections(View view) {
		int currentPos = mPager.getCurrentItem();
		ArrayList<Place> mPlaces = list.getPlaces();
		Place currentPlace = mPlaces.get(currentPos);

		Location m_location = new ServiceGPS(this).getLocation();

		double latitude = m_location.getLatitude();
		double longitude = m_location.getLongitude();

		if(viewFlag) {
			getSupportFragmentManager().popBackStack();
			viewFlag = false;
		}
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
				Uri.parse("http://maps.google.com/maps?" + "saddr=" + latitude
						+ "," + longitude + "&daddr=" + currentPlace.m_address));
		intent.setClassName("com.google.android.apps.maps",
				"com.google.android.maps.MapsActivity");
		startActivity(intent);
	}

	public void eatHere(View view) {
		/* Find the current restaurant selected */
		int currentPos = mPager.getCurrentItem();
		ArrayList<Place> mPlaces = list.getPlaces();
		Place currentPlace = mPlaces.get(currentPos);
		String pName = currentPlace.getName();
		if (!mUser.containsItem("been", pName)) {
			mUser.addItem("been", pName);
		}
	}

	public void addFavorite(View view) {
		/* Find the current restaurant selected */
		int currentPos = mPager.getCurrentItem();
		ArrayList<Place> mPlaces = list.getPlaces();
		Place currentPlace = mPlaces.get(currentPos);
		String pName = currentPlace.getName();
		if (!mUser.containsItem("favorite", pName)) {
			mUser.addItem("favorite", pName);
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		if (viewFlag) {
			getSupportFragmentManager().popBackStack();
			viewFlag = false;
		}
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* Add the action bar items to the view */
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		if (mUser.m_name == "") {
			InputStream in;

			try {
				Log.d("DEBUG", "Reading user_data.dat");
				in = new FileInputStream(this.getFilesDir() + "/user_data.dat");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				mUser.m_name = reader.readLine();
				int beenSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < beenSize; i++) {
					mUser.addItem("been", reader.readLine());
				}
				int favSize = Integer.parseInt(reader.readLine());
				for (int i = 0; i < favSize; i++) {
					mUser.addItem("favorite", reader.readLine());
				}

				reader.close();
				in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setViewPager();
		Toast.makeText(getBaseContext(), "Welcome, " + mUser.m_name,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onResume() {
		Log.d("DEBUG", "Resuming...");
		setViewPager();
		super.onResume();
	}

	@Override
	public void onStop() {
		Log.d("DEBUG", "Stopping...");
		super.onStop();
	}

	@Override
	public void onDestroy() {

		try {
			Log.d("DEBUG", "Saving to user_data");
			User user = User.getInstance();
			String save = user.toString();
			FileOutputStream outputStream = openFileOutput("user_data.dat",
					Context.MODE_PRIVATE);
			outputStream.write(save.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onDestroy();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d("DEBUG", "Pausing...");
	}

	@Override
	public void onRestart() {
		Log.d("DEBUG", "Restarting...");
		super.onRestart();
		setViewPager();
	}

	private void setViewPager() {
		ArrayList<Place> mPlaces = new ArrayList<Place>();

		/* Attempt to pass phone's GPS latitude and longitude */
		Double latitude = null;
		Double longitude = null;

		/* Find the user's current location */
		Location m_location = new ServiceGPS(this).getLocation();

		latitude = m_location.getLatitude();
		longitude = m_location.getLongitude();

		if (latitude == null || longitude == null) {
			latitude = 32.8762142;
			longitude = -117.2354577;
		}

		try {
			if (list.getSize() != 0) {
				Log.d("DEBUG", "Getting");
				mPlaces = list.getPlaces();
			} else {
				Log.d("DEBUG", "Setting");
				list.setPlaces(new YelpAPI().execute(latitude, longitude).get());
			}
		} catch (InterruptedException | ExecutionException e) {
		}

		/* Load the list view */
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		mPager.setOffscreenPageLimit(mPlaces.size());

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/* Handle presses on the action bar items */
		switch (item.getItemId()) {
		case R.id.profile:
			if(viewFlag) {
				getSupportFragmentManager().popBackStack();
				viewFlag = false;
			}
			Intent intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* Button onClick() functions */
	public void ratingSort(View view) {
		ArrayList<Place> mPlaces = list.getPlaces();

		if (mPlaces.isEmpty())
			return;

		Collections.sort(mPlaces, Place.ratingComparator);
		MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

	public void distanceSort(View view) {
		ArrayList<Place> mPlaces = list.getPlaces();

		if (mPlaces.isEmpty())
			return;

		Collections.sort(mPlaces, Place.distanceComparator);
		MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

	public void openSort(View view) {
		ArrayList<Place> mPlaces = list.getPlaces();

		if (mPlaces.isEmpty())
			return;

		Collections.sort(mPlaces, Place.openComparator);
		MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

}
