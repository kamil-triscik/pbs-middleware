package com.pbs.middleware.it.download;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.download.DownloadRequest;
import com.pbs.middleware.api.error.ErrorResponse;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.server.MiddlewareServer;
import cz.muni.ll.middleware.client.download.client.DownloadClient;
import cz.muni.ll.middleware.client.download.domain.DownloadRequestBuilder;
import cz.muni.ll.middleware.client.rest.exceptions.nonrecoverable.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_PREFIX;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_START;
import static com.pbs.middleware.api.error.CommonErrorCode.VALIDATION_ERROR;
import static com.pbs.middleware.it.config.utils.DownloadUtils.getClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
public class DownloadValidationIT extends MiddlewareIntegrationTest {

    @Test
    @DisplayName("Null connection")
    void null_connection_test() throws Exception {
        DownloadRequest request = new DownloadRequestBuilder("connection")
                .file("file.txt")
                .folder("/home/user")
                .remove(true)
                .build();
        request.setConnection(null);

        final String responseBody = mockMvc.perform(
                post(DOWNLOADS_PREFIX + DOWNLOADS_START)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(httpBasic(user1.getEmail(), user1.getPassword())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapper.readValue(responseBody, ErrorResponse.class);

        assertEquals(1, errorResponse.getErrors().size());
        assertEquals(VALIDATION_ERROR.toString(), errorResponse.getErrors().get(0).getCode());
        assertEquals("connection", errorResponse.getErrors().get(0).getPath());
        assertNull(errorResponse.getErrors().get(0).getValue());
        assertEquals("Connection does not exists", errorResponse.getErrors().get(0).getMessage());
    }

    @Test
    @DisplayName("Connection does not exists")
    void connection_does_not_exists_test() throws JsonProcessingException {
        final DownloadClient client = getClient(serverPort, user1.getEmail(), user1.getPassword());

        final String connection = "random_connection";
        DownloadRequest request = new DownloadRequestBuilder(connection)
                .file("file.txt")
                .folder("/home/user")
                .remove(true)
                .build();

        BadRequestException exception = assertThrows(BadRequestException.class, () -> client.startDownload(request));
        ErrorResponse errorResponse = mapper.readValue(exception.getMessage(), ErrorResponse.class);

        assertEquals(1, errorResponse.getErrors().size());
        assertEquals(VALIDATION_ERROR.toString(), errorResponse.getErrors().get(0).getCode());
        assertEquals("connection", errorResponse.getErrors().get(0).getPath());
        assertEquals(connection, errorResponse.getErrors().get(0).getValue());
        assertEquals("Connection does not exists", errorResponse.getErrors().get(0).getMessage());
    }

}
