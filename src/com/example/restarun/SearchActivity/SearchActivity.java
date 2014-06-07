package com.example.restarun.SearchActivity;

import info.androidhive.actionbar.model.SpinnerNavItem;
import info.androidhive.info.actionbar.adapter.TitleNavigationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.R;
import com.example.restarun.LoginActivity.MainActivity;
import com.example.restarun.ProfileActivity.ProfileFragment;
import com.example.restarun.User.User;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class SearchActivity extends ActionBarActivity{
    private static ArrayList<Place> mPlaces;
    private static ViewPager mPager;
    private static boolean profileBackStackFlag = true;
    private static boolean viewInfoBackStackFlag = true;
    //private static ViewInfoFragment viewInfoFragment = new ViewInfoFragment();
   // private static ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();

    private User mUser = User.getInstance();
    
    private ActionBar actionBar;
    private ArrayList<SpinnerNavItem> navSpinner;
    private TitleNavigationAdapter adapter;
    
    public void getInfo(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );
        /* Display the information fragment */
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        //TODO: Fix function call/member access.
        ViewInfoFragment viewInfoFragment = new ViewInfoFragment(currentPlace.getName(), currentPlace.getAddress());
        transaction.add( R.id.container, viewInfoFragment );
        if(viewInfoBackStackFlag) {
        	transaction.addToBackStack(null);
        }
        transaction.commit();

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
    }

    public void eatHere(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        if ( !mUser.containsItem( "been", currentPlace ) ) {
            mUser.addItem( "been", currentPlace );
        }
    }

    public void addFavorite(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        if ( !mUser.containsItem( "favorite", currentPlace ) ) {
            mUser.addItem( "favorite", currentPlace );
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
        getSupportFragmentManager().popBackStack();
        
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( false );
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );

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

        /* Load the list view */
        mPager = (ViewPager) findViewById( R.id.pager );
        MyAdapter mAdapter = new MyAdapter( this.getSupportFragmentManager(),
                mPlaces );
        mPager.setAdapter( mAdapter );
        mPager.setOffscreenPageLimit( mPlaces.size() );

        Toast.makeText( getBaseContext(), "Welcome, " + mUser.m_name,
                Toast.LENGTH_LONG ).show();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle presses on the action bar items */
        switch (item.getItemId()) {
        case R.id.profile:
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            ProfileFragment profileFragment = new ProfileFragment();
            transaction.add( R.id.container, profileFragment );
            if(profileBackStackFlag) {
            	transaction.addToBackStack(null);
            	profileBackStackFlag = false;
            }
            transaction.commit();
            
            return true;
            
        case R.id.logout:
        	return true;
        default:
            return super.onOptionsItemSelected( item );
        }
    }

    /* Button onClick() functions */
    public void ratingSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.ratingComparator );
        MyAdapter mAdapter = new MyAdapter( getSupportFragmentManager(),
                mPlaces );
        mPager.setAdapter( mAdapter );
    }

    public void distanceSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.distanceComparator );
        MyAdapter mAdapter = new MyAdapter( getSupportFragmentManager(),
                mPlaces );
        mPager.setAdapter( mAdapter );
    }

    public void openSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.openComparator );
        MyAdapter mAdapter = new MyAdapter( getSupportFragmentManager(),
                mPlaces );
        mPager.setAdapter( mAdapter );
    }

}
