package com.example.restarun.ProfileActivity;

import info.androidhive.actionbar.model.SpinnerNavItem;
import info.androidhive.info.actionbar.adapter.TitleNavigationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.R;
import com.example.restarun.LoginActivity.MainActivity;
import com.example.restarun.SearchActivity.ExpandableListAdapter;
import com.example.restarun.User.User;
import com.example.yelp.Place;
import com.facebook.widget.ProfilePictureView;

public class ProfileActivity extends ActionBarActivity {
    private TextView usernameText;
    private ProfilePictureView profilePictureView;

    private User mUser = User.getInstance();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );

        usernameText = (TextView) findViewById( R.id.welcomeText );

        profilePictureView = (ProfilePictureView) findViewById( R.id.selection_profile_pic );
        profilePictureView.setCropped( true );

        usernameText.setText( mUser.m_name );
        profilePictureView.setProfileId( mUser.m_photo );

        // get the listview
        expListView = (ExpandableListView) findViewById( R.id.lvExp );

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter( this, listDataHeader,
                listDataChild );

        // setting list adapter
        expListView.setAdapter( listAdapter );

        // Listview Group click listener
        expListView.setOnGroupClickListener( new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        } );

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener( new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText( getApplicationContext(),
                        listDataHeader.get( groupPosition ) + " Expanded",
                        Toast.LENGTH_SHORT ).show();
            }
        } );

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener( new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText( getApplicationContext(),
                        listDataHeader.get( groupPosition ) + " Collapsed",
                        Toast.LENGTH_SHORT ).show();

            }
        } );

        // Listview on child click listener
        expListView.setOnChildClickListener( new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get( groupPosition )
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get( groupPosition ) )
                                        .get( childPosition ),
                        Toast.LENGTH_SHORT ).show();
                return false;
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Add the action bar items to the view */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.profile, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle presses on the action bar items */
        switch (item.getItemId()) {
        case R.id.logout:
            mUser.logoutCalled = true;
            Intent logoutIntent = new Intent( this, MainActivity.class );
            startActivity( logoutIntent );
            finish();
            return true;

        default:
            return super.onOptionsItemSelected( item );
        }
    }

    
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add( "Favorites" );
        listDataHeader.add( "History" );

        // Adding child data
        List<String> favorites = new ArrayList<String>();
        for ( Place p : mUser.favoritedPlaces ) {
            favorites.add( p.m_name );
        }

        List<String> history = new ArrayList<String>();
        for ( Place p : mUser.beenPlaces ) {
            history.add( p.m_name );
        }

        listDataChild.put( listDataHeader.get( 0 ), favorites ); // Header,
                                                                 // Child
        listDataChild.put( listDataHeader.get( 1 ), history );
    }

}
