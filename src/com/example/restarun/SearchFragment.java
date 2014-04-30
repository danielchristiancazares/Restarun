package com.example.restarun;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restarun.googlePlaces.Place;
import com.example.restarun.googlePlaces.PlacesAPI;

public class SearchFragment extends Fragment {

	private View searchView;

	private double latitude = 32.8762142;
	private double longitude = -117.2354577;
	private Button quickSearchButton;
	private TextView writeText;
	private ImageView locImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/** Sets the current view **/
		searchView = inflater.inflate(R.layout.fragment_search, container,
				false);

		writeText = (TextView) searchView.findViewById(R.id.textView1);

		locImg = (ImageView) searchView.findViewById(R.id.imageView1);

		quickSearchButton = (Button) searchView.findViewById(R.id.quickSearch);
		quickSearchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				CheckLocation checkLoc = new CheckLocation(quickSearchButton
						.getContext());
				Location currentLocation = checkLoc.getLocation();
				if (currentLocation != null) {
					TextView latText = (TextView) searchView
							.findViewById(R.id.latTextView);
					TextView longText = (TextView) searchView
							.findViewById(R.id.longTextView);

					latitude = currentLocation.getLatitude();
					longitude = currentLocation.getLongitude();

					latText.setText("Latitude: " + latitude);
					longText.setText("Longitude: " + longitude);
				}

				ArrayList<Place> myPlaces = new PlacesAPI().findPlaces(
						latitude, longitude, "restaurant");
				StringBuilder myList = new StringBuilder("" + myPlaces.size()
						+ " places found.\n");

				myList.append(myPlaces.get(1).getName() + "\n\t"
						+ myPlaces.get(1).getVicinity() + "\n\t");
				locImg = (ImageView) searchView.findViewById(R.id.imageView1);
				try {
					StringBuilder imgUrl = new StringBuilder(
							"https://maps.googleapis.com/maps/api/place/photo?maxwidth=400");
					imgUrl.append("&photoreference=");
					imgUrl.append(myPlaces.get(1).getRef().toString());
					imgUrl.append("&sensor=true&key=AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE");
					new DownloadImageTask(locImg).execute(imgUrl.toString());

				} catch (Exception e) {

				}
				writeText.setText(myList.toString());
			}
		});

		return searchView;
	}

	private class CheckLocation extends Service implements LocationListener {

		protected LocationManager m_locationManager;

		public CheckLocation(Context paramContext) {
			try {
				m_locationManager = (LocationManager) paramContext
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

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView internalImage = null;

		public DownloadImageTask(ImageView bmImage) {
			this.internalImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon;
		}

		protected void onPostExecute(Bitmap result) {
			internalImage.setImageBitmap(result);
		}
	}
}