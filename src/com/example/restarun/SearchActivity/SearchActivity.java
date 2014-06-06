package com.example.restarun.SearchActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restarun.R;
import com.example.restarun.LoginActivity.MainActivity;
import com.example.restarun.User.User;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

//latlng for LA
//34.0204989
//-118.4117325

public class SearchActivity extends ActionBarActivity {
    private static ArrayList<Place> mPlaces;
    private static ViewPager mPager;

    private static ViewInfoFragment viewInfoFragment = new ViewInfoFragment();
    private static ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();

    private User mUser = User.getInstance();

    public void getInfo(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        /* Set the information for the resetaurant information fragment */
        TextView infoName = (TextView) findViewById( R.id.info_name );
        infoName.setText( currentPlace.getName() );

        TextView infoAddr = (TextView) findViewById( R.id.info_addr );
        infoAddr.setText( currentPlace.getAddress() );

        /* Display the information fragment */
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.show( viewInfoFragment );
        transaction.commit();
        setMap( currentPlace.getName(), currentPlace.m_googleAddress );

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
    }

    public void eatHere(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        if ( !mUser.contains( "been", currentPlace ) ) {
            mUser.add( "been", currentPlace );
        }
    }

    public void addFavorite(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        if ( !mUser.contains( "favorite", currentPlace ) ) {
            mUser.add( "favorite", currentPlace );
        }
    }

    // TODO: Move to profile activity.
    public void doLogout(View view) {
        Intent intent = new Intent( this, MainActivity.class );
        Bundle args = new Bundle();
        args.putBoolean( "logout", true );
        intent.putExtras( args );
        startActivity( intent );
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide( viewInfoFragment );
        transaction.hide( profileInfoFragment );
        transaction.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( false );
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );

        /* Retrieve the login information from the previous activity */
        Bundle b = getIntent().getExtras();
        mUser.setName( b.getString( "user_name" ) );
        mUser.setPhoto( b.getString( "FB_photo" ) );

        /* Preload all layout fragments */
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add( R.id.container, viewInfoFragment );
        transaction.add( R.id.container, profileInfoFragment );
        transaction.hide( viewInfoFragment );
        transaction.hide( profileInfoFragment );
        transaction.commit();

        /* Find the user's current location */
        Location m_location = new ServiceGPS( this ).getLocation();

        /* Attempt to pass phone's GPS latitude and longitude */
        Double latitude = null;
        Double longitude = null;

        latitude = m_location.getLatitude();
        longitude = m_location.getLongitude();

        if ( latitude == null || longitude == null ) {
            latitude = 32.8762142;
            longitude = -117.2354577;
        }
        try {
            mPlaces = new YelpAPI().execute( latitude, longitude ).get();
        } catch (InterruptedException | ExecutionException e) {
        }

        /* Select a random restaurant in the list and move it to first position */
        // Random rand = new Random();
        // int pos = rand.nextInt( mPlaces.size() );
        // int pos = 1;
        // Collections.swap( mPlaces, 0, pos );

        /* Load the list view */
        mPager = (ViewPager) findViewById( R.id.pager );
        mPager.setAdapter( new MyAdapter( getSupportFragmentManager() ) );
        mPager.setOffscreenPageLimit( mPlaces.size() );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Add the action bar items to the view */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide( viewInfoFragment );
        transaction.hide( profileInfoFragment );
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle presses on the action bar items */
        switch (item.getItemId()) {
        case R.id.profile:
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.show( profileInfoFragment );

            /* Set user profile information */
            TextView usernameText = (TextView) findViewById( R.id.welcomeText );
            TextView been = (TextView) findViewById( R.id.been );
            TextView favorited = (TextView) findViewById( R.id.favorited );
            TextView savedText = (TextView) findViewById( R.id.savedText );
            TextView favoritedText = (TextView) findViewById( R.id.favoritedText );

            
            usernameText.append( mUser.m_name );

            been.setText( "been to " + mUser.getBeenPlaces().size()
                    + " place(s)" );
            favorited.setText( "favorited " + mUser.getFavoritedPlaces().size()
                    + " place(s)" );

            savedText.setText( "History: " );
            for ( int i = 0; i < mUser.getBeenPlaces().size(); ++i ) {
                savedText.append( mUser.getBeenPlaces().get( i ).getName() );
                savedText.append( "\n" );
            }

            favoritedText.setText( "Favorites: " );
            for ( int i = 0; i < mUser.getFavoritedPlaces().size(); ++i ) {
                favoritedText.append( mUser.getFavoritedPlaces().get( i )
                        .getName() );
                favoritedText.append( "\n" );
            }
            android.app.ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled( true );
            transaction.commit();
            return true;
        default:
            return super.onOptionsItemSelected( item );
        }
    }

    /* Swipe-able list implementation */
    public static class MyAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> mFragments;

        public MyAdapter(android.support.v4.app.FragmentManager pFm) {
            super( pFm );

            mFragments = new ArrayList<Fragment>();
            for ( int i = 0; mFragments.size() != mPlaces.size(); ++i ) {
                mFragments.add( i, QuickSearchFragment.newInstance( i ) );
            }
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get( position );
        }
    }

    /* Button onClick() functions */
    public void ratingSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.ratingComparator );
        mPager.setAdapter( new MyAdapter( getSupportFragmentManager() ) );
    }

    public void distanceSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.distanceComparator );
        mPager.setAdapter( new MyAdapter( getSupportFragmentManager() ) );
    }

    public void openSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.openComparator );
        mPager.setAdapter( new MyAdapter( getSupportFragmentManager() ) );
    }

}
