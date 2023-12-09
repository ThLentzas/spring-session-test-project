package com.example.session.auth;

import com.example.session.entity.Role;

import java.util.Set;

record RegisterRequest(String username, String password, Set<Role> roles) {
}
