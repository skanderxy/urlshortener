package com.sbasly.urlshortener.dtos;

import lombok.Builder;

@Builder
public record UrlResponse (String originalUrl, String shortCode, String shortUrl) { }
