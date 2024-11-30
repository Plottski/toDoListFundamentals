package plottski.todolistfundamentals.Entities;

import jakarta.persistence.*;


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
    private String dueDate;

    @ManyToOne
    private UserItemList listOfItems;

    public ItemWithCreationDate(){

    }

    public ItemWithCreationDate(String title, String description, Long creationTime, int userID, String username, String dueDate) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.userID = userID;
        this.username = username;
        this.dueDate = dueDate;
        //this.listOfItems = listOfItems;
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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    /*

    public ItemWithCreationDate(String title, String description, Long creationTime, int userID, String username,
                                String dueDate, UserItemList userItemList) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.userID = userID;
        this.username = username;
        this.dueDate = dueDate;
        this.userItemList = userItemList;
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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public UserItemList getUserItemList() {
        return userItemList;
    }

    public void setUserItemList(UserItemList userItemList) {
        this.userItemList = userItemList;
    } */

    /*

    public ItemWithCreationDate(String title, String description, Long creationTime, int userID, String username, String dueDate) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.userID = userID;
        this.username = username;
        this.dueDate = dueDate;
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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    } */
}