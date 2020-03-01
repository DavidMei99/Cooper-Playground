package edu.cooper.store;

import edu.cooper.model.Group;
import edu.cooper.model.User;

import java.util.HashMap;
import java.util.Map;

public class GroupStoreImpl implements GroupStore{

    private final Map<Long, Group> groupList;

    public GroupStoreImpl() {
        this.groupList = new HashMap<>();
    }

    public Map<Long, Group> getGroupList() {return groupList;}

    @Override
    public void addGroup(Group group) {groupList.put(group.getGid(), group);}

    @Override
    public Group getGroup(Long gid) {return groupList.get(gid);}
}
