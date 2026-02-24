package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.service.UserService;
import com.divyanshu.knowledgehub.controller.request.user.RegisterUserRequest;
import com.divyanshu.knowledgehub.domain.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String register(@RequestBody RegisterUserRequest request) {
        User newUser = new User(
                UUID.randomUUID(),
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                null,
                Instant.now(),
                Instant.now()
        );

        String jwtToken = userService.registerUser(newUser);
        log.debug("Registered the user");
        return jwtToken;
    }
}
