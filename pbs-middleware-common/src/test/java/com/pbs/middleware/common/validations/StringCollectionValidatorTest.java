package com.pbs.middleware.common.validations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StringCollectionValidator tests")
class StringCollectionValidatorTest {

    private static final StringCollectionValidator validator = new StringCollectionValidator();

    private static Stream<Arguments> validData() {
        return Stream.of(
                Arguments.of(null, "Null collection"),
                Arguments.of(List.of(), "Empty list"),
                Arguments.of(List.of("data"), "List with not null item"),
                Arguments.of(Set.of(), "Empty set"),
                Arguments.of(Set.of("data"), "Set with not null item")
        );
    }

    @ParameterizedTest(name = "Valid collection test {index}: {1}")
    @DisplayName("Valid collection test")
    @MethodSource("validData")
    void validDataTest(Collection<String> collection, String message) {
        assertTrue(validator.isValid(collection, null), "Validator should return TRUE for test: " + message);
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of(List.of(""), "List with e,pty string item"),
                Arguments.of(List.of("     "), "List with blank item")
        );
    }

    @ParameterizedTest(name = "Invalid collection test {index}: {1}")
    @DisplayName("Invalid collection test")
    @MethodSource("invalidData")
    void invalidDataTest(Collection<String> collection, String message) {
        assertFalse(validator.isValid(collection, null), "Validator should return FALSE for test: " + message);
    }

}