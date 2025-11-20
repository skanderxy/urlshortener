package com.sbasly.urlshortener.services;

import com.sbasly.urlshortener.dtos.UrlResponse;
import com.sbasly.urlshortener.entities.Url;
import com.sbasly.urlshortener.repositories.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlServiceImplTest {

	private UrlRepository urlRepository;
	private ShortCodeGenerator shortCodeGenerator;
	private UrlServiceImpl urlService;

	@BeforeEach
	void setUp() {
		urlRepository = Mockito.mock(UrlRepository.class);
		shortCodeGenerator = Mockito.mock(ShortCodeGenerator.class);
		urlService = new UrlServiceImpl(shortCodeGenerator, urlRepository);
		try {
			var field = UrlServiceImpl.class.getDeclaredField("baseUrl");
			field.setAccessible(true);
			field.set(urlService, "http://localhost:8080");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void shorten_returnsExisting_whenOriginalExists() {
		String original = "https://example.com/existing";
		Url existing = Url.builder()
				.id(1L)
				.original(original)
				.shortened("abc123")
				.createdAt(LocalDateTime.now())
				.build();

		when(urlRepository.findByOriginal(original)).thenReturn(Optional.of(existing));

		UrlResponse response = urlService.shorten(original);

		assertNotNull(response);
		assertEquals(existing.getOriginal(), response.originalUrl());
		assertEquals(existing.getShortened(), response.shortCode());
		verify(urlRepository, never()).save(any());
	}

	@Test
	void shorten_createsNew_whenNotExists() {
		String original = "https://example.com/new";
		when(urlRepository.findByOriginal(original)).thenReturn(Optional.empty());
		when(shortCodeGenerator.generate(original)).thenReturn("newcode");

		ArgumentCaptor<Url> captor = ArgumentCaptor.forClass(Url.class);
		when(urlRepository.save(captor.capture())).thenAnswer(invocation -> {
			Url toSave = invocation.getArgument(0);
			toSave.setId(42L);
			toSave.setCreatedAt(LocalDateTime.now());
			return toSave;
		});

		UrlResponse response = null;
		try {
			response = urlService.shorten(original);
		} catch (Exception e) {
			fail("shorten should not throw in this scenario: " + e.getMessage());
		}

		assertNotNull(response);
		assertEquals(original, response.originalUrl());
		assertEquals("newcode", response.shortCode());
		assertTrue(response.shortUrl().endsWith("/newcode"));

		Url saved = captor.getValue();
		assertEquals(original, saved.getOriginal());
		assertEquals("newcode", saved.getShortened());
	}
}
