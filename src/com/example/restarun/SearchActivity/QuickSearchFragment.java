package com.example.restarun.SearchActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restarun.OnSwipeTouchListener;
import com.example.restarun.R;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class QuickSearchFragment extends Fragment implements AnimationListener {

	private View searchView;

	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

	private Location m_location;

	private ArrayList<Place> m_places;
	private ArrayList<String> m_categories;
	private Iterator<Place> m_iterator;

	private ImageView locImg;

	private TextView nameField;
	private TextView addressField;
	private TextView distanceField;

	private RatingBar ratingBar;

	private TranslateAnimation trans;
	private Place place;
	
	public void ratingSort(View view) {
		if (m_places.isEmpty())
			return;

		Collections.sort(m_places, Place.ratingComparator);
		m_iterator = m_places.iterator();
		displayPlace(m_iterator.next());

	}

	public void distanceSort(View view) {
		if (m_places.isEmpty())
			return;

		Collections.sort(m_places, Place.distanceComparator);
		m_iterator = m_places.iterator();
		displayPlace(m_iterator.next());

	}

	private void displayPlace(Place m_place) {

		if (m_places.isEmpty()) {
			nameField.setText("No places found.");
		}
		nameField.setText(m_place.getName());
		addressField.setText(m_place.getAddress());
		distanceField.setText(new DecimalFormat("##.##").format(m_place
				.getDistance()) + " miles away");
		ratingBar.setRating((float) m_place.getRating());
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		searchView = inflater.inflate(R.layout.fragment_quicksearch, container,
				false);
		trans = new TranslateAnimation(0,-500,0,0);
		trans.setDuration(500);
		
		locImg = (ImageView) searchView.findViewById(R.id.imageView1);
		nameField = (TextView) searchView.findViewById(R.id.name);
		addressField = (TextView) searchView.findViewById(R.id.address);
		distanceField = (TextView) searchView.findViewById(R.id.distance);
		ratingBar = (RatingBar) searchView.findViewById(R.id.ratingBar);
		ratingBar.setIsIndicator(true);

		m_location = new ServiceGPS(getActivity()).getLocation();
		m_places = new YelpAPI().getPlaces(m_location, "restaurant");
		m_iterator = m_places.iterator();
		displayPlace(m_iterator.next());

		m_categories = new ArrayList<String>();
		for (Place aPlace : m_places) {
			if (m_categories.contains(aPlace.getCategory())) {
				continue;
			}

			m_categories.add(aPlace.getCategory());

		}

		locImg.setOnTouchListener(new OnSwipeTouchListener(locImg.getContext()) {

			public void onSwipeRight() {
				System.exit(0);
			}

			public void onSwipeLeft() {
				if (m_iterator.hasNext()) {
					place = m_iterator.next();
					displayPlace(place);
					locImg.startAnimation(trans);
				}
			}
		});
		
		return searchView;

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		
			locImg.setVisibility(ImageView.GONE);
			
			if (place.getPhoto() != null) {
				locImg.setImageURI(place.getPhoto());
			} else {
				locImg.setImageResource(R.drawable.no_photos_available);
			}		
			locImg.setVisibility(ImageView.VISIBLE);
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	
	

}
