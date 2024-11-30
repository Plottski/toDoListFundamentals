package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.ItemWithCreationDate;
import plottski.todolistfundamentals.Entities.UserForDB;
import plottski.todolistfundamentals.Entities.UserItemList;

import java.util.ArrayList;

public interface UserItemListsRepo  extends CrudRepository<UserItemList, Integer> {

    ArrayList<UserItemList> findAll();

}
