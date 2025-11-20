package com.sbasly.urlshortener.exceptions;

public enum ErrorCode {
	VALIDATION_ERROR("VALIDATION_ERROR", "Validation error"),
	CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "Invalid request"),
	URL_NOT_FOUND("URL_NOT_FOUND", "No URL found for this short code"),
	INTERNAL_ERROR("INTERNAL_ERROR", "An unexpected error occurred");

	public final String code;
	public final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}