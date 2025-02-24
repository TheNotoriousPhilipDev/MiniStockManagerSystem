package org.agomez.msvc.oauth.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String username;

    private String password;

    private boolean admin;

    private List<Role> roles;

    private Boolean enabled;

    private String email;

    public User(String username, String password, boolean enabled, String email) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
    }

    public Boolean isEnabled() {
        return enabled;
    }
}
