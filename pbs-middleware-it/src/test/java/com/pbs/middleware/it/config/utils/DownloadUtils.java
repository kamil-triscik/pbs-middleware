package com.pbs.middleware.it.config.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.download.Download;
import com.pbs.middleware.it.config.TestUser;
import cz.muni.ll.middleware.client.authorization.AuthenticationType;
import cz.muni.ll.middleware.client.download.client.DownloadClient;
import cz.muni.ll.middleware.client.download.client.DownloadClientBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_PREFIX;
import static com.pbs.middleware.api.download.ApiConfig.DOWNLOADS_EVENTS;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DownloadUtils {

    public static DownloadClient getClient(int port, String email, String password) {
        return new DownloadClientBuilder("http://localhost:" + port, email, password)
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

    public static Download waitTill(UUID downloadId, DownloadClient client, Predicate<Download> condition) throws InterruptedException {
        Download download = client.getDownload(downloadId);

        while (condition.test(download)) {
            Thread.sleep(3000);
            download = client.getDownload(downloadId);
        }
        return download;
    }

    public static Map<String, String> getEvents(MockMvc mockMvc, UUID domainId, TestUser testUser) throws Exception {
        String res = mockMvc.perform(
                get(DOWNLOADS_PREFIX + DOWNLOADS_EVENTS.replace("{id}", domainId.toString()))
                        .with(httpBasic(testUser.getEmail(), testUser.getPassword())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readValue(res, Map.class);

    }

    public static void getEvents(MockMvc mockMvc, UUID domainId, TestUser testUser, ResultMatcher matcher) throws Exception {
        mockMvc.perform(
                get(DOWNLOADS_PREFIX + DOWNLOADS_EVENTS.replace("{id}", domainId.toString()))
                        .with(httpBasic(testUser.getEmail(), testUser.getPassword())))
                .andDo(print())
                .andExpect(matcher);

    }
}
