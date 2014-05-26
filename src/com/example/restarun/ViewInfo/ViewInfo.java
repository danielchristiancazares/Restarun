package com.example.restarun.ViewInfo;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.restarun.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class ViewInfo extends ActionBarActivity {

    private String m_name;
    private String m_address;
    private String m_number;
    private Double m_latitude;
    private Double m_longitude;
    
    private static GoogleMap googleMap;

    public void endTask(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_info_view );
        m_name = getIntent().getExtras().getString( "name" );
        m_address = getIntent().getExtras().getString( "address" );
        m_number = getIntent().getExtras().getString( "number" );

        TextView name = (TextView) findViewById( R.id.name );
        TextView address = (TextView) findViewById( R.id.address );
        TextView number = (TextView) findViewById( R.id.number );

        name.setText( m_name );
        address.setText( m_address );
        number.setText( m_number );

        googleMap = ((MapFragment) getFragmentManager()
                .findFragmentById( R.id.map )).getMap();
        Geocoder coder = new Geocoder( this );
        Address location = null;

        try {

            List<Address> addressList;
            addressList = coder.getFromLocationName( m_address, 5 );
            if ( address != null ) {

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
                                    .getLongitude() ) ).zoom( 12 ).build();

            googleMap.moveCamera( CameraUpdateFactory
                    .newCameraPosition( cameraPosition ) );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
