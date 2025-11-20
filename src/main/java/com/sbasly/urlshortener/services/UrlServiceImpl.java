package com.sbasly.urlshortener.services;

import com.sbasly.urlshortener.dtos.UrlResponse;
import com.sbasly.urlshortener.entities.Url;
import com.sbasly.urlshortener.repositories.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UrlServiceImpl implements UrlService {

	private final ShortCodeGenerator shortCodeGenerator;
	private final UrlRepository urlRepository;

	@Value("${app.base-url}")
	private String baseUrl;

	@Override
	public List<UrlResponse> findAll() {
		return urlRepository.findAll()
				.stream()
				.map(this::buildUrlResponse)
				.toList();
	}

	@Override
	public UrlResponse shorten(String originalUrl) {
		return urlRepository.findByOriginal(originalUrl)
				.map(this::buildUrlResponse)
				.orElseGet(() -> saveUrl(originalUrl));
	}

	@Override
	public UrlResponse getByShortCode(String shortCode) {
		return urlRepository.findByShortened(shortCode)
				.map(this::buildUrlResponse)
				.orElseThrow(EntityNotFoundException::new);
	}

	private UrlResponse saveUrl(String originalUrl) {
		String shortCode = shortCodeGenerator.generate(originalUrl);
		Url url = Url.builder()
				.original(originalUrl)
				.shortened(shortCode)
				.build();

		Url savedUrl = urlRepository.save(url);

		return buildUrlResponse(savedUrl);
	}

	private UrlResponse buildUrlResponse(Url url) {
		return UrlResponse.builder()
				.originalUrl(url.getOriginal())
				.shortCode(url.getShortened())
				.shortUrl(baseUrl + "/" + url.getShortened())
				.build();
	}
}
