package cz.muni.ll.middleware.client.download.client;

import com.pbs.middleware.api.download.Download;
import com.pbs.middleware.api.download.DownloadRequest;
import cz.muni.ll.middleware.client.download.domain.FileContent;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;
import java.util.UUID;

import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_PREFIX;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_CANCEL;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_CONFIRM;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_DELETE;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_GET;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_GET_FILE;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_START;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_START_WITH_ID;

public interface DownloadClient {

    @RequestLine("GET " + DOWNLOADS_PREFIX)
    @Headers("Accept: application/json")
    List<UUID> getAllDownloads() throws LLMClientException;

    @RequestLine("POST " + DOWNLOADS_PREFIX + DOWNLOADS_START)
    @Headers({"Content-type: application/json"})
    UUID startDownload(DownloadRequest downloadRequest);

    @RequestLine("POST " + DOWNLOADS_PREFIX + DOWNLOADS_START_WITH_ID)
    @Headers({"Content-type: application/json"})
    UUID startDownload(DownloadRequest downloadRequest, @Param("id") UUID id);

    @RequestLine("GET " + DOWNLOADS_PREFIX + DOWNLOADS_GET)
    @Headers("Accept: application/json")
    Download getDownload(@Param("id") UUID id);

    @RequestLine("GET " + DOWNLOADS_PREFIX + DOWNLOADS_GET_FILE)
    @Headers("Accept: application/octet-stream")
    FileContent downloadFileContent(@Param("id") UUID id, @Param("filename") String filename);

    @RequestLine("GET " + DOWNLOADS_PREFIX + DOWNLOADS_CONFIRM)
    void confirm(@Param("id") UUID id);

    @RequestLine("POST " + DOWNLOADS_PREFIX + DOWNLOADS_CANCEL)
    void cancel(@Param("id") UUID id);

    @RequestLine("POST " + DOWNLOADS_PREFIX + DOWNLOADS_CANCEL + "?reason={reason}")
    void cancel(@Param("id") UUID id, @Param("reason") String reason);

    @RequestLine("DELETE " + DOWNLOADS_PREFIX + DOWNLOADS_DELETE)
    void delete(@Param("id") UUID id);

}
