package edu.cooper;


import com.google.gson.Gson;
import edu.cooper.TemplateEngine.FreeMarkerEngine;
import edu.cooper.store.*;
import edu.cooper.util.JsonTransformer;
import org.jdbi.v3.core.Jdbi;
import spark.Redirect;
import spark.Spark;
import spark.ModelAndView;
import edu.cooper.store.GroupStoreJdbi;
import edu.cooper.store.UserStoreJdbi;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;

import static spark.Spark.*;


public class Main {
    public static void main(String[] args) throws IOException {
        // User user1 = new User("Joseph", "123456ab");
        // Spark.get("/uid1", (req, res) -> user1.getUid());

        staticFileLocation("/public");
        Gson gson = new Gson();
        String url = "jdbc:h2:~/cpdb";
        Jdbi jdbi = Jdbi.create(url);
        GroupStoreJdbi groupStore = new GroupStoreJdbi(jdbi);
        UserStoreJdbi userStore = new UserStoreJdbi(jdbi);
        EventStoreJdbi eventStore = new EventStoreJdbi(jdbi);
        UserGroupRelJdbi userGroupRel = new UserGroupRelJdbi(jdbi);
        UserEventRelJdbi userEventRel = new UserEventRelJdbi(jdbi);
        // create tables in "cpdb" database
        groupStore.populateDb();
        userStore.populateDb();
        eventStore.populateDb();
        userGroupRel.populateDb();
        userEventRel.populateDb();

        // UserStore userStore = new UserStoreImpl();
        // GroupStore groupStore = new GroupStoreImpl();
        Service service = new Service(userStore, groupStore, eventStore, userGroupRel, userEventRel);
        Handler handler = new Handler(service, gson);

        JsonTransformer jsonTransformer = new JsonTransformer();

        //Spark.get("/ping", (req, res) -> handler.welcome(), jsonTransformer); // pass

        Spark.get("/", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("title", "Welcome to Cooper Playground");
            viewObjects.put("templateName", "home.ftl");
            return new ModelAndView(viewObjects, "main.ftl");
        }, new FreeMarkerEngine());


        //user register
        //Spark.post("/user/register/:username/pwd/:password/email/:email",
               // (req, res) -> handler.createUser(req), jsonTransformer); // pass

        Spark.get("/createUser", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "createForm.ftl");
            return new ModelAndView(viewObjects, "main.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/createUser", (request, response) -> {
            return handler.createUser(request);
        });


        //user login
        //Spark.get("/user/login/:username/pwd/:password", (req, res) -> handler.loginUser(req), jsonTransformer); //pass

