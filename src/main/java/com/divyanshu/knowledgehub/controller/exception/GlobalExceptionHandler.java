package com.divyanshu.knowledgehub.controller.exception;

import com.divyanshu.knowledgehub.application.exception.ApplicationException;
import com.divyanshu.knowledgehub.application.exception.ContentParsingException;
import com.divyanshu.knowledgehub.application.exception.DocumentAlreadyExistsException;
import com.divyanshu.knowledgehub.application.exception.EmbeddingException;
import com.divyanshu.knowledgehub.application.exception.LlmException;
import com.divyanshu.knowledgehub.application.exception.InvalidCredentialsException;
import com.divyanshu.knowledgehub.application.exception.StorageException;
import com.divyanshu.knowledgehub.application.exception.UrlFetchException;
import com.divyanshu.knowledgehub.application.exception.UserAlreadyExistsException;
import com.divyanshu.knowledgehub.application.exception.UserNotFoundException;
import com.divyanshu.knowledgehub.application.exception.WorkspaceAlreadyExistsException;
import com.divyanshu.knowledgehub.application.exception.WorkspaceNotFoundException;
import com.divyanshu.knowledgehub.application.exception.WorkspacePersistenceException;
import com.divyanshu.knowledgehub.domain.exception.DomainException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DatabaseException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.DuplicateEntityException;
import com.divyanshu.knowledgehub.infrastructure.persistence.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Request validation failed: {} path={}", message, request.getRequestURI());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_ERROR",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            DomainException ex,
            HttpServletRequest request
    ) {
        log.warn("Domain validation error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "DOMAIN_VALIDATION_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("User not found: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "USER_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        log.warn("Conflict - user already exists: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "USER_ALREADY_EXISTS",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            HttpServletRequest request
    ) {
        log.warn("Invalid login attempt path={}", request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_CREDENTIALS",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(WorkspaceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleWorkspaceAlreadyExistsException(
            WorkspaceAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        log.warn("Conflict - workspace already exists: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "WORKSPACE_ALREADY_EXISTS",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(WorkspaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWorkspaceNotFoundException(
            WorkspaceNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("Workspace not found: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "WORKSPACE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntityException(
            DuplicateEntityException ex,
            HttpServletRequest request
    ) {
        log.warn("Duplicate entity: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "DUPLICATE_ENTITY",
                "A record with the same unique identifier already exists.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDocumentAlreadyExistsException(
            DocumentAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        log.warn("Duplicate document: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "DOCUMENT_ALREADY_EXISTS",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UrlFetchException.class)
    public ResponseEntity<ErrorResponse> handleUrlFetchException(
            UrlFetchException ex,
            HttpServletRequest request
    ) {
        log.warn("URL fetch failed: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_GATEWAY.value(),
                "URL_FETCH_FAILED",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(ContentParsingException.class)
    public ResponseEntity<ErrorResponse> handleContentParsingException(
            ContentParsingException ex,
            HttpServletRequest request
    ) {
        log.error("Content parsing failed: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "CONTENT_PARSING_FAILED",
                "Failed to parse the uploaded content. Ensure the file is a valid, non-corrupted document.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(EmbeddingException.class)
    public ResponseEntity<ErrorResponse> handleEmbeddingException(
            EmbeddingException ex,
            HttpServletRequest request
    ) {
        log.error("Embedding service error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "EMBEDDING_SERVICE_UNAVAILABLE",
                "The embedding service is currently unavailable. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(LlmException.class)
    public ResponseEntity<ErrorResponse> handleLlmException(
            LlmException ex,
            HttpServletRequest request
    ) {
        log.error("LLM service error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "LLM_SERVICE_UNAVAILABLE",
                "The LLM service is currently unavailable. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorResponse> handleStorageException(
            StorageException ex,
            HttpServletRequest request
    ) {
        log.error("Storage error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "STORAGE_SERVICE_UNAVAILABLE",
                "The storage service is currently unavailable. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("Entity not found: {} path={}", ex.getMessage(), request.getRequestURI());
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "ENTITY_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            DatabaseException ex,
            HttpServletRequest request
    ) {
        log.error("Database error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "DATABASE_ERROR",
                "A database error occurred. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(WorkspacePersistenceException.class)
    public ResponseEntity<ErrorResponse> handleWorkspacePersistenceException(
            WorkspacePersistenceException ex,
            HttpServletRequest request
    ) {
        log.error("Workspace persistence error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "WORKSPACE_PERSISTENCE_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            HttpServletRequest request
    ) {
        log.error("Application error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "APPLICATION_ERROR",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        log.warn("Invalid request parameters: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        String message = String.format(
                "Invalid value '%s' for parameter '%s'",
                ex.getValue(),
                ex.getName()
        );

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "INVALID_REQUEST_PARAMETER",
                message,
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unexpected error: {} path={}", ex.getMessage(), request.getRequestURI(), ex);
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "UNEXPECTED_ERROR",
                "Something went wrong. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
