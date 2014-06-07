package com.example.restarun.SearchActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restarun.R;
import com.example.yelp.Place;

public class ViewInfoFragment extends Fragment {

    Place m_place;
    ImageView googleMap;
    Bitmap m_bitmap;

    public ViewInfoFragment() {
        Log.d("DEBUG", "Empty ctor");
    }
    
    public ViewInfoFragment(Place pPlace) {
        m_place = pPlace;
    }

    public void setMap() {
        googleMap.setImageBitmap( m_bitmap );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_info, container, false );

        /* Set the information for the resetaurant information fragment */
        TextView infoName = (TextView) view.findViewById( R.id.info_name );
        infoName.setText( m_place.m_name );

        TextView infoAddr = (TextView) view.findViewById( R.id.info_addr );
        infoAddr.setText( m_place.m_address );

        googleMap = (ImageView) view.findViewById( R.id.map );
        setMap();
        return view;
    }
}
