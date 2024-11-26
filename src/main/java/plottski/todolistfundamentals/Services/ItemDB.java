package plottski.todolistfundamentals.Services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.ItemWithCreationDate;

import java.util.ArrayList;
import java.util.Optional;

public interface ItemDB extends CrudRepository<ItemWithCreationDate, Integer> {
    //ItemWithCreationDate findByTitle(String title);

    //ItemWithCreationDate findByUserID(int id);

    @Query(value = "SELECT * FROM items WHERE userID = ?1", nativeQuery = true)
    ItemWithCreationDate findByUserIDNative(int id);
    ArrayList<ItemWithCreationDate> findAll();
}
