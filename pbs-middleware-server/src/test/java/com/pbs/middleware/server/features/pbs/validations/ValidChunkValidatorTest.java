package com.pbs.middleware.server.features.pbs.validations;

import com.pbs.middleware.api.template.Chunk;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Valid chunk validator tests")
class ValidChunkValidatorTest {

    private static Stream<Arguments> validChunks() {
        return Stream.of(
                Arguments.of(new Chunk("id", 1L, null, null, null)),
                Arguments.of(new Chunk("id", null, 1L, null, null)),
                Arguments.of(new Chunk("id", null, 1L, null, emptyMap())),
                Arguments.of(new Chunk("id", null, 1L, null, Map.of("a", "b"))),
                Arguments.of(new Chunk("id", null, null, "4GB", null)),
                Arguments.of(new Chunk("id", null, null, "4GB", emptyMap())),
                Arguments.of(new Chunk("id", null, null, "4GB", Map.of("a", "b"))),
                Arguments.of(new Chunk("id", null, null, null, Map.of("a", "b"))),
                Arguments.of(new Chunk("id", 1L, 1L, null, null)),
                Arguments.of(new Chunk("id", 1L, 1L, null, emptyMap())),
                Arguments.of(new Chunk("id", 1L, 1L, null, Map.of("a", "b"))),
                Arguments.of(new Chunk("id", 1L, null, "4GB", null)),
                Arguments.of(new Chunk("id", 1L, null, "4GB", emptyMap())),
                Arguments.of(new Chunk("id", 1L, null, "4GB", Map.of("a", "b"))),
                Arguments.of(new Chunk("id", 1L, null, null, Map.of("a", "b"))),
                Arguments.of(new Chunk("id", 1L, null, null, emptyMap())),
                Arguments.of(new Chunk("id", 1L, 1L, "4GB", null)),
                Arguments.of(new Chunk("id", 1L, 1L, null, Map.of("a", "b"))),
                Arguments.of(new Chunk("id", 1L, 1L, "4GB", Map.of("a", "b"))),
                Arguments.of(new Chunk("id", 1L, 1L, "4GB", emptyMap()))
                );
    }

    private static Stream<Arguments> invalidChunks() {
        return Stream.of(
                Arguments.of(new Chunk(),  "Empty chunk"),
                Arguments.of(new Chunk("id", null, null, null, null), "Only id is set"),
                Arguments.of(new Chunk("id", null, null, "", null), "Empty mem is set"),
                Arguments.of(new Chunk("id", null, null, " ", null), "Blank mem is set"),
                Arguments.of(new Chunk("id", null, null, null, emptyMap()), "empty custom resources map"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(null, null); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("a", null); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("a", ""); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("a", " "); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(null, "b"); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(null, ""); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(null, " "); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("", null); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("", "b"); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(" ", null); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(" ", "b"); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("", ""); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(" ", ""); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put("", " "); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(" ", " "); }}), "Map with invalid values"),
                Arguments.of(new Chunk("id", null, null, null, new HashMap<>() {{ put(" ", " "); }}), "Map with invalid values")
        );
    }

    @Test
    @DisplayName("Null chunk test")
    void nullChunk() {
        assertTrue(new ValidChunkValidator().isValid(null, null), "Null chunk should be validated as valid");
    }

    @ParameterizedTest(name = "Invalid chunk test {index} - {1}")
    @MethodSource("invalidChunks")
    @DisplayName("Invalid chunks validation test")
    void emptyChunk(Chunk chunkDto, String name) {
        assertFalse(new ValidChunkValidator().isValid(chunkDto, null), "Invalid chunk should be validated as not valid");
    }

    @ParameterizedTest(name = "Valid chunk test {index}")
    @MethodSource("validChunks")
    @DisplayName("Valid chunks validation test")
    void validChunk(Chunk chunkDto) {
        assertTrue(new ValidChunkValidator().isValid(chunkDto, null), "Valid chunk should be validated as valid");
    }


}