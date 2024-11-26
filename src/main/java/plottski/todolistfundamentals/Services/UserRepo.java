package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import plottski.todolistfundamentals.Entities.UserForDB;


//@Repository
//@RepositoryRestResource(collectionResourceRel = "users", path = "users") PagingAndSortingRepository<UserForDB, Integer>,
public interface UserRepo extends CrudRepository<UserForDB, Integer> {

    UserForDB findByUsername(String username);

}
