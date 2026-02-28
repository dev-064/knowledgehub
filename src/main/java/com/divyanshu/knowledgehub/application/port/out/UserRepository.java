package com.divyanshu.knowledgehub.application.port.out;

import com.divyanshu.knowledgehub.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    Optional<User> getUser(String email);

    Optional<User> getUser(UUID id);
}
