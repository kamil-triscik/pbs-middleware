package com.pbs.middleware.server.features.pbs.qstat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using= QstatResponseDeserializer.class)
public class QstatResponse {

    private final Long timestamp;

    private final String pbsVersion;

    private final String pbsServer;

    private final Map<String, Qstat> qstats;

}
