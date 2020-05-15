package cz.muni.ll.middleware.client.upload.client;

import com.pbs.middleware.api.upload.Upload;
import cz.muni.ll.middleware.client.rest.exceptions.LLMClientException;
import cz.muni.ll.middleware.client.upload.domain.UploadFileRequest;
import cz.muni.ll.middleware.client.upload.domain.UploadRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.toList;

/**
 * Decorator for UploadClientDescriptor.
 * Created to keep same api for all clients
 */
@RequiredArgsConstructor
public class UploadClient {

    private final UploadClientDescriptor uploadClient;

    public List<UUID> getAllUploads() throws LLMClientException {
        return uploadClient.getAllUploads();
    }

    public UUID uploadData(UploadRequest uploadRequest) {
        com.pbs.middleware.api.upload.UploadRequest dto = toDto(uploadRequest);

        UUID uploadId = uploadRequest.getId() != null
                ? uploadClient.startUpload(uploadRequest.getId(), dto)
                : uploadClient.startUpload(dto);

        // upload multiple with one request

        uploadRequest.getFiles()
                .stream()
                .map(UploadFileRequest::getFile)
                .forEach(file -> uploadClient.uploadFile(uploadId, file));

        return uploadId;

    }

    private com.pbs.middleware.api.upload.UploadRequest toDto(UploadRequest uploadRequest) {
        com.pbs.middleware.api.upload.UploadRequest dto = new com.pbs.middleware.api.upload.UploadRequest();

        dto.setConnection(uploadRequest.getConnection());
        dto.setDestination(uploadRequest.getDestination());
        dto.setEmail(uploadRequest.getEmail());
        dto.setDescription(uploadRequest.getDescription());
        dto.setFiles(
                uploadRequest
                        .getFiles()
                        .stream()
                        .map(req -> new com.pbs.middleware.api.upload.UploadFileRequest(
                                req.getFilename(),
                                req.getRename(),
                                req.getExtract(),
                                req.getSecured()
                        ))
                        .collect(toList())
        );

        return dto;
    }

    public Upload getUpload(UUID id) {
        return uploadClient.getUpload(id);
    }

    public void cancel(UUID id, String reason, boolean removeUploaded) {
        uploadClient.cancel(id, reason, removeUploaded);
    }

    public void cancel(UUID id, String reason) {
        cancel(id, reason, false);
    }

    public void cancel(UUID id, boolean removeUploaded) {
        cancel(id, "undefined", removeUploaded);
    }

    public void cancel(UUID id) {
        cancel(id, "undefined", false);
    }

    public void delete(UUID id) {
        uploadClient.delete(id);
    }

}
