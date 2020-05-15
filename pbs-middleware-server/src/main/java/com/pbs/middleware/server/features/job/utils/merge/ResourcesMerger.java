package com.pbs.middleware.server.features.job.utils.merge;

import com.pbs.middleware.server.common.utils.AbstractMerger;
import com.pbs.middleware.common.pbs.Resources;
import com.pbs.middleware.common.pbs.Walltime;

public class ResourcesMerger extends AbstractMerger<Resources> {

    public static Resources mergeResources(Resources origin, Resources changes) {
        if (origin == null && changes == null) {
            return null;
        }
        if (origin != null && changes == null) {
            return deepCopy(origin);
        } else if (origin == null) {
            return deepCopy(changes);
        }


        Resources merged = deepCopy(origin);

        if (changes.getWalltime() != null) {
            merged.setWalltime(Walltime.from(changes.getWalltime().toString()));
        }

        if (isSet(changes.getCustomResources())) {
            merged.setCustomResources(mergeMap(merged.getCustomResources(), changes.getCustomResources()));
        }

        if (isSet(changes.getChunks())) {
            merged.setChunks(ChunkMerger.mergeChunks(merged.getChunks(), changes.getChunks()));
        }

        return merged;
    }

}
