package plottski.todolistfundamentals.Services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import plottski.todolistfundamentals.Entities.ItemWithCreationDate;
import java.util.ArrayList;
import java.util.List;


public interface ItemDB extends CrudRepository<ItemWithCreationDate, Integer> {
    //ItemWithCreationDate findByTitle(String title);

    //ItemWithCreationDate findByUserID(int id);

    ItemWithCreationDate findByusernameAndTitle(String username, String title);

    ItemWithCreationDate findByid(int id);

    @Query(value = "SELECT * FROM items WHERE userID = ?1", nativeQuery = true)
    ItemWithCreationDate findByUserIDNative(int id);

    //ItemWithCreationDate findByTitle(String title);

    @Query(value = "SELECT * FROM items WHERE listID = ?1", nativeQuery = true)
    List<ItemWithCreationDate> findByListID(@Param("listID") int listID);

    ArrayList<ItemWithCreationDate> findAll();

    ArrayList<ItemWithCreationDate> findAllByListID(int listID);
}
