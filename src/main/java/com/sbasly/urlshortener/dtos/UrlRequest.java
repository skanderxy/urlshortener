package com.sbasly.urlshortener.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UrlRequest(@NotBlank @URL String originalUrl) {}
