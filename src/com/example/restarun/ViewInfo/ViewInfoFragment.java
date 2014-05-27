package com.example.restarun.ViewInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restarun.R;
import com.google.android.gms.maps.SupportMapFragment;

public class ViewInfoFragment extends Fragment {

    private String m_name;
    private String m_address;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
