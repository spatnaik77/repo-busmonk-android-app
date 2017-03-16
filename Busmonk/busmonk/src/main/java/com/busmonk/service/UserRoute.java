/**
 * 
 */
package com.busmonk.service;

/**
 * @author Siddharth Patnaik
 *
 */
public class UserRoute {
	
	private String id;
	private String name;
	private String userId;
	private String busId;
	private String boardingPoint;
	private String dropPoint;

	//following attributes are not persisted. They need to be filled
	private String boardingTime;
	private String dropTime;
	private String driverName;
	Cab cab;



	public UserRoute()
	{

	}

	public UserRoute(String id, String name, String userId, String busId, String boardingPoint, String dropPoint) {
		this.id = id;
		this.name = name;
		this.userId = userId;
		this.busId = busId;
		this.boardingPoint = boardingPoint;
		this.dropPoint = dropPoint;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBoardingPoint() {
		return boardingPoint;
	}
	public void setBoardingPoint(String boardingPoint) {
		this.boardingPoint = boardingPoint;
	}
	public String getDropPoint() {
		return dropPoint;
	}
	public void setDropPoint(String dropPoint) {
		this.dropPoint = dropPoint;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getBoardingTime() {
		return boardingTime;
	}

	public void setBoardingTime(String boardingTime) {
		this.boardingTime = boardingTime;
	}

	public String getDropTime() {
		return dropTime;
	}

	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}

	public Cab getCab() {
		return cab;
	}

	public void setCab(Cab cab) {
		this.cab = cab;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
}
