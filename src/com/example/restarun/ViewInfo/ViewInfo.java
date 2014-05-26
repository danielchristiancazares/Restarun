package com.example.restarun.ViewInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.restarun.R;

public class ViewInfo extends ActionBarActivity {

    String mName;

    public void endTask(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_info_view );
        mName = getIntent().getExtras().getString( "name" );
        TextView name = (TextView) findViewById(R.id.textView1);
        name.setText(mName);

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
