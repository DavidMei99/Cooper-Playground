package edu.cooper;

import com.google.gson.Gson;
import edu.cooper.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.*;
import spark.Request;

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
           return "User does not exist\r\n";
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
        //System.out.println(gtemp.getGid());
        String ename = etemp.ename;
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exits\r\n";
        else if (gtemp.getAdminid() == utemp.getUid() && service.isValidEname(gtemp, ename)){
            CreateEventRequest event = new CreateEventRequest(etemp.ename,etemp.etime,etemp.location, gtemp.getGid());
            System.out.println(event.getGroupId());
            service.createEvent(event);
            return "Successfully created the event " + event.getEname() + " in " + gtemp.getGname() +
                    " by " + utemp.getUname() + "\r\n";
        }else{
            return "Fail to create an event\r\n";
        }
    }

    public String attendGroup(final Request request){
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        groupAttend groupattend = gson.fromJson(request.body(), groupAttend.class);
        Group gtemp = service.getGroupByGname(groupattend.gname);
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
        System.out.println(request.body());
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        eventAttend eventattend = gson.fromJson(request.body(), eventAttend.class);
        Group gtemp = service.getGroupByGname(eventattend.gname);
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exit\r\n";
        else if (!service.userInGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not in the group " + gtemp.getGname()+"\r\n";
        Event etemp = service.getEventByEname(eventattend.ename, gtemp.getGid());
        if(etemp == null)
            return "Event does not exist\r\n";
        if (service.userInEvent(utemp.getUid(), etemp.getEid())){
            return "You are already in event " + etemp.getEname() + "\r\n";
        }
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

    public List<GroupLess> getUserGroupsByUnameLess(final Request request){
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        List<Group> glist = service.getUserGroups(utemp.getUid());
        List<GroupLess> glesslist = new ArrayList<>();
        for (Group group: glist) {
            GroupLess gtemp = new GroupLess(group.getGname(), service.getUser(group.getAdminid()).getUname());
            glesslist.add(gtemp);
        }
        return glesslist;

    }

    public List<Group> getGroupList(final Request request){
        return service.getGroupList();
    }

    public List<Event> getEventList(final Request request){
        return service.getEventList();
    }

    public List<Event> getUserEventsByUname(final Request request){
        return service.getEventsByUname(request.session().attribute("currentUser"));
    }

    public List<EventLess> getUserEventsByUnameLess(final Request request){
        List<Event> elist = service.getEventsByUname(request.session().attribute("currentUser"));

        List<EventLess> elesslist = new ArrayList<>();
        for (Event event: elist) {
            EventLess etemp = new EventLess(event.getEname(), event.getEtime(), event.getLocation(),
                    service.getGroup(event.getGroupId()).getGname());
            elesslist.add(etemp);
        }
        return elesslist;

    }

    public List<Event> getEventsByGname(final Request request){
        Long gid = service.getGroupByGname(request.params("gname")).getGid();
        System.out.println(gid);
        return service.getEventsByGid(gid);
    }

    public List<User> getUsersByGname(final Request request){
        Long gid = service.getGroupByGname(request.params("gname")).getGid();
        return service.getGroupUsers(gid);
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
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        addToGroup addtogroup = gson.fromJson(request.body(), addToGroup.class);
        Group gtemp = service.getGroupByGname(addtogroup.gname);
        User utemp2 = service.getUserByUname(addtogroup.uname);
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
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        addToEvent addtoevent = gson.fromJson(request.body(), addToEvent.class);
        Group gtemp = service.getGroupByGname(addtoevent.gname);
        User utemp2 = service.getUserByUname(addtoevent.uname);
        if (utemp == null)
            return "User does not exist\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        Event etemp = service.getEventByEname(addtoevent.ename, gtemp.getGid());
        if (etemp == null)
            return "Event does not exist\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (!service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is not in group " + gtemp.getGname() + "\r\n";
        else if (service.userInEvent(utemp2.getUid(), etemp.getEid()))
            return "Target user is already in event " + etemp.getEname() + "\r\n";

        //service.addUserEvent(utemp2, etemp);

        // Recipient's email ID needs to be mentioned.
        String to = utemp2.getEmail();

        // Sender's email ID needs to be mentioned
        String from = "invtie.cooperplayground@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("invite.cooperplayground@gmail.com", "cooperplayground");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Cooper Playground Event Invitation");

            // Set email
            String emailText = "<html><body>"+"<p>"+ "Dear " + utemp2.getUname() + "," + "<br>"
                    + utemp.getUname() + " is inviting you to the event "
                    + etemp.getEname() + " in " + etemp.getLocation()
                    + " on " + etemp.getEtime() + ". "
                    + "Please click the following link if you would like to attend:\n"
                    + "</p>" + "<p>"
                    + "<a href="
                    + "'http://localhost:4567/user/"
                    + utemp.getUname()
                    + "/group/" + gtemp.getGname() + "/event/"
                    + etemp.getEname() + "/user/" + utemp2.getUname()
                    + "/invite'>" + "I would like to attend" + "</a>"+"</p>"
                    + "<p>"
                    + "Thanks,"+ "<br>"
                    + "Cooper Playground" + "</p>" +"</body></html>";

            // Now set the actual message
            message.setText(emailText, "UTF-8", "html");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


        return utemp2.getUname() + " is successfully invited to the event " + etemp.getEname() + "\r\n";
    }

    public String addUserToEventURL(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        User utemp2 = service.getUserByUname(request.params(":username2"));
        if (utemp == null)
            return "User does not exist\r\n";
        else if (gtemp == null)
            return "Group does not exist\r\n";
        Event etemp = service.getEventByEname(request.params(":eventname"), gtemp.getGid());
        if (etemp == null)
            return "Event does not exist\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
        else if (!service.isAdminOfGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not admin of the group " + gtemp.getGname() + "\r\n";
        else if (!service.userInGroup(utemp2.getUid(), gtemp.getGid()))
            return "Target user is not in group " + gtemp.getGname() + "\r\n";
        else if (service.userInEvent(utemp2.getUid(), etemp.getEid()))
            return "Target user is already in event " + etemp.getEname() + "\r\n";
        service.addUserEvent(utemp2, etemp);

        return utemp2.getUname() + " is successfully added to the event " + etemp.getEname();

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
        User utemp = service.getUserByUname(request.session().attribute("currentUser"));
        addToGroup transferadmin = gson.fromJson(request.body(), addToGroup.class);
        User utemp2 = service.getUserByUname(transferadmin.uname);
        Group gtemp = service.getGroupByGname(transferadmin.gname);
        if (utemp == null)
            return "User does not exist\r\n";
        else if (utemp2 == null)
            return "Target user does not exist\r\n";
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

    public class groupAttend{
        public String gname = "";

        public groupAttend(){

        }

    }

    public class eventAttend{
        public String gname = "";
        public String ename = "";

        public eventAttend(){

        }

    }

    public class groupGname {
        public String gname = "";

        public groupGname() {

        }
    }

    public class addToGroup {
        public String gname = "";
        public String uname = "";
        public addToGroup(){

        }

    }
    public class addToEvent {
        public String gname = "";
        public String uname = "";
        public String ename = "";
        public addToEvent(){

        }

    }


    public class GroupLess {
        public String GroupName = "";
        public String adminName = "";
        public GroupLess(String GroupName, String adminName){
            this.GroupName = GroupName;
            this.adminName = adminName;
        }
    }

    public class EventLess {
        public String eventName = "";
        public String eventTime = "";
        public String eventLocation = "";
        public String groupName = "";
        public EventLess(String eventName, String eventTime, String eventLocation, String groupName){
            this.eventName = eventName;
            this.eventTime = eventTime;
            this.eventLocation = eventLocation;
            this.groupName = groupName;
        }

    }

}
