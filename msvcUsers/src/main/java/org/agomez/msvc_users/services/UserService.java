package org.agomez.msvc_users.services;

import lombok.RequiredArgsConstructor;
import org.agomez.msvc_users.entities.Role;
import org.agomez.msvc_users.entities.User;
import org.agomez.msvc_users.repositories.RoleRepository;
import org.agomez.msvc_users.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles(user));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(User user, Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.map(userDb -> {
            userDb.setEmail(user.getEmail());
            userDb.setUsername(user.getUsername());
            if (user.isEnabled() == null) {
                userDb.setEnabled(true);
            } else {
                userDb.setEnabled(user.isEnabled());
            }
            userDb.setRoles(getRoles(user));

            return Optional.of(userRepository.save(userDb));
        }).orElseGet(Optional::empty);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User>  findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser (Long id) {
        userRepository.deleteById(id);
    }

    private List<Role> getRoles (User user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(roles::add);
        if (user.isAdmin()) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(roles::add);
        }
        return roles;
    }

}
