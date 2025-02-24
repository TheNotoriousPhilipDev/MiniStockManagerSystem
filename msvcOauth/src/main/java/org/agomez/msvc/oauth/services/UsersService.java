package org.agomez.msvc.oauth.services;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.agomez.msvc.oauth.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(UsersService.class.getName());
    private final Tracer tracer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Username requested");

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        try {
            tracer.currentSpan().tag("username created: ", username);
            User user = webClient
                    .get()
                    .uri("/username/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            List<GrantedAuthority> roles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, roles);
        } catch (WebClientResponseException e) {
            tracer.currentSpan().tag("error", e.getMessage());
            throw new UsernameNotFoundException("User not found");
        }
    }
}
