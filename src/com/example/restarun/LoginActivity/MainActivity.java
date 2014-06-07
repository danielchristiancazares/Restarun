package com.example.restarun.LoginActivity;

import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.facebook.widget.LoginButton;

public class MainActivity extends FragmentActivity {

    public User mUser = User.getInstance();

    private static final int SPLASH = 0;
    private static final int SELECTION = 1;
    private Fragment loginFragment;
    private boolean isResumed = false;

    private UiLifecycleHelper uiHelper;
    private static LoginButton authButton;

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(final Session session, final SessionState state,
                final Exception exception) {
            onSessionStateChange( session, state, exception );
        }
    };

    private void onSessionStateChange(final Session session,
            SessionState state, Exception exception) {
        if ( session != null && session.isOpened() ) {
            // Get the user's data.
            makeMeRequest( session );
        }
    }

    private void makeMeRequest(final Session session) {
        // Make an API call to get user data and define a
        // new callback to handle the response.
        Request request = Request.newMeRequest( session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if ( session == Session.getActiveSession() ) {
                            if ( user != null ) {
                                mUser.m_name = user.getName();
                                mUser.m_photo = user.getId();
                                startSearch();
                            }
                        }
                        if ( response.getError() != null ) {
                            // Handle errors, will do so later.
                        }
                    }
                } );
        request.executeAsync();
    }

    public void guestLogin(View view) {
        mUser.m_name = "Guest";
        mUser.m_photo = "";
        startSearch();
    }

    private void startSearch() {
        Intent intent = new Intent( this, SearchActivity.class );
        startActivity( intent );
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        onLogout();
        uiHelper = new UiLifecycleHelper( this, statusCallback );
        uiHelper.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        Typeface font = Typeface.createFromAsset( getAssets(),
                "fonts/Roboto-Regular.ttf" );

        /* Store references to the buttons */
        authButton = (LoginButton) findViewById( R.id.authButton );
        /* Grant Facebook permissions */

        authButton.setReadPermissions( Arrays.asList( "public_profile",
                "user_likes", "user_status" ) );
        authButton.setBackgroundResource( R.drawable.buttons );
        authButton.setTypeface( font );
        authButton.setEnabled( true );

        Session.openActiveSession( this, true, statusCallback );
    }

    public static void callFacebookLogout(Context context) {
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
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
       // onLogout();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //onLogout();
    }

    public void onLogout() {
        if ( mUser.logoutCalled == true )
            callFacebookLogout( getBaseContext() );
        mUser.logoutCalled = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        isResumed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        uiHelper.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        uiHelper.onSaveInstanceState( outState );
    }
}