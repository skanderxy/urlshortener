package com.sbasly.urlshortener.exceptions;

import com.sbasly.urlshortener.dtos.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// Validation errors (DTO validation: @URL, @NotBlank, etc.)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + " " + err.getDefaultMessage())
				.findFirst()
				.orElse(ErrorCode.VALIDATION_ERROR.message);

		log.warn("{} - {}", ErrorCode.VALIDATION_ERROR.message, message);

		return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR.code, message);
	}

	// For validation errors thrown by JPA or other constraints
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
		String message = ex.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.findFirst()
				.orElse(ErrorCode.CONSTRAINT_VIOLATION.message);

		log.warn("{} - {}", ErrorCode.CONSTRAINT_VIOLATION.message, message);

		return buildResponse(HttpStatus.BAD_REQUEST, ErrorCode.CONSTRAINT_VIOLATION.code, message);
	}

	// Entity is not found in the repository
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
		log.warn("{} - {}", ErrorCode.URL_NOT_FOUND.message, ex.getMessage());

		return buildResponse(HttpStatus.NOT_FOUND, ErrorCode.URL_NOT_FOUND.code, ErrorCode.URL_NOT_FOUND.message);
	}

	// Catch-all
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		log.error("{} - {}", ErrorCode.INTERNAL_ERROR.message, ex.getMessage());

		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR.code,
				ErrorCode.INTERNAL_ERROR.message);
	}

	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String code, String message) {
		return ResponseEntity.status(status)
				.body(
						ErrorResponse.builder()
								.timestamp(Instant.now())
								.status(status.value())
								.code(code)
								.message(message)
								.build()
				);
	}
}
