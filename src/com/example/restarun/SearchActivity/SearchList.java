package com.example.restarun.SearchActivity;

import java.util.ArrayList;

import android.util.Log;

import com.example.yelp.Place;

public class SearchList {
    public volatile static ArrayList<Place> mPlaces = new ArrayList<Place>();

    private volatile static SearchList list = null;

    private SearchList() {
    }

    public ArrayList<Place> getPlaces() {
        return mPlaces;
    }

    public void setPlaces(ArrayList<Place> pPlaces) {
        mPlaces = pPlaces;
    }

    public static SearchList getInstance() {
        if ( list == null ) {
            synchronized (SearchList.class) {
                if ( list == null ) {
                    list = new SearchList();
                }
            }
        }
        return list;
    }

    public void addItem(Place place) {
        if ( !mPlaces.contains( place ) )
            mPlaces.add( place );
    }
}
