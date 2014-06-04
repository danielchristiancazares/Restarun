package com.example.restarun.SearchActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
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

public class SearchActivity extends ActionBarActivity {
    private static ArrayList<Place> mPlaces;

    private static ViewPager mPager;

    private static ViewInfoFragment viewInfoFragment = new ViewInfoFragment();
    private static ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();

    private User mUser = new User();

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

    public void getInfo(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        /* Set the information for the information fragment */
        TextView infoName = (TextView) findViewById( R.id.info_name );
        infoName.setText( currentPlace.getName() );

        TextView infoAddr = (TextView) findViewById( R.id.info_addr );
        infoAddr.setText( currentPlace.getAddress() );

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.show( viewInfoFragment );
        viewInfoFragment.m_name = currentPlace.getName();
        viewInfoFragment.m_address = currentPlace.getAddress();
        viewInfoFragment.setMap();
        transaction.commit();

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
    }

    public void eatHere(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        if ( !mUser.beenPlaces.contains( currentPlace ) ) {
            mUser.beenPlaces.add( currentPlace );
            Log.d( "DEBUG", "Added to history: " + currentPlace.m_name );
        } else {
            Log.d( "DEBUG", "Already saved: " + currentPlace.m_name );
        }
    }

    public void addFavorite(View view) {
        /* Find the current restaurant selected */
        int currentPos = mPager.getCurrentItem();
        Place currentPlace = mPlaces.get( currentPos );

        if ( !mUser.favoritedPlaces.contains( currentPlace ) ) {
            mUser.favoritedPlaces.add( currentPlace );
            Log.d( "DEBUG", "Added to favorites: " + currentPlace.m_name );
        } else {
            Log.d( "DEBUG", "Already favorited: " + currentPlace.m_name );
        }
    }
    
    public void doLogout(View view) {
        /* Preload all layout fragments */
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.remove( viewInfoFragment );
        transaction.remove( profileInfoFragment );
        Log.d( "DEBUG", "Doin' the dirty..." );
        transaction.commit();

        Intent intent = new Intent( this, MainActivity.class );
        Bundle args = new Bundle();
        args.putBoolean( "logout", true );
        intent.putExtras( args );
        startActivity( intent );
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide( viewInfoFragment );
        transaction.hide( profileInfoFragment );
        transaction.commit();
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
        mUser.m_name = b.getString( "user_name" );
        Log.d( "DEBUG", "Receiving: " + mUser.m_name );
        mUser.m_fbPhoto = b.getString( "FB_photo" );
        mUser.beenPlaces = new ArrayList<Place>();
        mUser.favoritedPlaces = new ArrayList<Place>();

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
        try {
            mPlaces = new YelpAPI().execute( m_location.getLatitude(),
                    m_location.getLongitude() ).get();
        } catch (NullPointerException e) {
            /* Default to pre-set coordinates in case GPS fails */
            try {
                Log.w( "Restarun",
                        "GPS coordinates not found!\nFalling back to predefined coordinates" );
                mPlaces = new YelpAPI().execute( 32.8762142, -117.2354577 )
                        .get();
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        /* Select a random restaurant in the list and move it to first position */
        Random rand = new Random();
        int pos = rand.nextInt( mPlaces.size() );
        Collections.swap( mPlaces, 0, pos );

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
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle presses on the action bar items */
        switch (item.getItemId()) {
        case R.id.profile:
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.show( profileInfoFragment );

            /* Set user profile information */
            TextView usernameText = (TextView) findViewById( R.id.welcomeText );
            usernameText.setText( "Hi, " + mUser.m_name );
            TextView been = (TextView) findViewById( R.id.been );
            been.setText( "been to " + mUser.beenPlaces.size() + " place(s)" );
            // TextView saved = (TextView) findViewById( R.id.saved );
            // saved.setText( "saved " + mUser.savedDeals.size() + " deal(s)" );
            TextView favorited = (TextView) findViewById( R.id.favorited );
            favorited.setText( "favorited " + mUser.favoritedPlaces.size()
                    + " place(s)" );

            TextView savedText = (TextView) findViewById( R.id.savedText );
            savedText.setText( "History: " );
            for ( int i = 0; i < mUser.beenPlaces.size(); ++i ) {
                savedText.append( mUser.beenPlaces.get( i ).getName() );
                savedText.append( "\n" );

            }
            
            TextView favoritedText = (TextView) findViewById( R.id.favoritedText );
            favoritedText.setText( "Favorites: " );
            for ( int i = 0; i < mUser.favoritedPlaces.size(); ++i ) {
                favoritedText.append( mUser.favoritedPlaces.get( i ).getName() );
                favoritedText.append( "\n" );

            }
            
            Log.d( "DEBUG", mUser.m_name );
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

    public static class QuickSearchFragment extends Fragment {

        private String mName;
        private String mImageURL;
        private String mAddress;
        private String mNumber;
        private double mRating;
        private double mDistance;
        private Boolean mIsClosed;

        static QuickSearchFragment newInstance(int num) {
            QuickSearchFragment frag = new QuickSearchFragment();

            Place p = mPlaces.get( num );
            frag.mName = p.m_name;
            frag.mImageURL = p.m_imageURL;
            frag.mAddress = p.m_address;
            frag.mNumber = p.m_number;
            frag.mRating = p.m_rating;
            frag.mDistance = p.m_distance;
            frag.mIsClosed = p.m_isClosed;

            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );
            Log.d("DEBUG","onCreate()");

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.d("DEBUG","onCreateView()");

            View view = inflater.inflate( R.layout.fragment_quicksearch,
                    container, false );

            ImageView image = (ImageView) view.findViewById( R.id.imageView1 );
            TextView name = (TextView) view.findViewById( R.id.name );
            TextView distance = (TextView) view.findViewById( R.id.distance );
            TextView closed = (TextView) view.findViewById( R.id.closed );

            RatingBar rating = (RatingBar) view.findViewById( R.id.ratingBar );
            rating.setIsIndicator( true );

            Typeface font = Typeface.createFromAsset(
                    getActivity().getAssets(), "fonts/Roboto-Regular.ttf" );
            Typeface font2 = Typeface.createFromAsset( getActivity()
                    .getAssets(), "fonts/Roboto-Light.ttf" );

            name.setTextColor( Color.WHITE );
            distance.setTextColor( Color.WHITE );
            closed.setTextColor( Color.WHITE );

            name.setTypeface( font );
            distance.setTypeface( font2 );
            closed.setTypeface( font2 );

            name.setText( mName );
            if ( mIsClosed == false )
                closed.setText( "Currently closed!" );
            else
                closed.setText( "Currently open!" );

            rating.setRating( (float) mRating );

            DecimalFormat fmtDistance = new DecimalFormat( "##.##" );
            distance.setText( fmtDistance.format( mDistance ) + " miles away" );

            try {
                image.setImageBitmap( new DownloadImage().execute( mImageURL )
                        .get() );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return view;
        }

        private static class DownloadImage extends
                AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    URL url = new URL( strings[0] );
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setDoInput( true );
                    connection.connect();

                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream( input );
                    connection.disconnect();
                    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }
    }

}
