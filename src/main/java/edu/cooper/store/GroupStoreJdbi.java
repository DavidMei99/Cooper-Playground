package edu.cooper.store;
import org.jdbi.v3.core.Jdbi;
import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;

public class GroupStoreJdbi implements GroupStore{

    private final Jdbi jdbi;

    public GroupStoreJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void populateDb() {
        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "create table groups (gid bigint auto_increment, gname varchar(255), adminid bigint);"));
    }

    @Override
    public List<Group> getGroupList() {
        return jdbi.withHandle(
                handle ->
                        handle.createQuery
                                ("select gid, gname, adminid from groups").mapToBean(Group.class).list());
    }

    @Override
    public Group addGroup(final Handler.CreateGroupRequest createGroupRequest) {
        return jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate("insert into groups (gname, adminid) values (:gname, :adminid)")
                                .bind("gname", createGroupRequest.getGname())
                                .bind("adminid", createGroupRequest.getAdminid())
                                .executeAndReturnGeneratedKeys("gid")
                                .mapToBean(Group.class)
                                .one());
    }

    /*@Override
    public void addEvent(Event event) {
        ;
    }*/

    @Override
    public Group getGroup(Long gid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select gid, gname, adminid from groups where gid = ?", gid).mapToBean(Group.class).one());
    }

    @Override
    public Group getGroupByGname(String gname) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select gid, gname, adminid from groups where gname = ?", gname).mapToBean(Group.class).one());
    }
}
