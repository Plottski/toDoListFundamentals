package plottski.todolistfundamentals.Controllers;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import plottski.todolistfundamentals.Entities.Item;
import plottski.todolistfundamentals.Entities.User;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ServerController {

    private final HashMap<String, User> userDB = new HashMap<String, User>();
    private final HashMap<User, ArrayList<Item>> itemDB = new HashMap<User, ArrayList<Item>>();

    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<User> userSignUp(HttpSession session, @RequestBody User user) {
        System.out.println(user.getUsername());
        if (userDB.containsKey(user.getPassword())) {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }
        userDB.putIfAbsent(user.getPassword(), user);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("password", user.getPassword());
        user.setLoggedIn(true);
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
            Item itemForDB = new Item(item.getTitle(), item.getDescription(), userCreator);
            System.out.println(itemForDB);
        if (itemDB.containsKey(userCreator)) {
            ArrayList<Item> userItems = itemDB.get(userCreator);
            userItems.add(itemForDB);
            itemDB.replace(userCreator, userItems);
            return new ResponseEntity<ArrayList<Item>>(userItems, HttpStatus.OK);
        }
        else {
            ArrayList<Item> newUserItems = new ArrayList<>();
            newUserItems.add(itemForDB);
            itemDB.put(userCreator, newUserItems);
            return new ResponseEntity<ArrayList<Item>>(newUserItems, HttpStatus.OK);
            }
        }
        return new ResponseEntity<ArrayList<Item>>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "delete-item", method = RequestMethod.DELETE)
    public ResponseEntity<ArrayList<Item>> deleteUserItems(HttpSession session, @RequestBody Item item) {
        if (userDB.containsKey(session.getAttribute("password").toString())) {
            //User userDeleter = new User(session.getAttribute("username").toString(), session.getAttribute("password").toString(),true);
            User user = userDB.get(session.getAttribute("password").toString());
            ArrayList<Item> userItems = itemDB.get(user);
            for (int i = 0; i < userItems.size(); i++) {
                if (userItems.contains(item.getTitle())) {
                    userItems.remove(item);
                    itemDB.put(user, userItems);
                    return new ResponseEntity<ArrayList<Item>>(userItems, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
