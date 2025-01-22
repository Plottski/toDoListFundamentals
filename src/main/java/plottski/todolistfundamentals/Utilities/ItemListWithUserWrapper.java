package plottski.todolistfundamentals.Utilities;

import plottski.todolistfundamentals.Entities.ItemList;
import plottski.todolistfundamentals.Entities.User;

public class ItemListWithUserWrapper {

    private String listName;

    private String username;

    public ItemListWithUserWrapper() {
    }

    public ItemListWithUserWrapper(String listName, String username) {
        this.listName = listName;
        this.username = username;
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
}
