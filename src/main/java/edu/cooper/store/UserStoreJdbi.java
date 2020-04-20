package edu.cooper.store;
import org.jdbi.v3.core.Jdbi;
import edu.cooper.Handler;
import edu.cooper.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserStoreJdbi implements UserStore{

    private final Jdbi jdbi;

    public UserStoreJdbi(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void populateDb() {
        jdbi.withHandle(
                handle ->
                        handle.execute(
                                "create table if not exists users (uid bigint auto_increment, uname varchar(255), pwd varchar(255), email varchar(255));"));
    }

    @Override
    public List<User> getUserList() {
        return jdbi.withHandle(
                handle ->
                        handle.createQuery
                                ("select uid, uname, pwd, email from users").mapToBean(User.class).list());
    }

    @Override
    public User addUser(final Handler.CreateUserRequest createUserRequest) {
        return jdbi.withHandle(
                handle ->
                        handle
                                .createUpdate("insert into users (uname, pwd, email) values (:uname, :pwd, :email)")
                                .bind("uname", createUserRequest.getUname())
                                .bind("pwd", createUserRequest.getPwd())
                                .bind("email", createUserRequest.getEmail())
                                .executeAndReturnGeneratedKeys("uid")
                                .mapToBean(User.class)
                                .one());
    }

    @Override
    public User getUser(Long uid) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select uid, uname, pwd, email from users where uid = ?", uid).mapToBean(User.class).one());
    }

    @Override
    public Optional<User> getUserByUname(String uname) {
        return jdbi.withHandle(
                handle ->
                        handle.select
                                ("select uid, uname, pwd, email from users where uname = ?", uname).mapToBean(User.class).findOne());
    }
}

