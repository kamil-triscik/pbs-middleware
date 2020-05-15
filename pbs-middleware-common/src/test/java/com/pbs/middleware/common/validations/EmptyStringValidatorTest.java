package com.pbs.middleware.common.validations;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmptyStringValidator tests")
class EmptyStringValidatorTest {

    private static final EmptyStringValidator validator = new EmptyStringValidator();

    private static Stream<Arguments> validData() {
        return Stream.of(
                Arguments.of(null, "Null string"),
                Arguments.of("data", "Non empty string"),
                Arguments.of("     data", "Non empty string with long blank space")
        );
    }

    @ParameterizedTest(name = "Valid collection test {index}: {1}")
    @DisplayName("Valid string test")
    @MethodSource("validData")
    void validDataTest(String value, String message) {
        assertTrue(validator.isValid(value, null), "Validator should return TRUE for test: " + message);
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of("", "Empty string"),
                Arguments.of("     ", "Blank string")
        );
    }

    @ParameterizedTest(name = "Invalid collection test {index}: {1}")
    @DisplayName("Invalid string test")
    @MethodSource("invalidData")
    void invalidDataTest(String value, String message) {
        assertFalse(validator.isValid(value, null), "Validator should return FALSE for test: " + message);
    }

}