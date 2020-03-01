package edu.cooper.model;

import java.util.List;
import java.util.Map;

public class User {
    private final Long uid;
    private static Long count = 0L;
    private final String uname;
    private final String pwd;
    private String email;

    private Map<Long, Boolean> groupAdmin;

    public Boolean isAdmin(Long gid){
        return groupAdmin.get(gid);
    }

    public void setAdmin(Long gid, Boolean state){
        groupAdmin.put(gid,state);
    }


    public User(String uname, String pwd){
        this.uid = ++count;
        this.uname = uname;
        this.pwd = pwd;
    }

    public Long getUid() {return uid;}

    public String getUname() {return uname;}

    public String getPwd() {return pwd;}

    public String getEmail() {return email;}

    public String toString(){
        return Long.toString(uid) + " " + uname + " " + pwd + "\r\n";
    }

}
