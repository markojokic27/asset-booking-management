package de.bdr.asset.management.core.exception;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.zxing.WriterException;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler for exception
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String TIMESTAMP = "timestamp";
    public static final String ISSUE = "issue";
    public static final String INVALID_PARAMS = "invalidParams";

    /*
                Generic handler if a resource is not found in the database.

                Example:
                - Have database with assets with Ids from 1-10
                - Request an asset with id 25
                - Does not exist so return status 404, ResourceNotFoundException
            */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("Resource not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler for when a resource conflicts with an existing one.

        Example:
        - Attempt to create a resource with a unique field that already exists
    */
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDuplicateException(DuplicateResourceException ex, HttpServletRequest request) {

        log.warn("Duplicate resource at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problemDetail.setTitle("Duplicate resource");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler for when a resource has invalid date range.

        Example:
        - Attempt to create a resource in the past
    */
    @ExceptionHandler(InvalidDateRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleInvalidDateRange(InvalidDateRangeException ex, HttpServletRequest request) {

        log.warn("Invalid date range at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        problemDetail.setTitle("Invalid date range");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler for when a resources cannot be put through unallowed action.

        Example:
        - Attempt to update a resource that is CANCELLED
    */
    @ExceptionHandler(ActionNotAllowedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ProblemDetail handleActionNotAllowed(ActionNotAllowedException ex, HttpServletRequest request) {

        log.warn("Action not allowed at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage()
        );

        problemDetail.setTitle("Action not allowed");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail handleDatabaseConflict(DataIntegrityViolationException ex, HttpServletRequest request) {

        log.warn("Conflict for reservation at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Selected time for reservation is already taken"
        );

        problemDetail.setTitle("Conflict with reservation");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler for when a resources validation via @Valid fails.

        Example:
        - Attempt to create a resource with a field that is not nullable
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<Map<String, String>> invalidParams = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        ISSUE, error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value"
                ))
                .toList();

        log.warn("Validation failed at URI [{}]. Invalid parameters: {}", request.getRequestURI(), invalidParams);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Data not valid"
        );

        problemDetail.setTitle("Invalid data");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());
        problemDetail.setProperty(INVALID_PARAMS, invalidParams);

        return problemDetail;
    }

    /*
        Generic handler for when a request parameter or path variable
        cannot be converted to the expected type.

        Example:
        - Endpoint expects a Long id but receives "abc"
    */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String fieldName = ex.getName();
        Object value = ex.getValue();

        Map<String, String> invalidParam = Map.of(
                "field", fieldName,
                "rejectedValue", value != null ? value.toString() : "null",
                ISSUE, "Invalid value for parameter"
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request parameter"
        );

        problemDetail.setTitle("Type mismatch");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());
        problemDetail.setProperty(INVALID_PARAMS, List.of(invalidParam));

        return problemDetail;
    }

    /*
        Generic handler to indicate that a method has been passed an illegal or inappropriate argument.

        Example:
        - We try to get a Pageable object with page -1 or page size 0 or illegal sort string
    */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage() != null ? ex.getMessage() : "Invalid request"
        );

        problemDetail.setTitle("Invalid request");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler to indicate that a resource does not have the specified property.

        Example:
        - Jpa method to find a user by "firstName" when it is not defined for a user, we use "name"
    */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handlePropertyReference(PropertyReferenceException ex, HttpServletRequest request) {

        Map<String, String> invalidParam = Map.of(
                "property", ex.getPropertyName(),
                "entity", ex.getType().getType().getSimpleName(),
                ISSUE, "No such property on entity"
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid property reference"
        );

        problemDetail.setTitle("Invalid property");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());
        problemDetail.setProperty(INVALID_PARAMS, List.of(invalidParam));

        return problemDetail;
    }

    /*
        Generic handler to indicate that a method has been invoked at an illegal or inappropriate time.

        Example:
        - Attempt to approve or reject a booking that is not in PENDING state
    */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleIllegalState(IllegalStateException ex, HttpServletRequest request) {

        log.warn("Illegal state at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage() != null ? ex.getMessage() : "Illegal state for requested operation"
        );

        problemDetail.setTitle("Illegal state");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler if an endpoint is accessed without proper authorization.

        Example:
        - Employee tries to change department details
    */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {

        log.warn("Access denied at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );

        problemDetail.setTitle("Access denied");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler for errors during barcode/QR code generation.

        Example:
        - QR code generation fails due to invalid input or encoding constraints
        (e.g. empty content or unsupported barcode format), resulting in WriterException
    */
    @ExceptionHandler(WriterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleWriterException(WriterException ex, HttpServletRequest request) {

        log.error("Writer exception at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Writer exception"
        );

        problemDetail.setTitle("Writer exception");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler for errors during I/O

        Example:
        - Attempt to fetch a file that does not exist
    */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleIOException(IOException ex, HttpServletRequest request) {

        log.error("I/O exception at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "I/O exception"
        );

        problemDetail.setTitle("I/O exception");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Generic handler if a user is not found by username, used by UserDetailsServiceImpl for JWT authentication.

        Example:
        - Attempt to find user with username "abc" who does not exist
    */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(UsernameNotFoundException ex, HttpServletRequest request) {

        log.warn("User not found with username at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        problemDetail.setTitle("User not found with username");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {

        log.warn("Incorrect username or password at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );

        problemDetail.setTitle("Incorrect username or password.");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ProblemDetail handleJwt(JwtException ex, HttpServletRequest request) {

        log.warn("Token invalid at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );

        problemDetail.setTitle("Invalid token");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }

    /*
        Handles all other exceptions that are not defined
    */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleUncaughtException(Exception ex, HttpServletRequest request) {

        log.error("Unexpected internal server error at URI [{}]. Message: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error"
        );

        problemDetail.setTitle("Unexpected internal server error");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty(TIMESTAMP, Instant.now());

        return problemDetail;
    }
}