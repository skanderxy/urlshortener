package com.sbasly.urlshortener.services;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generates short, readable codes from long URLs.
 * <p>
 * This generator takes an input URL, cleans it, and produces a stable
 * 10-character code that will always be the same for the same URL.
 * <p>
 * How it works:
 * <p> 1. The URL is hashed using SHA-256.
 * <p> 2. The first 8 bytes of the hash are converted into a positive long.
 * <p> 3. That number is encoded in Base62 (0-9, a-z, A-Z) to keep the code short.
 * <p> 4. The result is trimmed to a maximum of 10 characters.
 */
@Component
public class ShortCodeGenerator {

	private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int BASE = ALPHABET.length();
	private static final int SHORT_CODE_LENGTH = 10;

	public String generate(String url) {
		String normalized = url.trim();

		byte[] hash = sha256(normalized);

		long value = bytesToLong(hash);

		if (value < 0) {
			value = -value;
		}

		String base62 = toBase62(value);

		return base62.length() > SHORT_CODE_LENGTH ? base62.substring(0, SHORT_CODE_LENGTH) : base62;
	}

	private byte[] sha256(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return md.digest(input.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}

	private long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getLong();
	}

	private String toBase62(long value) {
		if (value == 0) {
			return "0";
		}

		StringBuilder sb = new StringBuilder();

		while (value > 0) {
			int index = (int) (value % BASE);
			sb.append(ALPHABET.charAt(index));
			value /= BASE;
		}

		return sb.reverse().toString();
	}
}
