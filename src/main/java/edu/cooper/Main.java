package edu.cooper;

import edu.cooper.store.*;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        // User user1 = new User("Joseph", "123456ab");
        // Spark.get("/uid1", (req, res) -> user1.getUid());
        UserStore userStore = new UserStoreImpl();
        Service service = new Service(userStore);
        Handler handler = new Handler(service);

        Spark.post("/register/:username/pwd/:password",
                (req, res) -> handler.createUser(req));

        Spark.get("/login/:username/pwd/:password", (req, res) -> handler.loginUser(req));
    }
}









