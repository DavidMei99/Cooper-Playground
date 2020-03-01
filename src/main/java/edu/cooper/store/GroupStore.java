package edu.cooper.store;

import edu.cooper.model.*;

import java.util.Map;

public interface GroupStore {
    Map<Long, Group> getGroupList();

    void addGroup(Group group);

    void addEvent(Event event);

    Group getGroup(Long gid);

    Group getGroupByGname(String gname);
}