package edu.cooper.model;

import java.util.*;

public class Group {
    private String gname;
    private Long gid;
    // private static Long count = 0L;
    private Long adminid;

    public Group(Long gid, String gname, Long adminid){
        this.gname = gname;
        this.gid = gid;
        this.adminid = adminid;
    }

    public Group(){
        this.gname = "";
        this.gid = 0L;
        this.adminid = 0L;
    }

    public Long getGid() {return gid;}

    public String getGname() {return gname;}

    public Long getAdminid() {return adminid;}

    public void setAdminid(Long adminid) {
        this.adminid = adminid;
    }

    public String toString(){ return Long.toString(gid) + " " + gname + " " + adminid + "\r\n"; }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }
}
