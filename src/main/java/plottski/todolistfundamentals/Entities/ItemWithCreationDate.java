package plottski.todolistfundamentals.Entities;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class ItemWithCreationDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long creationTime;

    @Column(nullable = false)
    private int userID;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int listID;

    @Column(nullable = false)
    private String listName;

    @Column(nullable = false)
    private String dueDate;

    @ManyToOne
    private UserItemList listOfItems;

    public ItemWithCreationDate() {

    }

    public ItemWithCreationDate(String title, String description, Long creationTime, int userID, String username, int listID, String listName, String dueDate, UserItemList listOfItems) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.userID = userID;
        this.username = username;
        this.listID = listID;
        this.listName = listName;
        this.dueDate = dueDate;
        this.listOfItems = listOfItems;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public UserItemList getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(UserItemList listOfItems) {
        this.listOfItems = listOfItems;
    }
}



   /* @Override
    public int compareTo(ItemWithCreationDate item) {
        return this.title.compareTo(item.title);
    }

    @Override
    public String toString() {
        return "ItemWithCreationDate{" +
                "title='" + title + '\'' +
                ", description=" + description + '\'' +
                ", creationTime=" + creationTime + '\'' +
                ", userId=" + userID + '\'' +
                ", username=" + username + '\'' +
                ", listID=" + listID + '\'' +
                ", listName=" + listName + '\'' +
                ", dueDate=" + dueDate + '\'' +
                ", listOfItems=" + listOfItems +
                '}';
    } */

    /*
    public ItemWithCreationDate(String title, String description, Long creationTime, int userID, int listID, String username, String listName, String dueDate) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.userID = userID;
        this.listID = listID;
        this.username = username;
        this.listName = listName;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public UserItemList getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(UserItemList listOfItems) {
        this.listOfItems = listOfItems;
    } */
