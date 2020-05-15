package com.pbs.middleware.server.features.pbs.qsub;

import com.pbs.middleware.common.pbs.Chunk;
import com.pbs.middleware.common.pbs.Walltime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

class QsubResourcesBuilder {

    private static final String ASSIGN_OPERATOR = "=";
    private static final String PARAM_SEPARATOR = ":";

    private Walltime walltime = null;
    private final Map<String, String> customResources = new LinkedHashMap<>();
    private final List<Chunk> chunks = new LinkedList<>();

    QsubResourcesBuilder walltime(Walltime walltime) {
        this.walltime = walltime;
        return this;
    }

    QsubResourcesBuilder customResource(String key, String value) {
        this.customResources.put(key, value);
        return this;
    }

    QsubResourcesBuilder customResource(Map<String, String> customResources) {
        if (customResources != null) {
            this.customResources.putAll(customResources);
        }
        return this;
    }

    QsubResourcesBuilder chunk(Chunk chunk) {
        if (chunk != null) {
            this.chunks.add(chunk);
        }
        return this;
    }

    QsubResourcesBuilder chunks(List<Chunk> chunks) {
        if (chunks != null) {
            chunks.stream().filter(Objects::nonNull).forEach(this.chunks::add);
        }
        return this;
    }

    String build() {
        String resources = "";

        if (this.walltime != null) {
            resources += " -l walltime=" + this.walltime.toString();
        }

        String customResourcesString = this.customResources
                .keySet()
                .stream()
                .filter(Objects::nonNull)
                .filter(StringUtils::isNotBlank)
                .filter(key -> this.customResources.get(key) != null)
                .filter(key -> StringUtils.isNotBlank(this.customResources.get(key)))
                .map(key -> format("%s%s%s", key, ASSIGN_OPERATOR, this.customResources.get(key)))
                .collect(joining(PARAM_SEPARATOR));
        if (!customResourcesString.isBlank()) {
            resources += " -l " + customResourcesString;
        }


        if (!chunks.isEmpty()) {
            Chunk fistChunk = chunks.get(0);
            String chunksString = format(" -l select=%s", fistChunk.toPbsString(PARAM_SEPARATOR, ASSIGN_OPERATOR));

            if (this.chunks.size() > 1) {
                chunksString += "+" + chunks.subList(1, chunks.size())
                        .stream()
                        .map(chunk -> chunk.toPbsString(PARAM_SEPARATOR, ASSIGN_OPERATOR))
                        .collect(joining("+"));
            }

            resources += chunksString;
        }

        return resources;
    }
}
