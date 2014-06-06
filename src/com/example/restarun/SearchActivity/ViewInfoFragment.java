package com.example.restarun.SearchActivity;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restarun.R;

public class ViewInfoFragment extends Fragment {

    String m_name, m_address;
    
    ImageView googleMap;
    
    ViewInfoFragment(String pName, String pAddress) {
        m_name = pName;
        m_address = pAddress;
    }
    public void setMap() {

        String unparsedURL = "http://maps.googleapis.com/maps/api/staticmap?center="
                + m_address
                + "&zoom=15&size=1000x1000&maptype=roadmap&markers="
                + m_address;
        String mapURL = unparsedURL.replaceAll( " ", "%20" );
        try {
            googleMap.setImageBitmap( new DownloadImage().execute( mapURL )
                    .get() );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_info, container, false );
        
        /* Set the information for the resetaurant information fragment */
        TextView infoName = (TextView) view.findViewById( R.id.info_name );
        infoName.setText( m_name );

        TextView infoAddr = (TextView) view.findViewById( R.id.info_addr );
        infoAddr.setText( m_address );
        
        googleMap = (ImageView) view.findViewById( R.id.map );
        
        setMap();
        
        return view;
    }
}
