package com.divyanshu.knowledgehub.infrastructure.persistence.adapter;

import com.divyanshu.knowledgehub.application.exception.UserAlreadyExistsException;
import com.divyanshu.knowledgehub.application.port.out.UserRepository;
import com.divyanshu.knowledgehub.domain.model.User;
import com.divyanshu.knowledgehub.infrastructure.persistence.entity.UserEntity;
import com.divyanshu.knowledgehub.infrastructure.persistence.repository.JpaUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

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

        UserEntity entity = new UserEntity(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        try {
            UserEntity saved = jpaUserRepository.save(entity);
            log.info("User saved successfully with id: {}", saved.getId());

            return new User(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    saved.getPassword(),
                    null,
                    saved.getCreatedAt(),
                    saved.getUpdatedAt()
            );
        } catch (DataIntegrityViolationException e) {
            log.warn("DB constraint violation while saving user with email: {}", user.getEmail());
            throw new UserAlreadyExistsException(user.getEmail());
        }
    }

    @Override
    public User getUser(String email){
    }
}
