package com.pbs.middleware.it.config;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.api.script.Script;
import com.pbs.middleware.it.config.containers.MongoContainer;
import com.pbs.middleware.it.config.containers.PsqlContainer;
import com.pbs.middleware.it.config.containers.RemoteServerContainer;
import java.util.UUID;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@Sql(scripts = "classpath:init_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(initializers = {
        PsqlContainer.Initializer.class,
        MongoContainer.Initializer.class,
        RemoteServerContainer.Initializer.class
})
public abstract class MiddlewareIntegrationTest {

    protected final ObjectMapper mapper = new ObjectMapper();

    // see init_db.sql in resources
    protected TestUser user1 = new TestUser("user1@localhost", "1234");
    protected TestUser user2 = new TestUser("user2@localhost", "1234");

    protected Script testScript = new Script() {{
        setId(UUID.fromString("c45ecf15-776d-4a71-b478-464ae576fca5"));
        setName("testScript");
        setDescription("testScriptDescription");
        setCode("println()");
    }};

    @Autowired
    protected MockMvc mockMvc;

    @LocalServerPort
    protected int serverPort;

    @Value("${middleware.server.security.default.admin.email}")
    public String email;

    @Value("${middleware.server.security.default.admin.password}")
    public String password;

    @Value("${remote.host}:${remote.port}")
    public String remoteUrl;

    protected TestUser getAdmin() {
        return new TestUser(email, password);
    }

}