        Spark.get("/login", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "loginForm.ftl");
            return new ModelAndView(viewObjects, "main.ftl");
        }, new FreeMarkerEngine());

        //redirect.post("/login", "/");

        Spark.post("/login", (request, response) -> {

            String returnVal = handler.loginUser(request);
            if (returnVal.compareTo("Login Success\r\n")==0)
                return returnVal;
            else
                return 0;
                });

        Spark.get("/loginSuccess", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            String uname = request.session().attribute("currentUser");
            viewObjects.put("title", "Welcome Back to Cooper Playground, "+uname);
            //viewObjects.put("name", uname);
            viewObjects.put("templateName", "home.ftl");
            return new ModelAndView(viewObjects, "loginSuccess.ftl");
        }, new FreeMarkerEngine());

        Spark.get("/loginGroup", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            String uname = request.session().attribute("currentUser");
            viewObjects.put("title", "Welcome to your Groups, "+uname);
            //viewObjects.put("name", uname);
            viewObjects.put("templateName", "home.ftl");
            return new ModelAndView(viewObjects, "loginGroup.ftl");
        }, new FreeMarkerEngine());

        Spark.get("/loginEvent", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            String uname = request.session().attribute("currentUser");
            viewObjects.put("title", "Welcome to your Events, "+uname);
            //viewObjects.put("name", uname);
            viewObjects.put("templateName", "home.ftl");
            return new ModelAndView(viewObjects, "loginEvent.ftl");
        }, new FreeMarkerEngine());



        //create group
        //Spark.post("/user/:username/group/:groupname/create", (req, res) -> handler.createGroup(req), jsonTransformer); // pass
        Spark.get("/createGroup", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "createGroupForm.ftl");
            return new ModelAndView(viewObjects, "loginGroup.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/createGroup", (request, response) -> {
            return handler.createGroup(request);
        });

        //create event
        Spark.get("/createEvent", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "createEventForm.ftl");
            return new ModelAndView(viewObjects, "loginEvent.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/createEvent", (request, response) -> {
            return handler.createEvent(request);
        });


        //attend group
        //Spark.put("/user/:username/group/:groupname/attend", (req, res) -> handler.attendGroup(req), jsonTransformer); // pass

        Spark.get("/attendGroup", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "attendGroupForm.ftl");
            return new ModelAndView(viewObjects, "loginGroup.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/attendGroup", (request, response) -> {
            return handler.attendGroup(request);
        });

        Spark.get("/attendEvent", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "attendEventForm.ftl");
            return new ModelAndView(viewObjects, "loginEvent.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/attendEvent", (request, response) -> {
            return handler.attendEvent(request);
        });


        //add user to group
        Spark.put("/user/:username/group/:groupname/user/:username2/invite",
                (req, res) -> handler.addUserToGroup(req), jsonTransformer); // pass

        Spark.get("/inviteGroup", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "inviteGroupForm.ftl");
            return new ModelAndView(viewObjects, "loginGroup.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/inviteGroup", (request, response) -> {
            System.out.println(request.body());
            return handler.addUserToGroup(request);
        });

        //remove user from group
        Spark.put("/user/:username/group/:groupname/user/:username2/remove",
                (req, res) -> handler.removeUserFromGroup(req), jsonTransformer); // pass

        //create event
        //creator is not default in created event !!!
        Spark.post("/user/:username/group/:groupname/event/:eventname/create",
                (req, res) -> handler.createEvent(req), jsonTransformer); // pass

        //attend event
        Spark.put("/user/:username/group/:groupname/event/:eventname/attend",
                (req, res) -> handler.attendEvent(req), jsonTransformer); // pass

        //add member to event
        Spark.put("/user/:username/group/:groupname/event/:eventname/user/:username2/invite",
                (req, res) -> handler.addUserToEvent(req), jsonTransformer); // pass

        //remove member from event
        Spark.put("/user/:username/group/:groupname/event/:eventname/user/:username2/remove",
                (req, res) -> handler.removeUserFromEvent(req), jsonTransformer); // pass

        // edit event time and location
        Spark.put("/user/:username/group/:groupname/event/:eventname/time/:time/location/:location/edit",
                (req, res) -> handler.editEvent(req), jsonTransformer); // pass

        //get users (for debug)
        Spark.get("/user/list", (req, res) -> handler.getUserList(req), jsonTransformer); // pass

        //get user's groups (for debug)
        Spark.get("/user/:uid/group/view", (req, res) -> handler.getUserGroups(req), jsonTransformer); // pass

        Spark.get("/getMyGroup", (request, response) -> {
            response.status(200);
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "showMyGroup.ftl");
            return new ModelAndView(viewObjects, "loginGroup.ftl");
        }, new FreeMarkerEngine());

        get("/getGroups", (request, response) -> {
            response.status(200);
            return gson.toJson(handler.getUserGroupsByUname(request));
        });

        get("/getGroupsLess", (request, response) -> {
            response.status(200);
            return gson.toJson(handler.getUserGroupsByUnameLess(request));
        });

        Spark.get("/getMyEvent", (request, response) -> {
            response.status(200);
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "showMyEvent.ftl");
            return new ModelAndView(viewObjects, "loginEvent.ftl");
        }, new FreeMarkerEngine());

        get("/getEvents", (request, response) -> {
            response.status(200);
            return gson.toJson(handler.getUserEventsByUname(request));
        });

        get("/getEventsLess", (request, response) -> {
            response.status(200);
            return gson.toJson(handler.getUserEventsByUnameLess(request));
        });

        get("/getGroupEvents/:gname", (request, response) -> {
            response.status(200);
            return gson.toJson(handler.getEventsByGname(request));
        });

        get("/getGroupUsers/:gname", (request, response) -> {
            response.status(200);
            return gson.toJson(handler.getUsersByGname(request));
        });


        //get user's groups (for user by uname)
        Spark.get("/user/uname/:username/group/view", (req, res) -> handler.getUserGroupsByUname(req), jsonTransformer); // pass

        //get groups (for debug)
        Spark.get("/group/list", (req, res) -> handler.getGroupList(req), jsonTransformer); // pass

        //get events
        Spark.get("/event/list", (req, res) -> handler.getEventList(req), jsonTransformer); // pass

        //get user's events (for user by uname)
        Spark.get("/user/uname/:username/event/view", (req, res) -> handler.getUserEventsByUname(req), jsonTransformer); // pass

        //trans group's admin
        Spark.put("/user/:username/user/:username2/group/:groupname/transfer", (req, res) -> handler.transferAdmin(req), jsonTransformer); // pass

        Spark.get("/transferAdmin", (request, response) -> {
            Map<String, Object> viewObjects = new HashMap<String, Object>();
            viewObjects.put("templateName", "transferAdminForm.ftl");
            return new ModelAndView(viewObjects, "loginGroup.ftl");
        }, new FreeMarkerEngine());

        Spark.post("/transferAdmin", (request, response) -> {
            System.out.println(request.body());
            return handler.transferAdmin(request);
        });

        //remove groups

        //remove events

        //rsvp in frontend

        //leave group

        //not attend event
    }
}









