package com.example.yelp;

public class Place {
	String m_MobileURL;
	float m_Rating;
	String m_Name;
	String m_city;

	public Place(String pName, Float pRating, String pCity) {
		this.m_city = pCity;
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
	
	public String getCity() 
	{
		return m_city;
	}
}
