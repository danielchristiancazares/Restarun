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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restarun.R;
import com.example.restarun.ViewInfo.ViewInfo;
import com.example.restarun.gpsTracker.ServiceGPS;
import com.example.yelp.Place;
import com.example.yelp.YelpAPI;

public class SearchActivity extends ActionBarActivity {
    private static QuickSearchFilterFragment quickSearchFilterFrag;

    private static Location m_location;

    private static ArrayList<Place> mPlaces;
    private static ArrayList<String> m_categories;

    private static MyAdapter mAdapter;
    private static ViewPager mPager;

    /*
     * Button onClick() functions
     */
    public void ratingSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.ratingComparator );
        mAdapter = new MyAdapter( getSupportFragmentManager() );
        mPager.setAdapter( mAdapter );
    }

    public void distanceSort(View view) {
        if ( mPlaces.isEmpty() )
            return;

        Collections.sort( mPlaces, Place.distanceComparator );
        mAdapter = new MyAdapter( getSupportFragmentManager() );
        mPager.setAdapter( mAdapter );
    }

    public void getInfo(View view) {
        Intent mIntent = new Intent( this, ViewInfo.class );
        
        int getPos = mPager.getCurrentItem();
        Place curPlace = mPlaces.get( getPos );
        Log.d("DEBUG",getPos + "");
        // Supply num input as an argument.
        Bundle args = new Bundle();
        
        args.putString( "name", curPlace.getName() );
        args.putString( "address", curPlace.getAddress() );
        args.putString( "imageurl", curPlace.getImageURL() );
        args.putDouble( "rating", curPlace.getRating() );
        args.putDouble( "distance", curPlace.getDistance() );
        mIntent.putExtras(args);
        startActivityForResult( mIntent, 0 );
    }

    public void advancedSearch(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_search );

        quickSearchFilterFrag = new QuickSearchFilterFragment();
        getSupportFragmentManager().beginTransaction()
                .replace( R.id.botView, quickSearchFilterFrag ).commit();

        m_location = new ServiceGPS( this ).getLocation();
        mPlaces = new YelpAPI().getPlaces( m_location, "restaurant" );

        mAdapter = new MyAdapter( getSupportFragmentManager() );
        mPager = (ViewPager) findViewById( R.id.pager );
        mPager.setAdapter( mAdapter );

        m_categories = new ArrayList<String>();
        for ( Place aPlace : mPlaces ) {
            if ( m_categories.contains( aPlace.getCategory() ) ) {
                continue;
            }

            m_categories.add( aPlace.getCategory() );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return super.onCreateOptionsMenu( menu );
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

        public MyAdapter(FragmentManager fm) {
            super( fm );
            for ( int i = 0; mFragments.size() != mPlaces.size(); ++i ) {
                mFragments.add( QuickSearchFragment.newInstance( i ) );
            }
        }

        @Override
        public int getCount() {
            return mPlaces.size();
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
        private double mRating;
        private double mDistance;

        private DownloadImage imgTask;

        static QuickSearchFragment newInstance(int num) {
            QuickSearchFragment frag = new QuickSearchFragment();

            Place p = mPlaces.get( num );
            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putString( "name", p.getName() );
            args.putString( "address", p.getAddress() );
            args.putString( "imageurl", p.getImageURL() );
            args.putDouble( "rating", p.getRating() );
            args.putDouble( "distance", p.getDistance() );
            frag.setArguments( args );

            return frag;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate( savedInstanceState );

            // Store received Bundle arguments
            mName = getArguments().getString( "name" );
            mAddress = getArguments().getString( "address" );
            mImageURL = getArguments().getString( "imageurl" );
            mRating = getArguments().getDouble( "rating" );
            mDistance = getArguments().getDouble( "distance" );

            imgTask = new DownloadImage();

            imgTask.execute( mImageURL );
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
                image.setImageBitmap( imgTask.get() );
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return view;
        }

        private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

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
                    Log.d( "DEBUG", "Completed!" );
                    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(Bitmap result) {
            }

        }
    }

}
