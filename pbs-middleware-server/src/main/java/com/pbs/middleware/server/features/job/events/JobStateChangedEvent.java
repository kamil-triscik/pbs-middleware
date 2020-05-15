package com.pbs.middleware.server.features.job.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pbs.middleware.server.common.utils.Optional;
import com.pbs.middleware.server.features.job.domain.State;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.pbs.middleware.server.common.utils.Optional.ofNullable;

/**
 * Event used when job state change.
 *
 * @author <a href="mailto:kamil.triscik@gmail.com">Kamil Triscik</a>
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "Job")
public class JobStateChangedEvent extends JobEvent {

    private static final String STATE_KEY = "state";

    private static final String SUB_STATE_KEY = "subState";

    private static final String COMMENT_KEY = "comment";

    private static final String EXEC_HOST_KEY = "execHost";

    private static final String EXEC_VNODE_KEY = "execVNode";

    private static final String RUN_COUNT_KEY = "runCount";

    private Map<String, String> changes;

    public JobStateChangedEvent(UUID domainId) {
        super(domainId);
        this.changes = new HashMap<>();
    }

    @JsonIgnore
    public void setState(State state) {
        changes.put(STATE_KEY, state.getPbsLabel());
    }

    @JsonIgnore
    public Optional<State> getState() {
        return ofNullable(changes.get(STATE_KEY)).map(State::valueOf);
    }

    @JsonIgnore
    public void setSubstate(Integer subState) {
        changes.put(SUB_STATE_KEY, subState.toString());
    }

    @JsonIgnore
    public Optional<Integer> getSubstate() {
        return ofNullable(changes.get(SUB_STATE_KEY)).map(Integer::valueOf);
    }

    @JsonIgnore
    public void setComment(String comment) {
        changes.put(COMMENT_KEY, comment);
    }

    @JsonIgnore
    public Optional<String> getComment() {
        return ofNullable(changes.get(COMMENT_KEY));
    }

    @JsonIgnore
    public void setExecHost(String execHost) {
        changes.put(EXEC_HOST_KEY, execHost);
    }

    @JsonIgnore
    public Optional<String> getExecHost() {
        return ofNullable(changes.get(EXEC_HOST_KEY));
    }

    @JsonIgnore
    public void setExecVNode(String execVNode) {
        changes.put(EXEC_VNODE_KEY, execVNode);
    }

    @JsonIgnore
    public Optional<String> getExecVNode() {
        return ofNullable(changes.get(EXEC_VNODE_KEY));
    }

    @JsonIgnore
    public void setRunCount(Integer runCount) {
        changes.put(RUN_COUNT_KEY, runCount.toString());
    }

    @JsonIgnore
    public Optional<Integer> getRunCount() {
        return ofNullable(changes.get(RUN_COUNT_KEY)).map(Integer::valueOf);
    }
}
