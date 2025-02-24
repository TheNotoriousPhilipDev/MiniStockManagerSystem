package org.agomez.msvc_users.repositories;

import org.agomez.msvc_users.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

}
