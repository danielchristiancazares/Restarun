package com.example.restarun.SearchActivity;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.restarun.R;

public class ViewInfoFragment extends Fragment {

    public void setMap(String m_name, String m_address) {

        ImageView googleMap = (ImageView) findViewById( R.id.map );
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
        return inflater.inflate( R.layout.fragment_info, container, false );
    }
}
