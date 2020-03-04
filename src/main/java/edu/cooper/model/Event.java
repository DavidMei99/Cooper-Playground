package edu.cooper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event {
    private final Long eid;
    private static Long count = 0L;
    private final String ename;
    private String etime;
    private String location;
    private final Long groupId;
    private List<Long> attendees;

    public Event(String ename, Long groupId){
        this.eid = ++count;
        this.ename = ename;
        this.etime = "";
        this.location = "";
        this.groupId = groupId;
        this.attendees = new ArrayList<>();
    }

    public Long getGroupId(){
        return groupId;
    }

    public String getEname() {return ename;}

    public Long getEid() {return eid;}

    public String getEtime() {return etime;}

    public String getLocation() {return location;}

    public void attendEvent(Long uid) {attendees.add(uid); }

    public void removeUser(Long uid) {attendees.remove(uid); }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String toString(){
        return eid + " " + ename + " " + groupId + " " + etime + " " + location + "\r\n";
    }
}