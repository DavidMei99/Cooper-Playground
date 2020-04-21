package edu.cooper;

import com.google.gson.Gson;
import edu.cooper.store.*;
import edu.cooper.util.JsonTransformer;
import org.jdbi.v3.core.Jdbi;
import spark.Spark;
import edu.cooper.store.GroupStoreJdbi;
import edu.cooper.store.UserStoreJdbi;


public class Main {
    public static void main(String[] args) {
        // User user1 = new User("Joseph", "123456ab");
        // Spark.get("/uid1", (req, res) -> user1.getUid());

        Gson gson = new Gson();
        String url = "jdbc:h2:~/cpdb";
        Jdbi jdbi = Jdbi.create(url);
        GroupStoreJdbi groupStore = new GroupStoreJdbi(jdbi);
        UserStoreJdbi userStore = new UserStoreJdbi(jdbi);
        EventStoreJdbi eventStore = new EventStoreJdbi(jdbi);
        UserGroupRelJdbi userGroupRel = new UserGroupRelJdbi(jdbi);
        UserEventRelJdbi userEventRel = new UserEventRelJdbi(jdbi);
        // create tables in "cpdb" database
        groupStore.populateDb();
        userStore.populateDb();
        eventStore.populateDb();
        userGroupRel.populateDb();
        userEventRel.populateDb();

        // UserStore userStore = new UserStoreImpl();
        // GroupStore groupStore = new GroupStoreImpl();
        Service service = new Service(userStore, groupStore, eventStore, userGroupRel, userEventRel);
        Handler handler = new Handler(service, gson);

        JsonTransformer jsonTransformer = new JsonTransformer();

        Spark.get("/ping", (req, res) -> handler.welcome(), jsonTransformer); // pass

        //user register
        Spark.post("/user/register/:username/pwd/:password/email/:email",
                (req, res) -> handler.createUser(req), jsonTransformer); // pass

        //user login
        Spark.get("/user/login/:username/pwd/:password", (req, res) -> handler.loginUser(req), jsonTransformer); //pass

        //create group
        Spark.post("/user/:username/group/:groupname/create", (req, res) -> handler.createGroup(req), jsonTransformer); // pass

        //attend group
        Spark.put("/user/:username/group/:groupname/attend", (req, res) -> handler.attendGroup(req), jsonTransformer); // pass

        //add user to group
        Spark.put("/user/:username/group/:groupname/user/:username2/invite",
                (req, res) -> handler.addUserToGroup(req), jsonTransformer); // pass

        //remove user from group
        Spark.put("/user/:username/group/:groupname/user/:username2/remove",
                (req, res) -> handler.removeUserFromGroup(req), jsonTransformer); // pass

        //create event
        //creator is not default in created event !!!
        Spark.post("/user/:username/group/:groupname/event/:eventname/create",
                (req, res) -> handler.createEvent(req), jsonTransformer); // pass

        //attend event
        Spark.put("/user/:username/group/:groupname/event/:eventname/attend",
                (req, res) -> handler.attendEvent(req), jsonTransformer); // pass

        //add member to event
        Spark.put("/user/:username/group/:groupname/event/:eventname/user/:username2/invite",
                (req, res) -> handler.addUserToEvent(req), jsonTransformer); // pass

        //remove member from event
        Spark.put("/user/:username/group/:groupname/event/:eventname/user/:username2/remove",
                (req, res) -> handler.removeUserFromEvent(req), jsonTransformer); // pass

        // edit event time and location
        Spark.put("/user/:username/group/:groupname/event/:eventname/time/:time/location/:location/edit",
                (req, res) -> handler.editEvent(req), jsonTransformer); // pass

        //get users (for debug)
        Spark.get("/user/list", (req, res) -> handler.getUserList(req), jsonTransformer); // pass

        //get user's groups (for debug)
        Spark.get("/user/:uid/group/view", (req, res) -> handler.getUserGroups(req), jsonTransformer); // pass

        //get user's groups (for user by uname)
        Spark.get("/user/uname/:username/group/view", (req, res) -> handler.getUserGroupsByUname(req), jsonTransformer); // pass

        //get groups (for debug)
        Spark.get("/group/list", (req, res) -> handler.getGroupList(req), jsonTransformer); // pass

        //get events
        Spark.get("/event/list", (req, res) -> handler.getEventList(req), jsonTransformer); // pass

        //get user's events (for user by uname)
        Spark.get("/user/uname/:username/event/view", (req, res) -> handler.getUserEventsByUname(req), jsonTransformer); // pass

        //trans group's admin
        Spark.put("/user/:username/user/:username2/group/:groupname/transfer", (req, res) -> handler.transferAdmin(req), jsonTransformer); // pass

        //remove groups

        //remove events

        //rsvp in frontend

        //leave group

        //not attend event
    }
}









