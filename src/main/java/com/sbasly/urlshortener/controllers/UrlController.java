package com.sbasly.urlshortener.controllers;

import com.sbasly.urlshortener.dtos.UrlRequest;
import com.sbasly.urlshortener.dtos.UrlResponse;
import com.sbasly.urlshortener.services.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UrlController {

	private final UrlService urlService;

	@GetMapping
	public ResponseEntity<List<UrlResponse>> findAll() {
		return ResponseEntity.ok(urlService.findAll());
	}

	@GetMapping("/{shortCode}")
	public ResponseEntity<UrlResponse> getByShortCode(@PathVariable String shortCode) {
		return ResponseEntity.ok(urlService.getByShortCode(shortCode));
	}

	@PostMapping
	public ResponseEntity<UrlResponse> shorten(@Valid @RequestBody UrlRequest urlRequest) {
		return ResponseEntity.ok(urlService.shorten(urlRequest.originalUrl()));
	}
}
