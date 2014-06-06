package com.example.restarun.SearchActivity;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restarun.R;

import com.example.yelp.Place;

public class QuickSearchFragment extends Fragment {

    private String mName;
    private String mImageURL;
    private String mAddress;
    private String mNumber;
    private String mGoogleAddress;
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
        frag.mGoogleAddress = p.m_googleAddress;

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
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
        if ( mIsClosed == true )
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
}