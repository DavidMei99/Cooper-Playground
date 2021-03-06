package edu.cooper.store;
import org.jdbi.v3.core.Jdbi;
import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;

public class UserEventRelJdbi {

    private final Jdbi jdbi;

    public UserEventRelJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void populateDb() {
        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "create table if not exists userevent (uid bigint, eid bigint);"));
    }

    public void addUserEvent(Long uid, Long eid) {
        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate("insert into userevent (uid, eid) values (:uid, :eid)")
                                .bind("uid", uid)
                                .bind("eid", eid)
                                .execute());
    }

    public List<Event> getUserEventList(Long uid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select events.eid, ename, etime, location, groupId from events join userevent on userevent.eid=events.eid where uid = ?", uid)
                                .mapToBean(Event.class).list());
    }

    public Boolean userInEvent(Long uid, Long eid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select eid from userevent where uid = ?", uid)
                                .mapTo(Long.class).list()).contains(eid);
    }

    public void removeUserFromEvent(Long uid, Long eid) {
        jdbi.withHandle(
                handle ->
                        handle.createUpdate("delete from userevent where uid=:uid and eid=:eid")
                                .bind("uid", uid)
                                .bind("eid", eid)
                                .execute());
    }
}
