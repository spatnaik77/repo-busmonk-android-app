package com.busmonk.service;

import java.util.*;

/**
 * Created by sr250345 on 11/10/16.
 */
public class UserRouteDetail {

    private String id;
    private String name;
    private String userId;
    private Bus bus;
    private Cab cab;
    private Stop boardingPoint;
    private Stop dropPoint;
    private List<BusTiming> busTimingList;
    private long walkingTimeFromSourceToPickupStop;
    private long walkingTimeFromDropStopToDestination;

    public boolean isSelected = false;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Stop getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(Stop boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public Stop getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(Stop dropPoint) {
        this.dropPoint = dropPoint;
    }

    public List<BusTiming> getBusTimingList() {
        return busTimingList;
    }

    public void setBusTimingList(List<BusTiming> busTimingList) {
        this.busTimingList = busTimingList;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public long getWalkingTimeFromDropStopToDestination() {
        return walkingTimeFromDropStopToDestination;
    }

    public void setWalkingTimeFromDropStopToDestination(long walkingTimeFromDropStopToDestination) {
        this.walkingTimeFromDropStopToDestination = walkingTimeFromDropStopToDestination;
    }

    public long getWalkingTimeFromSourceToPickupStop() {
        return walkingTimeFromSourceToPickupStop;
    }

    public void setWalkingTimeFromSourceToPickupStop(long walkingTimeFromSourceToPickupStop) {
        this.walkingTimeFromSourceToPickupStop = walkingTimeFromSourceToPickupStop;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    //utility method
    public String getStopTime(String stopId)
    {
        String result = null;
        for(BusTiming bt : busTimingList)
        {
            if(bt.getStop().equals(stopId))
            {
                result = bt.getTime();
                break;
            }
        }
        return result;
    }

    public UserRoute getUserRoute()
    {
        UserRoute ur = new UserRoute();
        ur.setId(this.id);
        ur.setUserId(this.userId);
        ur.setBusId(this.bus.getId());
        ur.setBoardingPoint(this.boardingPoint.getId());
        ur.setDropPoint(this.dropPoint.getId());
        ur.setName(this.name);
        ur.setBoardingTime(getStopTime(this.boardingPoint.getId()));
        ur.setDropTime(getStopTime(this.dropPoint.getId()));
        ur.setDriverName(this.bus.getDriver());
        ur.setCab(this.cab);
        return ur;
    }


}
