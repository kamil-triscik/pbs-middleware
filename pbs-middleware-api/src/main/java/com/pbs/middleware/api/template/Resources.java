package com.pbs.middleware.api.template;

import com.pbs.middleware.common.validations.NotEmptyString;
import com.pbs.middleware.common.validations.ValidCollection;
import com.pbs.middleware.common.validations.ValidMap;
import com.pbs.middleware.common.validations.ValidStringMap;
import com.pbs.middleware.common.validations.ValidWalltime;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.Data;

@Data
@Valid
@ApiModel(value = "Resources", description = "Template DTO representing jpb resources.")
public class Resources {

    @NotEmptyString
    @ValidWalltime
    private String walltime;

    @ValidMap(fieldName = "customResources")
    @ValidStringMap(fieldName = "customResources")
    private Map<String, String> customResources;

    @Valid
//    @UniqueChunks
    @ValidCollection(fieldName = "chunks")
    private List<Chunk> chunks;

}
