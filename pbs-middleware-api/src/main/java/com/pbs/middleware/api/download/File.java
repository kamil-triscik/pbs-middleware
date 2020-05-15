package com.pbs.middleware.api.download;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "File", description = "DTO representing downloaded file")
public class File {

    @ApiModelProperty(name = "path", value = "File absolute path on source server")
    private String path;

    @ApiModelProperty(name = "size", value = "File size in number of bytes")
    private Long size;

    @ApiModelProperty(name = "hash", value = "File hash")
    private String hash;

    @ApiModelProperty(name = "state", value = "Files download state", allowableValues = "UNKNOWN, IN_PROGRESS, PREPARED, FAILED, FINISHED")
    private DownloadFileState state;

    /**
     * Method extract filename from path field.
     *
     * @return filename.
     */
    @JsonIgnore
    public String getFilename() {
        return Paths.get(path).getFileName().toString();
    }

}