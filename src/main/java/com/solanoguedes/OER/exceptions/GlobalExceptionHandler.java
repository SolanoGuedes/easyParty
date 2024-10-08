package com.solanoguedes.OER.exceptions;

import java.io.IOException;

import com.solanoguedes.OER.service.exceptions.AuthorizationException;
import com.solanoguedes.OER.service.exceptions.DataBindingViolationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

    @Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
    @RestControllerAdvice
    public class GlobalExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationFailureHandler {

        @Value("${server.error.include-exception}")
        private boolean printStackTrace;

        @Override
        @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    "Validation error. Check 'errors' field for details.");
            for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.unprocessableEntity().body(errorResponse);
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<Object> handleAllUncaughtException(
                Exception exception,
                WebRequest request) {
            String errorMessage;

            // Verifica se a exceção contém a mensagem específica que você quer tratar
            if (exception.getMessage().contains("Usuário já está seguindo este usuário")) {
                errorMessage = "Usuário já está seguindo este usuário.";
                log.warn(errorMessage); // Você pode registrar um aviso em vez de um erro aqui
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
            }

            // Se não for a mensagem específica, continua com a lógica original
            errorMessage = "Unknown error occurred";
            log.error(errorMessage, exception);
            return buildErrorResponse(exception, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ResponseEntity<Object> handleDataIntegrityViolationException(
                DataIntegrityViolationException dataIntegrityViolationException,
                WebRequest request) {
            String errorMessage = dataIntegrityViolationException.getMostSpecificCause().getMessage();
            log.error("Failed to save entity with integrity problems: " + errorMessage, dataIntegrityViolationException);
            return buildErrorResponse(
                    dataIntegrityViolationException,
                    errorMessage,
                    HttpStatus.CONFLICT,
                    request);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        public ResponseEntity<Object> handleConstraintViolationException(
                ConstraintViolationException constraintViolationException,
                WebRequest request) {
            log.error("Failed to validate element", constraintViolationException);
            return buildErrorResponse(
                    constraintViolationException,
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    request);
        }

        @ExceptionHandler(ObjectNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<Object> handleObjectNotFoundException(
                ObjectNotFoundException objectNotFoundException,
                WebRequest request) {
            log.error("Failed to find the requested element", objectNotFoundException);
            return buildErrorResponse(
                    objectNotFoundException,
                    HttpStatus.NOT_FOUND,
                    request);
        }

        @ExceptionHandler(DataBindingViolationException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public ResponseEntity<Object> handleDataBindingViolationException(
                DataBindingViolationException dataBindingViolationException,
                WebRequest request) {
            log.error("Failed to save entity with associated data", dataBindingViolationException);
            return buildErrorResponse(
                    dataBindingViolationException,
                    HttpStatus.CONFLICT,
                    request);
        }

        @ExceptionHandler(AuthenticationException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        public ResponseEntity<Object> handleAuthenticationException(
                AuthenticationException authenticationException,
                WebRequest request) {
            log.error("Authentication error ", authenticationException);
            return buildErrorResponse(
                    authenticationException,
                    HttpStatus.UNAUTHORIZED,
                    request);
        }

        @ExceptionHandler(AccessDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ResponseEntity<Object> handleAccessDeniedException(
                AccessDeniedException accessDeniedException,
                WebRequest request) {
            log.error("Authorization error ", accessDeniedException);
            return buildErrorResponse(
                    accessDeniedException,
                    HttpStatus.FORBIDDEN,
                    request);
        }

        @ExceptionHandler(AuthorizationException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ResponseEntity<Object> handleAuthorizationException(
                AuthorizationException authorizationException,
                WebRequest request) {
            log.error("Authorization error ", authorizationException);
            return buildErrorResponse(
                    authorizationException,
                    HttpStatus.FORBIDDEN,
                    request);
        }

        private ResponseEntity<Object> buildErrorResponse(
                Exception exception,
                HttpStatus httpStatus,
                WebRequest request) {
            return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
        }

        private ResponseEntity<Object> buildErrorResponse(
                Exception exception,
                String message,
                HttpStatus httpStatus,
                WebRequest request) {
            ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);
            if (this.printStackTrace) {
                errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
            }
            return ResponseEntity.status(httpStatus).body(errorResponse);
        }

        @Override
        public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException exception) throws IOException, jakarta.servlet.ServletException {
            Integer status = HttpStatus.UNAUTHORIZED.value();
            response.setStatus(status);
            response.setContentType("application/json");
            ErrorResponse errorResponse = new ErrorResponse(status, "Username or password are invalid");
            response.getWriter().append(errorResponse.toJson());
        }
    }

