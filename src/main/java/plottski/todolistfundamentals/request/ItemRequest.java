package plottski.todolistfundamentals.request;

import plottski.todolistfundamentals.Entities.ItemWithCreationDate;

public class ItemRequest extends ItemWithCreationDate {

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

