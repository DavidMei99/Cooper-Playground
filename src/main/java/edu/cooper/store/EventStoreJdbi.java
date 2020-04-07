package edu.cooper.store;
import org.jdbi.v3.core.Jdbi;
import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;

public class EventStoreJdbi implements EventStore {

    private final Jdbi jdbi;

    public EventStoreJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void populateDb() {
        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "create table events (eid bigint auto_increment, ename varchar(255), etime varchar(255), location varchar(255));"));
    }

    @Override
    public List<Event> getEventList() {
        return jdbi.withHandle(
                handle ->
                        handle.createQuery
                                ("select eid, ename, etime, location from events").mapToBean(Event.class).list());
    }

    @Override
    public Event addEvent(Handler.CreateEventRequest createEventRequest) {
        return jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate("insert into events (ename, etime, location) values (:ename, :etime, :location)")
                                .bind("ename", createEventRequest.getEname())
                                .bind("etime", createEventRequest.getEtime())
                                .bind("location", createEventRequest.getLocation())
                                .executeAndReturnGeneratedKeys("eid")
                                .mapToBean(Event.class)
                                .one());
    }

    @Override
    public Event getEvent(Long eid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select eid, ename, etime, location from groups where eid = ?", eid).mapToBean(Event.class).one());
    }

    @Override
    public void editEvent(Long eid, String etime, String location) {
        jdbi.withHandle(
                handle ->
                        handle.createUpdate
                                ("update events() set etime=:etime, location=:location where eid=:eid")
                                        .bind("etime", etime).bind("location", location).bind("eid", eid).execute());
    }
}
