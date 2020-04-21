package edu.cooper.model;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    @Expose private Long uid;
    // private static Long count = 0L;
    @Expose private String uname;
    @Expose private String pwd;
    @Expose private String email;

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
    }

    public User(){
        this.uid = 0L;
        this.uname = "";
        this.pwd = "";
        this.email = "";
    }

    public Long getUid() {return uid;}

    public String getUname() {return uname;}

    public String getPwd() {return pwd;}

    public String getEmail() {return email;}

    public String toString(){
        return Long.toString(uid) + " " + uname + " " + pwd + "\r\n";
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
