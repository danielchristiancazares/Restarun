package com.example.restarun.googlePlaces;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Place {
	private String id;
	private String icon;
	private String name;
	private String vicinity;
	private String photo_reference;
	private Double latitude;
	private Double longitude;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRef() {
		return photo_reference;
	}

	public void setRef(String ref) {
		this.photo_reference = ref;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	static Place jsonToPontoReferencia(JSONObject pontoReferencia) {
		try {
			Place result = new Place();

			JSONObject geometry = (JSONObject) pontoReferencia.get("geometry");
			JSONObject location = (JSONObject) geometry.get("location");
			try {
				JSONArray photos = pontoReferencia.getJSONArray("photos");
				if (photos != null) {
					JSONObject photoArray = (JSONObject) photos.get(0);
					result.setRef(photoArray.get("photo_reference").toString());
				}
			} catch (JSONException e) {

			}
			result.setLatitude((Double) location.get("lat"));
			result.setLongitude((Double) location.get("lng"));
			result.setIcon(pontoReferencia.getString("icon"));
			result.setName(pontoReferencia.getString("name"));
			result.setVicinity(pontoReferencia.getString("vicinity"));
			result.setId(pontoReferencia.getString("id"));

			return result;
		} catch (JSONException ex) {
			Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public String toString() {
		return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name
				+ ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}

}
