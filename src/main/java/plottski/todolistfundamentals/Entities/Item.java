package plottski.todolistfundamentals.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Item {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long creationTime;

    @Column(nullable = false)
    private String dueDate;

    @ManyToOne
    @JsonBackReference(value = "item-list")
    private ItemList itemList;

//    @ManyToOne
//    @JsonBackReference(value = "user-items")
//    private User user;

    public Item() {

    }

    public Item(String title, String description, Long creationTime, String dueDate, ItemList itemList) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.dueDate = dueDate;
        this.itemList = itemList;
    }

    //    public Item(String title, String description, Long creationTime, String dueDate, String creatorName, ItemList itemList, User user) {
//        this.title = title;
//        this.description = description;
//        this.creationTime = creationTime;
//        this.dueDate = dueDate;
//        this.creatorName = creatorName;
//        this.itemList = itemList;
//        this.user = user;
//    }

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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
}