package plottski.todolistfundamentals.Controllers;

import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import plottski.todolistfundamentals.Entities.*;
import plottski.todolistfundamentals.Services.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


import static java.lang.Double.parseDouble;

@RestController
public class ServerController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ToDoListRepo toDoListRepo;

    @Autowired
    CollaborationRepo collaborations;



    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<User> userSignUp(HttpSession session, @RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()) != null && !isValidEmail(user.getEmail())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        user.setLoggedIn(true);
        userRepo.save(user);
        session.setAttribute("username", user.getUsername());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> userLogin(HttpSession session, @RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            User userFromDB = userRepo.findByUsername(user.getUsername());
            session.setAttribute("username", userFromDB.getUsername());
            userFromDB.setLoggedIn(true);
            userRepo.save(userFromDB);
            return new ResponseEntity<User>(userFromDB, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/add-list", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> newUserList(HttpSession session, @RequestBody ToDoList toDoList) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            toDoList.setUserForDB(userFromDB);
            toDoListRepo.save(toDoList);
            return new ResponseEntity<>(toDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(path = "/add-item", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> addItemToUserItemList(HttpSession session, @RequestBody Item theItem) {
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        ToDoList theToDoList = toDoListRepo.findByListNameAndUser(theItem.getListName(), userFromDB);
        theItem.setTodoList(theToDoList);
        setItemDateAndUser(theItem, userFromDB);
        theToDoList.getItems().add(theItem);
        theToDoList.getItems().add(theItem);
        toDoListRepo.save(theToDoList);
        return new ResponseEntity<>(theToDoList, HttpStatus.OK);
    }


    @RequestMapping(path = "/delete-item", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> deleteUserItem(HttpSession session, @RequestBody Item item) {
        System.out.println(item.getDescription());
        System.out.println(item.getTitle());
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            itemRepo.delete(item);
                return new ResponseEntity<>(toDoListRepo.findByListNameAndUser(item.getTodoList().getListName(), userFromDB), HttpStatus.OK);
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/get-all-items", method = RequestMethod.POST)
    public ResponseEntity<List<Item>> allUserItems(HttpSession session, @RequestBody User user) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB != null) {
            List<Item> allItemsForUser = new ArrayList<>();
            for (ToDoList toDoList : userFromDB.getTodoLists()) {
                allItemsForUser.addAll(toDoList.getItems());
            }
            return new ResponseEntity<List<Item>>(allItemsForUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }



    @RequestMapping(path = "/get-lists", method = RequestMethod.GET)
    public ResponseEntity<List<ToDoList>> getUserLists(HttpSession session) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() == null && !userFromDB.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(toDoListRepo.findAllListsById(userFromDB.getId()), HttpStatus.OK);
    }

    @RequestMapping(path = "/find-specific-list", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> findSpecificList(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            ToDoList specificToDoList = toDoListRepo.findByListNameAndUser(listName, userFromDB);
            return new ResponseEntity<>(specificToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/create-new-list", method = RequestMethod.POST)
    public ResponseEntity<List<String>> createUserList(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB.getUsername() != null) {
            ToDoList toDoList = new ToDoList(listName, userFromDB.getUsername(),userFromDB, null, null);
            //List<ToDoList> userItemLists =toDoListRepo.findAllListsByid(userFromDB.getId());
            ToDoList toDolist = new ToDoList(listName, userFromDB.getUsername(), userFromDB, null, null);
            userFromDB.getTodoLists().add(toDolist);
            //userFromDB.setUserItemListList(userItemLists);
            userRepo.save(userFromDB);
            //toDoListRepo.save(toDoList);
            List<ToDoList> allUsersLists = userFromDB.getTodoLists();
            List<String> strListOfUserLists = allUsersLists.stream()
                    .map(ToDoList::getListName)
                    .toList();
            return new ResponseEntity<>(strListOfUserLists, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private static void setItemDateAndUser(Item theItem, User userFromDB) {
        Instant instant = Instant.ofEpochMilli(theItem.getCreationTime());
        //put the unformatted due date in a string
        String theDueDateWhole = theItem.getDueDate();
        //split the string into an array of strings
        String[] dateParts = theDueDateWhole.split("-");
        //order the string to spit out "American" style date formatting
        String formattedDueDate = dateParts[1] + "/" + dateParts[2] + "/" + dateParts[0];
        //set the formatted due date and save in the DB
        theItem.setDueDate(formattedDueDate);
        theItem.getTodoList().setUserForDB(userFromDB);
    }

    public boolean validUser(HttpSession session) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (userFromDB == null || !userFromDB.isLoggedIn()) {
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        return email.contains("@");
    }

    @RequestMapping(path = "/sort-items-ascending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsAscending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-descending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsDescending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
                    //items.findAllByListID(theUserItemList.getId());
            itemsToSort.sort(titleDescendingComparator);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-descriptions-ascending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByDescriptionAscending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(descriptionAscendingComparator);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-descriptions-descending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByDescriptionDescending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(descriptionDescendingComparator);
            Collections.reverse(itemsToSort);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-users-ascending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByUsersAscending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(usersComparator);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-users-descending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByUsersDescending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(usersComparator);
            Collections.reverse(itemsToSort);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-creationdate-ascending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByCreationDateAscending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(creationDateComparator);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-creationdate-descending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByCreationDateDescending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(creationDateComparator);
            Collections.reverse(itemsToSort);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-duedate-ascending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByDueDateAscending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(dueDateComparator);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/sort-items-duedate-descending", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> sortItemsByDueDateDescending(HttpSession session, @RequestBody String listName) {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        ToDoList theToDoList = findAndSortUserListAscending(allToDoLists, userFromDB, listName);
        if (theToDoList != null) {
            List<Item> itemsToSort = theToDoList.getItems();
            itemsToSort.sort(dueDateComparator);
            Collections.reverse(itemsToSort);
            theToDoList.setItems(itemsToSort);
            return new ResponseEntity<>(theToDoList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/filterByTitle", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> filterItemsByTitle(HttpSession session, @RequestBody HashMap<String, String> json) {
        User user = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ToDoList toDoList = user.getTodoLists().stream()
                .filter(x->x.getListName().equals(json.get("listName"))).toList().getFirst();
        if (toDoList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(toDoList, HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByUsername", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> filterItemsByUsername(HttpSession session, @RequestBody HashMap<String, String> json) {
        User user = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<ToDoList> filteredToDoLists = user.getTodoLists().stream()
                .filter(toDoList -> toDoList.getListName().equals(json.get("listName")))
                .toList();
        if (filteredToDoLists.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredToDoLists.getFirst(), HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByCreationTime", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> filterItemsByCreationTime(HttpSession session, @RequestBody HashMap<String, String> json) {
        User user = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ToDoList toDoList = user.getTodoLists().stream()
                .filter(x -> x.getListName().equals(json.get("listName")))
                .toList().getFirst();
        if (toDoList==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Item> itemsWithSpecificCreationDates = toDoList.getItems().stream()
                .filter(x -> x.getCreationTime().toString().equals(json.get("creationTime")))
                .toList();
        toDoList.setItems(itemsWithSpecificCreationDates);

        if (toDoList.getItems().getFirst() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(toDoList, HttpStatus.OK);
    }

    @RequestMapping(path = "/filterByDueDate", method = RequestMethod.POST)
    public ResponseEntity<ToDoList> filterItemsByDueDate(HttpSession session, @RequestBody HashMap<String, String> json) {
        User user = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<ToDoList> filteredToDoLists = user.getTodoLists().stream()
                .filter(toDoList -> toDoList.getListName().equals(json.get("listName")))
                .toList();

        if (filteredToDoLists.size()==0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(filteredToDoLists.getFirst(), HttpStatus.OK);
    }


    @RequestMapping(path = "/edit-user-stuff", method = RequestMethod.POST)
        public ResponseEntity<User> modifyUserDetails(HttpSession session, @RequestBody User theUser) {
            User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
            if (!validUser(session)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            userFromDB.setUsername(userFromDB.getUsername());
            userFromDB.setPassword(userFromDB.getPassword());
            userFromDB.setEmail(userFromDB.getEmail());
            userRepo.save(theUser);
            return new ResponseEntity<>(theUser, HttpStatus.OK);

    }

    @RequestMapping(path = "/exportToExcel", method = RequestMethod.POST)
    public ResponseEntity<List<Item>> exportItemsToExcel(HttpSession session, @RequestBody String listName) throws IOException {
        User userFromDB = userRepo.findByUsername(session.getAttribute("username").toString());
        if (!validUser(session)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ToDoList toDoList = toDoListRepo.findByListName(listName);
        List<Item> userItems = toDoList.getItems();

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

        for (Item item : userItems) {
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
            userCell.setCellValue(item.getTodoList().getUser().getUsername());

            Cell creationTimeCell = row.createCell(3);
            creationTimeCell.setCellValue(creationTime);

            Cell dueDateCell = row.createCell(4);
            dueDateCell.setCellValue(item.getDueDate());
        }

        try (FileOutputStream fileOut = new FileOutputStream(userFromDB.getUsername() +"-"+ listName +"-Export.xlsx")) {
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

    Comparator<Item> dueDateComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getDueDate().compareTo(item2.getDueDate());
        }
    };

    Comparator<Item> creationDateComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getCreationTime().compareTo(item2.getCreationTime());
        }
    };

    Comparator<Item> usersComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getTodoList().getUser().getUsername().compareToIgnoreCase(item2.getTodoList().getUser().getUsername());
        }
    };

    Comparator<Item> titleComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getTitle().compareToIgnoreCase(item2.getTitle());
        }
    };

    Comparator<Item> titleDescendingComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item2.getTitle().compareToIgnoreCase(item1.getTitle());
        }
    };

    Comparator<Item> descriptionAscendingComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
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

    Comparator<Item> descriptionDescendingComparator = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
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


    public ToDoList findAndSortUserListAscending(ArrayList<ToDoList> allToDoLists, User userFromDB, String listName) {
        for (int i = 0; i < allToDoLists.size(); i++) {
            if (allToDoLists.get(i).getUser().getId() == userFromDB.getId() && allToDoLists.get(i).getListName().equals(listName)) {
                ToDoList theToDoList = allToDoLists.get(i);
                List<Item> itemsToSort = theToDoList.getItems();
                itemsToSort.sort(titleComparator);
                theToDoList.setItems(itemsToSort);
                return theToDoList;
            }
        }
        return null;
    }


    public ToDoList getSpecificUserItemList(HttpSession session, String listName, User userFromDB) {
        ArrayList<ToDoList> allToDoLists = toDoListRepo.findAll();
        System.out.println(listName);
        System.out.println(userFromDB.getUsername());
        for (int i = 0; i < allToDoLists.size(); i++) {
            if (allToDoLists.get(i).getUser().getId() == userFromDB.getId() && allToDoLists.get(i).getListName().equals(listName)) {
                ToDoList theToDoList = allToDoLists.get(i);
                System.out.println(theToDoList.getUser().getUsername());
                return theToDoList;
            }
        }
        return null;
    }
}