package com.example.restarun.googlePlaces;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Place {
	private String m_ID, m_Icon, m_Name, m_Address, m_photoRef;

	private Double m_Lat, m_Long;

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
		Place result = new Place();
		try {

			JSONObject geometry = (JSONObject) paramJSONObject.get("geometry");
			JSONObject location = (JSONObject) geometry.get("location");

			try {
				JSONArray photos = paramJSONObject.getJSONArray("photos");
				if (photos != null) {
					JSONObject photoArray = (JSONObject) photos.get(0);
					result.setRef(photoArray.get("photo_reference").toString());
				}
			} catch (JSONException e) {
			}

			result.setLatitude((Double) location.get("lat"));
			result.setLongitude((Double) location.get("lng"));
			result.setIcon(paramJSONObject.getString("icon"));
			result.setName(paramJSONObject.getString("name"));
			result.setVicinity(paramJSONObject.getString("vicinity"));
			result.setID(paramJSONObject.getString("id"));

			return result;
		} catch (JSONException ex) {
			Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
