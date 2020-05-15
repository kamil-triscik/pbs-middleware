package com.pbs.middleware.it.config.containers;

import lombok.Getter;

public enum ContainerType {

    PSQL("psql_container"),
    MONGO("mongo_container"),
    SERVER("server_container");

    @Getter
    final private String resourceName;

    ContainerType(String resourceName) {
        this.resourceName = resourceName;
    }
}
