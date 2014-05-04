package com.example.restarun.gpsTracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationFinder extends Service implements LocationListener {

	private LocationManager m_locationManager;

	public LocationFinder(Context context) {
		try {
			m_locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			m_locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 3000, // 3 sec
					10, this);
		} catch (Exception e) {

		}
	}

	public Location getLocation() {
		return m_locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}