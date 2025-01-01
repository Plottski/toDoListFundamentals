package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.Item;

public interface ItemRepo extends CrudRepository<Item, Integer> {

}
