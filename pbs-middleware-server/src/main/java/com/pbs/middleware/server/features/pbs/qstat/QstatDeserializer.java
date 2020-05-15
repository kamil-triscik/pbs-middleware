package com.pbs.middleware.server.features.pbs.qstat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.pbs.middleware.server.features.job.domain.State;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.pbs.middleware.server.common.utils.Optional.ofNullable;

public class QstatDeserializer extends StdDeserializer<Qstat> {

    private final ObjectMapper mapper = new ObjectMapper();

    public QstatDeserializer() {
        this(null);
    }

    public QstatDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Qstat deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode node = p.getCodec().readTree(p);

        return node.has("jobId") ? deserializeMiddlewareVersion(node) : processJob(node);
    }

    private Qstat processJob(JsonNode node) {
        Qstat jobStatus = new Qstat();

        jobStatus.setJobName(ofNullable(node.get("Job_Name")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setJobOwner(ofNullable(node.get("Job_Owner")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setJobState(State.of(ofNullable(node.get("job_state")).map(it -> it.asText(null)).orElse(null)));
        jobStatus.setQueue(ofNullable(node.get("queue")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setExitStatus(ofNullable(node.get("Exit_status")).map(it -> it.asInt(-1)).orElse(-1));
        jobStatus.setJobDir(ofNullable(node.get("jobdir")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setErrorPath(ofNullable(node.get("Error_Path")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setOutputPath(ofNullable(node.get("Output_Path")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setComment(ofNullable(node.get("comment")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setSubstate(ofNullable(node.get("substate")).map(it -> it.asInt(-1)).orElse(-1));
        jobStatus.setExecHost(ofNullable(node.get("exec_host")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setExecVnode(ofNullable(node.get("exec_vnode")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setRunCount(ofNullable(node.get("run_count")).map(it -> it.asInt(-1)).orElse(-1));
        jobStatus.setResourcesUsed(getMapObject("resources_used", node, mapper));
        jobStatus.setResourcesList(getMapObject("Resource_List", node, mapper));
        jobStatus.setVariableList(getMapObject("Variable_List", node, mapper));

        if (jobStatus.getResourcesUsed().get("walltime") != null) {
            jobStatus.setWalltimes(new LinkedList<>() {{
                add((String) jobStatus.getResourcesUsed().get("walltime"));
            }});
        } else if (jobStatus.getWalltimes() == null) {
            jobStatus.setWalltimes(new LinkedList<>());
        }

        if (jobStatus.getResourcesUsed().get("cput") != null) {
            jobStatus.setCput(new LinkedList<>() {{
                add((String) jobStatus.getResourcesUsed().get("cput"));
            }});
        } else if (jobStatus.getCput() == null) {
            jobStatus.setCput(new LinkedList<>());
        }

        return jobStatus;
    }

    private Qstat deserializeMiddlewareVersion(JsonNode node) {
        Qstat jobStatus = new Qstat();

        jobStatus.setTimestamp(ofNullable(node.get("timestamp")).map(it -> it.asLong(-1L)).orElse(-1L));
        jobStatus.setPbsServer(ofNullable(node.get("pbsServer")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setJobId(ofNullable(node.get("jobId")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setJobName(ofNullable(node.get("jobName")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setJobOwner(ofNullable(node.get("jobOwner")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setJobState(State.valueOf(ofNullable(node.get("jobState")).map(it -> it.asText(null)).orElse(null)));
        jobStatus.setQueue(ofNullable(node.get("queue")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setExitStatus(ofNullable(node.get("exitStatus")).map(it -> it.asInt(-1)).orElse(-1));
        jobStatus.setJobDir(ofNullable(node.get("jobDir")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setErrorPath(ofNullable(node.get("errorPath")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setOutputPath(ofNullable(node.get("outputPath")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setComment(ofNullable(node.get("comment")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setSubstate(ofNullable(node.get("substate")).map(it -> it.asInt(-1)).orElse(-1));
        jobStatus.setExecHost(ofNullable(node.get("execHost")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setExecVnode(ofNullable(node.get("execVnode")).map(it -> it.asText(null)).orElse(null));
        jobStatus.setRunCount(ofNullable(node.get("runCount")).map(it -> it.asInt(-1)).orElse(-1));
        jobStatus.setResourcesUsed(getMapObject("resourcesUsed", node, mapper));
        jobStatus.setResourcesList(getMapObject("resourceList", node, mapper));
        jobStatus.setVariableList(getMapObject("variableList", node, mapper));

        jobStatus.setWalltimes(getListObject("walltimes", node, mapper));
        jobStatus.setCput(getListObject("cput", node, mapper));

        return jobStatus;
    }

    private Map<String, Object> getMapObject(String property, JsonNode node, ObjectMapper mapper) {
        if (node.get(property) == null || node.get(property).isNull()) {
            return new HashMap<>();
        }
        return (Map<String, Object>) mapper.convertValue(node.get(property), Map.class);
    }

    private List<String> getListObject(String property, JsonNode node, ObjectMapper mapper) {
        if (node.get(property) == null || node.get(property).isNull()) {
            return new LinkedList<>();
        }
        return (List<String>) mapper.convertValue(node.get(property), List.class);

    }
}
