package com.sbasly.urlshortener.dtos;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(Instant timestamp, int status, String code, String message) {}
