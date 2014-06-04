package com.example.restarun.LoginActivity;

import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
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

    public User m_user;
    private boolean action = false;
    
    private static UiLifecycleHelper uihelper;

    private Request.GraphUserCallback requestGraphUserCallback;
    private Session.StatusCallback sessionStatusCallback = new SessionStatusCallback();

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            if ( session.isOpened() ) {
                Request.newMeRequest( session, requestGraphUserCallback )
                        .executeAsync();
            }

        }
    }

    private class RequestGraphUserCallback implements Request.GraphUserCallback {
        @Override
        public void onCompleted(GraphUser user, Response response) {
            if ( user != null ) {
                m_user.m_name = user.getName();
                Log.d( "DEBUG", "user is " + m_user.m_name );
                performLogin();
            }
        }
    }

    private void performLogin() {
        Log.d("DEBUG",getDeviceId());
        Intent intent = new Intent( this, SearchActivity.class );
        Bundle args = new Bundle();
        Log.d( "DEBUG", "performLogin() Passing: " + m_user.m_name );
        args.putString( "user_name", m_user.m_name );
        args.putString( "FB_photo", m_user.m_fbPhoto );
        intent.putExtras( args );
        startActivity( intent );
        finish();
    }

    public void guestLogin(View view) {
        m_user.m_name = "Guest";
        m_user.m_fbPhoto = null;
        performLogin();
    }

    public static void performLogout(Context context) {
        Log.d("DEBUG","Performing logout");
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
    // 8 digits - 4 characters - 4 characters - 4 characters - 12 characters
    // 8 + 4 + 12 + 12 = 12 + 12 + 12 = 36
    public String getDeviceId() {
        // TelephonyManager objects provide access to different phone services
        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService( Context.TELEPHONY_SERVICE );

        final String tmDevice, tmSerial, androidId;

        // getDeviceId returns the phone's IMEI or MEID/ESN
        tmDevice = (String) tm.getDeviceId();

        // getSimSerialNumber returns the SIM card serial number
        tmSerial = (String) tm.getSimSerialNumber();

        // Android OS creates a 64-bit random number on first device boot
        androidId = (String) android.provider.Settings.Secure.getString(
                getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID );

        // We use the androidId, tmDevice, and tmSerial to create a unique UDID
        UUID deviceUuid = new UUID( androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode() );

        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        getSupportFragmentManager().beginTransaction()
                .replace( R.id.container, new LoginFragment() ).commit();
        Typeface font = Typeface.createFromAsset( this.getAssets(),
                "fonts/Roboto-Regular.ttf" );

        m_user = new User();

        sessionStatusCallback = new SessionStatusCallback();
        requestGraphUserCallback = new RequestGraphUserCallback();

        uihelper = new UiLifecycleHelper( this, sessionStatusCallback );
        uihelper.onCreate( savedInstanceState );

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
        uihelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uihelper.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        uihelper.onSaveInstanceState( outState );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uihelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        uihelper.onActivityResult( requestCode, resultCode, data );
    }
}