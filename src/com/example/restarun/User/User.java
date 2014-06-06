package com.example.restarun.User;

import java.util.ArrayList;

import android.util.Log;

import com.example.yelp.Place;

public class User {
    public String m_name = "";
    public String m_photo = "";
    public static ArrayList<Place> beenPlaces = new ArrayList<Place>();
    public static ArrayList<Place> favoritedPlaces = new ArrayList<Place>();

    private volatile static User user = null;

    private User() {

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
