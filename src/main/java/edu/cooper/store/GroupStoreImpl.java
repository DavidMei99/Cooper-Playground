package edu.cooper.store;

import edu.cooper.model.Event;
import edu.cooper.model.Group;
import edu.cooper.model.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupStoreImpl implements GroupStore{

    private final Map<Long, Group> groupList;

    public GroupStoreImpl() {
        this.groupList = new HashMap<>();
    }

    public List<Group> getGroupList() {return groupList;}

    @Override
    public void addGroup(Group group) {
        groupList.put(group.getGid(), group);
    }

    @Override
    public void addEvent(Event event) {
        getGroup(event.getGroupId()).addEvent2Group(event);
    }

    @Override
    public Group getGroup(Long gid) {return groupList.get(gid);}

    @Override
    public Group getGroupByGname(String gname) {
        Iterator<Map.Entry<Long, Group>> itr = groupList.entrySet().iterator();
        while(itr.hasNext())
        {
            Map.Entry<Long, Group> entry = itr.next();
            if(gname.compareTo(entry.getValue().getGname())==0){
                return entry.getValue();
            }
        }
        return null;
    }




}
