package edu.cooper;

import edu.cooper.model.*;
import edu.cooper.*;
import java.util.List;
import java.util.Map;

import spark.Request;

public class Handler {

    private final Service service;

    public Handler(Service service) {
        this.service = service;
    }

    //create new user object with unique username and pwd
    //add created user object to UserStore
    public String createUser(final Request request){
        User user = new User(request.params(":username"), request.params(":password"));
        if(service.isValidUname(user.getUname())) {
            service.createUser(user);
            return user.toString();
        }
        return "Username already exists\r\n";
    }

    //check if username exist
    //return login states
   public String loginUser(final Request request){
       User utemp = service.getUserByUname(request.params(":username"));
       if (utemp == null)
           return "User does not exits\r\n";
       if(service.isCorrectPwd(utemp.getUname(), request.params(":password")))
           return "Login Success\r\n";
       else
           return "Login Failure: The username or password is not correct\r\n";
   }

    //create new group object with groupname and username
    //call service.createGroup
    public String createGroup(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        if (utemp == null)
            return "User does not exits\r\n";
        Group group = new Group(request.params(":groupname"),utemp.getUid());
        if(service.isValidGname(group.getGname())) {
            service.createGroup(group);
            return group.toString();
        }
        return "Group name already exists\r\n";

    }

    public String createEvent(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        String ename = request.params(":eventname");
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exits\r\n";
        else if (utemp.isAdmin(gtemp.getGid()) && service.isValidEname(gtemp, ename)){
            Event event = new Event(ename, gtemp.getGid());
            service.createEvent(event);
            return event.toString();
        }else{
            return "fail to create an event\r\n";
        }
    }

    public String attendGroup(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exits\r\n";
        else if(utemp.getGroupAdmin().containsKey(gtemp.getGid()))
            return "You are already in the group\r\n";
        else{
            utemp.addGroup(gtemp.getGid());
            gtemp.addUser(utemp.getUid());

            return "You are successfully added to group" + " " + gtemp.getGname() + "\r\n";
        }


    }

    public String attendEvent(final Request request){
        User utemp = service.getUserByUname(request.params(":username"));
        Group gtemp = service.getGroupByGname(request.params(":groupname"));
        if(gtemp == null)
            return "Group does not exist\r\n";
        else if (utemp == null)
            return "User does not exits\r\n";
        else if (!service.userInGroup(utemp.getUid(), gtemp.getGid()))
            return "User is not in the group " + gtemp.getGname()+"\r\n";
        Event etemp = gtemp.getEventByEname(request.params(":eventname"));
        if(etemp == null)
            return "Event does not exits\r\n";
        utemp.attendEvent(etemp);
        etemp.attendEvent(utemp.getUid());
        return utemp.getUname() + " successfully attended " + etemp.getEname() + "\r\n";

    }


    public Map<Long, User> getUserList(final Request request){
        return service.getUserList();
    }

    public List<Group> getUserGroups(final Request request){
        User utemp = service.getUser(Long.valueOf(request.params(":uid")));
        return service.getUserGroups(utemp);
    }

    public List<Group> getUserGroupsByUname(final Request request){
        User utemp = service.getUserByUname(request.params(":uname"));
        return service.getUserGroups(utemp);
    }

    public Map<Long, Group> getGroupList(final Request request){
        return service.getGroupList();
    }

    public List<Event> getEventList(final Request request){
        return service.getEventList();
    }

    public List<Event> getUserEventsByUname(final Request request){
        return service.getEventsByUname(request.params(":uname"));
    }

}
