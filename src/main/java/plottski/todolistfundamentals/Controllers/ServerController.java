package plottski.todolistfundamentals.Controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import plottski.todolistfundamentals.Entities.*;
import plottski.todolistfundamentals.Services.*;
import plottski.todolistfundamentals.Utilities.ItemListWithUserWrapper;

import java.util.*;


import static java.lang.Double.parseDouble;

@RestController
public class ServerController {

    @Autowired
    UserRepo users;

    @Autowired
    ItemRepo items;

    @Transactional
    private void delete(int id) {
            items.deleteById(id);
    }

    @Autowired
    ItemListsRepo itemLists;


    @RequestMapping(path = "/signup", method = RequestMethod.POST)
    public ResponseEntity<User> userSignUp(HttpSession session, @RequestBody User user) {

        if (users.findByUsername(user.getUsername()) == null && !user.getEmail().contains("@")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        user.setLoggedIn(true);
        users.save(user);
        session.setAttribute("username", user.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);

//        if (users.findByUsername(user.getUsername()) != null && !isValidEmail(user.getEmail())) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        user.setLoggedIn(true);
//        users.save(user);
//        session.setAttribute("username", user.getUsername());
//        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<User> userLogin(HttpSession session, @RequestBody User user) {
        if (users.findByUsername(user.getUsername()) == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user2 = users.findByUsername(user.getUsername());
        session.setAttribute("username", user.getUsername());
        user.setLoggedIn(true);
        users.save(user2);
        return new ResponseEntity<User>(user2, HttpStatus.OK);
    }


    @RequestMapping(path = "/add-list", method = RequestMethod.POST)
    public ResponseEntity<ItemList> newUserList(HttpSession session, @RequestBody String listName) {
        User user = users.findByUsername(session.getAttribute("username").toString());
        if (user.getUsername() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ItemList itemList = new ItemList(listName, null, user, null);
        user.getItemLists().add(itemList);
        users.save(user);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/get-lists", method = RequestMethod.GET)
    public ResponseEntity<List<ItemList>> findUserItemLists(HttpSession session) {
        User user = users.findByUsername(session.getAttribute("username").toString());
        if (user.getUsername() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ItemList> userItemLists = itemLists.findAllListsByUser(user);
        return new ResponseEntity<>(userItemLists, HttpStatus.OK);
    }

    @RequestMapping(path = "/find-specific-list", method = RequestMethod.POST)
    public ResponseEntity<ItemList> findSpecificList(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);
        ItemList itemList = itemLists.findByListNameAndUser(listName, user);
        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;
        return new ResponseEntity<>(itemList, HttpStatus.OK);

    }

    //ItemAndListWrapperUtilClass itemAndListWrapperUtilClass
    // Notes for help needed, If I am sending back an object like an item but I also want to send something else back

    // that I need or want to use on the back end how do I wrap the request?

    // Should I create a new object that extends ITEM just to include other parameters that I need or want to send back?

    // HashMap String, String will work but it would only work for me because I know ahead of time what the keys are. No one else will, how to prevent?

    // Best practice for this situation. What if I want to send back an arraylist of items and also an array list of usernames for example? This is an actual
    // use case for this project once collaborators are integrated since it could have multiple different people that can add or delete items.

    @PostMapping("/add-item")
    public ResponseEntity<ItemList> addItemToList(HttpSession session, @RequestBody HashMap<String, String> itemWithListName) {
        //public ResponseEntity<ItemListWithUserWrapper> addItemToList(HttpSession session, @RequestBody HashMap<String, String> itemWithListName) {
        User user = findUserBySession(session);
//        if (itemAndListWrapperUtilClass == null || user == null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//
        if (itemWithListName == null || user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String listName = itemWithListName.get("listName");
        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        //method for dupe check did not work trying this for the naming convention. I probably need to change listName to title to make it easy.
        String title = listName;


        // This is to stop item duplication. The idea is that if I pull the item out by ItemList id and by the title of
        // the item input from the client. Then the item exists and we should not do anything with the input since it
        // would be a duplicate item.
//        if (items.findByItemListAndTitle(itemList, listName) != null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
        //System.out.println(items.findByItemListAndTitle(itemList, title).getUser());
        if (items.findByItemListAndTitle(itemList, title) != null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

//        if (items.findByTitle(itemWithListName.get("title")) != null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }

        long creationTime = Long.parseLong(itemWithListName.get("creationTime"));
//        Item item = new Item(itemWithListName.get("title"), itemWithListName.get("description"), creationTime,
//                itemWithListName.get("dueDate"), user.getUsername(), itemList, user);

        Item item = new Item(itemWithListName.get("title"), itemWithListName.get("description"), creationTime,
                itemWithListName.get("dueDate"), itemList);

        //Item item = itemAndListWrapperUtilClass.getItem();
        //String listName = itemAndListWrapperUtilClass.getListName();

        System.out.println(item.getDescription());
        System.out.println(listName);

        itemList.getItems().add(item);
        itemLists.save(itemList);
        //ItemListWithUserWrapper itemListWithUserWrapper = new ItemListWithUserWrapper(itemList, user);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
        //return new ResponseEntity<>(itemListWithUserWrapper, HttpStatus.OK);
    }

@PostMapping("/add-collaborator")
public ResponseEntity<ItemList> addCollaboratorToItemList(HttpSession session, @RequestBody ItemListWithUserWrapper itemListWithUserWrapper) {
        User user = findUserBySession(session);

    System.out.println(itemListWithUserWrapper.getListName());
    System.out.println(itemListWithUserWrapper.getUsername());

        User userCollaboratorToAdd = users.findByUsername(itemListWithUserWrapper.getUsername());

        ItemList itemList = itemLists.findByListNameAndUser(itemListWithUserWrapper.getListName(), user);

        if (itemList.getCollaborators().stream().anyMatch(user1 ->
            user1.getUsername().equals(userCollaboratorToAdd.getUsername()))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        itemList.getCollaborators().add(userCollaboratorToAdd);
        itemLists.save(itemList);

        return new ResponseEntity<>(itemList, HttpStatus.OK);

    }

//    @RequestMapping(path = "/add-item", method = RequestMethod.POST)
//    public ResponseEntity<ItemList> addItemToList(HttpSession session, @RequestBody Item item, @RequestParam String listName) {
//        User user = findUserBySession(session);
//        if (item == null || user == null) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        System.out.println(item.getDescription());
//        System.out.println(listName);
//        ItemList itemList = itemLists.findByListNameAndUser(listName, user);
//        itemList.getItems().add(item);
//        itemLists.save(itemList);
//        return new ResponseEntity<>(itemList, HttpStatus.OK);
//    }

    @PostMapping("/sort-items-ascending")
    public ResponseEntity<ItemList> sortItemsAscending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(titleComparator);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/sort-items-descending", method = RequestMethod.POST)
    public ResponseEntity<ItemList> sortItemsDescending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(titleComparator.reversed());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @RequestMapping(path = "/sort-items-descriptions-ascending", method = RequestMethod.POST)
    public ResponseEntity<ItemList> sortItemsByDescriptionAscending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(descriptionAscendingComparator);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/sort-items-descriptions-descending")
    public ResponseEntity<ItemList> sortItemsByDescriptionDescending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(descriptionAscendingComparator.reversed());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

//    @RequestMapping(path = "/sort-items-users-ascending", method = RequestMethod.POST)
//    public ResponseEntity<ItemList> sortItemsByUsersAscending(HttpSession session, @RequestBody String listName) {
//        User user = findUserBySession(session);
//
//        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
//        if (FORBIDDEN != null) return FORBIDDEN;
//
//        ItemList itemList = itemLists.findByListNameAndUser(listName, user);
//
//        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
//        if (NOT_FOUND != null) return NOT_FOUND;
//
//        itemList.getItems().sort(usersComparator);
//        return new ResponseEntity<>(itemList, HttpStatus.OK);
//    }

//    @PostMapping("/sort-items-users-descending")
//    public ResponseEntity<ItemList> sortItemsByUsersDescending(HttpSession session, @RequestBody String listName) {
//        User user = findUserBySession(session);
//
//        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
//        if (FORBIDDEN != null) return FORBIDDEN;
//
//        ItemList itemList = itemLists.findByListNameAndUser(listName, user);
//
//        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
//        if (NOT_FOUND != null) return NOT_FOUND;
//
//        itemList.getItems().sort(usersComparator.reversed());
//        return new ResponseEntity<>(itemList, HttpStatus.OK);
//    }

    @PostMapping("/sort-items-creationdate-ascending")
    public ResponseEntity<ItemList> sortItemsByCreationDateAscending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(creationDateComparator);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/sort-items-creationdate-descending")
    public ResponseEntity<ItemList> sortItemsByCreationDateDescending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(creationDateComparator.reversed());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/sort-items-duedate-ascending")
    public ResponseEntity<ItemList> sortItemsByDueDateAscending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(dueDateComparator);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/sort-items-duedate-descending")
    public ResponseEntity<ItemList> sortItemsByDueDateDescending(HttpSession session, @RequestBody String listName) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        ItemList itemList = itemLists.findByListNameAndUser(listName, user);

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;

        itemList.getItems().sort(dueDateComparator.reversed());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/delete-item")
    public ResponseEntity<ItemList> deleteUserItem(HttpSession session, @RequestBody Item item) {
        User user = findUserBySession(session);

        ResponseEntity<ItemList> FORBIDDEN = userValidationCheck(user);
        if (FORBIDDEN != null) return FORBIDDEN;

        System.out.println(item.getId());

        ItemList itemList = itemLists.findByItems(item);

        //deleteUserItem(item, session);

        System.out.println(itemList.getItems().size());

//        for (Item item2 : itemList.getItems()) {
//            System.out.println(item2.getId());
//        }

        ResponseEntity<ItemList> NOT_FOUND = itemListNullCheck(itemList);
        if (NOT_FOUND != null) return NOT_FOUND;


        Item itemToDelete = items.findById(item.getId());
        System.out.println(itemList.getItems().size());
        //items.delete(item);
        //items.delete(itemToDelete);
        itemList.getItems().remove(itemToDelete);
        System.out.println(itemList.getItems().size());
        itemLists.save(itemList);
       // itemLists.save(itemList);
        //items.deleteById(item.getId());
        //items.deleteByItem(item);

        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

//    @Transactional
//    public void deleteUserItem(Item item, HttpSession session) {
//        User user = findUserBySession(session);
//        System.out.println(user.getItems().size());
//        user.getItems().remove(item);
//        System.out.println(user.getItems().size());
//        users.save(user);
//    }


    private static ResponseEntity<ItemList> itemListNullCheck(ItemList itemList) {
        //ComparatorWrapper comparatorWrapper = new ComparatorWrapper();
        //Comparator<Item> dueDateComparator = //comparatorWrapper.getDueDateComparator();
        if (itemList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return null;
    }

    private static ResponseEntity<ItemList> userValidationCheck(User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return null;
    }

    private Comparator<Item> dueDateComparator = new Comparator<Item>() {
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

//    Comparator<Item> usersComparator = new Comparator<Item>() {
//        @Override
//        public int compare(Item item1, Item item2) {
//            return item1.getCreatorName().compareToIgnoreCase(item2.getCreatorName());
//        }
//    };




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


    public User findUserBySession(HttpSession session) {
        User user = users.findByUsername(session.getAttribute("username").toString());
        if (user == null) {
            return null;
        }
        return user;
    }




}



