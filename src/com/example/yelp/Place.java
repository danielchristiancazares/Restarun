package com.example.yelp;

import java.util.ArrayList;
import java.util.Comparator;

public class Place {
    public String m_name;
    public String m_address;
    public String m_category;
    public String m_sortableCat;
    public String m_imageURL;
    public String m_number;
    public String m_googleAddress;
    /*
     * m_deal[0] is deal_id
     * m_deal[1] is deal_title
     * m_deal[2] is deal_url
     * m_deal[3] is deal_time_start
     */
    //public String[] m_deal = new String[4];
    
    public double m_rating;
    public double m_distance;
    public Boolean m_isClosed;

    public Place(String pName, double pRating, String pAddress,
            double pDistance, String pCat, String pSortCat, String pImageURL, String pNumber, Boolean pIsClosed ) {
        m_address = pAddress;
        m_rating = pRating;
        m_name = pName;
        m_distance = pDistance;
        m_category = pCat;
        m_sortableCat = pSortCat;
        m_number = pNumber;
        m_isClosed = pIsClosed;
        setImageURL( pImageURL );
    }

    // Accessors
    public String getName() {
        return m_name;
    }

    public double getRating() {
        return m_rating;
    }

    public String getAddress() {
        return m_address;
    }
    
    public String getNumber() {
        return m_number;
    }

    public String getCategory() {
        return m_category;
    }

    public String getSortCat() {
        return m_sortableCat;
    }

    public String getPhoto() {
        return getImageURL();
    }

    public double getDistance() {
        return m_distance;
    }

    public String getImageURL() {
        return m_imageURL;
    }

    public void setImageURL(String pImageURL) {
        m_imageURL = pImageURL;
    }

    public static Comparator<Place> ratingComparator = new Comparator<Place>() {

        public int compare(Place firstPlace, Place secondPlace) {
            double firstRating = firstPlace.getRating();
            double secondRating = secondPlace.getRating();

            return firstRating < secondRating ? 1 : -1;

        }
    };

    /* Comparator for sorting the list by distance */
    public static Comparator<Place> distanceComparator = new Comparator<Place>() {

        public int compare(Place firstPlace, Place secondPlace) {

            double firstDist = firstPlace.getDistance();
            double secondDist = secondPlace.getDistance();

            return firstDist > secondDist ? 1 : -1;

        }
    };

    /* Comparator for sorting the list by whether or not it's open */
    public static Comparator<Place> openComparator = new Comparator<Place>() {

        public int compare(Place firstPlace, Place secondPlace) {

            Boolean firstDist = firstPlace.m_isClosed;
            Boolean secondDist = secondPlace.m_isClosed;
            if( firstDist == true && secondDist == false )
            {
                return 1;
            }
            else if( firstDist == false && secondDist == true )
            {
                return -1;
            } else
            {
                return 0;
            }

        }
    };
    
}
