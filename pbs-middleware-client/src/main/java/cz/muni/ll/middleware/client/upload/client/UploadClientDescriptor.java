package cz.muni.ll.middleware.client.upload.client;

import com.pbs.middleware.api.upload.Upload;
import com.pbs.middleware.api.upload.UploadRequest;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_PREFIX;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_CANCEL;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_DELETE;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_FILE;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_FILE_MULTIPLE;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_GET;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_START;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_START_WITH_ID;

public interface UploadClientDescriptor {

    @RequestLine("GET " + UPLOADS_PREFIX)
    @Headers("Accept: application/json")
    List<UUID> getAllUploads() throws LLMClientException;

    @RequestLine("POST " + UPLOADS_PREFIX + UPLOADS_START)
    @Headers({"Content-type: application/json"})
    UUID startUpload(UploadRequest uploadRequest);

    @RequestLine("POST " + UPLOADS_PREFIX + UPLOADS_START_WITH_ID)
    @Headers({"Content-type: application/json"})
    UUID startUpload(@Param("id") UUID id, UploadRequest uploadRequest);

    @RequestLine("POST " + UPLOADS_PREFIX + UPLOADS_FILE)
    @Headers({"Content-type: multipart/form-data"})
    void uploadFile(@Param("id") UUID id,
                    @Param("file") File file);

    @RequestLine("POST " + UPLOADS_PREFIX + UPLOADS_FILE_MULTIPLE)
    @Headers({"Content-type: multipart/form-data"})
    UUID uploadFiles(@Param("id") UUID id,
                     @Param("files") List<File> files);

    @RequestLine("GET " + UPLOADS_PREFIX + UPLOADS_GET)
    @Headers("Accept: application/json")
    Upload getUpload(@Param("id") UUID id);

    @RequestLine("POST " + UPLOADS_PREFIX + UPLOADS_CANCEL + "?reason={reason}&removeUploaded={removeUploaded}")
    void cancel(
            @Param("id") UUID id,
            @Param("reason") String reason,
            @Param("removeUploaded") boolean removeUploaded);

    @RequestLine("DELETE " + UPLOADS_PREFIX + UPLOADS_DELETE)
    void delete(@Param("id") UUID id);

}
