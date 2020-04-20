package edu.cooper.store;

import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GroupStore {
    List<Group> getGroupList();

    Optional<Group> addGroup(final Handler.CreateGroupRequest createGroupRequest);

    // void addEvent(Event event);

    Optional<Group> getGroup(Long gid);

    Optional<Group> getGroupByGname(String gname);

    void updateAdmin(Long gid, Long adminid);

}