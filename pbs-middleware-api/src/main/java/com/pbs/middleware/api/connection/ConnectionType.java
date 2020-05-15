package com.pbs.middleware.api.connection;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ConnectionType", description = "Enumeration of available connection types.")
public enum ConnectionType {

    /**
     * Type of connection which does not require any authentication.
     */
    LOCAL,
    /**
     * Type of connection using standard login + password authentication.
     */
    PASSWORD_AUTH,
    /**
     * Type of connection using SSH key.
     */
    SSH_KEY_AUTH;

}
