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

        /* Instantiate a new request */
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

        /* Variables stored from the JSON response */
        JSONObject jsonResponse;
        JSONArray unparsedBsnsArray;

        JSONObject current;
        JSONObject location;
        JSONObject open;
        JSONArray categories;
        JSONArray displayAddress;
        JSONObject googleAddress;
        String GoogleAddress;
        
        try {
            jsonResponse = new JSONObject( request.send().getBody() );
            /* Store the entire array of businesses */
            unparsedBsnsArray = jsonResponse.getJSONArray( "businesses" );

            /* Loop through all the unparsed businesses and store the list */
            for ( int i = 0; i < unparsedBsnsArray.length(); ++i ) {

                /* Store current business object */
                current = unparsedBsnsArray.getJSONObject( i );

                /* Store whether or not the business is currently open */
                Boolean is_closed = current.getBoolean( "is_closed" );

                /* Store display category and internal category */
                categories = current.getJSONArray( "categories" );

                /* Store the address */
                displayAddress = current.getJSONObject( "location" )
                        .getJSONArray( "display_address" );
                
                googleAddress = current.getJSONObject( "location" );
                String gAdd1 = googleAddress.getJSONArray( "address" ).get(0).toString();
                String gAdd2 = googleAddress.get( "city" ).toString();
                
                GoogleAddress = gAdd1 + "," + gAdd2;
                String Address = "";
                int j;
                for ( j = 0; j < displayAddress.length() - 1; ++j ) {
                    Address += (displayAddress.get( j ).toString() + "\n");
                }
                Address += (displayAddress.get( j ).toString());

                JSONArray m_categories = categories.getJSONArray( 0 );

                String Name = current.get( "name" ).toString();
                String ImageURL = current.get( "image_url" ).toString();
                String Category = m_categories.get( 0 ).toString();
                String SortableCategory = m_categories.get( 1 ).toString();
                String Number = null;
                try {
                    Number = current.get( "display_phone" ).toString();
                } catch (JSONException e) {

                }
                double Rating = Float.parseFloat( current.get( "rating" )
                        .toString() );
                double Distance = Double.parseDouble( current.get( "distance" )
                        .toString() ) / 1609.34;
                Place newPlace = new Place( Name, Rating, Address, Distance,
                        Category, SortableCategory, ImageURL, Number, is_closed );
                newPlace.m_googleAddress = GoogleAddress;
                foundPlaces.add( newPlace );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foundPlaces;
    }
}
