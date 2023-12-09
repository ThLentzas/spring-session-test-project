package com.example.session.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.session.entity.Role;
import com.example.session.entity.User;
import com.example.session.exception.UnauthorizedException;
import com.example.session.role.RoleRepository;
import com.example.session.security.SecurityUser;
import com.example.session.user.UserService;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
class AuthService {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    Authentication registerUser(RegisterRequest request) {
        User user = new User(request.username(), passwordEncoder.encode(request.password()));
        Set<Role> roles = request.roles().stream()
                .map(role -> this.roleRepository.findByType(role.getType())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role type: " + role.getType())))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        user = this.userService.registerUser(user);
        SecurityUser securityUser = new SecurityUser(user);

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }

    Authentication loginUser(LoginRequest request) {
        Authentication authentication;

        try {
           authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.username(),
                    request.password()));
        } catch (BadCredentialsException bce) {
            throw new UnauthorizedException("Username or password is incorrect");
        }

        return authentication;
    }
}
