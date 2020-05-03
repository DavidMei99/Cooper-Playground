package edu.cooper.store;
import org.jdbi.v3.core.Jdbi;
import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EventStoreJdbi implements EventStore {

    private final Jdbi jdbi;

    public EventStoreJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void populateDb() {
        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "drop table if exists events"));

        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "create table if not exists events (eid bigint auto_increment, ename varchar(255), etime varchar(255), location varchar(255), groupId bigint);"));
    }

    @Override
    public List<Event> getEventList() {
        return jdbi.withHandle(
                handle ->
                        handle.createQuery
                                ("select eid, ename, etime, location, groupId from events").mapToBean(Event.class).list());
    }

    @Override
    public Optional<Event> addEvent(Handler.CreateEventRequest createEventRequest) {
        System.out.println(createEventRequest.getGroupId());
        return jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate("insert into events (ename, etime, location, groupId) values (:ename, :etime, :location, :groupId)")
                                .bind("ename", createEventRequest.getEname())
                                .bind("etime", createEventRequest.getEtime())
                                .bind("location", createEventRequest.getLocation())
                                .bind("groupId", createEventRequest.getGroupId())
                                .executeAndReturnGeneratedKeys("eid")
                                .mapToBean(Event.class)
                                .findOne());

    }

    @Override
    public Optional<Event> getEvent(Long eid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select eid, ename, etime, location, groupId from events where eid = ?", eid)
                                .mapToBean(Event.class)
                                .findOne());
    }

    @Override
    public void editEvent(Long eid, String etime, String location) {
        jdbi.withHandle(
                handle ->
                        handle.createUpdate
                                ("update events set etime=:etime, location=:location where eid=:eid")
                                .bind("etime", etime)
                                .bind("location", location)
                                .bind("eid", eid).execute());
    }

    @Override
    public List<Event> getGroupEvents(Long gid) {
        return jdbi.withHandle(
                handle ->
                        handle.createQuery
                                ("select eid, ename, etime, location, groupId from events where groupId=:groupId")
                                .bind("groupId", gid)
                                .mapToBean(Event.class).list());
    }

    @Override
    public Optional<Event> getEventByEname(String ename, Long gid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select eid, ename, etime, location, groupId from events where ename=:ename and groupId =:groupId")
                                .bind("ename", ename).bind("groupId", gid).mapToBean(Event.class).findOne());
    }

}
