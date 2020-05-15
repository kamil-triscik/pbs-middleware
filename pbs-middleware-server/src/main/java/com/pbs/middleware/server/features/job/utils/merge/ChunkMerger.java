package com.pbs.middleware.server.features.job.utils.merge;

import com.pbs.middleware.server.common.utils.AbstractMerger;
import com.pbs.middleware.common.pbs.Chunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChunkMerger extends AbstractMerger<Chunk> {

    public static List<Chunk> mergeChunks(List<Chunk> origin, List<Chunk> changes) {
        if (origin == null && changes == null) {
            return null;
        }
        if (origin != null && changes == null) {
            return new ArrayList<>(origin);
        } else if (origin == null) {
            return new ArrayList<>(changes);
        }

        Map<String, Chunk> merged = origin.stream().collect(Collectors.toMap(Chunk::getId, it -> it));
        Map<String, Chunk> changesMap = changes.stream().collect(Collectors.toMap(Chunk::getId, it -> it));

        changesMap.forEach((key, value) -> {
            if (merged.containsKey(key)) {
                if(value.getCount().equals(0L)) {
                    merged.remove(key);
                } else {
                    merged.put(key, mergeChunk(merged.get(key), value));
                }
            } else {
                merged.put(key, deepCopy(value));
            }
        });

        return new ArrayList<>(merged.values());
    }

    private static Chunk mergeChunk(Chunk origin, Chunk change) {
        Chunk merged = deepCopy(origin);

        if (isSet(change.getCount())) {
            merged.setCount(change.getCount());
        }
        if (isSet(change.getNcpus())) {
            merged.setNcpus(change.getNcpus());
        }
        if (isSet(change.getMem())) {
            merged.setMem(change.getMem());
        }
        if (isSet(change.getCustomResources())) {
            merged.setCustomResources(mergeMap(origin.getCustomResources(), change.getCustomResources()));
        }

        return merged;
    }

}
