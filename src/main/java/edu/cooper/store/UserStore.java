package edu.cooper.store;

import edu.cooper.model.*;

import java.util.Map;

public interface UserStore {

    Map<Long, User> getUserList();

    void addUser(User user);

    User getUser(Long uid);

    User getUserByUname(String uname);
}
