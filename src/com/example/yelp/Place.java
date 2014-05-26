package com.example.yelp;

import java.util.Comparator;

public class Place {
    private String m_name;
    private String m_address;
    private String m_category;
    private String m_sortableCat;
    private String m_imageURL;

    private double m_rating;
    private double m_distance;

    public Place(String pName, double pRating, String pAddress,
            double pDistance, String pCat, String pSortCat, String pImageURL) {
        m_address = pAddress;
        m_rating = pRating;
        m_name = pName;
        m_distance = pDistance;
        m_category = pCat;
        m_sortableCat = pSortCat;
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

    /* Comparator for sorting the list by roll no */
    public static Comparator<Place> distanceComparator = new Comparator<Place>() {

        public int compare(Place firstPlace, Place secondPlace) {

            double firstDist = firstPlace.getDistance();
            double secondDist = secondPlace.getDistance();

            return firstDist > secondDist ? 1 : -1;

        }
    };

}
