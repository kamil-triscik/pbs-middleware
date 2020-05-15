package com.pbs.middleware.it.script;

import com.pbs.middleware.api.error.Error;
import com.pbs.middleware.api.error.ErrorResponse;
import com.pbs.middleware.api.script.CreateScript;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.server.MiddlewareServer;
import java.util.Calendar;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
@DisplayName("Create script tests")
public class CreateScriptIT extends MiddlewareIntegrationTest {

    @Test
    @DisplayName("Valid script creation")
    void valid_create_test() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        final String name = "script" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.MILLISECOND);
        CreateScript createScript = getScript();
        createScript.setName(name);
        Script newScript = sendSuccessfulRequest(createScript);

        assertNotNull(newScript.getId());
        assertEquals(name, newScript.getName());
        assertEquals(createScript.getDescription(), newScript.getDescription());
        assertEquals(createScript.getCode(), newScript.getCode());
    }

    @Test
    @DisplayName("Name is empty string")
    void empty_name_test() throws Exception {
        final String field = "name";
        final String value = "";
        final String message = "Script name cannot be empty.";

        CreateScript createScript = getScript();
        createScript.setName(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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

        CreateScript createScript = getScript();
        createScript.setName(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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

        CreateScript createScript = getScript();
        createScript.setName(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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
        final String value = "scriptX";
        final String message = "Script name already exists";

        CreateScript createScript = getScript();
        createScript.setName(value);

        sendSuccessfulRequest(createScript);

        createScript = getScript();
        createScript.setName(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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
        final String message = "Script description name cannot be longer than 100 characters.";

        CreateScript createScript = getScript();
        createScript.setDescription(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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
        final String message = "Script code cannot be empty.";

        CreateScript createScript = getScript();
        createScript.setCode(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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
        final String message = "Script code cannot be empty.";

        CreateScript createScript = getScript();
        createScript.setCode(value);

        ErrorResponse errorResponse = sendRequest(createScript);
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

        CreateScript createScript = getScript();
        createScript.setCode(value);

        ErrorResponse errorResponse = sendRequest(createScript);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    private ErrorResponse sendRequest(CreateScript createScript) throws Exception {
        String responseBody = mockMvc.perform(
                post(SCRIPTS)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(createScript))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, ErrorResponse.class);
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
