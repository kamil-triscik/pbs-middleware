package com.pbs.middleware.common.pbs;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resources {

    private Walltime walltime;

    private Map<String, String> customResources;

    private List<Chunk> chunks;

}
