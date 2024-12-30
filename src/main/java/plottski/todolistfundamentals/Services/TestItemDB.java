package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.ItemWithCreationDate;

public interface TestItemDB extends CrudRepository<ItemWithCreationDate, Long> {
}
