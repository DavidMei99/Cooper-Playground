package edu.cooper.model;

public class Group {
    private String gname;
    private final Long gid;
    private static Long count = 0L;
    private Long adminid;

    public Group(String gname, Long gid, Long adminid){
        this.gname = gname;
        this.gid = ++count;
        this.adminid = adminid;
    }
}
