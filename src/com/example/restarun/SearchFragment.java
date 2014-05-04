package com.example.restarun;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
