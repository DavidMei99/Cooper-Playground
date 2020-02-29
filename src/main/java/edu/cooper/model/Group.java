package edu.cooper.model;

public class Group {
    private final String gname;
    private final Long gid;
    private static Long count = 0L;
    private Long adminid;

    public Group(String gname, Long adminid){
        this.gname = gname;
        this.gid = ++count;
        this.adminid = adminid;
    }

    public Long getGid() {return gid;}

    public String getGname() {return gname;}

    public Long getAdminid() {return adminid;}
}
