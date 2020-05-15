package com.pbs.middleware.server.features.pbs.qsub;

import com.pbs.middleware.common.pbs.Chunk;
import com.pbs.middleware.common.pbs.Walltime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

@DisplayName("Qsub resources builder test")
class QsubResourcesBuilderTest {

    private static Stream<Arguments> invalidCustomResources() {
        return Stream.of(
                Arguments.of(null, null, "key=null, value=null"),
                Arguments.of(null, "", "key=null, value=\"\""),
                Arguments.of(null, " ", "key=null, value=\" \""),
                Arguments.of("", null, "key=\"\", value=null"),
                Arguments.of(" ", null, "key=\" \", value=null"),
                Arguments.of("", "", "key=\"\", value=\"\""),
                Arguments.of(" ", "", "key=\" \", value=\"\""),
                Arguments.of("", " ", "key=\"\", value=\" \""),
                Arguments.of(" ", " ", "key=\" \", value=\" \"")
        );
    }

    private static Stream<Arguments> invalidCustomResourcesMap() {
        return Stream.of(
                Arguments.of((LinkedHashMap<String, String>) null, "NULL map"),
                Arguments.of(new LinkedHashMap<>(), "Empty map"),
                Arguments.of(new LinkedHashMap<>() {{
                    put(null, null);
                    put(null, null);
                    put(null, "");
                    put(null, " ");
                    put("", null);
                    put(" ", null);
                    put("", "");
                    put(" ", "");
                    put("", " ");
                    put(" ", " ");
                }}, "Map with only invalid data")
        );
    }

    private static Stream<Arguments> singleChunks() {
        return Stream.of(
                Arguments.of((Chunk) null, "", "Null chunk"),
                Arguments.of(new Chunk("id", 1L, 1L, null, emptyMap()), " -l select=1:ncpus=1", "One valid chunk")
        );
    }

    private static Stream<Arguments> multipleChunks() {
        return Stream.of(
                Arguments.of(null, "", "NULL chunks list"),
                Arguments.of(emptyList(), "", "Empty list of chunks"),
                Arguments.of(new LinkedList<>() {{
                    add(null);
                }}, "", "List with null values"),
                Arguments.of(new LinkedList<>() {{
                                 add(new Chunk("id", 1L, 1L, null, emptyMap()));
                                 add(null);
                             }},
                        " -l select=1:ncpus=1", "List with chunk and also null value"),
                Arguments.of(new LinkedList<>() {{
                    add(new Chunk("id", 1L, 1L, null, emptyMap()));
                    add(new Chunk("id", 1L, 1L, "4GB", emptyMap()));
                }}, " -l select=1:ncpus=1+1:ncpus=1:mem=4GB", "List with 2 different chunks")
        );
    }

    @Test
    @DisplayName("Empty builder test")
    void emptyBuilderTest() {
        assertEquals("Empty builder should produce empty string",
                "", new QsubResourcesBuilder().build());
    }

    @Test
    @DisplayName("Null walltime test")
    void nullWalltimeTest() {
        assertEquals("Null walltime should produce empty string",
                "", new QsubResourcesBuilder().walltime(null).build());
    }

    @Test
    @DisplayName("Builder with walltime test")
    void walltimeTest() {
        final Walltime walltime = Walltime.from("1:0:0");
        final String expectedWalltime = " -l walltime=" + walltime.toString();

        assertEquals("Invalid walltime",
                expectedWalltime, new QsubResourcesBuilder().walltime(walltime).build());
    }

    @Test
    @DisplayName("Null custom resource key test")
    void nullCustomPropertyTest() {
        assertEquals("Null walltime should produce empty string",
                "", new QsubResourcesBuilder().customResource(null, null).build());
    }

    @ParameterizedTest(name = "Custom resources {index} - {2}")
    @MethodSource("invalidCustomResources")
    @DisplayName("Invalid custom resources test")
    void invalidCustomResourcesTest(String key, String val, String name) {
        assertEquals("Invalid custom resource should produce empty string",
                "", new QsubResourcesBuilder().customResource(key, val).build());
    }

    @ParameterizedTest(name = "Custom resources map {index} - {1}")
    @MethodSource("invalidCustomResourcesMap")
    @DisplayName("Invalid custom resources map test")
    void invalidCustomResourcesMapTest(Map<String, String> map, String name) {
        assertEquals("Invalid custom resource should produce empty string",
                "", new QsubResourcesBuilder().customResource(map).build());
    }

    @Test
    @DisplayName("Builder with single custom resource test")
    void singleCustomResourceTest() {
        final String key = "rsKey";
        final String val = "rsVal";

        final String expectedResult = " -l " + key + "=" + val;

        assertEquals("Invalid custom resource string",
                expectedResult, new QsubResourcesBuilder().customResource(key, val).build());
    }

