package com.example.restarun.SearchActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

    private static MyAdapter mAdapter;
    private static ViewPager mPager;

    private static ViewInfoFragment newFragment = new ViewInfoFragment();

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
        // Create new fragment and transaction

        int getPos = mPager.getCurrentItem();
        Place curPlace = mPlaces.get( getPos );
        // Supply num input as an argument.
        Bundle args = new Bundle();

        args.putString( "name", curPlace.getName() );
        args.putString( "address", curPlace.getAddress() );
        args.putString( "number", curPlace.getNumber() );
        args.putDouble( "rating", curPlace.getRating() );
        args.putDouble( "distance", curPlace.getDistance() );

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.show( newFragment );
        transaction.commit();

    }

    public void endTask(View view) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.hide(newFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_search );

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add( R.id.container, newFragment );
        transaction.addToBackStack( null );
        transaction.hide( newFragment );
        transaction.commit();

        Location m_location = new ServiceGPS( this ).getLocation();

        try {
            mPlaces = new YelpAPI().execute( m_location.getLatitude(),
                    m_location.getLongitude() ).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mAdapter = new MyAdapter( getSupportFragmentManager() );
        mPager = (ViewPager) findViewById( R.id.pager );
        mPager.setAdapter( mAdapter );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return super.onCreateOptionsMenu( menu );
    }

    public static class MyAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

        public MyAdapter(android.support.v4.app.FragmentManager pFm) {
            super( pFm );
            for ( Place p : mPlaces ) {
                mFragments.add( QuickSearchFragment.newInstance( mPlaces
                        .indexOf( p ) ) );
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

        private static String mName;
        private static String mImageURL;
        private static String mAddress;
        private static String mNumber;
        private static double mRating;
        private static double mDistance;

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

            // Store received Bundle arguments
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
