package com.example.restarun.ViewInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.restarun.R;
import com.example.yelp.Place;

public class ViewInfo extends ActionBarActivity {

    private String m_name;
    private String m_address;
    private String m_number;

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

        
        TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);
        TextView number = (TextView) findViewById(R.id.number);
        
        name.setText(m_name);
        address.setText(m_address);
        number.setText(m_number);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );
        return super.onCreateOptionsMenu( menu );
    }
}
