package edu.cooper;

import edu.cooper.store.*;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        // User user1 = new User("Joseph", "123456ab");
        // Spark.get("/uid1", (req, res) -> user1.getUid());
        UserStore userStore = new UserStoreImpl();
        GroupStore groupStore = new GroupStoreImpl();
        Service service = new Service(userStore, groupStore);
        Handler handler = new Handler(service);


        Spark.get("/ping", (req, res) -> handler.welcome());

        //user register
        Spark.post("/user/register/:username/pwd/:password",
                (req, res) -> handler.createUser(req));

        //user login
        Spark.get("/user/login/:username/pwd/:password", (req, res) -> handler.loginUser(req));

        //create group
        Spark.post("/user/:username/group/:groupname/create", (req, res) -> handler.createGroup(req));

        //attend group
        Spark.put("/user/:username/group/:groupname/attend", (req, res) -> handler.attendGroup(req));

        //add user to group
        Spark.put("/user/:username/group/:groupname/user/:username2/invite",
                (req, res) -> handler.addUserToGroup(req));

        //remove user from group
        Spark.put("/user/:username/group/:groupname/user/:username2/remove",
                (req, res) -> handler.removeUserFromGroup(req));

        //create event
        Spark.post("/user/:username/group/:groupname/event/:eventname/create",
                (req, res) -> handler.createEvent(req));

        //attend event
        Spark.put("/user/:username/group/:groupname/event/:eventname/attend",
                (req, res) -> handler.attendEvent(req));

        //add member to event
        Spark.put("/user/:username/group/:groupname/event/:eventname/user/:username2/invite",
                (req, res) -> handler.addUserToEvent(req));

        //remove member from event
        Spark.put("/user/:username/group/:groupname/event/:eventname/user/:username2/remove",
                (req, res) -> handler.removeUserFromEvent(req));

        // edit event time and location
        Spark.put("/user/:username/group/:groupname/event/:eventname/time/:time/location/:location/edit",
                (req, res) -> handler.editEvent(req));

        //get users (for debug)
        Spark.get("/user/list", (req, res) -> handler.getUserList(req));

        //get user's groups (for debug)
        Spark.get("/user/:uid/group/view", (req, res) -> handler.getUserGroups(req));

        //get user's groups (for user by uname)
        Spark.get("/user/uname/:uname/group/view", (req, res) -> handler.getUserGroupsByUname(req));

        //get groups (for debug)
        Spark.get("/group/list", (req, res) -> handler.getGroupList(req));

        //get events
        Spark.get("/event/list", (req, res) -> handler.getEventList(req));

        //get user's events (for user by uname)
        Spark.get("/user/uname/:uname/event/view", (req, res) -> handler.getUserEventsByUname(req));
    }
}









