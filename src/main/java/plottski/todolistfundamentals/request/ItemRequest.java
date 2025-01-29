package plottski.todolistfundamentals.request;

import plottski.todolistfundamentals.Entities.Item;

public class ItemRequest extends Item {

    private String listName;

    public ItemRequest(String listName) {

        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}

