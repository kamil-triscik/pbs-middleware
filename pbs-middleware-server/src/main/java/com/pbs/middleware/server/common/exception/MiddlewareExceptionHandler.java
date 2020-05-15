package com.pbs.middleware.server.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.error.Error;
import com.pbs.middleware.api.error.ErrorResponse;
import com.pbs.middleware.server.MiddlewareServer;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import static com.pbs.middleware.api.error.CommonErrorCode.INTERNAL_ERROR;
import static com.pbs.middleware.api.error.CommonErrorCode.INVALID_REQUEST_BODY;
import static com.pbs.middleware.api.error.CommonErrorCode.INVALID_REQUEST_METHOD;
import static com.pbs.middleware.api.error.CommonErrorCode.UNAUTHORIZED;
import static com.pbs.middleware.api.error.CommonErrorCode.VALIDATION_ERROR;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@ControllerAdvice
public class MiddlewareExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MiddlewareServer.class.getName());

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<ErrorResponse> handle(NullPointerException ex) {
        logger.error("Middleware exception:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(INTERNAL_ERROR.toString())
                                .message(ex.getMessage())
                                .build()),
                INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MiddlewareException.class)
    private ResponseEntity<ErrorResponse> handle(MiddlewareException ex) {
        logger.error("Middleware exception:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(ex.getErrorCode())
                                .message(ex.getErrorMessage())
                                .path(ex.getField()).build()),
                ex.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ErrorResponse> handle(ConstraintViolationException ex) {
        logger.error("Validation error:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(ex.getConstraintViolations()
                        .stream()
                        .map(mapValidationError)
                        .map(it -> it.code(VALIDATION_ERROR.toString()))
                        .map(Error.ErrorBuilder::build)
                        .collect(Collectors.toList())), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    private ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException ex) {
        logger.error("Validation error:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                // todo path
                                .code(VALIDATION_ERROR.toString())
                                .message(ex.getMessage())
                                .build()), BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<ErrorResponse> handle(MissingServletRequestParameterException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(BAD_REQUEST.toString())
                                .message(ex.getMessage())
                                .build()),
                BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    private ResponseEntity<ErrorResponse> handle(MissingServletRequestPartException ex) {
        logger.error("Invalid request body:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(INVALID_REQUEST_BODY.toString())
                                .message(ex.getMessage())
                                .build()),
                BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException ex) {
        logger.error("Invalid request body:", ex);
        ErrorResponse response;
        if (ex.getBindingResult().getErrorCount() > 0) {
            response = ErrorResponse.of(ex.getBindingResult().getAllErrors().stream()
                    .filter(it -> it instanceof FieldError)
                    .map(it -> (FieldError) it)
                    .map(it -> Error.builder()
                            .value(it.getRejectedValue())
                            .path(it.getField())
                            .message(it.getDefaultMessage())
                            .code(VALIDATION_ERROR.toString())
                            .build())
                    .collect(Collectors.toList()));
        } else {
            response = ErrorResponse.of(
                    Error.builder()
                            .code(INVALID_REQUEST_BODY.toString())
                            .message(ex.getMessage())
                            .build());
        }
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException ex) {
        logger.error("Invalid request method:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(INVALID_REQUEST_METHOD.toString())
                                .message(ex.getMessage())
                                .build()),
                METHOD_NOT_ALLOWED);
    }

    /**
     * All unhandled exception are returned as [org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR]
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handle(Exception ex) {
        logger.error("Server exception:", ex);
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(INTERNAL_ERROR.toString())
                                .message(ex.getMessage())
                                .build()),
                INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    private ResponseEntity<ErrorResponse> handle(EmptyResultDataAccessException ex) {
        logger.error("Server exception:", ex);
        return ResponseEntity.notFound().build();
    }

    /**
     * All unhandled exception are returned as [com.pbs.middleware.server.common.exception.AccessDeniedException]
     */
    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        if (ex.getMessage().toLowerCase().contains("access is denied")) {
            return new ResponseEntity<>("Unauthorized Access", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                ErrorResponse.of(
                        Error.builder()
                                .code(INTERNAL_ERROR.toString())
                                .message(ex.getMessage())
                                .build()),
                INTERNAL_SERVER_ERROR);
    }

    /**
     * All unhandled exception are returned as [com.pbs.middleware.server.common.exception.AccessDeniedException]
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException2(AccessDeniedException ex) {
        return new ResponseEntity<>(ErrorResponse.of(
                Error.builder()
                        .code(ex.getErrorCode())
                        .message(ex.getMessage())
                        .build()), ex.getHttpStatus());
    }

    /**
     * Handles {@link org.springframework.security.authentication.BadCredentialsException}.
     *
     * @param e exception
     * @return {@link org.springframework.http.HttpStatus#FORBIDDEN}
     */
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> badCredentials(BadCredentialsException e) {
        logger.error("Request forbidden.", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(
                        Error.builder()
                                .code(UNAUTHORIZED.toString())
                                .message(e.getMessage())
                                .build()));
    }

    /**
     * Handles {@link org.springframework.mail.MailSendException}.
     *
     * @param e exception
     * @return {@link org.springframework.http.HttpStatus#BAD_REQUEST}
     */
    @ExceptionHandler({MailSendException.class})
    public ResponseEntity<ErrorResponse> mailSend(MailSendException e) {
        logger.error("Provided email is unavailable.", e);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ErrorResponse.of(
                        Error.builder()
                                .code("MAIL_SENDING_FAILED")
                                .message("Provided email is unavailable.")
                                .build()));
    }

    private static final Function<ConstraintViolation<?>, Error.ErrorBuilder> mapValidationError = error ->
//                    (Boolean) error.getConstraintDescriptor().getAttributes().getOrDefault("isClassType", false) ? error.getInvalidValue() : null,
            Error.builder().value(getValue(error))
                    .path(getFieldName(error)).message(error.getMessage());

    private static Object getValue(ConstraintViolation<?> error) {
        if ((Boolean) error.getConstraintDescriptor().getAttributes().getOrDefault("isClassType", false)) {
            Map<String, Object> map = objectMapper.convertValue(error.getInvalidValue(), Map.class);
            return ofNullable(map.get(getFieldName(error))).map(Object::toString).orElse(null);
        } else {
            return error.getInvalidValue();
        }
    }

    private static String getFieldName(ConstraintViolation<?> error) {
        return ofNullable((String) error.getConstraintDescriptor().getAttributes().get("fieldName")).orElse(getPath(error.getPropertyPath().toString()));
    }

    private static String getPath(String path) {
        String[] splitedPath = path.split("\\.");
        return splitedPath[splitedPath.length - 1];
    }

}
