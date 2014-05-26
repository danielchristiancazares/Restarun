package com.example.restarun.LoginActivity;

import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.example.restarun.R;
import com.example.restarun.SearchActivity.SearchActivity;

public class MainActivity extends ActionBarActivity {

    public void guestLogin(View view) {
        Intent intent = new Intent( this, SearchActivity.class );
        startActivity( intent );
        finish();
    }

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

        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction()
                .add( R.id.container, loginFragment ).commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}