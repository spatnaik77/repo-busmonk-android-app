/**
 * 
 */
package com.busmonk.service;

/**
 * @author Siddharth Patnaik
 *
 */
public class Cab {
	
	private String id;
	private String registrationNumber;
	private String description;
	
	private String busType;
	private String acType;//	A/C  or Non A/C
	private int seatingCapacity;
	private int availableSeatingCapacity;
	
	public Cab()
	{
		
	}
	
	public Cab(String id, String registrationNumber, String acType, int seatingCapacity) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.acType = acType;
		this.seatingCapacity = seatingCapacity;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSeatingCapacity() {
		return seatingCapacity;
	}
	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getAcType() {
		return acType;
	}
	public void setAcType(String acType) {
		this.acType = acType;
	}
	public int getAvailableSeatingCapacity() {
		return availableSeatingCapacity;
	}
	public void setAvailableSeatingCapacity(int availableSeatingCapacity) {
		this.availableSeatingCapacity = availableSeatingCapacity;
	}
	
	

}
