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
        service.createUser(user);
        return user.toString();
    }


}
