package edu.cooper;

import edu.cooper.model.User;
import spark.Spark;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(123L);
        Spark.get("/hello", (req, res) -> user1.uid);
    }
}









