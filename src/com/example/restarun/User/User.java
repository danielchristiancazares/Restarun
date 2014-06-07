package com.example.restarun.User;

import java.io.Serializable;
import java.util.ArrayList;

import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.restarun.SearchActivity.MyAdapter;
import com.example.yelp.Place;

public class User implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2120503951824490511L;
    public String m_name = "";
    public String m_photo = "";
    public static ArrayList<String> beenPlaces = new ArrayList<String>();
    public static ArrayList<String> favoritedPlaces = new ArrayList<String>();
    public boolean logoutCalled = false;

    public static MyAdapter mAdapter = null;

    private volatile static User user = null;

    private User() {

    }
    
    public String toString() {
    	String result = "";
    	result += m_name;
    	result += "\n" + beenPlaces.size() + "\n";
    	for(String s : beenPlaces) {
    		result += s + "\n";
    	}
    	result +=  favoritedPlaces.size() + "\n";
    	for(String s : favoritedPlaces) {
    		result += s + "\n";
    	}
    	return result;
    }
    public String getName() {
        return m_name;
    }

    public String getPhoto() {
        return m_photo;
    }

    public ArrayList<String> getBeenPlaces() {
        return beenPlaces;
    }

    public ArrayList<String> getFavoritedPlaces() {
        return favoritedPlaces;
    }

    public void setName(String name) {
        m_name = name;
    }

    public void setPhoto(String photo) {
        m_photo = photo;
    }

    public static User getInstance() {
        if ( user == null ) {
            synchronized (User.class) {
                if ( user == null ) {
                    user = new User();
                }
            }
        }
        return user;
    }

    public MyAdapter getAdapter(
            android.support.v4.app.FragmentManager pFm) {
        if ( mAdapter == null ) {
        	mAdapter = new MyAdapter( pFm );
        }

        return mAdapter;

    }

    public void addItem(String flag, String name) {
        switch (flag) {
        case "been":
            beenPlaces.add( name );
            break;

        case "favorite":
            favoritedPlaces.add( name );
            break;

        default:
            break;
        }
    }

    public boolean containsItem(String flag, String name) {
        switch (flag) {
        case "been":
            if ( !beenPlaces.contains( name ) )
                return false;
            return true;

        case "favorite":
            if ( !favoritedPlaces.contains( name ) )
                return false;

            return true;
        }
        return false;
    }
}
