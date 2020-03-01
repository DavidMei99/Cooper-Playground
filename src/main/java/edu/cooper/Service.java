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

    //call userStore.addUser
    public User createUser(User user){
        userStore.addUser(user);
        return user;
    }

    //check if user name is valid
    public Boolean isValidUname(String uname){
        if(userStore.getUserByUname(uname) == null)
            return true;
        else
            return false;
    }

    //check if user exists. if exists, find user's pwd and check if it matches the input
    public Boolean isCorrectPwd(String uname, String pwd){
        User loginuser = userStore.getUserByUname(uname);
        if(loginuser == null)
            return false;
        String loginPwd = loginuser.getPwd();

        if(pwd.compareTo(loginPwd) == 0)
            return true;
        else
            return false;
    }

    //call groupStore.addGroup and add group to user's group list and set the user to admin
    public Group createGroup(Group group) {
        groupStore.addGroup(group);
        userStore.getUser(group.getAdminid()).setAdmin(group.getGid(), true);
        return group;
    }

    public Event createEvent(Event event) {
        groupStore.addEvent(event);
        return event;
    }

    public User getUserByUname(String uname) {return userStore.getUserByUname(uname);}

    public Group getGroupByGname(String gname) {return groupStore.getGroupByGname(gname);}
}
