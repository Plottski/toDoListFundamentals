package plottski.todolistfundamentals.Services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import plottski.todolistfundamentals.Entities.UserForDB;
import org.springframework.data.jpa.repository.JpaRepository;


//@Repository
//@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepo extends PagingAndSortingRepository<UserForDB, Integer>, JpaRepository<UserForDB, Integer> {

    UserForDB findByUsername(String username);

}
