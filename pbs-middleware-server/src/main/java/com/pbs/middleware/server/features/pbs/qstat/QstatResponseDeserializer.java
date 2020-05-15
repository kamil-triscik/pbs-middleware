package com.pbs.middleware.server.features.pbs.qstat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QstatResponseDeserializer extends StdDeserializer<QstatResponse> {

    private final ObjectMapper mapper = new ObjectMapper();

    public QstatResponseDeserializer() {
        this(null);
    }

    public QstatResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public QstatResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        QstatResponse qstatResponse = new QstatResponse(
                node.get("timestamp").asLong(-1L),
                node.get("pbs_version").asText("<UNKNOWN>"),
                node.get("pbs_server").asText("<UNKNOWN>"),
                new HashMap<>()
        );

        if (!node.get("Jobs").isEmpty()) {
            node.get("Jobs").fieldNames().forEachRemaining(jobId -> {
                try {
                    Qstat qstat = mapper.treeToValue(node.get("Jobs").get(jobId), Qstat.class);

                    qstat.setTimestamp(qstatResponse.getTimestamp());
                    qstat.setPbsServer(qstatResponse.getPbsServer());

                    qstatResponse.getQstats().put(jobId, qstat);
                } catch (JsonProcessingException e) {
                    log.error("Failed to deserialize job " + jobId, e);
                }
            });
        }

        return qstatResponse;
    }
}
