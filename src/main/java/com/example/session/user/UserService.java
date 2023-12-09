package com.example.session.user;

import com.example.session.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User registerUser(User user) {
        /*
            Here we would perform business logic for duplicates etc, that's why we inject the user service in the auth
            service and not the user repository.
         */
        return this.userRepository.save(user);
    }
}
