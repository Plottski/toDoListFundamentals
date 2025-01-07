package plottski.todolistfundamentals.Services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import plottski.todolistfundamentals.Entities.Item;
import plottski.todolistfundamentals.Entities.ItemList;

import java.util.ArrayList;
import java.util.List;


public interface ItemRepo extends CrudRepository<Item, Integer> {

    Item findByTitle(String title);

    Item findByItemListAndTitle(ItemList itemList, String title);



    //Item deleteByItem(Item item);
//    Item findByTitle(String title);
//
//    //ItemWithCreationDate findByUserID(int id);
//
//    Item findByusernameAndTitle(String username, String title);
//
//    ArrayList<Item> findAllByUserIDAndTitle(int userID, String title);
//
//    ArrayList<Item> findAllByUserIDAndDescription(int userID, String description);
//
//    ArrayList<Item> findAllByUserIDAndUsername(int userID, String username);
//
//    ArrayList<Item> findAllByUserIDAndListName(int userID, String listName);
//
//    ArrayList<Item> findAllByUserIDAndDueDate(int userID, String dueDate);
//
//    //@Query(value = "SELECT * FROM items WHERE userID = ?1 AND title = ?1")
//    //ArrayList<ItemWithCreationDate> findAllByUserIDAndTitle(int userID, String title);
//
//    @Query(value = "SELECT * FROM items WHERE userID = ?1", nativeQuery = true)
//    Item findByUserIDNative(int id);
//
//    //ItemWithCreationDate findByTitle(String title);
//
//    @Query(value = "SELECT * FROM items WHERE listID = ?1", nativeQuery = true)
//    List<Item> findByListID(@Param("listID") int listID);
//
//    ArrayList<Item> findAll();
//
//    ArrayList<Item> findAllByListID(int listID);
}
