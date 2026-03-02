package com.divyanshu.knowledgehub.infrastructure.persistence.repository;

import com.divyanshu.knowledgehub.domain.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDataRepository extends JpaRepository<Document, UUID> {
}
