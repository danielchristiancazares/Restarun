package com.example.restarun.SearchActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.restarun.R;

public class SearchFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_search, container, false);
		
		Button search = (Button) view.findViewById(R.id.quickSearch);
		search.setTextColor(Color.WHITE);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
		search.setTypeface(font);
		
		return view;
	}
}
