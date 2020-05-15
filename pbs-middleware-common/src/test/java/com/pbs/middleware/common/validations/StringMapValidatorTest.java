package com.pbs.middleware.common.validations;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StringMapValidator tests")
class StringMapValidatorTest {

    private static final StringMapValidator validator = new StringMapValidator();

    private static Stream<Arguments> validData() {
        return Stream.of(
                Arguments.of(null, "Null map"),
                Arguments.of(Map.of(), "Empty map"),
                Arguments.of(Map.of("key", "value"), "Map with not null key value")
        );
    }

    @ParameterizedTest(name = "Valid map test {index}: {1}")
    @DisplayName("Valid map test")
    @MethodSource("validData")
    void validDataTest(Map<String, String> map, String message) {
        assertTrue(validator.isValid(map, null), "Validator should return TRUE for test: " + message);
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of(Map.of("", ""), "Map with empty string key value"),
                Arguments.of(Map.of("key", ""), "Map with empty string value"),
                Arguments.of(Map.of("", "value"), "Map with empty string key"),
                Arguments.of(Map.of("", "  "), "Map with empty string key and blank value"),
                Arguments.of(Map.of("  ", ""), "Map with blank key empty value"),
                Arguments.of(Map.of("  ", "  "), "Map with blank string key value"),
                Arguments.of(Map.of("   ", "value"), "Map with blank string value"),
                Arguments.of(Map.of("key", "   "), "Map with blank string key")
        );
    }

    @ParameterizedTest(name = "Invalid map test {index}: {1}")
    @DisplayName("Invalid map test")
    @MethodSource("invalidData")
    void invalidDataTest(Map<String, String> map, String message) {
        assertFalse(validator.isValid(map, null), "Validator should return FALSE for test: " + message);
    }

}