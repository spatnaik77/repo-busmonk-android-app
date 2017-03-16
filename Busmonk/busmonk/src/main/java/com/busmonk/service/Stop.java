/**
 * 
 */
package com.busmonk.service;

/**
 * @author Siddharth Patnaik
 *
 */
public class Stop {
	
	private String id;
	private String name;
	private double lattitude;
	private double longitude;
	
	public Stop()
	{
		
	}
	public Stop(String id, String name, double lat, double longd)
	{
		this.id = id;
		this.name = name;
		this.lattitude = lat;
		this.longitude = longd;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLattitude() {
		return lattitude;
	}
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "Stop [name=" + name + "]";
	}

	
	
}
