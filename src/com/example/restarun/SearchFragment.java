package com.example.restarun;

import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

				writeText.setVisibility(searchView.INVISIBLE);

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

				writeText.setVisibility(searchView.VISIBLE);

			}
		});

		return searchView;
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