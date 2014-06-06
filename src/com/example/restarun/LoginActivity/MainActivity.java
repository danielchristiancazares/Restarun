package com.example.restarun.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.example.restarun.R;
import com.example.restarun.SearchActivity.SearchActivity;
import com.example.restarun.User.User;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class MainActivity extends ActionBarActivity {

    public User mUser = User.getInstance();
    private boolean action = false;
   
   // private static UiLifecycleHelper uihelper;

    


    private void performLogin() {
        Intent intent = new Intent( this, SearchActivity.class );        
        startActivity( intent );
        finish();
    }

    public void guestLogin(View view) {
        mUser.m_name = "Guest";
        mUser.m_photo = "";
        performLogin();
    }

    public static void performLogout(Context context) {
        Session session = Session.getActiveSession();
        if ( session != null ) {
            if ( !session.isClosed() ) {
                session.closeAndClearTokenInformation();
                // clear your preferences if saved
            }
        } else {
            session = new Session( context );
            Session.setActiveSession( session );
            session.closeAndClearTokenInformation();
            // clear your preferences if saved
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        
        
        getSupportFragmentManager().beginTransaction()
                .replace( R.id.container, new LoginFragment() ).commit();
        Typeface font = Typeface.createFromAsset( this.getAssets(),
                "fonts/Roboto-Regular.ttf" );
       
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
              if (session.isOpened()) {

                // make request to the /me API
                Request.newMeRequest(session, new Request.GraphUserCallback() {

                  // callback after Graph API response with user object
                  @Override
                  public void onCompleted(GraphUser user, Response response) {
                      if(user != null)
                          Log.d("DEBUG","fbTest");
                  }
                }).executeAsync();
              }
            }
          });
        //uihelper = new UiLifecycleHelper( this, sessionStatusCallback );
        //uihelper.onCreate( savedInstanceState );
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        
        try {
            Bundle b = getIntent().getExtras();
            action = b.getBoolean( "logout" );
            if ( action != false )
                performLogout( getBaseContext() );
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        if ( session != null && (session.isOpened() || session.isClosed()) ) {
            if( action != true )
                performLogin();
        }
        //uihelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //uihelper.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        //uihelper.onSaveInstanceState( outState );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  uihelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        //uihelper.onActivityResult( requestCode, resultCode, data );
    }
}