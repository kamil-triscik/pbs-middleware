package com.pbs.middleware.it.config.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.connection.Connection;
import com.pbs.middleware.api.upload.Upload;
import com.pbs.middleware.it.config.TestUser;
import cz.muni.ll.middleware.client.authorization.AuthenticationType;
import cz.muni.ll.middleware.client.upload.client.UploadClient;
import cz.muni.ll.middleware.client.upload.client.UploadClientBuilder;
import cz.muni.ll.middleware.client.upload.domain.UploadRequest;
import cz.muni.ll.middleware.client.upload.domain.UploadRequestBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_PREFIX;
import static com.pbs.middleware.api.upload.ApiConfig.UPLOADS_GET_EVENTS;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadUtils {

    public static UploadClient getClient(int port, String email, String password) {
        return new UploadClientBuilder("http://localhost:" + port, email, password)
                .authenticationType(AuthenticationType.BASIC)
                .build();
    }

    public static File getFile(final String content) throws IOException {
        File file = File.createTempFile("test_file_", ".txt");

        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();

        return file;
    }

    public static UploadRequest getUploadRequest(Connection connection, File file, String destination) {
        return new UploadRequestBuilder(connection.getName(), destination)
                .description("test upload")
                .notify(false)
                .buildFile(file)
                    .build()
                .build();
    }

    public static Upload waitTill(UUID uploadId, UploadClient client, Predicate<Upload> condition) throws InterruptedException {
        Upload upload = client.getUpload(uploadId);

        while (condition.test(upload)) {
            Thread.sleep(3000);
            upload = client.getUpload(uploadId);
        }
        return upload;
    }

    public static Map<String, String> getEvents(MockMvc mockMvc, UUID domainId, TestUser testUser) throws Exception {
        String res = mockMvc.perform(
                get(UPLOADS_PREFIX + UPLOADS_GET_EVENTS.replace("{id}", domainId.toString()))
                        .with(httpBasic(testUser.getEmail(), testUser.getPassword())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readValue(res, Map.class);

    }

    public static void getEvents(MockMvc mockMvc, UUID domainId, TestUser testUser, ResultMatcher matcher) throws Exception {
        mockMvc.perform(
                get(UPLOADS_PREFIX + UPLOADS_GET_EVENTS.replace("{id}", domainId.toString()))
                        .with(httpBasic(testUser.getEmail(), testUser.getPassword())))
                .andDo(print())
                .andExpect(matcher);

    }
}
