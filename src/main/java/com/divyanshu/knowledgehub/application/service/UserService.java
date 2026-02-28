package com.divyanshu.knowledgehub.application.service;

import com.divyanshu.knowledgehub.application.exception.*;
import com.divyanshu.knowledgehub.application.port.out.UserRepository;
import com.divyanshu.knowledgehub.domain.model.User;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DatabaseException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        try {
            User savedUser = userRepository.save(encryptedUser);
            return jwtService.generateToken(savedUser);
        } catch (DuplicateEntityException e) {
            log.warn("Registration failed - user already exists: {}", user.getEmail());
            throw new UserAlreadyExistsException(user.getEmail());
        } catch (DatabaseException e) {
            log.error("Registration failed - database error for user: {}", user.getEmail(), e);
            throw new UserPersistenceException(user.getEmail());
        }
    }

    public String loginUser(String email, String password) {
        try {
            User user = userRepository.getUser(email).orElseThrow(() -> {
                return new UserNotFoundException(email);
            });
            if (passwordMatches(password, user.getPassword())) {
                return jwtService.generateToken(user);
            }
            throw new InvalidCredentialsException();
        } catch (DatabaseException e) {
            log.error("Login failed - database error for user: {}", email, e);
            throw new FetchUserException(email);
        }
    }

    public User getUser(UUID id) {
        try {
            User user = userRepository.getUser(id).orElseThrow(() -> {
                return new UserNotFoundException(id);
            });
            return user;
        } catch (DatabaseException e) {
            log.error("Login failed - database error for user: {}", id, e);
            throw new FetchUserException(id);
        }
    }

    private String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
