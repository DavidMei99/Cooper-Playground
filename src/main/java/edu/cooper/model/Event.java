package edu.cooper.model;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event {
    @Expose private Long eid;
    // private static Long count = 0L;
    @Expose private String ename;
    @Expose private String etime;
    @Expose private String location;
    @Expose private Long groupId;

    public Event(Long eid, String ename, Long groupId){
        this.eid = eid;
        this.ename = ename;
        this.etime = "";
        this.location = "";
        this.groupId = groupId;
    }

    public Event(){
        this.eid = 0L;
        this.ename = "";
        this.etime = "";
        this.location = "";
        this.groupId = 0L;
    }

    public Long getGroupId(){
        return groupId;
    }

    public String getEname() {return ename;}

    public Long getEid() {return eid;}

    public String getEtime() {return etime;}

    public String getLocation() {return location;}

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEid(Long eid) { this.eid = eid; }

    public void setEname(String ename) { this.ename = ename; }

    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String toString(){
        return eid + " " + ename + " " + groupId + " " + etime + " " + location + "\r\n";
    }
}