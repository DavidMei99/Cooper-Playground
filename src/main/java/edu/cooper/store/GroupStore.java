package edu.cooper.store;

import edu.cooper.model.Group;

import java.util.Map;

public interface GroupStore {
    Map<Long, Group> getGroupList();

    void addGroup(Group group);

    Group getGroup(Long uid);
}