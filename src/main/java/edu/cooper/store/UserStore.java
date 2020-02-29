package edu.cooper.store;

import edu.cooper.model.*;

public interface UserStore {

    void addUser(User user);

    User getUser(Long uid);
}
