/**
 * 
 */
package com.busmonk.service;

/**
 * @author Siddharth Patnaik
 *
 */
public class Bus {
	
	private String id;
	private String routeId;
	private String status;
	private String cab;
	private String driver;
	
	public Bus()
	{
		
	}
	public Bus(String id, String routeId, String status, String cab, String driver) {
		super();
		this.id = id;
		this.routeId = routeId;
		this.status = status;
		this.cab = cab;
		this.driver = driver;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCab() {
		return cab;
	}
	public void setCab(String cab) {
		this.cab = cab;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	
	
	

}
