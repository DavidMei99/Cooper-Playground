package edu.cooper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private final Long uid;
    // private static Long count = 0L;
    private final String uname;
    private final String pwd;
    private String email;

    private Map<Long, Boolean> groupAdmin;
    private List<Event> eventList;

    /*public Boolean isAdmin(Long gid){
        if (groupAdmin.get(gid) == null)
            return false;
        else
            return groupAdmin.get(gid);
    }*/

    public User(Long uid, String uname, String pwd, String email){
        this.uid = uid;
        this.uname = uname;
        this.pwd = pwd;
        this.email = email;
        this.groupAdmin = new HashMap<>();
        this.eventList = new ArrayList<>();
    }

    public void setAdmin(Long gid, Boolean state){
        groupAdmin.put(gid,state);
    }

    public Long getUid() {return uid;}

    public String getUname() {return uname;}

    public List<Event> getEventList() {return eventList;}

    public String getPwd() {return pwd;}

    public Map<Long, Boolean> getGroupAdmin() {return groupAdmin;}

    public String getEmail() {return email;}

    public String toString(){
        return Long.toString(uid) + " " + uname + " " + pwd + "\r\n";
    }

    public void addGroup(Long gid) {groupAdmin.put(gid, false); }

    public void removeGroup(Long gid) {groupAdmin.remove(gid);}

    public void attendEvent(Event event) {eventList.add(event);}

    public void removeEvent(Event event) {eventList.remove(event);}



}
