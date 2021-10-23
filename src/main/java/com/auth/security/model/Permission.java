package com.auth.security.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Permission {
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

    public String getPermission(){
        return permission;
    }
}
