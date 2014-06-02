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
import com.example.restarun.ViewInfo.ViewInfoFragment;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class SearchActivity extends ActionBarActivity {
    private static ArrayList<Place> mPlaces;

    private static ViewPager mPager;

    private static ViewInfoFragment newFragment = new ViewInfoFragment();

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

    public void getInfo(View view) {
        /* Find the current restaurant selected */
        int getPos = mPager.getCurrentItem();
        Place curPlace = mPlaces.get( getPos );

        /* Set the information for the information fragment */
        TextView infoName = (TextView) findViewById( R.id.info_name );
        infoName.setText( curPlace.getName() );

        TextView infoAddr = (TextView) findViewById( R.id.info_addr );
        infoAddr.setText( curPlace.getAddress() );

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.show( newFragment );
        newFragment.m_name = curPlace.getName();
        newFragment.m_address = curPlace.getAddress();
        newFragment.setMap();
        transaction.commit();

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( true );
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide( newFragment );
        transaction.commit();
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled( false );
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );

        Bundle b = getIntent().getExtras();
        String fbPhotoAddr = b.getString("FB_photo");
        
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        /* Preload the fragment containing the restaurant information layout */
        transaction.add( R.id.container, newFragment );
        transaction.hide( newFragment );
        transaction.commit();

        /* Find the user's current location */
        Location m_location = new ServiceGPS( this ).getLocation();

        /* Try to pass the user's latitude and longitude */
        try {
            mPlaces = new YelpAPI().execute( m_location.getLatitude(),
                    m_location.getLongitude() ).get();
        } catch (NullPointerException e) {
            /* Default to pre-set coordinates in order */
            try {
                mPlaces = new YelpAPI().execute( 32.8762142, -117.2354577 )
                        .get();
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        /* Select a random item in the list and place it in the first position */
        Random rand = new Random();
        int pos = rand.nextInt( mPlaces.size() );
        Collections.swap( mPlaces, 0, pos );

        /* Load the scrollable list */
        mPager = (ViewPager) findViewById( R.id.pager );
        mPager.setAdapter( new MyAdapter( getSupportFragmentManager() ) );
        mPager.setOffscreenPageLimit( mPlaces.size() );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
        case R.id.profile:
            Log.d( "DEBUG", "Clicked" );
            return true;
        default:
            return super.onOptionsItemSelected( item );
        }
    }

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

        static QuickSearchFragment newInstance(int num) {
            QuickSearchFragment frag = new QuickSearchFragment();

            Place p = mPlaces.get( num );
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString( "name", p.getName() );
            args.putString( "address", p.getAddress() );
            args.putString( "imageurl", p.getImageURL() );
            args.putString( "number", p.getNumber() );
            args.putDouble( "rating", p.getRating() );
            args.putDouble( "distance", p.getDistance() );
            frag.setArguments( args );

            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );

            /* Store received Bundle arguments */
            mName = getArguments().getString( "name" );
            mAddress = getArguments().getString( "address" );
            mImageURL = getArguments().getString( "imageurl" );
            mNumber = getArguments().getString( "number" );
            mRating = getArguments().getDouble( "rating" );
            mDistance = getArguments().getDouble( "distance" );
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View view = inflater.inflate( R.layout.fragment_quicksearch,
                    container, false );

            ImageView image = (ImageView) view.findViewById( R.id.imageView1 );
            TextView name = (TextView) view.findViewById( R.id.name );
            TextView distance = (TextView) view.findViewById( R.id.distance );
            RatingBar rating = (RatingBar) view.findViewById( R.id.ratingBar );
            rating.setIsIndicator( true );

            Typeface font = Typeface.createFromAsset(
                    getActivity().getAssets(), "fonts/Roboto-Regular.ttf" );
            Typeface font2 = Typeface.createFromAsset( getActivity()
                    .getAssets(), "fonts/Roboto-Light.ttf" );

            name.setTextColor( Color.WHITE );
            distance.setTextColor( Color.WHITE );

            name.setTypeface( font );
            distance.setTypeface( font2 );

            name.setText( mName );

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
