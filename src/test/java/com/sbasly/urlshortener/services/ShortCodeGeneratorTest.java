package com.sbasly.urlshortener.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShortCodeGeneratorTest {

	@Test
	void generate_isDeterministic_andValidCharacters() {
		ShortCodeGenerator generator = new ShortCodeGenerator();

		String input = "https://example.com/some/long/path?abc=123";

		String code1 = null;
		String code2 = null;
		try {
			code1 = generator.generate(input);
			code2 = generator.generate(input);
		} catch (Exception e) {
			fail("generate should not throw: " + e.getMessage());
		}

		assertNotNull(code1);
		assertEquals(code1, code2, "Generator should produce the same code for the same input");
		assertTrue(code1.length() <= 10, "Short code must be at most 10 characters");
		assertTrue(code1.matches("[0-9a-zA-Z]+"), "Short code should contain only Base62 characters");
	}

	@Test
	void generate_handlesDifferentInputs() {
		ShortCodeGenerator generator = new ShortCodeGenerator();

		String a = "https://example.com/a";
		String b = "https://example.com/b";

		String codeA = null;
		String codeB = null;
		try {
			codeA = generator.generate(a);
			codeB = generator.generate(b);
		} catch (Exception e) {
			fail("generate should not throw: " + e.getMessage());
		}

		assertNotNull(codeA);
		assertNotNull(codeB);
		// collisions are possible but unlikely; this asserts normal behavior
		assertNotEquals(codeA, codeB, "Different inputs should normally produce different codes");
	}
}
