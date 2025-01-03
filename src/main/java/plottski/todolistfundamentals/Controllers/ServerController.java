package plottski.todolistfundamentals.Controllers;

import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import plottski.todolistfundamentals.Entities.*;
import plottski.todolistfundamentals.Services.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


import static java.lang.Double.parseDouble;

@RestController
public class ServerController {

    @Autowired
    UserRepo users;

    @Autowired
    ItemDB items;

    @Autowired
    UserItemListsRepo userLists;

    @Autowired
    CollaborationRepo collaborations;

    private final HashMap<String, User> userDB = new HashMap<String, User>();
    private final HashMap<String, ArrayList<Item>> itemDB = new HashMap<String, ArrayList<Item>>();


    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<UserForDB> userSignUp(HttpSession session, @RequestBody UserForDB user) {
        if (users.findByUsername(user.getUsername()) != null && !isValidEmail(user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        user.setLoggedIn(true);
        users.save(user);
        session.setAttribute("username", user.getUsername());
        return new ResponseEntity<UserForDB>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserForDB> userLogin(HttpSession session, @RequestBody UserForDB user) {
        if (users.findByUsername(user.getUsername()) != null) {
            UserForDB userFromDB = users.findByUsername(user.getUsername());
            session.setAttribute("username", userFromDB.getUsername());
            userFromDB.setLoggedIn(true);
            users.save(userFromDB);
            return new ResponseEntity<UserForDB>(userFromDB, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/add-list", method = RequestMethod.POST)
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
    public ResponseEntity<UserItemList> addItemtoUserItemList(HttpSession session, @RequestBody ItemWithCreationDate theItem) {
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());

        setItemDateAndUser(theItem, userFromDB);


        //UserItemList userItemList = userLists.findByListName(theItem.getListName());
        //userItemList.getUserItems().add(theItem);

        //List theUserItems = userItemList.getUserItems();
        //theUserItems.add(theItem);
        //userItemList.setUserItems(theUserItems);
        //userLists.save(userItemList);

        UserItemList theUserItemList = userLists.findByListNameAndUserID(theItem.getListName(), userFromDB.getId());
        System.out.println(theUserItemList.getId());
        theUserItemList.getUserItems().add(theItem);
        userLists.save(theUserItemList);

        //theUserItemList.getUserItems().add(theItem);
        items.save(theItem);
        //userLists.save(theUserItemList);

        //items.save(theItem);


        if (theUserItemList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //theUserItemList.getUserItems().add(theItem);
        //userLists.save(theUserItemList);

        return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        //ResponseEntity<UserItemList> theUserItemList = getUserItemListResponseEntity(theItem, userFromDB);
    }


    @RequestMapping(path = "/delete-item", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> deleteUserItem(HttpSession session, @RequestBody ItemWithCreationDate item) {
        System.out.println(item.getDescription());
        System.out.println(item.getTitle());
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            ItemWithCreationDate itemFromDB = items.findByusernameAndTitle(userFromDB.getUsername(), item.getTitle());
            System.out.println(itemFromDB.getUserID());
            //ArrayList<ItemWithCreationDate> userListItems = items.findAllByListID(item.getListID());
            ArrayList<ItemWithCreationDate> userListItems = items.findAllByListID(itemFromDB.getListID());
            for (int i = 0; i < userListItems.size(); i++) {
                if (userListItems.get(i).getTitle().equals(item.getTitle())) {
                    ItemWithCreationDate itemToDeleteFromDB = userListItems.get(i);
                    userListItems.remove(i);
                    items.deleteById(itemFromDB.getId());
                    UserItemList updatedUserItemList = userLists.findByListName(itemFromDB.getListName());
                    updatedUserItemList.setUserItems(userListItems);
                    return new ResponseEntity<>(updatedUserItemList, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            System.out.println("you are hitting the endpoint successfully");
            return null;
        }
        return null;
    }

    @RequestMapping(path = "/get-lists", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<UserItemList>> getUserLists(HttpSession session) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null && userFromDB.isLoggedIn()) {
            ArrayList<UserItemList> theUserLists = userLists.findAll();
            for (int i = 0; i < theUserLists.size(); i++) {
                if (theUserLists.get(i).getUserID() != userFromDB.getId()) {
                    theUserLists.remove(i);
                }
            }
            return new ResponseEntity<>(theUserLists, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/find-specific-list", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> findSpecificList(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        //sortItemsAscending(session,listName);
        if (userFromDB.getUsername() != null) {
            ArrayList<UserItemList> allUserItemLists = userLists.findAll();
            for (int i = 0; i < allUserItemLists.size(); i++) {
                if (allUserItemLists.get(i).getUserID() == userFromDB.getId() && allUserItemLists.get(i).getListName().equals(listName)) {
                    UserItemList theUserItemList = allUserItemLists.get(i);
                    ArrayList<ItemWithCreationDate> allUserListItems = items.findAllByListID(theUserItemList.getId());
                    theUserItemList.setUserItems(allUserListItems);
                    return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/create-new-list", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<String>> createUserList(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            UserItemList userItemList = new UserItemList(listName, userFromDB.getUsername(), userFromDB.getId(), null, null);
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


    private ResponseEntity<UserItemList> getUserItemListResponseEntity(ItemWithCreationDate theItem, UserForDB userFromDB) {
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        for (int i = 0; i < allUserItemLists.size(); i++) {
            if (allUserItemLists.get(i).getListName().equals(theItem.getListName())) {
                UserItemList theUserItemList = allUserItemLists.get(i);
                theUserItemList.setUsername(userFromDB.getUsername());
                List<ItemWithCreationDate> itemsList = theUserItemList.getUserItems();
                userLists.save(theUserItemList);
                UserItemList userItemListForItemListID = userLists.findByListName(theItem.getListName());
                theItem.setListID(userItemListForItemListID.getId());
                items.save(theItem);
                theUserItemList.setUserItems(items.findAllByListID(theItem.getListID()));
                return new ResponseEntity<UserItemList>(theUserItemList, HttpStatus.OK);
            }
        }
        return null;
    }

    private static void setItemDateAndUser(ItemWithCreationDate theItem, UserForDB userFromDB) {
        Instant instant = Instant.ofEpochMilli(theItem.getCreationTime());
        //put the unformatted due date in a string
        String theDueDateWhole = theItem.getDueDate();
        //split the string into an array of strings
        String[] dateParts = theDueDateWhole.split("-");
        //order the string to spit out "American" style date formatting
        String formattedDueDate = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
        //set the formatted due date and save in the DB
        theItem.setDueDate(formattedDueDate);
        theItem.setUsername(userFromDB.getUsername());
        theItem.setUserID(userFromDB.getId());
        theItem.setUsername(userFromDB.getUsername());
    }

    public boolean validUser(HttpSession session) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB == null || !userFromDB.isLoggedIn()) {
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        return email.contains("@");
    }

    @RequestMapping(path = "/sort-items-ascending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsAscending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-descending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsDescending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(titleDescendingComparator);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-descriptions-ascending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByDescriptionAscending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(descriptionAscendingComparator);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-descriptions-descending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByDescriptionDescending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(descriptionDescendingComparator);
            Collections.reverse(itemsToSort);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-users-ascending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByUsersAscending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(usersComparator);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-users-descending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByUsersDescending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(usersComparator);
            Collections.reverse(itemsToSort);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-creationdate-ascending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByCreationDateAscending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(creationDateComparator);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-creationdate-descending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByCreationDateDescending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(creationDateComparator);
            Collections.reverse(itemsToSort);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-duedate-ascending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByDueDateAscending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(dueDateComparator);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-duedate-descending", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> sortItemsByDueDateDescending(HttpSession session, @RequestBody String listName) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        UserItemList theUserItemList = findAndSortUserListAscending(allUserItemLists, userFromDB, listName);
        if (theUserItemList != null) {
            ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(dueDateComparator);
            Collections.reverse(itemsToSort);
            theUserItemList.setUserItems(itemsToSort);
            return new ResponseEntity<>(theUserItemList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/filterByTitle", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> filterItemsByTitle(HttpSession session, @RequestBody HashMap<String, String> json) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndTitle(userFromDB.getId(), json.get("title"));
        UserItemList userItemList = userLists.findByListName(json.get("listName"));
        System.out.println(json.get("listName"));
        System.out.println(json.get("title"));
        if (userItemList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userItemList.setUserItems(userItems);
        return new ResponseEntity<>(userItemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByDescription", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> filterItemsByDescription(HttpSession session, @RequestBody HashMap<String, String> json) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndDescription(userFromDB.getId(), json.get("description"));
        UserItemList userItemList = userLists.findByListName(json.get("listName"));
        userItemList.setUserItems(userItems);
        System.out.println(json.get("listName"));
        System.out.println(json.get("description"));
        if (userItemList.getUserItems().getFirst() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userItemList.setUserItems(userItems);
        return new ResponseEntity<>(userItemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByUsername", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> filterItemsByUsername(HttpSession session, @RequestBody HashMap<String, String> json) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        //ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndDescription(userFromDB.getId(), json.get("description"));
        ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndUsername(userFromDB.getId(), json.get("username"));
        UserItemList userItemList = userLists.findByListName(json.get("listName"));
        userItemList.setUserItems(userItems);
        System.out.println(json.get("listName"));
        System.out.println(json.get("description"));
        if (userItemList.getUserItems().getFirst() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userItemList.setUserItems(userItems);
        return new ResponseEntity<>(userItemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByCreationTime", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> filterItemsByCreationTime(HttpSession session, @RequestBody HashMap<String, String> json) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndListName(userFromDB.getId(), json.get("listName"));
        ArrayList<ItemWithCreationDate> itemsWithSpecificCreationDates = new ArrayList<>();
        HashMap<String, ArrayList<ItemWithCreationDate>> longToStringDate = new HashMap<>();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd/yyyy");
        for (int i = 0; i < userItems.size(); i++) {
            Date dateToString = new Date(userItems.get(i).getCreationTime());
            if (timeFormatter.format(dateToString).equals(json.get("creationTime"))) {
                itemsWithSpecificCreationDates.add(userItems.get(i));
            }
        }
        longToStringDate.put(json.get("creationTime"), itemsWithSpecificCreationDates);

        UserItemList userItemList = userLists.findByListName(json.get("listName"));
        userItemList.setUserItems(itemsWithSpecificCreationDates);

        if (userItemList.getUserItems().getFirst() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userItemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByDueDate", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> filterItemsByDueDate(HttpSession session, @RequestBody HashMap<String, String> json) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndDueDate(userFromDB.getId(), json.get("dueDate"));
        UserItemList userItemList = userLists.findByListName(json.get("listName"));
        userItemList.setUserItems(userItems);

        if (userItemList.getUserItems().getFirst() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userItemList.setUserItems(userItems);
        return new ResponseEntity<>(userItemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/exportToExcel", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ItemWithCreationDate>> exportItemsToExcel(HttpSession session, @RequestBody String listname) throws IOException {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndListName(userFromDB.getId(), listname);

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Sheet1");
        int rowNum = 1;

        Row headerRow = sheet.createRow(1);

        Cell titleHeader = headerRow.createCell(0);
        titleHeader.setCellValue("Title");

        Cell descriptionHeader = headerRow.createCell(1);
        descriptionHeader.setCellValue("Description");

        Cell userHeader = headerRow.createCell(2);
        userHeader.setCellValue("User");

        Cell creationHeader = headerRow.createCell(3);
        creationHeader.setCellValue("Created");

        Cell dueDateHeader = headerRow.createCell(4);
        dueDateHeader.setCellValue("Due Date");

        for (ItemWithCreationDate item : userItems) {
            Row row = sheet.createRow(rowNum++);

            //I stored creation time as a long so I am converting it to a string to put into the sheet like you would see it on the website.
            SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date dateToString = new Date(item.getCreationTime());
            String creationTime = timeFormatter.format(dateToString);

            Cell titleCell = row.createCell(0);
            titleCell.setCellValue(item.getTitle());

            Cell descriptionCell = row.createCell(1);
            descriptionCell.setCellValue(item.getDescription());

            Cell userCell = row.createCell(2);
            userCell.setCellValue(item.getUsername());

            Cell creationTimeCell = row.createCell(3);
            creationTimeCell.setCellValue(creationTime);

            Cell dueDateCell = row.createCell(4);
            dueDateCell.setCellValue(item.getDueDate());
        }

        try (FileOutputStream fileOut = new FileOutputStream(userFromDB.getUsername() +"-"+ listname +"-Export.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Excel file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(userItems, HttpStatus.OK);
    }

    @RequestMapping(path = "/add-collaborator", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> addCollaboratorToExistingList(HttpSession session, @RequestBody HashMap<String, String> json) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        String listName = json.get("listName");
        String collaboratorUserName = json.get("collaboratorUserName");
        UserForDB userCollaboratorFromDB = users.findByUsername(collaboratorUserName);
        if (userCollaboratorFromDB.getUsername().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserItemList userListForCollaboration = userLists.findByListNameAndUserID(listName, userFromDB.getId());
        Collaboration collaboration = new Collaboration(userFromDB.getUsername(), userFromDB.getId(),
                userCollaboratorFromDB.getUsername(), userCollaboratorFromDB.getId(), userListForCollaboration.getId(),
                userListForCollaboration.getListName());

        collaborations.save(collaboration);

        return new ResponseEntity<>(userListForCollaboration, HttpStatus.OK);

    }




    //Would like a code review of what I was trying and why it was not working


      /*for (ItemWithCreationDate item : userItems) {
            System.out.println(userItems.size());
            SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date dateToString = new Date(item.getCreationTime());
            if (!timeFormatter.format(dateToString).equals(json.get("creationTime"))) {
                userItems.remove(item);
            }
        }*/
    //ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndDescription(userFromDB.getId(), json.get("description"));
    //ArrayList<ItemWithCreationDate> userItems = items.findAllByUserIDAndUsername(userFromDB.getId(), json.get("creationTime"));
       /*for (int i = 0; i < userItems.size(); i++) {
            SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date dateToString = new Date(userItems.get(i).getCreationTime());
           // longToStringDate.put(timeFormatter.format(dateToString), userItems.get(i));
            System.out.println(timeFormatter.format(dateToString));
            System.out.println(longToStringDate.size());
            System.out.println(longToStringDate.keySet());
            //System.out.println(dateToString);
            //System.out.println(userItems.size());
           /*if (timeFormatter.format(dateToString).equals(json.get("creationTime"))) {
                itemsWithSpecificCreationDates.add(userItems.get(i));;
            }*/
            /*if (!timeFormatter.format(dateToString).equals(json.get("creationTime"))) {
               // System.out.println(userItems.get(i).getTitle());
                //userItems.remove(userItems.get(i));
                userItems.remove(userItems.get(i));
               // i = 0;
            }*/
            //i++;

        //System.out.println(userItems.size());
        //SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd/yyyy");
        //Date dateToString = new Date(userItems.getFirst().getCreationTime());
        //String testDateParse = timeFormatter.format(dateToString);
        //System.out.println(testDateParse);

        //userItemList.setUserItems(itemsWithSpecificCreationDates);
        //System.out.println(json.get("listName"));
        //System.out.println(json.get("creationTime"));




    Comparator<ItemWithCreationDate> dueDateComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            return item1.getDueDate().compareTo(item2.getDueDate());
        }
    };

    Comparator<ItemWithCreationDate> creationDateComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            return item1.getCreationTime().compareTo(item2.getCreationTime());
        }
    };

    Comparator<ItemWithCreationDate> usersComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            return item1.getUsername().compareToIgnoreCase(item2.getUsername());
        }
    };




    Comparator<ItemWithCreationDate> titleComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            return item1.getTitle().compareToIgnoreCase(item2.getTitle());
        }
    };

    Comparator<ItemWithCreationDate> titleDescendingComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            return item2.getTitle().compareToIgnoreCase(item1.getTitle());
        }
    };

    Comparator<ItemWithCreationDate> descriptionAscendingComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            if (item1.getDescription() instanceof String && item2.getDescription() instanceof String) {
                return item1.getDescription().compareToIgnoreCase( item2.getDescription());
            }
            else {
                try {
                    Double numberDescriptionitem1 = parseDouble(item1.getDescription());
                    Double numberDescriptionItem2 = parseDouble(item1.getDescription());
                    if (numberDescriptionItem2 != null && numberDescriptionitem1 != null) {
                        return Double.compare(numberDescriptionitem1, numberDescriptionItem2);
                    } else if (numberDescriptionItem2 != null) {
                        return -1;
                    } else if (numberDescriptionitem1 != null) {
                        return 1;
                    } else if (item2.getDescription() instanceof String) {
                        return -1;
                    } else if (item1.getDescription() instanceof String) {
                        return 1;
                    }
                    return 0;
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };

    Comparator<ItemWithCreationDate> descriptionDescendingComparator = new Comparator<ItemWithCreationDate>() {
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            if (item1.getDescription() instanceof String && item2.getDescription() instanceof String) {
                return item1.getDescription().compareToIgnoreCase( item2.getDescription());
            }
            else {
                try {
                    Double numberDescriptionitem1 = parseDouble(item1.getDescription());
                    Double numberDescriptionItem2 = parseDouble(item1.getDescription());
                    if (numberDescriptionitem1 != null && numberDescriptionItem2 != null) {
                        return Double.compare(numberDescriptionitem1, numberDescriptionItem2);
                    } else if (numberDescriptionitem1 != null) {
                        return 1;
                    } else if (numberDescriptionItem2 != null) {
                        return -1;
                    } else if (item1.getDescription() instanceof String) {
                        return 1;
                    } else if (item2.getDescription() instanceof String) {
                        return -1;
                    }
                    return 0;
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };


    public UserItemList findAndSortUserListAscending(ArrayList<UserItemList> allUserItemLists, UserForDB userFromDB, String listName) {
        for (int i = 0; i < allUserItemLists.size(); i++) {
            if (allUserItemLists.get(i).getUserID() == userFromDB.getId() && allUserItemLists.get(i).getListName().equals(listName)) {
                UserItemList theUserItemList = allUserItemLists.get(i);
                ArrayList<ItemWithCreationDate> itemsToSort = items.findAllByListID(theUserItemList.getId());
                itemsToSort.sort(titleComparator);
                theUserItemList.setUserItems(itemsToSort);
                return theUserItemList;
            }
        }
        return null;
    }


    public UserItemList getSpecificUserItemList(HttpSession session, String listName, UserForDB userFromDB) {
        ArrayList<UserItemList> allUserItemLists = userLists.findAll();
        System.out.println(listName);
        System.out.println(userFromDB.getUsername());
        for (int i = 0; i < allUserItemLists.size(); i++) {
            //System.out.println(allUserItemLists.get(i));
            if (allUserItemLists.get(i).getUserID() == userFromDB.getId() && allUserItemLists.get(i).getListName().equals(listName)) {
                UserItemList theUserItemList = allUserItemLists.get(i);
                System.out.println(theUserItemList.getUsername());
                return theUserItemList;
            }
        }
        return null;
    }



}





/*Comparator<ItemWithCreationDate> dueDateComparator = new Comparator<ItemWithCreationDate>() {
        DateFormat stringToDate = new SimpleDateFormat("mm/dd/yyyy");
        @Override
        public int compare(ItemWithCreationDate item1, ItemWithCreationDate item2) {
            try {
                return stringToDate.parse(item1.getDescription()).compareTo(stringToDate.parse(item2.getDescription()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };*/


//UserItemList specificList = getSpecificUserItemList(session, listName, userFromDB);
        //System.out.println(specificList);
        //System.out.println(specificList.getUsername());
        //System.out.println(specificList.getListName());
        //System.out.println(specificList.getId());

        //Collections.sort(itemsToSort);

        //ArrayList<String> itemTitleStrings = new ArrayList<>();
        //for (int i = 0; i < itemsToSort.size(); i++) {
            //System.out.println(itemsToSort.get(i).getTitle());
            //Comparator<ItemWithCreationDate> titleComparator =
            // (itemsToSort.get(i).getTitle().compareToIgnoreCase(itemsToSort.get(i+1).getTitle()))
         //   ItemWithCreationDate currentItem = itemsToSort.get(i);
          //  itemTitleStrings.add(currentItem.getTitle());
        //}
        //for (int i = 0)
        //return new ResponseEntity<ArrayList<ItemWithCreationDate>>(itemsToSort, HttpStatus.OK);









 /* @RequestMapping(path = "/delete-item", method = RequestMethod.DELETE)
    public ResponseEntity<ArrayList<ItemWithCreationDate>> deleteUserItems(HttpSession session, @RequestBody ItemWithCreationDate item) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            ArrayList<ItemWithCreationDate> allUserItems = items.findAll();
            System.out.println(item.getTitle());
            for (int i = 0; i < allUserItems.size(); i++) {
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
    } */

    /*@RequestMapping(path = "/get-lists", method = RequestMethod.POST)
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
    } */




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

/*@RequestMapping(path = "/add-item", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> addItemToUserItemList(HttpSession session, @RequestBody ItemWithCreationDate theItem) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        //System.out.println(listName);
        if (userFromDB.getUsername() != null) {
            ArrayList<UserItemList> allUserItemLists = userLists.findUserListsByID(userFromDB.getId());
            System.out.println(allUserItemLists.get(0).getListName());
            for (int i = 0; i < allUserItemLists.size(); i++) {
                System.out.println(i);
                /*if (allUserItemLists.get(i).getUserID() == userFromDB.getId() && allUserItemLists.get(i).getUserID()) {
                    System.out.println(allUserItemLists.get(i).getUserID());
                    UserItemList actualUserItemList = allUserItemLists.get(i);
                    System.out.println(actualUserItemList.getListName());
                    //ItemWithCreationDate itemForDB = new ItemWithCreationDate(theItem.getTitle(), theItem.getDescription(),
                    //        theItem.getCreationTime(), userFromDB.getId(), userFromDB.getUsername(), theItem.getDueDate());
                    actualUserItemList.getUserItems().add(theItem);
                    //System.out.println(actualUserItemList.getUserItems().get(0).getTitle());
                    System.out.println(actualUserItemList.getUserItems().get(0).getTitle());
                    //List userItemsInList = actualUserItemList.getUserItems();
                    //userItemsInList.add(theItem);
                    //actualUserItemList.setUserItems(userItemsInList);
                    userLists.save(actualUserItemList);
                    return new ResponseEntity<>(actualUserItemList, HttpStatus.OK); */
// }
// }
//return new ResponseEntity<>(HttpStatus.NOT_FOUND);


   /* @RequestMapping(path = "/add-list", method = RequestMethod.POST)
    public ResponseEntity<UserItemList> newUserList(HttpSession session, @RequestBody String listName) {

    } */

    /*@RequestMapping(path = "/add-item", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ItemWithCreationDate>> userItems(HttpSession session, @RequestBody ItemWithCreationDate theItem) {
        UserForDB userFromDB = users.findByUsername(session.getAttribute("username").toString());
        theItem.setUserID(userFromDB.getId());
        theItem.setUsername(userFromDB.getUsername());
        System.out.println(theItem.getDueDate());
        Instant instant = Instant.ofEpochMilli(theItem.getCreationTime());
        //put the unformatted due date in a string
        String theDueDateWhole = theItem.getDueDate();
        //split the string into an array of strings
        String[] dateParts = theDueDateWhole.split("-");
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
    }*/

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

//System.out.println(userFromDB.getUsername());
//String listName = json.get("listName");
//System.out.println(listName);
//allUserItemLists.remove(i);
//System.out.println(allUserItems.get(i).getTitle());
//return new ResponseEntity<User>(emptyUser, HttpStatus.OK);\
//System.out.println(theUserItemList.getListName());
//System.out.println(allUserItemLists.get(i).getListName());
//System.out.println(allUserItemLists.get(i).getUserID());
//UserItemList theUserItems = (UserItemList) userLists.findAll();
//ArrayList<> theUserLists = (ArrayList) userLists.findAll();
//for (int i = 0; i < theUserLists.size(); i ++) {

//}
//HashMap<String, String> json
//System.out.println(userFromDB.getUsername());