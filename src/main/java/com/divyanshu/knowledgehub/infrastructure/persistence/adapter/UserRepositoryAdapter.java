package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.port.out.UserRepository;
import com.divyanshu.knowledgehub.domain.model.User;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.UserEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DatabaseException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryAdapter.class);

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        log.info("Saving user with email: {}", user.getEmail());
        try {
            UserEntity entity = new UserEntity(
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
            UserEntity saved = jpaUserRepository.save(entity);
            log.info("User saved successfully with id: {}", saved.getId());
            return mapToDomain(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("User already exists with email: " + user.getEmail(), e);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database error while saving user with email: " + user.getEmail(), e);
        }
    }

    @Override
    public Optional<User> getUser(UUID id) {
        try {
            return jpaUserRepository.findById(id).map(this::mapToDomain);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database error while fetching user with id: " + id, e);
        }
    }

    @Override
    public Optional<User> getUser(String email) {
        try {
            return jpaUserRepository.findByEmail(email).map(this::mapToDomain);
        } catch (DataAccessException e) {
            throw new DatabaseException("Database error while fetching user with email: " + email, e);
        }
    }

    private User mapToDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
