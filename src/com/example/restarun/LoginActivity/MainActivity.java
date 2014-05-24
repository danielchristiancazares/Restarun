package com.example.restarun.LoginActivity;

import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.example.restarun.R;
import com.example.restarun.SearchActivity.SearchActivity;


public class MainActivity extends FragmentActivity {

	private LoginFragment loginFragment;

	public void guestLogin(View view) {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivity(intent);
	}

	/**
	 * @author danielcazares
	 * @function_name: getDeviceId();
	 * @description: getDeviceId() is used to get the device's UDID.
	 **/
	public String getDeviceId() {
		/** TelephonyManager objects provide access to different phone services **/
		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;

		/** getDeviceId returns the IMEI or MEID/ESN of the phone **/
		tmDevice = (String) tm.getDeviceId();

		/** getSimSerialNumber returns the SIM card serial number **/
		tmSerial = (String) tm.getSimSerialNumber();

		/** Android OS creates a 64-bit random number on first device boot **/
		androidId = (String) android.provider.Settings.Secure.getString(
				getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		/** We use the androidId, tmDevice, and tmSerial to create a unique UDID **/
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

		String deviceId = deviceUuid.toString();
		return deviceId;
	}

	/**
	 * @author danielcazares
	 * @function_name: onCreate();
	 * @description: onCreate() is a superclass function override called upon
	 *               instantiation of the activity. Additionally, it checks for
	 *               previous saved states and instantiates and adds a new
	 *               loginFragment if none are found or retrieves the previous
	 *               fragment if it's found.
	 **/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		loginFragment = new LoginFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, loginFragment).commit();

	}

	/**
	 * @author danielcazares
	 * @function_name: onActivityResult();
	 * @description: This function is called upon exit of this activity.
	 *               Received immediately before onResume() when some other
	 *               activity is resumed.
	 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}