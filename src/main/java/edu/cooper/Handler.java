package edu.cooper;

import com.google.gson.Gson;
import edu.cooper.model.*;
import edu.cooper.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.Request;

import javax.servlet.ServletOutputStream;

public class Handler {

    private final Service service;
    private final Gson gson;

    public Handler(Service service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    //create new user object with unique username and pwd
    //add created user object to UserStore
    public String createUser(final Request request) throws IOException {
        // User user = new User(request.params(":username"), request.params(":password"), request.params(":email"));
        CreateUserRequest user = gson.fromJson(request.body(), CreateUserRequest.class);
        //CreateUserRequest user = new CreateUserRequest(request.params("userName"), request.params("passWord"), request.params("email"));
        if(service.isValidUname(user.getUname())) {
            service.createUser(user);

            return "Successfully registered user " + user.getUname() + "\r\n";
        }
        return "Username already exists\r\n";
    }

    //check if username exist
    //return login states
   public String loginUser(final Request request){
        //request.session().attribute("currentUser", );
       loginUser loginuser = gson.fromJson(request.body(), loginUser.class);
       User utemp = service.getUserByUname(loginuser.uname);
       if (utemp == null)
           return "User does not exits\r\n";
       if(service.isCorrectPwd(utemp.getUname(), loginuser.pwd)){
           request.session().attribute("currentUser", utemp.getUname());
           return "Login Success\r\n";
       }
       else
           return "Login Failure: The username or password is not correct\r\n";
   }

    //create new group object with groupname and username
    //call service.createGroup
    public String createGroup(final Request request){
        String uname = request.session().attribute("currentUser");
        User utemp = service.getUserByUname(uname);
        if (utemp == null)
            return "User does not exits\r\n";
        //CreateGroupRequest group = new CreateGroupRequest(request.params(":groupname"),utemp.getUid());
        CreateGroupRequest group = gson.fromJson(request.body(), CreateGroupRequest.class);
        group.setAdminid(utemp.getUid());
        if(service.isValidGname(group.getGname())) {
            service.createGroup(group);
            return "Successfully created the group " + group.getGname() + " by " + utemp.getUname() + "\r\n";
        }
        return "Group name already exists\r\n";

    }

    public String createEvent(final Request request){
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));

