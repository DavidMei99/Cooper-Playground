package edu.cooper.model;

public class User {
    private final Long uid;
    private static Long count = 0L;
    private final String uname;
    private final String pwd;
    private String email;

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
