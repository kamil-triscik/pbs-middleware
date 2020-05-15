package com.pbs.middleware.it.config.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.connection.Connection;
import com.pbs.middleware.api.connection.ConnectionType;
import com.pbs.middleware.api.connection.CreateConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConnectionUtils {

    private ConnectionUtils() { }

    public static Connection createConnection(String remoteUrl, String email, String password, MockMvc mockMvc) throws Exception {
        CreateConnection createConnection = new CreateConnection();
        createConnection.setName("conn_" + Calendar.getInstance().get(Calendar.SECOND));
        createConnection.setPassword("root");
        createConnection.setLogin("root");
        createConnection.setType(ConnectionType.PASSWORD_AUTH);
        createConnection.setSshHost(remoteUrl);
        createConnection.setPbsHost(remoteUrl);

        String res = mockMvc.perform(
                post("/api/connections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createConnection))
                        .with(httpBasic(email, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return new ObjectMapper().readValue(res, Connection.class);
    }

    public static List<Connection> getAll(String email, String password, MockMvc mockMvc) throws Exception {

        String res = mockMvc.perform(
                get("/api/connections")
                        .with(httpBasic(email, password)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return new ObjectMapper().readValue(res, new TypeReference<List<Connection>>() { });
    }

}
