package edu.cooper.store;
import org.jdbi.v3.core.Jdbi;
import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;

public class UserGroupRelJdbi {

    private final Jdbi jdbi;

    public UserGroupRelJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void populateDb() {
        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "create table if not exists usergroup (uid bigint, gid bigint);"));
    }

    public void addUserGroup(Long uid, Long gid) {
        jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate("insert into usergroup (uid, gid) values (:uid, :gid)")
                                .bind("uid", uid)
                                .bind("gid", gid)
                                .execute());
    }

    public List<Long> getUserGroupList(Long uid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select gid from usergroup where uid = ?", uid).mapTo(Long.class).list());
    }

    public List<Group> getUserGroupObjList(Long uid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select groups.gid, gname, adminid from groups join usergroup on usergroup.gid=groups.gid where usergroup.uid = ?", uid)
                                .mapToBean(Group.class).list());
    }

    public void removeUserFromGroup(Long uid, Long gid) {
        jdbi.withHandle(
                handle ->
                        handle.createUpdate("delete from usergroup where uid=:uid and gid=:gid")
                                .bind("uid", uid)
                                .bind("gid", gid)
                                .execute());
    }

}
