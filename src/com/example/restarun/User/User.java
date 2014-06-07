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
    public static ArrayList<Place> beenPlaces = new ArrayList<Place>();
    public static ArrayList<Place> favoritedPlaces = new ArrayList<Place>();
    public boolean logoutCalled = false;

    public static MyAdapter mAdapter = null;

    private volatile static User user = null;

    public User() {

    }

    public User(String pName, String pPhoto, ArrayList<Place> pBeen,
            ArrayList<Place> pFavorites, boolean pLogout, MyAdapter pAdapter,
            User pUser) {
        this.m_name = pName;
        this.m_photo = pPhoto;
        this.beenPlaces = pBeen;
        this.favoritedPlaces = pFavorites;
        this.logoutCalled = pLogout;
        this.mAdapter = pAdapter;
        this.user = pUser;
    }

    public String getName() {
        return m_name;
    }

    public String getPhoto() {
        return m_photo;
    }

    public ArrayList<Place> getBeenPlaces() {
        return beenPlaces;
    }

    public ArrayList<Place> getFavoritedPlaces() {
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

    public static MyAdapter getAdapter(
            android.support.v4.app.FragmentManager pFm) {
        if ( mAdapter == null ) {
            synchronized (MyAdapter.class) {
                if ( mAdapter == null ) {
                    mAdapter = new MyAdapter( pFm );
                }
            }
        }

        return mAdapter;

    }

    public String toString() {
        String result = "";
        result += m_name + " ";
        for(Place p : beenPlaces) {
            result += p.toString() + " ";
        }
        for(Place p : favoritedPlaces) {
            result += p.toString() + " ";
        }
        return result;
    }

    public void addItem(String flag, Place place) {
        switch (flag) {
        case "been":
            beenPlaces.add( place );
            break;

        case "favorite":
            favoritedPlaces.add( place );
            break;

        default:
            break;
        }
    }

    public boolean containsItem(String flag, Place place) {
        switch (flag) {
        case "been":
            if ( !beenPlaces.contains( place ) )
                return false;
            return true;

        case "favorite":
            if ( !favoritedPlaces.contains( place ) )
                return false;

            return true;
        }
        return false;
    }
}
