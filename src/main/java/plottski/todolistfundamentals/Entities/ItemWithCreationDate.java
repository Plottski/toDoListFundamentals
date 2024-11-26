package plottski.todolistfundamentals.Entities;

import java.util.Date;

public class ItemWithCreationDate {

    String title;

    String description;

    Date creationTime;

    User creator;

    public ItemWithCreationDate(String title, String description, Date creationTime, User creator) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.creator = creator;
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
