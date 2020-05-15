package com.pbs.middleware.it.script;

import com.pbs.middleware.api.script.CreateScript;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.server.MiddlewareServer;
import java.util.Calendar;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
@DisplayName("Delete script tests")
public class DeleteScriptIT extends MiddlewareIntegrationTest {

    @Test
    @DisplayName("Valid script deletion")
    void valid_create_test() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final String name = "script" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.MILLISECOND);
        CreateScript createScript = getScript();
        createScript.setName(name);
        Script newScript = sendSuccessfulRequest(createScript);

        mockMvc.perform(
                delete(SCRIPTS + "/" + newScript.getId())
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword())))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(
                get(SCRIPTS + "/" + newScript.getId())
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deleting non existing script")
    void delete_non_existing_test() throws Exception {
        final String id = UUID.randomUUID().toString();

        mockMvc.perform(
                get(SCRIPTS + "/" + id)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(
                delete(SCRIPTS + "/" + id)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();


    }


    private Script sendSuccessfulRequest(CreateScript createScript) throws Exception {
        String responseBody = mockMvc.perform(
                post(SCRIPTS)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(createScript))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, Script.class);
    }

    private CreateScript getScript() {
        CreateScript script = new CreateScript();
        script.setName("script1");
        script.setDescription("description");
        script.setCode("code");
        return script;
    }

}
