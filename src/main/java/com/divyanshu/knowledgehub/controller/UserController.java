package com.divyanshu.knowledgehub.controller;

import com.divyanshu.knowledgehub.application.service.UserService;
import com.divyanshu.knowledgehub.controller.request.user.LoginUserRequest;
import com.divyanshu.knowledgehub.controller.request.user.RegisterUserRequest;
import com.divyanshu.knowledgehub.domain.model.User;
import jakarta.validation.Valid;
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

    @PostMapping("register")
    public String register(@Valid @RequestBody RegisterUserRequest request) {
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
        log.debug("Registered the user with id : {} ", newUser.getId());
        return jwtToken;
    }

    @PostMapping("login")
    public String login(@Valid @RequestBody LoginUserRequest request) {


        String jwtToken = userService.registerUser(newUser);
        return jwtToken;
    }
}
