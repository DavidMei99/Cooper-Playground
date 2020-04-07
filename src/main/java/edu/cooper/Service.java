package edu.cooper;

import edu.cooper.model.*;
import edu.cooper.store.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Service {

    private final UserStoreJdbi userStore;
    private final GroupStoreJdbi groupStore;
    private final EventStoreJdbi eventStore;

    public Service(UserStoreJdbi userStore, GroupStoreJdbi groupStore, EventStoreJdbi eventStore){
        this.userStore = userStore;
        this.groupStore = groupStore;
        this.eventStore = eventStore;
    }

    //call userStore.addUser
    public User createUser(final Handler.CreateUserRequest createUserRequest) {
        return userStore.addUser(createUserRequest);
    }

    //call groupStore.addGroup and add group to user's group list and set the user to admin
    public Group createGroup(final Handler.CreateGroupRequest createGroupRequest) {
        return groupStore.addGroup(createGroupRequest);
        // userStore.getUser(group.getAdminid()).setAdmin(group.getGid(), true);
        // return group;
    }

    public Event createEvent(final Handler.CreateEventRequest createEventRequest) {
        return eventStore.addEvent(createEventRequest);
    }

    public List<User> getUserList(){
        return userStore.getUserList();
    }

    public List<Group> getGroupList(){
        return groupStore.getGroupList();
    }

    public List<Event> getEventList(){
        return eventStore.getEventList();
    }

    public User getUser(Long uid){
        return userStore.getUser(uid);
    }

    public User getUserByUname(String uname) {return userStore.getUserByUname(uname);}

    public Group getGroupByGname(String gname) {return groupStore.getGroupByGname(gname);}

    //check if user name is valid
    public Boolean isValidUname(String uname){
        if(userStore.getUserByUname(uname) == null)
            return true;
        else
            return false;
    }

    public Boolean isValidGname(String gname){
        if(groupStore.getGroupByGname(gname) == null)
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

    public Boolean isAdminOfGroup(long uid, Long gid) { return groupStore.getGroup(gid).getAdminid() == uid;}

    public void editEvent(Event event, String etime, String location) {
        // event.setEtime(etime);
        // event.setLocation(location);
        eventStore.editEvent(event.getEid(), etime, location);
    }






    public Boolean isValidEname(Group gtemp, String ename){
        return gtemp.getEventByEname(ename) == null;

    }

    public Boolean userInGroup(Long uid, Long gid){
        return (userStore.getUser(uid).getGroupAdmin().containsKey(gid));
    }

    public Boolean userInEvent(Long uid, Event event){return (userStore.getUser(uid).getEventList().contains(event)); }

    public List<Group> getUserGroups(User user){
        List<Group> temp = new ArrayList<>();
        Iterator<Map.Entry<Long, Boolean>> itr = user.getGroupAdmin().entrySet().iterator();
        while(itr.hasNext())
        {
            Map.Entry<Long, Boolean> entry = itr.next();
            temp.add(groupStore.getGroup(entry.getKey()));
        }
        return temp;
    }

    /*public List<Event> getEventList(){
        List<Event> events = new ArrayList<>();
        Map<Long, Group> groups = getGroupList();
        Iterator<Map.Entry<Long, Group>> itr = groups.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Long, Group> entry = itr.next();
            events.addAll(entry.getValue().getEventList());
        }
        return events;
    }*/

    public List<Event> getEventsByUname(String uname) {return userStore.getUserByUname(uname).getEventList();}

    public void removeUserFromGroup(User user, Group group) {
        List<Event> eventList = group.getEventList();
        for (Event event: eventList) {
            if (userInEvent(user.getUid(), event)) {
                user.removeEvent(event);
                event.removeUser(user.getUid());
            }
        }
        user.removeGroup(group.getGid());
        group.removeUser(user.getUid());
    }
}
