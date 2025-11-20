package com.sbasly.urlshortener.controllers;

import com.sbasly.urlshortener.dtos.UrlResponse;
import com.sbasly.urlshortener.exceptions.GlobalExceptionHandler;
import com.sbasly.urlshortener.services.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UrlControllerTest {

	private UrlService urlService;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		urlService = Mockito.mock(UrlService.class);
		UrlController controller = new UrlController(urlService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}

	@Test
	void getAll_returnsUrlList() throws Exception {
		UrlResponse r = UrlResponse.builder()
				.originalUrl("https://example.com/x")
				.shortCode("short")
				.shortUrl("http://localhost:8080/short")
				.build();

		when(urlService.findAll()).thenReturn(List.of(r));

		mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].originalUrl").value("https://example.com/x"))
				.andExpect(jsonPath("$[0].shortCode").value("short"));
	}

	@Test
	void getByShortCode_returnsURLItem() throws Exception {
		UrlResponse r = UrlResponse.builder()
				.originalUrl("https://example.com/y")
				.shortCode("short")
				.shortUrl("http://localhost:8080/efgh")
				.build();

		when(urlService.getByShortCode("short")).thenReturn(r);

		mockMvc.perform(get("/short").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.shortCode").value("short"))
				.andExpect(jsonPath("$.originalUrl").value("https://example.com/y"));
	}

	@Test
	void shorten_returnsCreatedResponse() throws Exception {
		String original = "https://example.com/new";
		UrlResponse r = UrlResponse.builder()
				.originalUrl(original)
				.shortCode("short")
				.shortUrl("http://localhost:8080/ncde")
				.build();

		when(urlService.shorten(original)).thenReturn(r);

		String body = "{\"originalUrl\":\"https://example.com/new\"}";

		mockMvc.perform(post("/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.shortCode").value("short"))
				.andExpect(jsonPath("$.originalUrl").value(original));
	}

	@Test
	void shorten_withMalformedUrl_returnsBadRequest() throws Exception {
		String body = "{\"originalUrl\":\"not-a-valid-url\"}";

		mockMvc.perform(post("/")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
				.andExpect(status().isBadRequest());
	}
}
