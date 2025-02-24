package org.agomez.msvc_users.services;

import org.agomez.msvc_users.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteUser(Long id);
    User save(User user);
    Optional<User> update(User user, Long id);

}
