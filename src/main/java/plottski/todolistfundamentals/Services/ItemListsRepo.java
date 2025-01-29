package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.Item;
import plottski.todolistfundamentals.Entities.ItemList;
import plottski.todolistfundamentals.Entities.User;

import java.util.ArrayList;
import java.util.List;

public interface ItemListsRepo extends CrudRepository<ItemList, Integer> {

    ItemList findByListNameAndUser(String listname, User user);

    ItemList findByItems(Item item);

//    ArrayList<ItemList> findAll();
//
//    //@Query(value = "SELECT * FROM userLists WHERE userID = ?1", nativeQuery = true)
//    //ArrayList<UserItemList> findUserListsByID(@Param("userID") int userID);
//
//    ArrayList<ItemList> findUserListsByid (int listID);
//
      List<ItemList> findAllListsByUser (User user);
//
//    ItemList findByListName (String listName);
//
//    ItemList findByListNameAndUserID(String listName, int userID);

}
