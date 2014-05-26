package com.example.yelp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

public class YelpAPI {

    public ArrayList<Place> getPlaces(Location pLoc, String searchTerm) {

        // Instantiate a Yelp object to send our Yelp API call
        Yelp m_Yelp = new Yelp( searchTerm, pLoc.getLatitude(),
                pLoc.getLongitude() );
        m_Yelp.execute();

        // Store the response received from Yelp
        String m_Response = null;
        try {
            m_Response = m_Yelp.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Store the JSON response
        JSONObject m_JSONResponse;
        ArrayList<Place> foundPlaces = new ArrayList<Place>();

        try {
            m_JSONResponse = new JSONObject( m_Response );

            // Store the entire array of businesses
            JSONArray businesses = m_JSONResponse.getJSONArray( "businesses" );

            int j;
            for ( int i = 0; i < businesses.length(); ++i ) {
                // Store the current business object
                JSONObject m_business = businesses.getJSONObject( i );
                // Store the location
                JSONObject m_location = m_business.getJSONObject( "location" );
                // Store the category
                JSONArray m_CatArray = m_business.getJSONArray( "categories" );
                // Store the address
                JSONArray m_displayAddress = m_location
                        .getJSONArray( "display_address" );
                // Store the deals
                JSONArray m_deals = null;
                try
                {
                    m_deals = m_business.getJSONArray( "deals" );
                }
                catch(JSONException e)
                {
                    Log.w("Restarun.err", "No value for deals");
                }
                if ( m_deals != null ) {
                    Log.d( "Restarun.dbg", "Deal found" );
                }
                StringBuilder m_AddressString = new StringBuilder();
                for ( j = 0; j < m_displayAddress.length() - 1; ++j ) {
                    m_AddressString.append( m_displayAddress.get( j )
                            .toString() + "\n" );
                }
                m_AddressString.append( m_displayAddress.get( j ).toString() );

                JSONArray m_categories = m_CatArray.getJSONArray( 0 );
                String Category = m_categories.get( 0 ).toString();
                String SortableCategory = m_categories.get( 1 ).toString();

                String Name = m_business.get( "name" ).toString();
                String ImageURL = m_business.get( "image_url" ).toString();
                String Address = m_AddressString.toString();
                double Rating = Float.parseFloat( m_business.get( "rating" )
                        .toString() );
                double Distance = Double.parseDouble( m_business.get(
                        "distance" ).toString() ) / 1609.34;
                Place newPlace = new Place( Name, Rating, Address, Distance,
                        Category, SortableCategory, ImageURL );
                foundPlaces.add( newPlace );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foundPlaces;

    }
}
