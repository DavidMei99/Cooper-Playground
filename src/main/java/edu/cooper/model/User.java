package edu.cooper.model;

public class User {
    private final Long uid;
    private static Long count = 0L;
    private final String uname;
    private final String pwd;

    public User(Long uid, String uname, String pwd){
        this.uid = ++count;
        this.uname = uname;
        this.pwd = pwd;
    }

}
