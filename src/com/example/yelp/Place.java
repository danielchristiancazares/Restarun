package com.example.yelp;

public class Place {
	String m_MobileURL;
	float m_Rating;
	String m_Name;

	public Place(String pName, Float pRating, String pMobileURL) {
		this.m_MobileURL = pMobileURL;
		this.m_Rating = pRating;
		this.m_Name = pName;
	}
	
	public String getName()
	{
		return m_Name;
	}
	
	public float getRating()
	{
		return m_Rating;
	}
}
