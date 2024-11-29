package plottski.todolistfundamentals.Services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.ItemWithCreationDate;
import java.util.ArrayList;


public interface ItemDB extends CrudRepository<ItemWithCreationDate, Integer> {
    //ItemWithCreationDate findByTitle(String title);

    //ItemWithCreationDate findByUserID(int id);

    @Query(value = "SELECT * FROM items WHERE userID = ?1", nativeQuery = true)
    ItemWithCreationDate findByUserIDNative(int id);

    //ItemWithCreationDate findByTitle(String title);

    ArrayList<ItemWithCreationDate> findAll();
}
