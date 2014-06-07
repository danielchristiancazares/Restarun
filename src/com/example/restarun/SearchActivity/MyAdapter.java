package com.example.restarun.SearchActivity;

import java.util.ArrayList;

import com.example.yelp.Place;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/* Swipe-able list implementation */
public class MyAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private SearchList list = SearchList.getInstance();
    
    public MyAdapter(android.support.v4.app.FragmentManager pFm) {
        super( pFm );
        ArrayList<Place> mPlaces = list.getPlaces();
        for ( int i = 0; mFragments.size() != mPlaces.size(); ++i ) {
            mFragments.add( i,
                    QuickSearchFragment.newInstance( mPlaces.get( i ) ) );
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get( position );
    }
}