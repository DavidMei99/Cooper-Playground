package edu.cooper.model;

import java.util.List;

public class Group {
    private final String gname;
    private final Long gid;
    private static Long count = 0L;
    private Long adminid;
    private List<Long> uidList;

    public Group(String gname, Long adminid){
        this.gname = gname;
        this.gid = ++count;
        this.adminid = adminid;
    }

    public Long getGid() {return gid;}

    public String getGname() {return gname;}

    public Long getAdminid() {return adminid;}

    public String toString(){
        return Long.toString(gid) + " " + gname + " " + adminid + "\r\n";
    }
}
