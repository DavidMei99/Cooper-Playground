package edu.cooper.store;

import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;

public interface UserStore {

    List<User> getUserList();

    User addUser(final Handler.CreateUserRequest createUserRequest);

    User getUser(Long uid);

    User getUserByUname(String uname);
}