        eventCreate etemp = gson.fromJson(request.body(), eventCreate.class);
        Group gtemp = service.getGroupByGname(etemp.gname);
        String ename = etemp.ename;
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exits\r\n";
        else if (gtemp.getAdminid() == utemp.getUid() && service.isValidEname(gtemp, ename)){
            CreateEventRequest event = new CreateEventRequest(etemp.ename,etemp.etime,etemp.location, gtemp.getGid());
            service.createEvent(event);
            return "Successfully created the event " + event.getEname() + " in " + gtemp.getGname() +
                    " by " + utemp.getUname() + "\r\n";
        }else{
            return "Fail to create an event\r\n";
        }
    }

    public String attendGroup(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exits\r\n";
        else if(service.userInGroup(utemp.getUid(), gtemp.getGid()))
            return "You are already in the group\r\n";
        else{
            service.addUserGroup(utemp, gtemp);
            return "You are successfully added to group" + " " + gtemp.getGname() + "\r\n";
        }
    }

    public String attendEvent(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exit\r\n";
        else if (!service.userInGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not in the group " + gtemp.getGname()+"\r\n";
        Event etemp = service.getEventByEname(request.params(":eventname"), gtemp.getGid());
        if(etemp == null)
            return "Event does not exist\r\n";

        service.addUserEvent(utemp, etemp);

        return utemp.getUname() + " successfully attended " + etemp.getEname() + "\r\n";
    }


    public List<User> getUserList(final Request request){
        return service.getUserList();
    }

    public List<Group> getUserGroups(final Request request){
        User utemp = service.getUser(Long.valueOf(request.params(":uid")));
        return service.getUserGroups(utemp.getUid());
    }

    public List<Group> getUserGroupsByUname(final Request request){
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        return service.getUserGroups(utemp.getUid());
    }

    public List<Group> getGroupList(final Request request){
        return service.getGroupList();
    }

    public List<Event> getEventList(final Request request){
        return service.getEventList();
    }

    public List<Event> getUserEventsByUname(final Request request){
        return service.getEventsByUname(request.params(":username"));
    }

    public String editEvent(final Request request) {
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        if (utemp == null)
            return "User does not exit\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        Event etemp = service.getEventByEname(request.params(":eventname"), gtemp.getGid());
        if (etemp == null)
            return "Even t does not exit\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";

        String etime = request.params(":time");
        String location = request.params(":location");
        service.editEvent(etemp, etime, location);
        etemp = service.getEventByEname(request.params(":eventname"), gtemp.getGid());

        return "Event " + etemp.getEname() + " will happen at " + etemp.getEtime() + " in " + etemp.getLocation() + "\r\n";
    }

    public String addUserToGroup(final Request request) {
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        User utemp2 = service.getUserByUname(request.params(":username2"));
        if (utemp == null)
            return "User does not exit\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is already in group " + gtemp.getGname() + "\r\n";

        service.addUserGroup(utemp2, gtemp);

        return utemp2.getUname() + " is successfully added to the group " + gtemp.getGname() + "\r\n";
    }

    public String removeUserFromGroup(final Request request) {
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        User utemp2 = service.getUserByUname(request.params(":username2"));
        if (utemp == null)
            return "User does not exit\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (!service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is not in group " + gtemp.getGname() + "\r\n";

        service.removeUserFromGroup(utemp2, gtemp);

        return utemp2.getUname() + " is successfully removed from the group " + gtemp.getGname() + "\r\n";
    }

    public String addUserToEvent(final Request request) {
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        User utemp2 = service.getUserByUname(request.params(":username2"));
        if (utemp == null)
            return "User does not exit\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        Event etemp = service.getEventByEname(request.params(":eventname"), gtemp.getGid());
        if (etemp == null)
            return "Event does not exit\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (!service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is not in group " + gtemp.getGname() + "\r\n";
        else if (service.userInEvent(utemp2.getUid(), etemp.getEid()))
            return "Target user is already in event " + etemp.getEname() + "\r\n";

        service.addUserEvent(utemp2, etemp);

        return utemp2.getUname() + " is successfully invited to the event " + etemp.getEname() + "\r\n";
    }

    public String removeUserFromEvent(final Request request) {
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        User utemp2 = service.getUserByUname(request.params(":username2"));
        if (utemp == null)
            return "User does not exit\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        Event etemp = service.getEventByEname(request.params(":eventname"), gtemp.getGid());
        if (etemp == null)
            return "Event does not exit\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (!service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is not in group " + gtemp.getGname() + "\r\n";
        else if (!service.userInEvent(utemp2.getUid(), etemp.getEid()))
            return "Target user is not in event " + etemp.getEname() + "\r\n";

        service.removeUserFromEvent(utemp2, etemp);

        return utemp2.getUname() + " is successfully removed from the event " + etemp.getEname() + "\r\n";
    }

    public String welcome() {
        return "\r\nYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY\r\n" +
                   ">Welcome to Cooper Playground!<\r\n" +
                   "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\r\n";
    }

    public String transferAdmin(final Request request) {
        User utemp = service.getUserByUname(request.params(":username"));
        User utemp2 = service.getUserByUname(request.params(":username2"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        if (utemp == null)
            return "User does not exit\r\n";
        else if (utemp2 == null)
            return "Target user does not exit\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (!service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is not in group " + gtemp.getGname() + "\r\n";
        gtemp.setAdminid(utemp2.getUid());
        service.setAdmin(gtemp, utemp2);
        return "Successfully transferred admin from " + utemp.getUname() + " to " + utemp2.getUname() + "\r\n";
    }


    public class CreateUserRequest {
        private final String uname;
        private final String pwd;
        private final String email;

        public CreateUserRequest(String uname, String pwd, String email) {
            this.uname = uname;
            this.pwd = pwd;
            this.email = email;
        }

        public String getUname() {
            return uname;
        }

        public String getPwd() {
            return pwd;
        }

        public String getEmail(){ return email; }
    }

    public class CreateGroupRequest {
        private final String gname;
        private Long adminid;

        public CreateGroupRequest(String gname, Long adminid) {
            this.gname = gname;
            this.adminid = adminid;
        }

        public String getGname() {
            return gname;
        }

        public Long getAdminid() {
            return adminid;
        }

        public void setAdminid(Long adminid) { this.adminid = adminid;}
    }

    public class CreateEventRequest {
        private String ename;
        private String etime;
        private String location;
        private Long groupId;

        public CreateEventRequest(String ename, String etime, String location, Long groupId) {
            this.ename=ename;
            this.etime=etime;
            this.location=location;
            this.groupId=groupId;

        }
        public void setEname(String ename) {this.ename = ename;}
        public void setEtime(String ename) {this.etime = etime;}
        public void setLocation(String location) {this.location = location;}
        public void setGroupId(Long groupId) {this.groupId = groupId;}


        public String getEname() {
            return ename;
        }

        public String getEtime() {
            return etime;
        }

        public String getLocation() { return location; }

        public Long getGroupId() { return groupId; }
    }

    public class loginUser{
        public String uname = "";
        public String pwd = "";
        public loginUser(){
        }
    }

    public class eventCreate{
        public String ename = "";
        public String etime = "";
        public String location = "";
        public String gname = "";
        public eventCreate(){

        }
    }

}
