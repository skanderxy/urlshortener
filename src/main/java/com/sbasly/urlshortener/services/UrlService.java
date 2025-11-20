package com.sbasly.urlshortener.services;

import com.sbasly.urlshortener.dtos.UrlResponse;

import java.util.List;

public interface UrlService {
	List<UrlResponse> findAll();
	UrlResponse shorten(String originalUrl);
	UrlResponse getByShortCode(String shortCode);
}
