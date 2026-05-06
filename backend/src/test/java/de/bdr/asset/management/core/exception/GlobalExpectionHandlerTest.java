package de.bdr.asset.management.core.exception;

import com.google.zxing.WriterException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.core.TypeInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    // --- ResourceNotFoundException ---

    @Test
    void shouldReturn404ForResourceNotFoundException() {

        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ProblemDetail result = handler.handleNotFound(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getDetail()).isEqualTo("Resource not found");
        assertThat(result.getTitle()).isEqualTo("Resource not found");
    }

    @Test
    void shouldSetInstanceUriForResourceNotFoundException() {

        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ProblemDetail result = handler.handleNotFound(ex, request);

        assertThat(result.getInstance()).hasToString("/test-uri");
    }

    // --- DuplicateResourceException ---

    @Test
    void shouldReturn409ForDuplicateResourceException() {

        DuplicateResourceException ex = new DuplicateResourceException("Duplicate resource");

        ProblemDetail result = handler.handleDuplicateException(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(result.getDetail()).isEqualTo("Duplicate resource");
        assertThat(result.getTitle()).isEqualTo("Duplicate resource");
    }

    // --- InvalidDateRangeException ---

    @Test
    void shouldReturn400ForInvalidDateRangeException() {

        InvalidDateRangeException ex = new InvalidDateRangeException("Invalid date range");

        ProblemDetail result = handler.handleInvalidDateRange(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getDetail()).isEqualTo("Invalid date range");
        assertThat(result.getTitle()).isEqualTo("Invalid date range");
    }

    // --- ActionNotAllowedException ---

    @Test
    void shouldReturn422ForActionNotAllowedException() {

        ActionNotAllowedException ex = new ActionNotAllowedException("Action not allowed");

        ProblemDetail result = handler.handleActionNotAllowed(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT.value());
        assertThat(result.getDetail()).isEqualTo("Action not allowed");
        assertThat(result.getTitle()).isEqualTo("Action not allowed");
    }

    // --- DataIntegrityViolationException ---

    @Test
    void shouldReturn409ForDataIntegrityViolationException() {

        DataIntegrityViolationException ex = new DataIntegrityViolationException("Constraint violation");

        ProblemDetail result = handler.handleDatabaseConflict(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(result.getDetail()).isEqualTo("Selected time for reservation is already taken");
        assertThat(result.getTitle()).isEqualTo("Conflict with reservation");
    }

    // --- MethodArgumentNotValidException ---

    @Test
    void shouldReturn400ForNotValidException_WithDefaultMessage() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getField()).thenReturn("email");
        when(fieldError.getDefaultMessage()).thenReturn("must be a well-formed email address");

        ProblemDetail result = handler.handleNotValid(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Invalid data");
        assertThat(result.getDetail()).isEqualTo("Data not valid");
        assertThat(result.getProperties()).isNotNull();
    }

    @Test
    void shouldReturn400ForNotValidException_WithNullMessage() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(fieldError.getField()).thenReturn("email");
        when(fieldError.getDefaultMessage()).thenReturn(null);

        ProblemDetail result = handler.handleNotValid(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Invalid data");
        assertThat(result.getDetail()).isEqualTo("Data not valid");
        assertThat(result.getProperties()).isNotNull();
    }

    // --- MethodArgumentTypeMismatchException ---

    @Test
    void shouldReturn400ForTypeMismatch_WithValue() {

        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        when(ex.getName()).thenReturn("assetId");
        when(ex.getValue()).thenReturn("invalid-string-id");

        ProblemDetail result = handler.handleTypeMismatch(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Type mismatch");
        assertThat(result.getDetail()).isEqualTo("Invalid request parameter");
        assertThat(result.getProperties()).isNotNull();
    }

    @Test
    void shouldReturn400ForTypeMismatch_WithNullValue() {

        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);

        when(ex.getName()).thenReturn("assetId");
        when(ex.getValue()).thenReturn(null);

        ProblemDetail result = handler.handleTypeMismatch(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Type mismatch");
        assertThat(result.getDetail()).isEqualTo("Invalid request parameter");
        assertThat(result.getProperties()).isNotNull();
    }

    // --- IllegalArgumentException ---

    @Test
    void shouldReturn400ForIllegalArgumentException() {

        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ProblemDetail result = handler.handleIllegalArgument(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getDetail()).isEqualTo("Invalid argument");
        assertThat(result.getTitle()).isEqualTo("Invalid request");
    }

    @Test
    void shouldReturn400WithDefaultMessageForNullIllegalArgumentException() {

        IllegalArgumentException ex = new IllegalArgumentException((String) null);

        ProblemDetail result = handler.handleIllegalArgument(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getDetail()).isEqualTo("Invalid request");
    }

    // --- PropertyReferenceException ---

    @Test
    void shouldReturn400ForPropertyReferenceException() {

        PropertyReferenceException ex = mock(PropertyReferenceException.class);
        TypeInformation<?> typeInfo = mock(TypeInformation.class);

        when(ex.getPropertyName()).thenReturn("unknownField");
        when(ex.getType()).thenReturn((TypeInformation) typeInfo);
        when(typeInfo.getType()).thenReturn((Class) String.class);

        ProblemDetail result = handler.handlePropertyReference(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Invalid property");
        assertThat(result.getDetail()).isEqualTo("Invalid property reference");
        assertThat(result.getProperties()).isNotNull();
    }

    // --- AccessDeniedException ---

    @Test
    void shouldReturn403ForAccessDeniedException() {

        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ProblemDetail result = handler.handleAccessDenied(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(result.getDetail()).isEqualTo("Access denied");
        assertThat(result.getTitle()).isEqualTo("Access denied");
    }

    // --- WriterException ---

    @Test
    void shouldReturn500ForWriterException() {

        WriterException ex = new WriterException("Writer error");

        ProblemDetail result = handler.handleWriterException(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getDetail()).isEqualTo("Writer exception");
        assertThat(result.getTitle()).isEqualTo("Writer exception");
    }

    // --- IOException ---

    @Test
    void shouldReturn500ForIOException() {

        IOException ex = new IOException("I/O error");

        ProblemDetail result = handler.handleIOException(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getDetail()).isEqualTo("I/O exception");
        assertThat(result.getTitle()).isEqualTo("I/O exception");
    }

    // --- UsernameNotFoundException ---

    @Test
    void shouldReturn404ForUsernameNotFoundException() {

        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");

        ProblemDetail result = handler.handleNotFound(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getDetail()).isEqualTo("User not found");
        assertThat(result.getTitle()).isEqualTo("User not found with username");
    }

    // --- BadCredentialsException ---

    @Test
    void shouldReturn401ForBadCredentialsException() {

        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ProblemDetail result = handler.handleBadCredentials(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(result.getDetail()).isEqualTo("Bad credentials");
        assertThat(result.getTitle()).isEqualTo("Incorrect username or password.");
    }

    // --- JwtException ---

    @Test
    void shouldReturn401ForJwtException() {

        JwtException ex = new JwtException("Invalid token");

        ProblemDetail result = handler.handleJwt(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(result.getDetail()).isEqualTo("Invalid token");
        assertThat(result.getTitle()).isEqualTo("Invalid token");
    }

    // --- Generic Exception ---

    @Test
    void shouldReturn500ForUnexpectedException() {

        Exception ex = new Exception("Unexpected error");

        ProblemDetail result = handler.handleUncaughtException(ex, request);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(result.getDetail()).isEqualTo("Unexpected error");
        assertThat(result.getTitle()).isEqualTo("Unexpected internal server error");
    }

    // --- timestamp property ---

    @Test
    void shouldSetTimestampProperty() {

        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ProblemDetail result = handler.handleNotFound(ex, request);

        assertThat(result.getProperties()).containsKey("timestamp");
    }
}