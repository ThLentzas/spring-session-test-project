package com.example.session.auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    void registerUser(@RequestBody RegisterRequest request, HttpSession session) {
        Authentication authentication = this.authService.registerUser(request);
        setupContext(authentication, session);
    }

    @PostMapping("/login")
    void loginUser(@RequestBody LoginRequest request, HttpSession session) {
        Authentication authentication = this.authService.loginUser(request);
        setupContext(authentication, session);
    }

    private void setupContext(Authentication authentication, HttpSession session) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }
}
