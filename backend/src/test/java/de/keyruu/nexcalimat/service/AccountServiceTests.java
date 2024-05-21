package de.keyruu.nexcalimat.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.keyruu.nexcalimat.graphql.exception.PinValidationException;

class AccountServiceTests
{
	@ParameterizedTest
	@ValueSource(strings = { "1234", "0123", "6969" })
	void testValidPins(String pin)
	{
		// given
		// when
		boolean isNumeric = AccountService.isNumeric(pin);

		// then
		assertTrue(isNumeric);
	}

	@ParameterizedTest
	@ValueSource(strings = { "abcd", "0,56", "0.345", "1.456" })
	void testInvalidPins(String pin)
	{
		// given
		// when
		boolean isNumeric = AccountService.isNumeric(pin);

		// then
		assertFalse(isNumeric);
	}

	@ParameterizedTest
	@ValueSource(strings = { "abcd", "0,56", "0.345", "1.456", "55655" })
	void validateInvalidPins(String pin)
	{
		// given
		// when
		// then
		assertThrows(PinValidationException.class, () -> AccountService.validatePin(pin));
	}
}
