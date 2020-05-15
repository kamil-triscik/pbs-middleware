package com.pbs.middleware.server.features.pbs.domain;

import com.pbs.middleware.common.pbs.Chunk;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

@DisplayName("Chunk to PBS Qsub test")
class ChunkTest {

    private static Stream<Arguments> singleChunks() {
        return Stream.of(
                Arguments.of(new Chunk("id", 1L, 0L, "", null), "1"),
                Arguments.of(new Chunk("id", 1L, 0L, "", emptyMap()), "1"),
                Arguments.of(new Chunk("id", null, 1L, "", emptyMap()), "1:ncpus=1"),
                Arguments.of(new Chunk("id", null, 0L, "4GB", emptyMap()), "1:mem=4GB"),
                Arguments.of(new Chunk("id", null, 0L, "", new HashMap<>() {{ put("k","v"); }}), "1:k=v"),
                Arguments.of(new Chunk("id", 1L, 1L, "", emptyMap()), "1:ncpus=1"),
                Arguments.of(new Chunk("id", 1L, 0L, "4GB", emptyMap()), "1:mem=4GB"),
                Arguments.of(new Chunk("id", 1L, 1L, "4GB", emptyMap()), "1:ncpus=1:mem=4GB"),
                Arguments.of(new Chunk("id", null, 1L, "4GB", emptyMap()), "1:ncpus=1:mem=4GB"),
                Arguments.of(new Chunk("id", 1L, 1L, "4GB", emptyMap()), "1:ncpus=1:mem=4GB"),
                Arguments.of(new Chunk("id", 1L, 0L, "", new HashMap<>() {{ put("k","v"); }}), "1:k=v"),
                Arguments.of(new Chunk("id", null, 1L, "", new HashMap<>() {{ put("k","v"); }}), "1:ncpus=1:k=v"),
                Arguments.of(new Chunk("id", null, 0L, "4GB", new HashMap<>() {{ put("k","v"); }}), "1:mem=4GB:k=v"),
                Arguments.of(new Chunk("id", 1L, 1L, "", new HashMap<>() {{ put("k","v"); }}), "1:ncpus=1:k=v"),
                Arguments.of(new Chunk("id", 1L, 0L, "4GB", new HashMap<>() {{ put("k","v"); }}), "1:mem=4GB:k=v"),
                Arguments.of(new Chunk("id", 1L, 1L, "4GB", new HashMap<>() {{ put("k","v"); }}), "1:ncpus=1:mem=4GB:k=v"),
                Arguments.of(new Chunk("id", null, 0L, "", new LinkedHashMap<>() {{ put("k","v"); put("a","b"); }}), "1:k=v:a=b")
        );
    }

    @ParameterizedTest
    @MethodSource("singleChunks")
    @DisplayName("Single chunks test")
    void singleChunkTest(Chunk chunk, String expectedResult) {
        assertEquals("Invalid chunk string",
                expectedResult, chunk.toPbsString(":", "="));
    }

}