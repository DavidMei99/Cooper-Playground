package edu.cooper;

import edu.cooper.*;
import edu.cooper.model.*;
import edu.cooper.store.*;
import java.util.List;
import java.util.stream.Collectors;

public class Service {

    private final UserStore userStore;

    public Service(UserStore userStore){
        this.userStore = userStore;
    }

    public User createUser(User user){
        userStore.addUser(user);
        return user;
    }
}
