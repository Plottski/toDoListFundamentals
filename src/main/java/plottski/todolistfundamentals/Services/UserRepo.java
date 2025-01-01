package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.User;

public interface UserRepo extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
