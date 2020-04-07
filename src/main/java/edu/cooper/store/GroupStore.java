package edu.cooper.store;

import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;

public interface GroupStore {
    List<Group> getGroupList();

    Group addGroup(final Handler.CreateGroupRequest createGroupRequest);

    // void addEvent(Event event);

    Group getGroup(Long gid);

    Group getGroupByGname(String gname);

}