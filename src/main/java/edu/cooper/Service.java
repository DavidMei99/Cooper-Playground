package edu.cooper;

import edu.cooper.*;
import edu.cooper.model.*;
import edu.cooper.store.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Service {

    private final UserStore userStore;

    public Service(UserStore userStore){
        this.userStore = userStore;
    }

    public User createUser(User user){
        userStore.addUser(user);
        return user;
    }

    public Boolean isValidUname(String uname){
        if(userStore.getPwdByUname(uname) == null)
            return true;
        else
            return false;
    }



    public Boolean isCorrectPwd(String uname, String pwd){
        String loginUname = userStore.getPwdByUname(uname);
        if(loginUname == null)
            return false;
        else if(pwd.compareTo(loginUname) == 0)
            return true;
        else
            return false;
    }
}
