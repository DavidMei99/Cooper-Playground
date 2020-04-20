package edu.cooper.store;

import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EventStore {
    List<Event> getEventList();

    Optional<Event> addEvent(final Handler.CreateEventRequest createEventRequest);

    Optional<Event> getEvent(Long eid);

    void editEvent(Long eid, String etime, String location);

    List<Event> getGroupEvents(Long gid);

    Optional<Event> getEventByEname(String ename, Long gid);
}
