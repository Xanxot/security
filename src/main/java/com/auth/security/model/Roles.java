package com.auth.security.model;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum Roles {
    USER(Set.of(Permission.USER_READ)),
    ADMIN(Set.of(Permission.USER_WRITE, Permission.USER_READ));

    private final Set<Permission> permissions;

    public Set<Permission> getPermissions() {
        return permissions;
    }
    public Set<SimpleGrantedAuthority> grantedAuthorities(){
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
