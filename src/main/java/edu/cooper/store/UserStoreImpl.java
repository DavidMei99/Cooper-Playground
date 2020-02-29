package edu.cooper.store;

import edu.cooper.model.*;
import edu.cooper.store.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserStoreImpl implements UserStore{

    private final Map<Long, User> userList;

    public UserStoreImpl() {
        this.userList = new HashMap<>();
    }

    @Override
    public void addUser(User user) {
        userList.put(user.getUid(), user);
    }

    @Override
    public User getUser(Long uid) {
        return userList.get(uid);
    }
}
