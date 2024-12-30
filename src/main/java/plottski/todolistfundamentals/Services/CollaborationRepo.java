package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.Collaboration;

public interface CollaborationRepo extends CrudRepository<Collaboration, Integer> {

}
