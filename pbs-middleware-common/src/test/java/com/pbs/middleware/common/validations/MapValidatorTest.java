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

@DisplayName("MapValidator tests")
class MapValidatorTest {

    private static final MapValidator validator = new MapValidator();

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
    void validDataTest(Map<?, ?> map, String message) {
        assertTrue(validator.isValid(map, null), "Validator should return TRUE for test: " + message);
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of(new HashMap<>() {{
                    put(null, null);
                }}, "Map with null key value"),
                Arguments.of(new HashMap<>() {{
                    put("key", null);
                }}, "Map with not null value"),
                Arguments.of(new HashMap<>() {{
                    put(null, "value");
                }}, "Map with not null key")
        );
    }

    @ParameterizedTest(name = "Invalid map test {index}: {1}")
    @DisplayName("Invalid map test")
    @MethodSource("invalidData")
    void invalidDataTest(Map<?, ?> map, String message) {
        assertFalse(validator.isValid(map, null), "Validator should return FALSE for test: " + message);
    }

}