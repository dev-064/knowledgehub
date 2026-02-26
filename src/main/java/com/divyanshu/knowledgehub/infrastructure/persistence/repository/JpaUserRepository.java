package com.divyanshu.knowledgehub.infrastructure.persistence.repository;

import com.divyanshu.knowledgehub.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

}
