package com.example.restarun;

import java.util.ArrayList;
import java.util.Iterator;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.gpsTracker.LocationFinder;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class QuickSearchFragment extends Fragment {

	private View searchView;

	private Location m_location;

	private TextView writeText;
	private ImageView locImg;
	private RatingBar ratingBar;

	private ArrayList<Place> m_placesList;
	private Iterator<Place> m_iterator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		searchView = inflater.inflate(R.layout.fragment_quicksearch, container,
				false);

		locImg = (ImageView) searchView.findViewById(R.id.imageView1);
		writeText = (TextView) searchView.findViewById(R.id.textView1);
		ratingBar = (RatingBar) searchView.findViewById(R.id.ratingBar);
		ratingBar.setIsIndicator(true);
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

		return searchView;

	}

	private void displayPlace(Place m_place) {

		StringBuilder myList = new StringBuilder();

		if (m_placesList.isEmpty()) {
			writeText.setText("No places found.");
		}

		myList.append(m_place.getName() + "\n");
		ratingBar.setRating(m_place.getRating());
		Log.d("DEBUG", m_place.getName());
		// if (m_place.get(Member.PHOTO_URL) != "") {
		// locImg.setImageBitmap(m_place.getPhoto());

		// } else {
		// locImg.setImageResource(R.drawable.no_photos_available);
		// }
		writeText.setText(myList.toString());
	}

}
