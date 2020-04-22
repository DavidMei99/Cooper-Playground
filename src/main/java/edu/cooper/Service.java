package edu.cooper;

import edu.cooper.model.*;
import edu.cooper.store.*;

import java.util.*;

public class Service {

    private final UserStoreJdbi userStore;
    private final GroupStoreJdbi groupStore;
    private final EventStoreJdbi eventStore;
    private final UserGroupRelJdbi userGroupRel;
    private final UserEventRelJdbi userEventRel;

    public Service(UserStoreJdbi userStore, GroupStoreJdbi groupStore, EventStoreJdbi eventStore,
                   UserGroupRelJdbi userGroupRel, UserEventRelJdbi userEventRel){
        this.userStore = userStore;
        this.groupStore = groupStore;
        this.eventStore = eventStore;
        this.userGroupRel = userGroupRel;
        this.userEventRel = userEventRel;
    }

    //call userStore.addUser
    public User createUser(final Handler.CreateUserRequest createUserRequest) {
        return userStore.addUser(createUserRequest);
    }

    //call groupStore.addGroup and add group to user's group list and set the user to admin
    public Group createGroup(final Handler.CreateGroupRequest createGroupRequest) {
        Group gtemp = groupStore.addGroup(createGroupRequest).get();
        userGroupRel.addUserGroup(createGroupRequest.getAdminid(), gtemp.getGid());
        return gtemp;
        // userStore.getUser(group.getAdminid()).setAdmin(group.getGid(), true);
        // return group;
    }

    public Event createEvent(final Handler.CreateEventRequest createEventRequest) {
        return eventStore.addEvent(createEventRequest).get();
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

    public User getUserByUname(String uname) {return userStore.getUserByUname(uname).get();}

    public Group getGroupByGname(String gname) {return groupStore.getGroupByGname(gname).get();}

    //check if user name is valid
    public Boolean isValidUname(String uname){
        if(userStore.getUserByUname(uname).isEmpty())
            return true;
        else
            return false;
    }

    public Boolean isValidGname(String gname){
        if(groupStore.getGroupByGname(gname).isEmpty())
            return true;
        else
            return false;
    }

    //check if user exists. if exists, find user's pwd and check if it matches the input
    public Boolean isCorrectPwd(String uname, String pwd){
        User loginuser = userStore.getUserByUname(uname).get();
        if(loginuser == null)
            return false;
        String loginPwd = loginuser.getPwd();

        if(pwd.compareTo(loginPwd) == 0)
            return true;
        else
            return false;
    }

    public Boolean isAdminOfGroup(long uid, Long gid) { return groupStore.getGroup(gid).get().getAdminid() == uid;}

    public void editEvent(Event event, String etime, String location) {
        // event.setEtime(etime);
        // event.setLocation(location);
        eventStore.editEvent(event.getEid(), etime, location);
    }

    public Boolean userInGroup(Long uid, Long gid){
        return userGroupRel.getUserGroupList(uid).contains(gid);
    }

    public Boolean userInEvent(Long uid, Long eid){return userEventRel.userInEvent(uid, eid); }

    public List<Group> getUserGroups(Long uid){ return userGroupRel.getUserGroupObjList(uid); }

    public List<Event> getEventsByUname(String uname) {return userEventRel.getUserEventList(userStore.getUserByUname(uname).get().getUid());}

    public void addUserGroup(User user, Group group) {
        userGroupRel.addUserGroup(user.getUid(), group.getGid());
    }

    public void addUserEvent(User user, Event event) {
        userEventRel.addUserEvent(user.getUid(), event.getEid());
    }

    public void removeUserFromGroup(User user, Group group) {
        List<Event> eventList = eventStore.getGroupEvents(group.getGid());
        for (Event event: eventList) {
            if (userInEvent(user.getUid(), event.getEid())) {
                userEventRel.removeUserFromEvent(user.getUid(), event.getEid());
            }
        }
        userGroupRel.removeUserFromGroup(user.getUid(), group.getGid());
    }

    public void removeUserFromEvent(User user, Event event) {
        userEventRel.removeUserFromEvent(user.getUid(), event.getEid());
    }

    public Boolean isValidEname(Group gtemp, String ename){
        return eventStore.getEventByEname(ename, gtemp.getGid()).isEmpty();
    }

    public Event getEventByEname(String ename, Long gid){
        return eventStore.getEventByEname(ename, gid).get();
    }

    public void setAdmin(Group group, User user){
        groupStore.updateAdmin(group.getGid(), user.getUid());
    }

    public Group getGroup(Long gid) { return groupStore.getGroup(gid).get();}

    /*public List<Group> getUserGroups(User user){
        List<Group> temp = new ArrayList<>();
        Iterator<Map.Entry<Long, Boolean>> itr = user.getGroupAdmin().entrySet().iterator();
        while(itr.hasNext())
        {
            Map.Entry<Long, Boolean> entry = itr.next();
            temp.add(groupStore.getGroup(entry.getKey()));
        }
        return temp;
    }*/

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
}
