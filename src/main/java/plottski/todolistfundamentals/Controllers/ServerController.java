package plottski.todolistfundamentals.Controllers;

import com.fasterxml.jackson.core.JsonParser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import plottski.todolistfundamentals.Entities.*;
import plottski.todolistfundamentals.Services.ItemDB;
import plottski.todolistfundamentals.Services.UserItemListsRepo;
import plottski.todolistfundamentals.Services.UserRepo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ServerController {

    @Autowired
    UserRepo users;

    @Autowired
    ItemDB items;

    @Autowired
    UserItemListsRepo userLists;

    private final HashMap<String, User> userDB = new HashMap<String, User>();
    private final HashMap<String, ArrayList<Item>> itemDB = new HashMap<String, ArrayList<Item>>();

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<UserForDB> userSignUp(HttpSession session, @RequestBody UserForDB user) {
        if (users.findByUsername(user.getUsername()) == null) {
            UserForDB userForDB = new UserForDB(user.getUsername(), user.getPassword(), "plottski@gmail.com", true);
            users.save(userForDB);
            session.setAttribute("username", userForDB.getUsername());
            return new ResponseEntity<UserForDB>(user, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserForDB> userLogin(HttpSession session, @RequestBody UserForDB user) {
        if (users.findByUsername(user.getUsername()) != null) {
            UserForDB userFromDB = users.findByUsername(user.getUsername());
            userFromDB.setLoggedIn(true);
            session.setAttribute("username", userFromDB.getUsername());
            return new ResponseEntity<UserForDB>(userFromDB, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "add-list", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> newUserList(HttpSession session, @RequestBody UserItemList userItemList) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            userItemList.setUserID(userFromDB.getId());
            userLists.save(userItemList);
            return new ResponseEntity<>(userItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/add-item", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ItemWithCreationDate>> userItems(HttpSession session, @RequestBody ItemWithCreationDate theItem) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        theItem.setUserID(userFromDB.getId());
        theItem.setUsername(userFromDB.getUsername());
        System.out.println(theItem.getDueDate());
        Instant instant = Instant.ofEpochMilli(theItem.getCreationTime());
        //put the unformatted due date in a string
        String theDueDateWhole = theItem.getDueDate();
        //split the string into an array of strings
        String [] dateParts = theDueDateWhole.split("-");
        //order the string to spit out "American" style date formatting
        String formattedDueDate = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
        //set the formatted due date and save in the DB
        System.out.println(formattedDueDate);
        theItem.setDueDate(formattedDueDate);
        items.save(theItem);
        if (userFromDB.getUsername() != null) {
            ArrayList<ItemWithCreationDate> dbItems = dbItems = items.findAll();
            for (int i = 0; i < dbItems.size(); i++) {
                if (dbItems.get(i).getUserID() != userFromDB.getId()) {
                    System.out.println(dbItems.get(i));
                    dbItems.remove(i);
                }
            }
            return new ResponseEntity<ArrayList<ItemWithCreationDate>>(dbItems, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

   /* @RequestMapping(path = "/delete-item", method = RequestMethod.DELETE)
    public ResponseEntity<ArrayList<Item>> deleteUserItems(HttpSession session, @RequestBody Item item) {
        if (userDB.containsKey(session.getAttribute("password").toString())) {
            //User userDeleter = new User(session.getAttribute("username").toString(), session.getAttribute("password").toString(),true);
            User user = userDB.get(session.getAttribute("password").toString());
            ArrayList<Item> userItems = itemDB.get(user.getUsername());
            for (int i = 0; i < userItems.size(); i++) {
                if (userItems.contains(item.getTitle())) {
                    userItems.remove(item);
                    itemDB.put(user.getUsername(), userItems);
                    return new ResponseEntity<ArrayList<Item>>(userItems, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } */

    @RequestMapping(path = "/delete-item", method = RequestMethod.DELETE)
    public ResponseEntity<ArrayList<ItemWithCreationDate>> deleteUserItems(HttpSession session, @RequestBody ItemWithCreationDate item) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            ArrayList<ItemWithCreationDate> allUserItems = items.findAll();
            System.out.println(item.getTitle());
            for (int i = 0; i < allUserItems.size(); i++) {
                //System.out.println(allUserItems.get(i).getTitle());
                if (allUserItems.get(i).getTitle().equals(item.getTitle())) {
                    System.out.println(allUserItems.get(i).getTitle());
                    items.delete(allUserItems.get(i));
                    allUserItems.remove(i);
                    System.out.println(allUserItems.get(i).getTitle());
                    return new ResponseEntity<>(allUserItems, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/get-all-items", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ItemWithCreationDate>> allUserItems(HttpSession session, @RequestBody UserForDB user) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB != null) {
            ArrayList<ItemWithCreationDate> dbItems = items.findAll();
            for (int i = 0; i < dbItems.size(); i++) {
                if (dbItems.get(i).getUserID() != userFromDB.getId()) {
                    System.out.println(dbItems.get(i));
                    dbItems.remove(i);
                }
            }
            return new ResponseEntity<ArrayList<ItemWithCreationDate>>(dbItems, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public ResponseEntity<User> userLogout(HttpSession session, @RequestBody User user) {
        if (userDB.containsKey(session.getAttribute("password").toString())) {
            User userFromDB = userDB.get(session.getAttribute("password").toString());
            userFromDB.setLoggedIn(false);
            userDB.replace(userFromDB.getPassword(), userFromDB);
            User emptyUser = new User(null, null, false);
            //return new ResponseEntity<User>(emptyUser, HttpStatus.OK);\
            System.out.println("you are hitting the endpoint successfully");
            return null;
        }
        return null;
    }

    @RequestMapping(path = "/get-lists", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<UserItemList>> getUserLists(HttpSession session) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        //System.out.println(userFromDB.getUsername());
        if (userFromDB.getUsername() != null && userFromDB.isLoggedIn()) {
            ArrayList<UserItemList> theUserLists = userLists.findAll();
            for (int i = 0; i < theUserLists.size(); i++) {
                if (theUserLists.get(i).getUserID() != userFromDB.getId()) {
                    theUserLists.remove(i);
                }
            }
            return new ResponseEntity<>(theUserLists, HttpStatus.OK);

            //UserItemList theUserItems = (UserItemList) userLists.findAll();
            //ArrayList<> theUserLists = (ArrayList) userLists.findAll();
            //for (int i = 0; i < theUserLists.size(); i ++) {

            //}
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //HashMap<String, String> json
    @RequestMapping(path = "/create-new-list", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<String>> createUserList(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        System.out.println(userFromDB.getUsername());
        //String listName = json.get("listName");
        System.out.println(listName);
        if (userFromDB.getUsername() != null) {
            UserItemList userItemList = new UserItemList(listName, userFromDB.getId(), null, null);
            userLists.save(userItemList);
            ArrayList<String> allUserListsNames = new ArrayList<>();
            ArrayList<UserItemList> allUsersLists = userLists.findAll();
            for (int i = 0; i < allUsersLists.size(); i++) {
                UserItemList eachItemList = allUsersLists.get(i);
                if (eachItemList.getUserID() == userFromDB.getId()) {
                    allUserListsNames.add(eachItemList.getListName());
                }
            }
            return new ResponseEntity<>(allUserListsNames, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/get-lists", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<UserItemList>> getUserItemLists(HttpSession session) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            ArrayList<UserItemList> allUserLists = userLists.findAll();
            for (int i = 0; i < allUserLists.size(); i++) {
                if (allUserLists.get(i).getUserID() != userFromDB.getId()) {
                    allUserLists.remove(i);
                }
            }
            return new ResponseEntity<>(allUserLists, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}



 /*
    @RequestMapping(path = "/get-all-items", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<Item>> allUserItems(HttpSession session, @RequestBody User user) {
        if (userDB.containsKey(session.getAttribute("password").toString())) {
            User userFromDB = userDB.get(session.getAttribute("password").toString());
            if (itemDB.containsKey(userFromDB.getUsername())) {
                ArrayList<Item> userItems = itemDB.get(userFromDB.getUsername());
                return new ResponseEntity<ArrayList<Item>>(userItems, HttpStatus.OK);
            }
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } */

//all deprecated code from hashmap database simulation.

/*@RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<User> userSignUp(HttpSession session, @RequestBody User user) {
        System.out.println(user.getUsername());
        //Trying to add persistance
        if (users.findByUsername(user.getUsername()) == null) {
            user.setLoggedIn(true);
            UserForDB userForDB = new UserForDB(user.getUsername(), user.getPassword(), "plottski@gmail.com", true);
            users.save(userForDB);
            System.out.println("user saved successfully");
            UserForDB userDBTest = users.findByUsername(user.getUsername());
            System.out.println(userDBTest);
            System.out.println(userDBTest.getUsername());
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (userDB.containsKey(user.getPassword())) {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
        userDB.putIfAbsent(user.getPassword(), user);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("password", user.getPassword());
        user.setLoggedIn(true);
        //users.save(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> userLogin(HttpSession session, @RequestBody User user) {
        if (userDB.containsKey(user.getPassword())) {
            user.setLoggedIn(true);
            System.out.println(user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("password", user.getPassword());
            session.setAttribute("loggedin", true);
           // if (itemDB.containsKey(user.getUsername())) {
           //     ArrayList<Item> userItems = itemDB.get(user.getUsername());
           //     return new ResponseEntity<>(userItems, HttpStatus.OK);
           // }
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else if (userDB == null) {
            return new ResponseEntity<User>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<User>(HttpStatus.FORBIDDEN);

    } */

   /* @RequestMapping(path = "/add-item", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<Item>> userItems(HttpSession session, @RequestBody Item item) {
        if (userDB.containsKey(session.getAttribute("password").toString())) {
            User userCreator = new User(session.getAttribute("username").toString(), session.getAttribute("password").toString(),
            true);
            if (item.getTitle().isEmpty() || item.getDescription().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            Item itemForDB = new Item(item.getTitle(), item.getDescription(), userCreator);
            System.out.println(itemForDB);
        if (itemDB.containsKey(userCreator.getUsername())) {
            ArrayList<Item> userItems = itemDB.get(userCreator.getUsername());
            userItems.add(itemForDB);
            System.out.println(userItems);
            itemDB.replace(userCreator.getUsername(), userItems);
            return new ResponseEntity<ArrayList<Item>>(userItems, HttpStatus.OK);
        }
        else {
            ArrayList<Item> newUserItems = new ArrayList<>();
            newUserItems.add(itemForDB);
            itemDB.put(userCreator.getUsername(), newUserItems);
            return new ResponseEntity<ArrayList<Item>>(newUserItems, HttpStatus.OK);
            }
        }
        return new ResponseEntity<ArrayList<Item>>(HttpStatus.FORBIDDEN);
    } */

