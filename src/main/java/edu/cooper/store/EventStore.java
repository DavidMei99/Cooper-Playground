package edu.cooper.store;

import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;

public interface EventStore {
    List<Event> getEventList();

    Event addEvent(final Handler.CreateEventRequest createEventRequest);

    Event getEvent(Long eid);

    void editEvent(Long eid, String etime, String location);

    List<Event> getGroupEvents(Long gid);
}
