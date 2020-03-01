package edu.cooper;

import edu.cooper.model.*;
import edu.cooper.*;
import java.util.List;
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
        return "Invalid Username, Choose a new one\r\n";
    }

    //check if username exist
    //return login states
   public String loginUser(final Request request){
       User user = new User(request.params(":username"), request.params(":password"));
       if(service.isCorrectPwd(user.getUname(), user.getPwd()))
           return "Login Success\r\n";
       else
           return "Login Failure: The username or password is not correct\r\n";
   }

   //create new group object with groupname and username
    // call service.createGroup

    public String createGroup(final Request request){
        Group group = new Group(request.params(":groupname"),service.getUserByUname(request.params(":username")).getUid());
        service.createGroup(group, group.getAdminid());
        return group.toString();
    }



}
