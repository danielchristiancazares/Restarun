/** Application package definition **/
package com.example.restarun;

/** Android-specific imports **/
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.Context;

/** Java-specific imports **/
import java.util.UUID;

/**
 * @author danielcazares
 * @class: MainActivity
 * @superclass: FragmentActivity
 * @function_protypes: public String getDeviceId(); - Returns the phone UDID.
 * 
 * @description: MainActivity is the container for the entire application and
 *               its fragments. This is the main class used to switch and pass
 *               data between fragments.
 */
public class MainActivity extends FragmentActivity {

	private LoginFragment loginfragment;

	/** Used to get the device's unique ID **/
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * A new fragment will be created if no previous fragment is found when
		 * first opening the program.
		 **/
		if (savedInstanceState == null) {
			loginfragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, loginfragment).commit();
		}
		/**
		 * If a previous state is found, then we simply refer to the previous
		 * login fragment.
		 */
		else {
			loginfragment = (LoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}

		/** DEBUGGING: Output the UDID to "info" level logging **/
		Log.i("UDID", getDeviceId());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// loginfragment.onActivityResult(requestCode, resultCode, data);
	}

}