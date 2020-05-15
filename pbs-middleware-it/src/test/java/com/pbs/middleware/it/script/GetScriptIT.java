package com.pbs.middleware.it.script;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.server.MiddlewareServer;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
@DisplayName("Fetching script tests")
public class GetScriptIT extends MiddlewareIntegrationTest {

    @Test
    @DisplayName("Valid script fetching")
    void valid_create_test() throws Exception {
        String responseBody = mockMvc.perform(
                get(SCRIPTS)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Script> scripts = mapper.readValue(responseBody, new TypeReference<>() {
        });

        Script script = scripts.get(0);

        String responseBody2 = mockMvc.perform(
                get(SCRIPTS + "/" + script.getId())
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Script script2 = mapper.readValue(responseBody2, Script.class);

        assertEquals(script, script2);

    }

    @Test
    @DisplayName("Fetching non existing script")
    void delete_non_existing_test() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(
                get(SCRIPTS + "/" + id)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


    }

}
