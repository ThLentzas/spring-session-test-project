package com.example.session.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import com.example.session.config.DeserializerConfig;
import com.example.session.config.SecurityConfig;
import com.example.session.entity.Role;
import com.example.session.entity.User;
import com.example.session.role.RoleType;
import com.example.session.security.SecurityUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

/*
    The below tests are for testing with csrf only. The TokenController does not need any tests because we don't have
    csrf on GET requests.
 */
@WebMvcTest(AuthController.class)
@Import({
        DeserializerConfig.class,
        SecurityConfig.class
})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    private static final String AUTH_PATH = "/api/v1/auth";

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldRegisterUser() throws Exception {
        String requestBody = """
                    {
                        "username": "Test11",
                        "password": "CyN549!@o2Cr",
                        "roles": [{
                            "type": "ADMIN"
                        }]
                    }
                """;

        Authentication authentication = getAuthentication();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(authentication);

        /*
            By default, the csrf token is included as query param. If we want to include the token as header
            with(csrf().asHeader())
         */
        this.mockMvc.perform(post(AUTH_PATH + "/register").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldReturnHTTP403WhenRegisterUserIsCalledWithInvalidCsrfToken() throws Exception {
        String requestBody = """
                    {
                        "username": "Test",
                        "password": "CyN549!@o2Cr",
                        "roles": [{
                            "type": "ADMIN"
                        }]
                    }
                """;

        Authentication authentication = getAuthentication();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(authentication);

        /*
            By default, the csrf token is included as query param. If we want to include the token as header
            with(csrf().asHeader())
         */
        this.mockMvc.perform(post(AUTH_PATH + "/register").with(csrf().useInvalidToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void shouldReturnHTTP403WhenRegisterUserIsCalledWithNoCsrfToken() throws Exception {
        String requestBody = """
                    {
                        "username": "Test",
                        "password": "CyN549!@o2Cr",
                        "roles": [{
                            "type": "ADMIN"
                        }]
                    }
                """;

        Authentication authentication = getAuthentication();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(authentication);

        /*
            By default, the csrf token is included as query param. If we want to include the token as header
            with(csrf().asHeader())
         */
        this.mockMvc.perform(post(AUTH_PATH + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }

    private Authentication getAuthentication() {
        Role role = new Role();
        role.setType(RoleType.ROLE_ADMIN);
        User user = new User("Test", "CyN549!@o2Cr");
        user.setRoles(Set.of(role));
        SecurityUser securityUser = new SecurityUser(user);

        return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
    }
}
