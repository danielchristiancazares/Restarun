package com.example.restarun.googlePlaces;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class Place {
	private String m_ID;
	private String m_Icon;
	private String m_Name;
	private String m_Address;
	private String m_photoRef;

	private Double m_Lat;
	private Double m_Long;

	private Bitmap m_placePhoto;
	private static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&sensor=true&key=AIzaSyB_9AfRh1FkxCyWmyMw93hqQKu_VCpEjFE&photoreference=";

	public Place(Double latitude, Double longitude, String name, String id,
			String icon, String address, String ref) {
		this.m_Lat = latitude;
		this.m_Long = longitude;
		this.m_ID = id;
		this.m_Icon = icon;
		this.m_Name = name;
		this.m_Address = address;
		this.m_photoRef = ref;

		if (m_photoRef != "") {
			new DownloadPhoto().execute(this);
		}
	}

	public Bitmap getPhoto() {
		return m_placePhoto;
	}

	public void setPhoto(Bitmap photo) {
		this.m_placePhoto = photo;
	}

	public String getID() {
		return m_ID;
	}

	public void setID(String id) {
		this.m_ID = id;
	}

	public String getRef() {
		return m_photoRef;
	}

	public void setRef(String ref) {
		this.m_photoRef = ref;
	}

	public String getIcon() {
		return m_Icon;
	}

	public void setIcon(String icon) {
		this.m_Icon = icon;
	}

	public Double getLatitude() {
		return m_Lat;
	}

	public void setLatitude(Double latitude) {
		this.m_Lat = latitude;
	}

	public Double getLongitude() {
		return m_Long;
	}

	public void setLongitude(Double longitude) {
		this.m_Long = longitude;
	}

	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		this.m_Name = name;
	}

	public String getVicinity() {
		return m_Address;
	}

	public void setVicinity(String vicinity) {
		this.m_Address = vicinity;
	}

	static Place parseJSON(JSONObject paramJSONObject) {
		try {

			JSONObject geometry = (JSONObject) paramJSONObject.get("geometry");
			JSONObject location = (JSONObject) geometry.get("location");
			String photoReference = "";
			try {
				JSONArray photos = paramJSONObject.getJSONArray("photos");
				if (photos != null) {
					JSONObject photoArray = (JSONObject) photos.get(0);
					photoReference = photoArray.get("photo_reference")
							.toString();
				}
			} catch (JSONException e) {
			}

			return new Place((Double) location.get("lat"),
					(Double) location.get("lng"),
					paramJSONObject.getString("name"),
					paramJSONObject.getString("id"),
					paramJSONObject.getString("icon"),
					paramJSONObject.getString("vicinity"), photoReference);
		} catch (JSONException ex) {
			Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private class DownloadPhoto extends AsyncTask<Place, Void, Bitmap> {
		Place m_place;

		protected Bitmap doInBackground(Place... place) {
			if (place[0].getRef() != null) {
				m_place = place[0];
				StringBuilder imgUrl = new StringBuilder(PHOTO_URL);
				imgUrl.append(place[0].getRef());
				Bitmap mIcon = null;
				try {
					InputStream in = new java.net.URL(imgUrl.toString())
							.openStream();
					mIcon = BitmapFactory.decodeStream(in);
					return mIcon;
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
					return null;
				}
			}
			return null;
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				m_place.setPhoto(result);
			} else {

			}
		}
	}
}
