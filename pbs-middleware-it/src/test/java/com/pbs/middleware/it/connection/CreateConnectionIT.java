package com.pbs.middleware.it.connection;

import com.pbs.middleware.api.connection.Connection;
import com.pbs.middleware.api.connection.ConnectionType;
import com.pbs.middleware.api.connection.CreateConnection;
import com.pbs.middleware.api.error.Error;
import com.pbs.middleware.api.error.ErrorResponse;
import com.pbs.middleware.it.config.MiddlewareIntegrationTest;
import com.pbs.middleware.server.MiddlewareServer;
import java.util.Calendar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.pbs.middleware.api.connection.ApiConfig.CONNECTIONS;
import static com.pbs.middleware.api.error.CommonErrorCode.VALIDATION_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MiddlewareServer.class
)
@Testcontainers
@DisplayName("Create connection tests")
public class CreateConnectionIT extends MiddlewareIntegrationTest {

    @Test
    @DisplayName("Valid connection creation")
    void valid_create_test() throws Exception {
        CreateConnection createConnection = getConnection();
        Connection connection = sendSuccessfulRequest(createConnection);

        assertNotNull(connection.getId());
        assertEquals(createConnection.getName(), connection.getName());
        assertEquals(createConnection.getDescription(), connection.getDescription());
        assertEquals(createConnection.getType(), connection.getType());
        assertEquals(createConnection.getLogin(), connection.getLogin());
        assertEquals(createConnection.getPassword(), connection.getPassword());
        assertNull(createConnection.getSshKey());
    }

    @Test
    @DisplayName("Name is empty string")
    void empty_name_test() throws Exception {
        final String field = "name";
        final String value = "";
        final String message = "Connection name cannot be empty.";

        CreateConnection createConnection = getConnection();
        createConnection.setName(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
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
        final String message = "Connection name cannot be empty.";

        CreateConnection createConnection = getConnection();
        createConnection.setName(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
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
        final String message = "Connection name cannot be longer than 20 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setName(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
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
        final String value = "connectionX";
        final String message = "Connection name already exists";

        CreateConnection createConnection = getConnection();
        createConnection.setName(value);

        sendSuccessfulRequest(createConnection);

        createConnection = getConnection();
        createConnection.setName(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
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
        final String message = "Connection description cannot be longer than 100 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setDescription(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Type is null")
    void null_type_test() throws Exception {
        final String field = "type";
        final ConnectionType value = null;
        final String message = "Connection type is mandatory.";

        CreateConnection createConnection = getConnection();
        createConnection.setType(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("SshHost is empty string")
    void empty_ssh_host_test() throws Exception {
        final String field = "sshHost";
        final String value = "";
        final String message = "Connection ssh host is mandatory.";

        CreateConnection createConnection = getConnection();
        createConnection.setSshHost(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("SshHost is null")
    void null_ssh_host_test() throws Exception {
        final String field = "sshHost";
        final String value = null;
        final String message = "Connection ssh host is mandatory.";

        CreateConnection createConnection = getConnection();
        createConnection.setSshHost(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("SshHost is longer than 100 characters")
    void long_ssh_host_test() throws Exception {
        final String field = "sshHost";
        final String longc = "looooooooooooooooooooooooooooooooooooooooooooooong";
        final String value = longc + longc + "1";
        final String message = "SSH host cannot be longer than 100 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setSshHost(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("PbsHost is empty string")
    void empty_pbs_host_test() throws Exception {
        final String field = "pbsHost";
        final String value = "";
        final String message = "Pbs host cannot be empty.";

        CreateConnection createConnection = getConnection();
        createConnection.setPbsHost(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("PbsHost is null")
    void null_pbs_host_test() throws Exception {
        final String field = "pbsHost";
        final String value = null;
        final String message = "Pbs host cannot be empty.";

        CreateConnection createConnection = getConnection();
        createConnection.setPbsHost(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("PbsHost is longer than 100 characters")
    void long_pbs_host_test() throws Exception {
        final String field = "pbsHost";
        final String longc = "looooooooooooooooooooooooooooooooooooooooooooooong";
        final String value = longc + longc + "1";
        final String message = "Pbs host cannot be longer than 100 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setPbsHost(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Login is longer than 100 characters")
    void long_login_test() throws Exception {
        final String field = "login";
        final String longc = "looooooooooooooooooooooooooooooooooooooooooooooong";
        final String value = longc + longc + "1";
        final String message = "Login cannot be longer than 100 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setLogin(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Password is longer than 100 characters")
    void long_password_test() throws Exception {
        final String field = "password";
        final String longc = "looooooooooooooooooooooooooooooooooooooooooooooong";
        final String value = longc + longc + "1";
        final String message = "Password cannot be longer than 100 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setPassword(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    @Test
    @DisplayName("Ssh key is longer than 300 characters")
    void long_ssh_key_test() throws Exception {
        final String field = "sshKey";
        final String longC = "looooooooooooooooooooooooooooooooooooooooooooooong";
        final String value = longC+longC+longC+longC+longC+longC+"1";
        final String message = "Path to SSH key cannot be longer than 300 characters.";

        CreateConnection createConnection = getConnection();
        createConnection.setSshKey(value);

        ErrorResponse errorResponse = sendRequest(createConnection);
        assertFalse(errorResponse.getErrors().isEmpty());
        assertEquals(1, errorResponse.getErrors().size());
        Error error = errorResponse.getErrors().get(0);
        assertEquals(VALIDATION_ERROR.toString(), error.getCode());
        assertEquals(field, error.getPath());
        assertEquals(value, error.getValue());
        assertEquals(message, error.getMessage());
    }

    private ErrorResponse sendRequest(CreateConnection createConnection) throws Exception {
        String responseBody = mockMvc.perform(
                post(CONNECTIONS)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(createConnection))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, ErrorResponse.class);
    }

    private Connection sendSuccessfulRequest(CreateConnection createConnection) throws Exception {
        String responseBody = mockMvc.perform(
                post(CONNECTIONS)
                        .with(httpBasic(getAdmin().getEmail(), getAdmin().getPassword()))
                        .content(mapper.writeValueAsString(createConnection))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(responseBody, Connection.class);
    }

    private CreateConnection getConnection() {
        CreateConnection connection = new CreateConnection();
        final Calendar calendar = Calendar.getInstance();
        connection.setName("conn_" + calendar.get(Calendar.MINUTE) + "-" + calendar.get(Calendar.MILLISECOND));
        connection.setDescription("description");
        connection.setType(ConnectionType.PASSWORD_AUTH);
        connection.setLogin("user");
        connection.setPassword("password");
        connection.setSshHost("sshHost");
        connection.setPbsHost("pbsHost");
        return connection;
    }

}
