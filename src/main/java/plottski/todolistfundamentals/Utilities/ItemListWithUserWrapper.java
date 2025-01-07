package plottski.todolistfundamentals.Utilities;

import plottski.todolistfundamentals.Entities.ItemList;
import plottski.todolistfundamentals.Entities.User;

public class ItemListWithUserWrapper {

    private ItemList itemList;

    private User user;

    public ItemListWithUserWrapper() {
    }

    public ItemListWithUserWrapper(ItemList itemList, User user) {
        this.itemList = itemList;
        this.user = user;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
