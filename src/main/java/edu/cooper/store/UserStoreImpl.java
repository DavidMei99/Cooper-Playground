package edu.cooper.store;

import edu.cooper.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class UserStoreImpl implements UserStore{

    private final Map<Long, User> userList;

    public UserStoreImpl() {
        this.userList = new HashMap<>();
    }

    public Map<Long, User> getUserList(){return userList;}

    @Override
    public void addUser(User user) {
        userList.put(user.getUid(), user);
    }

    @Override
    public User getUser(Long uid) {
        return userList.get(uid);
    }

    @Override
    public User getUserByUname(String uname) {
        Iterator<Map.Entry<Long, User>> itr = userList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Long, User> entry = itr.next();
            // System.out.println(entry.getValue());
            if (uname.compareTo(entry.getValue().getUname()) == 0) {
                return entry.getValue();
            }
        }
        return null;
    }
}
