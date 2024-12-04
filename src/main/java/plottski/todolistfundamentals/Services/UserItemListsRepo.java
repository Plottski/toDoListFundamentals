package plottski.todolistfundamentals.Services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import plottski.todolistfundamentals.Entities.UserItemList;

import java.util.ArrayList;

public interface UserItemListsRepo  extends CrudRepository<UserItemList, Integer> {

    ArrayList<UserItemList> findAll();

    //@Query(value = "SELECT * FROM userLists WHERE userID = ?1", nativeQuery = true)
    //ArrayList<UserItemList> findUserListsByID(@Param("userID") int userID);

    ArrayList<UserItemList> findUserListsByid (int listID);

    ArrayList<UserItemList> findAllListsByid (int userID);

    UserItemList findByListName (String listName);

}
