package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.User;
import plottski.todolistfundamentals.Entities.ToDoList;

import java.util.ArrayList;

public interface ToDoListRepo extends CrudRepository<ToDoList, Integer> {

    ArrayList<ToDoList> findAll();

    ArrayList<ToDoList> findAllListsById(int userID);

    ToDoList findByListName (String listName);

    ToDoList findByListNameAndUser(String listName, User user);
}
