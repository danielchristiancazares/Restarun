package com.example.restarun.SearchActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restarun.R;

public class QuickSearchFilterFragment extends Fragment {
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		View searchView = inflater.inflate(R.layout.fragment_filters,
				container, false);

		return searchView;
	}
	
}
