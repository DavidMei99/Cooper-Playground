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

        //user register
        Spark.post("/user/register/:username/pwd/:password",
                (req, res) -> handler.createUser(req));

        //user login
        Spark.get("/user/login/:username/pwd/:password", (req, res) -> handler.loginUser(req));

        //create group
        Spark.post("/user/:username/group/:groupname/create", (req, res) -> handler.createGroup(req));

        //create event
        Spark.post("/user/:username/group/:groupname/event/:eventname/create",
                (req, res) -> handler.createEvent(req));
    }
}









