package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserItemList {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String listName;

    @Column
    private String username;

    @Column
    private int userID;

    @OneToMany(mappedBy = "userItemList", cascade = CascadeType.ALL)
    private List<ItemWithCreationDate> userItems;

    //@OneToMany(mappedBy = "userItemList", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<ItemWithCreationDate> userItems;

    @OneToMany
    private List<UserForDB> collaborators;

    public UserItemList() {

    }

    public UserItemList(String listName, String username, int userID, List<ItemWithCreationDate> userItems, List<UserForDB> collaborators) {
        this.listName = listName;
        this.username = username;
        this.userID = userID;
        this.userItems = userItems;
        this.collaborators = collaborators;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<ItemWithCreationDate> getUserItems() {
        return userItems;
    }

    public void setUserItems(List<ItemWithCreationDate> userItems) {
        this.userItems = userItems;
    }

    public List<UserForDB> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<UserForDB> collaborators) {
        this.collaborators = collaborators;
    }

    /*

    public UserItemList(String listName, int userID, List<ItemWithCreationDate> userItems) {
        this.listName = listName;
        this.userID = userID;
        this.userItems = userItems;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<ItemWithCreationDate> getUserItems() {
        return userItems;
    }

    public void setUserItems(List<ItemWithCreationDate> userItems) {
        this.userItems = userItems;
    } */

    /*

    public UserItemList(String listName, int userID, ItemWithCreationDate item) {
        this.listName = listName;
        this.userID = userID;
        this.item = item;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ItemWithCreationDate getItem() {
        return item;
    }

    public void setItem(ItemWithCreationDate item) {
        this.item = item;
    } */

    /*
    @Column
    private ArrayList<ItemWithCreationDate> userItems;

    public UserItemList() {

    }

    public UserItemList(String listName, int userID, ArrayList<ItemWithCreationDate> userItems) {
        this.listName = listName;
        this.userID = userID;
        this.userItems = userItems;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ArrayList<ItemWithCreationDate> getUserItems() {
        return userItems;
    }

    public void setUserItems(ArrayList<ItemWithCreationDate> userItems) {
        this.userItems = userItems;
    } */
}
