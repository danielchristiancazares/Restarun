package com.example.restarun;

import java.util.ArrayList;
import java.util.Iterator;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.googlePlaces.Place;
import com.example.restarun.googlePlaces.PlacesAPI;
import com.example.restarun.gpsTracker.LocationFinder;

public class QuickSearchFragment extends Fragment {

	private View searchView;

	private Location m_location;

	private TextView writeText;
	private ImageView locImg;

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

		m_location = new LocationFinder(searchView.getContext()).getLocation();

		m_placesList = new PlacesAPI().findPlaces(m_location, "restaurant");
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
		myList.append(m_place.getVicinity() + "\n");
		if (m_place.getRef() != "") {
			locImg.setImageBitmap(m_place.getPhoto());
			writeText.setText(myList.toString());
		} else {
			locImg.setImageResource(R.drawable.no_photos_available);
		}

	}

}
