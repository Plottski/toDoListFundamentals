package plottski.todolistfundamentals.Controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import plottski.todolistfundamentals.Entities.Item;
import plottski.todolistfundamentals.Entities.User;
import plottski.todolistfundamentals.Entities.UserForDB;
import plottski.todolistfundamentals.Services.UserRepo;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ServerController {

    @Autowired
    UserRepo users;

    private final HashMap<String, User> userDB = new HashMap<String, User>();
    private final HashMap<String, ArrayList<Item>> itemDB = new HashMap<String, ArrayList<Item>>();

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
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

    }

    @RequestMapping(path = "/add-item", method = RequestMethod.POST)
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
    }

    @RequestMapping(path = "/delete-item", method = RequestMethod.DELETE)
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
    }

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
}
