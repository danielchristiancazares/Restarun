package com.example.restarun.SearchActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restarun.R;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class SearchActivity extends FragmentActivity {
	private QuickSearchFilterFragment quickSearchFilterFrag;

	private Location m_location;

	private static ArrayList<Place> mPlaces;
	private ArrayList<String> m_categories;

	private MyAdapter mAdapter;
	private ViewPager mPager;

	public void ratingSort(View view) {
		if (mPlaces.isEmpty())
			return;

		Collections.sort(mPlaces, Place.ratingComparator);
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

	public void distanceSort(View view) {
		if (mPlaces.isEmpty())
			return;

		Collections.sort(mPlaces, Place.distanceComparator);
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
	}

	public void quickSearch(View view) {

	}

	public void advancedSearch(View view) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		quickSearchFilterFrag = new QuickSearchFilterFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.botView, quickSearchFilterFrag).commit();

		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();

		m_location = new ServiceGPS(this).getLocation();
		mPlaces = new YelpAPI().getPlaces(m_location, "restaurant");

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		/*
		 * m_categories = new ArrayList<String>(); for (Place aPlace : m_places)
		 * { if (m_categories.contains(aPlace.getCategory())) { continue; }
		 * 
		 * m_categories.add(aPlace.getCategory());
		 * 
		 * }
		 * 
		 * locImg.setOnTouchListener(new
		 * OnSwipeTouchListener(locImg.getContext()) {
		 * 
		 * public void onSwipeRight() { System.exit(0); }
		 * 
		 * public void onSwipeLeft() { if (m_iterator.hasNext()) { place =
		 * m_iterator.next(); displayPlace(place); locImg.startAnimation(trans);
		 * } } });
		 */
	}

	public static class MyAdapter extends FragmentStatePagerAdapter {

		ArrayList<Fragment> pageViews = new ArrayList<Fragment>();

		public MyAdapter(FragmentManager fm) {
			super(fm);
			for (int i = 0; pageViews.size() != mPlaces.size(); ++i) {
				pageViews.add(QuickSearchFragment.newInstance(mPlaces.get(i)));
			}
		}

		@Override
		public int getCount() {
			return mPlaces.size();
		}

		@Override
		public Fragment getItem(int position) {

			return pageViews.get(position);
		}

	}

	public static class QuickSearchFragment extends Fragment {

		private ImageView locImg;

		private TextView nameField;
		private TextView addressField;
		private TextView distanceField;

		private RatingBar ratingBar;

		private String mName;
		private String mAddress;
		private double mRating;
		private double mDistance;

		static QuickSearchFragment newInstance(Place p) {
			QuickSearchFragment quickSearchFrag = new QuickSearchFragment();
			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putString("name", p.getName());
			args.putString("address", p.getAddress());
			args.putDouble("rating", p.getRating());
			args.putDouble("distance", p.getDistance());
			quickSearchFrag.setArguments(args);

			return quickSearchFrag;
		}

		/*
		 * public void setPlace(Place pPlace) { m_place = pPlace;
		 * 
		 * nameField.setText(m_place.getName());
		 * addressField.setText(m_place.getAddress()); }
		 */

		@Override
		public void onCreate(Bundle savedInstanceState) {

			/** Sets the current view **/
			super.onCreate(savedInstanceState);

			mName = getArguments().getString("name");
			mAddress = getArguments().getString("address");
			mRating = getArguments().getDouble("rating");
			mDistance = getArguments().getDouble("distance");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View searchView = inflater.inflate(R.layout.fragment_quicksearch,
					container, false);

			locImg = (ImageView) searchView.findViewById(R.id.imageView1);

			nameField = (TextView) searchView.findViewById(R.id.name);
			addressField = (TextView) searchView.findViewById(R.id.address);
			distanceField = (TextView) searchView.findViewById(R.id.distance);
			ratingBar = (RatingBar) searchView.findViewById(R.id.ratingBar);
			ratingBar.setIsIndicator(true);

			Typeface font = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/Roboto-Regular.ttf");
			Typeface font2 = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/Roboto-Light.ttf");

			nameField.setTextColor(Color.WHITE);
			nameField.setTypeface(font);

			addressField.setTextColor(Color.WHITE);
			addressField.setTypeface(font2);

			distanceField.setTextColor(Color.WHITE);
			distanceField.setTypeface(font2);

			nameField.setText(mName);
			addressField.setText(mAddress);
			ratingBar.setRating((float) mRating);

			distanceField.setText(new DecimalFormat("##.##").format(mDistance)
					+ " miles away");

			return searchView;

		}
	}

}
