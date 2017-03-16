/**
 * 
 */
package com.busmonk.service;

/**
 * @author Siddharth Patnaik
 *
 */
public class BusTiming {
	
	private String id;
	private String busId;
	private String stop;
	private String time;
	
	public BusTiming()
	{
		
	}
	
	public BusTiming(String id, String busId, String stop, String time) {
		super();
		this.id = id;
		this.busId = busId;
		this.stop = stop;
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	

}
