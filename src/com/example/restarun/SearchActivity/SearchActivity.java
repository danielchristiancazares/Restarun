package com.example.restarun.SearchActivity;

import java.util.Collections;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.example.restarun.R;
import com.example.yelp.Place;

public class SearchActivity extends FragmentActivity {
	private QuickSearchFragment quickSearchFrag;
	private QuickSearchFilterFragment quickSearchFilterFrag;

	private static final int NUM_PAGES = 5;

	private ObjectAnimator filterAnimation;

	private static Boolean filterOptions = true;

	public void ratingSort(View view) {
		quickSearchFrag.ratingSort(view);

	}

	public void distanceSort(View view) {
		quickSearchFrag.distanceSort(view);

	}

	public void filterButton(View view) {
		View quickSearchFilterFrag = findViewById(R.id.fragment_filters);

		if (filterOptions == true) {
			filterAnimation = ObjectAnimator.ofFloat(quickSearchFilterFrag,
					"translationY", -160.0f);
			filterAnimation.start();

			filterOptions = false;

		} else {
			filterAnimation = ObjectAnimator.ofFloat(quickSearchFilterFrag,
					"translationY", 0.0f);
			filterAnimation.start();

			filterOptions = true;

		}
	}

	public void quickSearch(View view) {
		quickSearchFrag = new QuickSearchFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.topView, quickSearchFrag).commit();

		quickSearchFilterFrag = new QuickSearchFilterFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.botView, quickSearchFilterFrag).commit();

	}

	public void advancedSearch(View view) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		SearchFragment searchFragment = new SearchFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.topView, searchFragment).commit();

	}

	/**
	 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects,
	 * in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return new QuickSearchFragment();
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

}
