package com.example.restarun.User;

import java.util.ArrayList;

import com.example.yelp.Place;

public class User {
    public String m_name = "";
    public String m_photo = "";
    private ArrayList<Place> beenPlaces;
    private ArrayList<Place> savedDeals;
    private ArrayList<Place> favoritedPlaces;
    
    private volatile static User user;
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
    	if(user==null) {
    		synchronized(User.class) {
    			if(user == null) {
    				user = new User();
    			}
    		}
    	}
    	return user;
    }
    
    public void add(String flag, Place place) {
    	switch(flag) {
    	case "been":
    		if(beenPlaces == null) {
    			beenPlaces = new ArrayList<Place>();
    		}
    		beenPlaces.add(place);
    		break;
    	
    	case "favorite":
    		if(favoritedPlaces == null) {
    			favoritedPlaces = new ArrayList<Place>();
    		}
    		favoritedPlaces.add(place);
    		break;
    		
    	default:
    		break;
    	}
    }
    
    public boolean contains(String flag, Place place){
    	switch(flag) {
    	case "been":
    		for(Place a:beenPlaces){
    			if(a.getAddress().equals(place.getAddress())){
    				return false;
    			}
    		}
    		return true;
    	
    	case "favorite":
    		for(Place a:favoritedPlaces) {
    			if(a.getAddress().equals(place.getAddress()))
    				return false;
    		}
    		return true;
    	}
    	return false;
    }
}
