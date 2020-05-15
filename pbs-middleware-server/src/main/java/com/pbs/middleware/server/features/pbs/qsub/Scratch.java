package com.pbs.middleware.server.features.pbs.qsub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import static java.util.Optional.ofNullable;

@Data
public class Scratch {

    private ScratchType scratchType = ScratchType.LOCAL;

    private Integer scratchSize;

    private MemoryUnit memoryUnit;

    @JsonIgnore
    public String getMemory() {
        return scratchSize + this.getMemoryUnit().getLabel();
    }

    @JsonIgnore
    public boolean isScratchMemory() {
        return scratchSize != null && scratchSize > 0;
    }

    public Scratch merge(Scratch scratch) {
        if (scratch == null) {
            return this;
        }

        Scratch merged = new Scratch();

        merged.setScratchType(ofNullable(scratch.getScratchType()).orElse(this.getScratchType()));
        merged.setScratchSize(ofNullable(scratch.getScratchSize()).orElse(this.getScratchSize()));
        merged.setMemoryUnit(ofNullable(scratch.getMemoryUnit()).orElse(this.getMemoryUnit()));

        return merged;
    }

}
