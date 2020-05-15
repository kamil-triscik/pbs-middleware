package com.pbs.middleware.server.features.connection;

public class ConnectionConfig {

    public static final String TABLE_NAME = "connection";

    public static final String DISCRIMINATOR_COLUMN = "type";

    public static final String GENERAL_CONNECTION = "GENERAL";
    public static final String PASSWORD_CONNECTION = "PASSWORD";
    public static final String SSH_KEY_CONNECTION = "SSH_KEY";


}
