package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class ItemList {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String listName;

    @OneToMany(mappedBy = "itemList",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "item-list")
    private List<Item> items;

    @ManyToOne
    @JsonBackReference(value = "user-lists")
    private User user;

    @OneToMany
    @JsonManagedReference(value = "itemListCollaborators")
    private List<Collaborator> collaborators;

    public ItemList() {
    }

    public ItemList(String listName, List<Item> items, User user) {
        this.listName = listName;
        this.items = items;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

//    @OneToMany(mappedBy = "userItemList", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Item> userItems = new ArrayList<>();
//
//    //@OneToMany(mappedBy = "userItemList", cascade = CascadeType.ALL, orphanRemoval = true)
//    //private List<ItemWithCreationDate> userItems;
//
//    @OneToMany
//    private List<User> collaborators;
//
//    public ItemList() {
//
//    }
//
//    public ItemList(String listName, String username, int userID, List<Item> userItems, List<User> collaborators) {
//        this.listName = listName;
//        this.username = username;
//        this.userID = userID;
//        this.userItems = userItems;
//        this.collaborators = collaborators;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getListName() {
//        return listName;
//    }
//
//    public void setListName(String listName) {
//        this.listName = listName;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public List<Item> getUserItems() {
//        return userItems;
//    }
//
//    public void setUserItems(List<Item> userItems) {
//        this.userItems = userItems;
//    }
//
//    public List<User> getCollaborators() {
//        return collaborators;
//    }
//
//    public void setCollaborators(List<User> collaborators) {
//        this.collaborators = collaborators;
//    }
//
//    /*
//
//    public UserItemList(String listName, int userID, List<ItemWithCreationDate> userItems) {
//        this.listName = listName;
//        this.userID = userID;
//        this.userItems = userItems;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getListName() {
//        return listName;
//    }
//
//    public void setListName(String listName) {
//        this.listName = listName;
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public List<ItemWithCreationDate> getUserItems() {
//        return userItems;
//    }
//
//    public void setUserItems(List<ItemWithCreationDate> userItems) {
//        this.userItems = userItems;
//    } */
//
//    /*
//
//    public UserItemList(String listName, int userID, ItemWithCreationDate item) {
//        this.listName = listName;
//        this.userID = userID;
//        this.item = item;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getListName() {
//        return listName;
//    }
//
//    public void setListName(String listName) {
//        this.listName = listName;
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public ItemWithCreationDate getItem() {
//        return item;
//    }
//
//    public void setItem(ItemWithCreationDate item) {
//        this.item = item;
//    } */
//
//    /*
//    @Column
//    private ArrayList<ItemWithCreationDate> userItems;
//
//    public UserItemList() {
//
//    }
//
//    public UserItemList(String listName, int userID, ArrayList<ItemWithCreationDate> userItems) {
//        this.listName = listName;
//        this.userID = userID;
//        this.userItems = userItems;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public String getListName() {
//        return listName;
//    }
//
//    public void setListName(String listName) {
//        this.listName = listName;
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public ArrayList<ItemWithCreationDate> getUserItems() {
//        return userItems;
//    }
//
//    public void setUserItems(ArrayList<ItemWithCreationDate> userItems) {
//        this.userItems = userItems;
//    } */
//}
