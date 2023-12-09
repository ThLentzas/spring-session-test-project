package com.example.session.security;

import com.example.session.entity.Role;
import org.springframework.security.core.GrantedAuthority;


public record SecurityRole(Role role) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return role.getType().name();
    }
}
