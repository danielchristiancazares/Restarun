package com.example.restarun.SearchActivity;

import com.example.restarun.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class SearchActivity extends FragmentActivity {

	public void quickSearch(View view) {
		QuickSearchFragment quickSearchFrag = new QuickSearchFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, quickSearchFrag).commit();
	}

	public void advancedSearch(View view) {

	}
	
	public void 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		SearchFragment searchFragment = new SearchFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, searchFragment).commit();

	}
}
