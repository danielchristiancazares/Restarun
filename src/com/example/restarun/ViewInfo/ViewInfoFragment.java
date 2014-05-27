package com.example.restarun.ViewInfo;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restarun.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class ViewInfoFragment extends Fragment {

    private String m_name;
    public String m_address;
    private String m_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_info, container, false );
        initilizeMap();
        return view;
    }

    private void initilizeMap() {
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById( R.id.map );

        if ( mSupportMapFragment == null ) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace( R.id.map, mSupportMapFragment );
            fragmentTransaction.commit();
        }

    }

    public void setMap() {

        GoogleMap googleMap = ( (SupportMapFragment) getFragmentManager()
                .findFragmentById( R.id.map )).getMap();
        Geocoder coder = new Geocoder( this.getActivity() );
        Address location = null;

        try {

            List<Address> addressList;
            addressList = coder.getFromLocationName( m_address, 5 );
            if ( addressList != null ) {

                location = addressList.get( 0 );
                location.getLatitude();
                location.getLongitude();

            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        if ( location != null ) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(
                            new LatLng( location.getLatitude(), location
                                    .getLongitude() ) ).zoom( 15 ).build();

            googleMap.moveCamera( CameraUpdateFactory
                    .newCameraPosition( cameraPosition ) );
        }
    }
    
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
