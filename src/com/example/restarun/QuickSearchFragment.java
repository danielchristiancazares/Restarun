package com.example.restarun;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.animation.ObjectAnimator;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.gpsTracker.LocationFinder;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class QuickSearchFragment extends Fragment {

	private View searchView;

	private Location m_location;

	private TextView nameField;
	private TextView addressField;
	private TextView distanceField;
	private TextView sortBy;

	private ImageView locImg;

	private RatingBar ratingBar;

	private RadioButton ratingRadio;
	private RadioButton distanceRadio;
	private ObjectAnimator openRadio;

	private ArrayList<Place> m_placesList;
	private Iterator<Place> m_iterator;

	private Button filterButton;
	private ObjectAnimator openFilter;
	private static Boolean filterButtonClosed = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		searchView = inflater.inflate(R.layout.fragment_quicksearch, container,
				false);

		locImg = (ImageView) searchView.findViewById(R.id.imageView1);
		nameField = (TextView) searchView.findViewById(R.id.name);
		addressField = (TextView) searchView.findViewById(R.id.address);
		distanceField = (TextView) searchView.findViewById(R.id.distance);
		ratingBar = (RatingBar) searchView.findViewById(R.id.ratingBar);
		ratingBar.setIsIndicator(true);

		filterButton = (Button) searchView.findViewById(R.id.filterButton);
		openFilter = ObjectAnimator.ofFloat(filterButton, "translationY",
				-220.0f);
		// radioGroup = (RadioGroup) searchView.findViewById(R.id.radioGroup1);
		sortBy = (TextView) searchView.findViewById(R.id.sortBy);
		ratingRadio = (RadioButton) searchView.findViewById(R.id.ratingRadio);
		distanceRadio = (RadioButton) searchView
				.findViewById(R.id.distanceRadio);

		/*
		 * First, we create a LocationFinder class to find our GPS latitude and
		 * longitude. This returns a Location object. The Location object has a
		 * latitude and longitude member.
		 */
		m_location = new LocationFinder(searchView.getContext()).getLocation();

		/*
		 * Then, we create a YelpAPI class and call getPlaces() and pass the
		 * Location object and a search term that we want. In our case, we want
		 * to find local restaurants.
		 */
		m_placesList = new YelpAPI().getPlaces(m_location, "restaurant");

		m_iterator = m_placesList.iterator();
		displayPlace(m_iterator.next());

		openRadio = ObjectAnimator.ofFloat(sortBy, "translationY", 200.0f);
		openRadio.start();

		openRadio = ObjectAnimator.ofFloat(ratingRadio, "translationY", 200.0f);
		openRadio.start();

		openRadio = ObjectAnimator.ofFloat(distanceRadio, "translationY",
				200.0f);
		openRadio.start();

		locImg.setOnTouchListener(new OnSwipeTouchListener(locImg.getContext()) {

			public void onSwipeRight() {
			}

			public void onSwipeLeft() {
				if (m_iterator.hasNext()) {
					displayPlace(m_iterator.next());
				} else {
					Toast.makeText(searchView.getContext(),
							getString(R.string.end_of_list), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		ratingRadio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(m_placesList.isEmpty())) {
					Collections.sort(m_placesList, Place.ratingComparator);
					m_iterator = m_placesList.iterator();
					displayPlace(m_iterator.next());
				}
			}
		});
		
		distanceRadio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(m_placesList.isEmpty())) {
					Collections.sort(m_placesList, Place.distanceComparator);
					m_iterator = m_placesList.iterator();
					displayPlace(m_iterator.next());
				}
			}
		});
		
		filterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (filterButtonClosed == true) {
					openFilter.start();

					openRadio = ObjectAnimator.ofFloat(sortBy, "translationY",
							-10.0f);
					openRadio.start();

					openRadio = ObjectAnimator.ofFloat(ratingRadio,
							"translationY", -10.0f);
					openRadio.start();

					openRadio = ObjectAnimator.ofFloat(distanceRadio,
							"translationY", -10.0f);
					openRadio.start();

					openFilter = ObjectAnimator.ofFloat(filterButton,
							"translationY", 0.0f);
					filterButtonClosed = false;
					sortBy.setVisibility(View.VISIBLE);
					distanceRadio.setVisibility(View.VISIBLE);
					ratingRadio.setVisibility(View.VISIBLE);
				} else {

					openFilter.start();
					openFilter = ObjectAnimator.ofFloat(filterButton,
							"translationY", -220.0f);

					openRadio = ObjectAnimator.ofFloat(sortBy, "translationY",
							200.0f);
					openRadio.start();

					openRadio = ObjectAnimator.ofFloat(ratingRadio,
							"translationY", 200.0f);
					openRadio.start();

					openRadio = ObjectAnimator.ofFloat(distanceRadio,
							"translationY", 200.0f);
					openRadio.start();

					filterButtonClosed = true;

				}
			}

		});

		return searchView;

	}

	private void displayPlace(Place m_place) {

		if (m_placesList.isEmpty()) {
			nameField.setText("No places found.");
		}
		nameField.setText(m_place.getName());
		addressField.setText(m_place.getAddress());
		distanceField.setText(new DecimalFormat("##.##").format(m_place
				.getDistance()) + " miles away");
		ratingBar.setRating((float) m_place.getRating());
		if (m_place.getPhoto() != null) {
			locImg.setImageURI(m_place.getPhoto());

		} else {
			locImg.setImageResource(R.drawable.no_photos_available);
		}
	}

}
