package com.pbs.middleware.api.template;

import com.pbs.middleware.common.validations.NotEmptyString;
import com.pbs.middleware.common.validations.ValidMap;
import com.pbs.middleware.common.validations.ValidStringMap;
import io.swagger.annotations.ApiModel;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Valid
//@ValidChunk
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Chunk", description = "DTO representing chunk object.")
public class Chunk {

    @NotNull(message = "Chunk id can not be null")
    @NotEmptyString(message = "Chunk id can not be blank")
    private String id;

    @Size(min = 1, message = "min count of nodes is 1")
    private Long count;

    @Size(min = 1, message = "min count of CPUs is 1")
    private Long ncpus;

    @NotBlank
    private String mem;

    @ValidMap(fieldName = "customResources")
    @ValidStringMap(fieldName = "customResources")
    private Map<String, String> customResources;
}
