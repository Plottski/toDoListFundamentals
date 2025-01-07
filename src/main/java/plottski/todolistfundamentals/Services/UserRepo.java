package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.User;


//@Repository
//@RepositoryRestResource(collectionResourceRel = "users", path = "users") PagingAndSortingRepository<UserForDB, Integer>,
public interface UserRepo extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
