package com.example.yelp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.os.AsyncTask;

public class YelpAPI extends AsyncTask<Double, Void, ArrayList<Place>> {
    private static final String YELP_CONSUMER_KEY = "kQt6c0n37McCFys-eTKRHw";
    private static final String YELP_CONSUMER_SECRET = "fRgOT-RxIHvB_z_7JAmP-we4gEc";
    private static final String YELP_TOKEN = "g0wYS5bxLJalAOUNNHgfMyCJq2QJnrbN";
    private static final String YELP_TOKEN_SECRET = "XOLETYDWykubOZoSdwNIVcw7WVs";

    @Override
    protected ArrayList<Place> doInBackground(Double... loc) {
        OAuthRequest request = new OAuthRequest( Verb.GET,
                "http://api.yelp.com/v2/search" );

        request.addQuerystringParameter( "term", "restaurant" );
        request.addQuerystringParameter( "ll", loc[0] + "," + loc[1] );

        OAuthService service = new ServiceBuilder().provider( YelpAuth.class )
                .apiKey( YELP_CONSUMER_KEY ).apiSecret( YELP_CONSUMER_SECRET )
                .build();

        service.signRequest( new Token( YELP_TOKEN, YELP_TOKEN_SECRET ),
                request );

        ArrayList<Place> foundPlaces = new ArrayList<Place>();
        try {
            JSONObject m_JSONResponse = new JSONObject( request.send()
                    .getBody() );

            // Store the entire array of businesses
            JSONArray businesses = m_JSONResponse.getJSONArray( "businesses" );

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

                StringBuilder m_AddressString = new StringBuilder();
                int j;
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
                String Number = m_business.get( "display_phone" ).toString();
                // Store the deals
                Deal m_newDeal = null;
                try {
                    JSONArray m_deals = m_business.getJSONArray( "deals" );
                    if ( m_deals != null ) {
                        for ( int k = 0; k < m_deals.length(); ++k ) {
                            String deal_id = m_deals.getJSONObject( k )
                                    .get( "id" ).toString();
                            String deal_title = m_deals.getJSONObject( k )
                                    .get( "title" ).toString();
                            String deal_url = m_deals.getJSONObject( k )
                                    .get( "url" ).toString();
                            String deal_start = m_deals.getJSONObject( k )
                                    .get( "time_start" ).toString();
                            m_newDeal = new Deal( deal_id, deal_title,
                                    deal_url, deal_start );
                        }
                    }
                } catch (JSONException e) {
                }

                double Rating = Float.parseFloat( m_business.get( "rating" )
                        .toString() );
                double Distance = Double.parseDouble( m_business.get(
                        "distance" ).toString() ) / 1609.34;
                Place newPlace = new Place( Name, Rating, Address, Distance,
                        Category, SortableCategory, ImageURL, Number );
                if ( m_newDeal != null ) {
                    newPlace.setDeal( m_newDeal );
                }
                foundPlaces.add( newPlace );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foundPlaces;
    }

}
