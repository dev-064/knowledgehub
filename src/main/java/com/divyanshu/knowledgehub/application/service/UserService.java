package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.exception.UserAlreadyExistsException;
import com.divyanshu.knowledgehub.application.port.out.UserRepository;
import com.divyanshu.knowledgehub.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserService(BCryptPasswordEncoder passwordEncoder, JwtService jwtService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {
        String hashedPassword = hashPassword(user.getPassword());
        User encryptedUser = new User(
                user.getId(),
                user.getName(),
                user.getEmail(),
                hashedPassword,
                user.getWorkspaces(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        try {
            User savedUser = userRepository.save(encryptedUser);
            return jwtService.generateToken(savedUser);
        } catch (UserAlreadyExistsException e) {
            log.warn("Registration failed — user already exists: {}", user.getEmail());
            throw e;
        }
    }

    public String loginUser(String email, String password) {
        try {
            String hashedPassword = hashPassword(password);

        } catch (UserAlreadyExistsException e) {
            log.warn("Registration failed — user already exists: {}", user.getEmail());
            throw e;
        }
    }

    private String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
