package plottski.todolistfundamentals.Entities;

public class Item {

    String title;

    String description;

    User creator;

    public Item(String title, String description, User creator) {
        this.title = title;
        this.description = description;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
