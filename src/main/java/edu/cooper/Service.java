package edu.cooper;

import edu.cooper.model.*;
import edu.cooper.store.*;

public class Service {

    private final UserStore userStore;
    private final GroupStore groupStore;

    public Service(UserStore userStore, GroupStore groupStore){
        this.userStore = userStore;
        this.groupStore = groupStore;
    }

    public User createUser(User user){
        userStore.addUser(user);
        return user;
    }

    public Boolean isValidUname(String uname){
        if(userStore.getUserByUname(uname) == null)
            return true;
        else
            return false;
    }

    public Boolean isCorrectPwd(String uname, String pwd){
        String loginUname = userStore.getUserByUname(uname).getPwd();
        if(loginUname == null)
            return false;
        else if(pwd.compareTo(loginUname) == 0)
            return true;
        else
            return false;
    }

    public Group createGroup(Group group) {
        groupStore.addGroup(group);
        return group;
    }

    public User getUserByUname(String uname) {return userStore.getUserByUname(uname);}
}
