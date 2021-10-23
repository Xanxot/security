package com.auth.security.rest;

import com.auth.security.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private List<User> usersList = Stream.of(
                    new User(1L, "John", "Cena"),
                    new User(2L, "Mark", "Two"),
                    new User(3L, "Hello", "World"))
            .collect(Collectors.toList());

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    private List<User> getAll() {
        return usersList;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    private User getById(@PathVariable Long id) {
        return usersList.stream().filter(usersList -> usersList.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:write')")
    private List<User> create(@RequestBody User user) {
        usersList.add(user);
        return usersList;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    private List<User> deleteUserById(@PathVariable Long id) {
        usersList.removeIf(usersList ->usersList.getId().equals(id));
        return usersList;
    }


}
