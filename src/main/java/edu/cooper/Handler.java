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

    public String createUser(final Request request){
        User user = new User(request.params(":username"), request.params(":password"));
        if(service.isValidUname(user.getUname())) {
            service.createUser(user);
            return user.toString();
        }
        return "Invalid Username, Choose a new one\r\n";
    }

   public String loginUser(final Request request){
       User user = new User(request.params(":username"), request.params(":password"));
       if(service.isCorrectPwd(user.getUname(), user.getPwd()))
           return "Login Success\r\n";
       else
           return "Login Failure: The username or password is not correct\r\n";


   }



}