    @Test
    @DisplayName("Builder with multiple custom resources test")
    void multipleCustomResourcesTest() {
        final String key1 = "rsKey1";
        final String val1 = "rsVal1";
        final String key2 = "rsKey2";
        final String val2 = "rsVal2";

        final String expectedResult = " -l " + key1 + "=" + val1 + ":" + key2 + "=" + val2;

        QsubResourcesBuilder builder = new QsubResourcesBuilder()
                .customResource(key1, val1)
                .customResource(key2, val2);

        assertEquals("Invalid custom resource string", expectedResult, builder.build());
    }

    @Test
    @DisplayName("Invalid custom resources map test")
    void singleCustomResourceMapTest() {
        final String key = "rsKey";
        final String val = "rsVal";

        final String expectedResult = " -l " + key + "=" + val;

        assertEquals("Invalid custom resource string",
                expectedResult, new QsubResourcesBuilder().customResource(Map.of(key, val)).build());
    }

    @Test
    @DisplayName("Builder with multiple custom resources map test")
    void multipleCustomResourcesMapTest() {
        final String key1 = "rsKey1";
        final String val1 = "rsVal1";
        final String key2 = "rsKey2";
        final String val2 = "rsVal2";

        final String expectedResult = " -l " + key1 + "=" + val1 + ":" + key2 + "=" + val2;

        final Map<String, String> customResourcesMap = new LinkedHashMap<>(){{
            put(key1, val1);
            put(key2, val2);
        }};

        QsubResourcesBuilder builder = new QsubResourcesBuilder().customResource(customResourcesMap);

        assertEquals("Invalid custom resource string", expectedResult, builder.build());
    }

    @Test
    @DisplayName("Map and key value combination of custom resources test")
    void mapKeyValueCustomResourceTest() {
        final String key1 = "rsKey1";
        final String val1 = "rsVal1";
        final String key2 = "rsKey2";
        final String val2 = "rsVal2";

        final String expectedResult = " -l " + key1 + "=" + val1 + ":" + key2 + "=" + val2;

        QsubResourcesBuilder builder = new QsubResourcesBuilder()
                .customResource(Map.of(key1, val1))
                .customResource(key2, val2);

        assertEquals("Invalid custom resource string", expectedResult, builder.build());
    }

    @Test
    @DisplayName("Null chunk test")
    void nullChunkTest() {
        assertEquals("Null chunk should produce empty string",
                "", new QsubResourcesBuilder().chunk(null).build());
    }

    @Test
    @DisplayName("Null chunks collection test")
    void nullChunksCollectionTest() {
        assertEquals("Null chunks collection should produce empty string",
                "", new QsubResourcesBuilder().chunks(null).build());
    }

    @Test
    @DisplayName("Empty chunks collection test")
    void emptyChunksCollectionTest() {
        assertEquals("Empty chunks collection should produce empty string",
                "", new QsubResourcesBuilder().chunks(emptyList()).build());
    }

    @Test
    @DisplayName("Chunks collection with null values test")
    void chunksCollectionWithNullTest() {
        assertEquals("Chunks collection with null values should produce empty string",
                "", new QsubResourcesBuilder().chunks(new LinkedList<>() {{
                    add(null);
                }}).build());
    }

    @ParameterizedTest(name = "Single chunk {index} - {2}")
    @MethodSource("singleChunks")
    @DisplayName("Single chunks test")
    void singleChunkTest(Chunk chunk, String expectedResult, String name) {
        assertEquals("Invalid chunk string",
                expectedResult, new QsubResourcesBuilder().chunk(chunk).build());
    }

    @ParameterizedTest(name = "Multiple chunks {index} - {2}")
    @MethodSource("multipleChunks")
    @DisplayName("Multiple chunks test")
    void multipleChunksTest(List<Chunk> chunks, String expectedResult, String name) {
        assertEquals("Invalid chunk string",
                expectedResult, new QsubResourcesBuilder().chunks(chunks).build());
    }

    @Test
    @DisplayName("All props setup test")
    void allSetup() {
        List<Chunk> chunks = List.of(
            new Chunk("id", 1L, 1L, null, emptyMap()),
            new Chunk("id", 1L, 1L, "4GB", emptyMap()));

        final String expectedChunksString = " -l select=1:ncpus=1+1:ncpus=1:mem=4GB";

        final Walltime walltime = Walltime.from("1:0:0");
        final String expectedWalltime = " -l walltime=" + walltime.toString();

        final String key1 = "rsKey1";
        final String val1 = "rsVal1";
        final String key2 = "rsKey2";
        final String val2 = "rsVal2";

        final String expectedCPResult = " -l " + key1 + "=" + val1 + ":" + key2 + "=" + val2;

        final String expectedResult = expectedWalltime + expectedCPResult + expectedChunksString;

        QsubResourcesBuilder builder = new QsubResourcesBuilder()
                .customResource(Map.of(key1, val1))
                .customResource(key2, val2)
                .walltime(walltime)
                .chunks(chunks);

        assertEquals("Invalid chunk string",
                expectedResult, builder.build());
    }

}