package com.pbs.middleware.server.features.pbs.validations;

import com.pbs.middleware.api.template.Chunk;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Unique chunk validator tests")
class UniqueChunksValidatorTest {

    @Test
    @DisplayName("Null list of chunks test")
    void nullListChunksTest() {
        assertTrue(new UniqueChunksValidator().isValid(null, null),
                "Null list of chunks should be validated as valid");
    }

    @Test
    @DisplayName("Empty list of chunks test")
    void emptyListOfChunksTest() {
        assertTrue(new UniqueChunksValidator().isValid(emptyList(), null),
                "Empty list of chunks should be validated as valid");
    }

    @Test
    @DisplayName("List with one chunk test")
    void listWithOneChunkTest() {
        assertTrue(new UniqueChunksValidator().isValid(List.of(new Chunk("id", null, null, null, null)), null),
                "List with one chunk should be validated as valid");
    }

    @Test
    @DisplayName("List with 2 chunks with different ids test")
    void listWith2DiffChunksTest() {
        assertTrue(new UniqueChunksValidator().isValid(List.of(
                new Chunk("id", null, null, null, null),
                new Chunk("id2", null, null, null, null)), null),
                "List with 2 chunks with different ids should be validated as valid");
    }

    @Test
    @DisplayName("List with 2 chunks with same ids test")
    void listWith2SameChunksTest() {
        assertFalse(new UniqueChunksValidator().isValid(List.of(
                new Chunk("id", null, null, null, null),
                new Chunk("id", null, null, null, null)), null),
                "List with 2 chunks with same ids should be validated as valid");
    }


}