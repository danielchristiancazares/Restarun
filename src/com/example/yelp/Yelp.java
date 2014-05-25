package com.example.yelp;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author danielcazares
 * @description Our Yelp class extends AsyncTask to allow web-related requests
 *              to run on a seperate thread from the main.
 */
public class Yelp extends AsyncTask<Void, Void, String> {
    OAuthService service;
    Token accessToken;
    String m_Response = null;
    String m_Term;
    double m_Latitude, m_Longitude;

    // Yelp API call required constants
    static private final String YELP_CONSUMER_KEY = "kQt6c0n37McCFys-eTKRHw";
    static private final String YELP_CONSUMER_SECRET = "fRgOT-RxIHvB_z_7JAmP-we4gEc";
    static private final String YELP_TOKEN = "g0wYS5bxLJalAOUNNHgfMyCJq2QJnrbN";
    static private final String YELP_TOKEN_SECRET = "XOLETYDWykubOZoSdwNIVcw7WVs";

    /** Yelp's constructor takes in the search term, and */
    public Yelp(String pTerm, double pLat, double pLong) {
        service = new ServiceBuilder().provider( YelpAuth.class )
                .apiKey( YELP_CONSUMER_KEY ).apiSecret( YELP_CONSUMER_SECRET )
                .build();
        m_Latitude = pLat;
        m_Longitude = pLong;
        accessToken = new Token( YELP_TOKEN, YELP_TOKEN_SECRET );
    }

    @Override
    protected String doInBackground(Void... params) {
        OAuthRequest request = new OAuthRequest( Verb.GET,
                "http://api.yelp.com/v2/search" );

        request.addQuerystringParameter( "term", "restaurant" );
        request.addQuerystringParameter( "ll", m_Latitude + "," + m_Longitude );

        service.signRequest( accessToken, request );

        Response response = request.send();
        Log.d("DEBUG",response.getBody());
        return response.getBody();
    }

    @Override
    protected void onPostExecute(String result) {
    }


}
