package com.pbs.middleware.it.script;

import com.pbs.middleware.api.error.Error;
import com.pbs.middleware.api.error.ErrorResponse;
import com.pbs.middleware.api.script.CreateScript;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.api.script.UpdateScript;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.server.MiddlewareServer;
import java.util.Calendar;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.pbs.middleware.api.error.CommonErrorCode.VALIDATION_ERROR;
import static com.pbs.middleware.api.script.ApiConfig.SCRIPTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
@DisplayName("Update script tests")
public class UpdateScriptIT extends MiddlewareIntegrationTest {


    @Test
    @DisplayName("Valid script update")
    void valid_update_test() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final String name = "script" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.MILLISECOND);
        UpdateScript updateScript = getScript();
        updateScript.setName(name);
        Script updatedScript = sendSuccessfulRequest(updateScript);

        assertNotNull(updatedScript.getId());
        assertEquals(name, updatedScript.getName());
        assertEquals(updateScript.getDescription(), updatedScript.getDescription());
        assertEquals(updateScript.getCode(), updatedScript.getCode());
    }

    @Test
    @DisplayName("Non-existing script update")
    void nonexist_script_test() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final String name = "script" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.MILLISECOND);
        UpdateScript updateScript = getScript();
        updateScript.setName(name);

        final String id = UUID.randomUUID().toString();
        mockMvc.perform(
                get(SCRIPTS + "/" + id)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(
                put(SCRIPTS + "/" + id)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(updateScript))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Name is empty string")
    void empty_name_test() throws Exception {
        final String field = "name";
        final String value = "";
        final String message = "Script name cannot be empty.";

        UpdateScript updateScript = getScript();
        updateScript.setName(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Name is null")
    void null_name_test() throws Exception {
        final String field = "name";
        final String value = null;
        final String message = "Script name cannot be empty.";

        UpdateScript updateScript = getScript();
        updateScript.setName(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Name is longer than 20 characters")
    void long_name_test() throws Exception {
        final String field = "name";
        final String value = "loooooooooooooongName";
        final String message = "Script name cannot be longer than 20 characters.";

        UpdateScript updateScript = getScript();
        updateScript.setName(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Name already exists")
    void name_already_exists_test() throws Exception {
        final String field = "name";
        Calendar calendar = Calendar.getInstance();
        final String value = "script" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.SECOND);
        final String message = "Script name already exists";

        CreateScript newScript = new CreateScript();
        newScript.setName(value);
        newScript.setDescription("description");
        newScript.setCode("code");
        mockMvc.perform(
                post(SCRIPTS)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(newScript))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        UpdateScript updateScript = getScript();
        updateScript.setName(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Description is longer than 100 characters")
    void long_description_test() throws Exception {
        final String field = "description";

        final String value = "looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooongDescr";
        final String message = "Description name cannot be longer than 100 characters.";

        UpdateScript updateScript = getScript();
        updateScript.setDescription(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Code is empty string")
    void empty_code_test() throws Exception {
        final String field = "code";
        final String value = "";
        final String message = "Code cannot be empty.";

        UpdateScript updateScript = getScript();
        updateScript.setCode(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Code is null")
    void null_code_test() throws Exception {
        final String field = "code";
        final String value = null;
        final String message = "Code cannot be empty.";

        UpdateScript updateScript = getScript();
        updateScript.setCode(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @Disabled
    @DisplayName("Code is not valid groovy")
    void invalid_code_test() throws Exception {
        final String field = "code";
        final String value = "null"; //todo
        final String message = "Invalid groovy script";

        UpdateScript updateScript = getScript();
        updateScript.setCode(value);

        ErrorResponse errorResponse = sendRequest(updateScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    private ErrorResponse sendRequest(UpdateScript updateScript) throws Exception {
        String responseBody = mockMvc.perform(
                put(SCRIPTS + "/" + testScript.getId().toString())
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(updateScript))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, ErrorResponse.class);
    }

    private Script sendSuccessfulRequest(UpdateScript updateScript) throws Exception {
        String responseBody = mockMvc.perform(
                put(SCRIPTS + "/" + testScript.getId().toString())
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(updateScript))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, Script.class);
    }

    private UpdateScript getScript() {
        UpdateScript script = new UpdateScript();
        script.setName(testScript.getName());
        script.setDescription(testScript.getDescription());
        script.setCode(testScript.getCode());
        return script;
    }


}
