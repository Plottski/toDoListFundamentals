package plottski.todolistfundamentals.Utilities;

import plottski.todolistfundamentals.Entities.Item;

import java.io.Serializable;

public class ItemAndListWrapperUtilClass implements Serializable {

    private Item item;

    private String listName;

    public ItemAndListWrapperUtilClass(Item item, String listName) {
        this.item = item;
        this.listName = listName;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
