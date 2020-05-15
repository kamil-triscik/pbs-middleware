package com.pbs.middleware.server.common.domain;

import lombok.Data;

@Data
public class Id<T> {

    private T domainId;

}
