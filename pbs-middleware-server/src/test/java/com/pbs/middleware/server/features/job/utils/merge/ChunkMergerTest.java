package com.pbs.middleware.server.features.job.utils.merge;

import com.pbs.middleware.common.pbs.Chunk;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Chunks merger tests")
class ChunkMergerTest {

    @Test
    @DisplayName("Changes and origin are null")
    void argsAreNullTest() {
        assertNull(ChunkMerger.mergeChunks(null, null));
    }

    @Test
    @DisplayName("Origin argument is null")
    void originIsNullTest() {
        List<Chunk> chunks = List.of(new Chunk());

        List<Chunk> result = ChunkMerger.mergeChunks(null, chunks);

        assertNotNull(result);
        assertNotSame(chunks, result);
        assertEquals(chunks, result);
    }

    @Test
    @DisplayName("Changes argument is null")
    void changesIsNullTest() {
        List<Chunk> chunks = List.of(new Chunk());

        List<Chunk> result = ChunkMerger.mergeChunks(chunks, null);

        assertNotNull(result);
        assertNotSame(chunks, result);
        assertEquals(chunks, result);
    }

    @Test
    @DisplayName("Merge two different chunks")
    void twoDifferentChunksTest() {
        Chunk origChunk = new Chunk("chunk1", 1L, null, null, null);
        Chunk newChunk = new Chunk("chunk2", 1L, null, null, null);

        List<Chunk> result = ChunkMerger.mergeChunks(List.of(origChunk), List.of(newChunk));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(origChunk));
        assertTrue(result.contains(newChunk));
    }

    @Test
    @DisplayName("Remove chunk")
    void removeChunkTest() {
        Chunk origChunk = new Chunk("chunk1", 1L, null, null, null);
        Chunk newChunk = new Chunk("chunk1", 0L, null, null, null);

        List<Chunk> result = ChunkMerger.mergeChunks(List.of(origChunk), List.of(newChunk));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Merge chunk")
    void mergeChunkTest() {
        final Long newCount = 2L;
        final Long newNcpus = 2L;
        final String newMem = "4GB";

        Map<String, String> origCP = Map.of("a", "b", "c", "d", "del", "toDel", "", "empty", "empty2", "", "blank", " ", " ", "blank2");
        Map<String, String> changeCP = new HashMap<>() {{
            put("c", "dd");
            put("e", "f");
            put("del", null);
            put("  ", "blank");
            put("empty3", "");
            put("blank3", "    ");
        }};

        Chunk origChunk = new Chunk("chunk1", 1L, null, null, origCP);
        Chunk newChunk = new Chunk("chunk1", newCount, newNcpus, newMem, changeCP);

        List<Chunk> result = ChunkMerger.mergeChunks(List.of(origChunk), List.of(newChunk));

        assertNotNull(result);
        assertEquals(1, result.size());

        Chunk merged = result.get(0);
        assertEquals(newCount, merged.getCount());
        assertEquals(newNcpus, merged.getNcpus());
        assertEquals(newMem, merged.getMem());

        assertNotSame(origChunk.getCustomResources(), merged.getCustomResources());

        assertEquals(3, merged.getCustomResources().size());
        assertEquals("b", merged.getCustomResources().get("a"));
        assertEquals("dd", merged.getCustomResources().get("c"));
        assertEquals("f", merged.getCustomResources().get("e"));
        assertNull(merged.getCustomResources().get("del"));
        assertNull(merged.getCustomResources().get("empty2"));
        assertNull(merged.getCustomResources().get("blank"));
        assertNull(merged.getCustomResources().get(""));
        assertNull(merged.getCustomResources().get(" "));
        assertNull(merged.getCustomResources().get("empty3"));
        assertNull(merged.getCustomResources().get("blank3"));
        assertNull(merged.getCustomResources().get("  "));
    }

}