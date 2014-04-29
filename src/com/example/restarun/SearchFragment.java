package com.example.restarun;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.restarun.googlePlaces.Place;
import com.example.restarun.googlePlaces.PlacesAPI;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

				// for (int i = 0; i < myPlaces.size(); i++) {
				// }
				myList.append(myPlaces.get(0).getName() + "\n\t"
						+ myPlaces.get(0).getVicinity() + "\n");
				locImg = (ImageView) searchView.findViewById(R.id.imageView1);

				String imgURL = myPlaces.get(0).getIcon().toString();

				writeText.setText(myList.toString());

				writeText.setVisibility(searchView.VISIBLE);
				
				new DownloadImageTask(locImg).execute(imgURL);
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
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			internalImage.setImageBitmap(result);
		}
	}
}