package com.pbs.middleware.server.common.storage.temporary;

public interface TemporaryEntity {

    String getId();

    byte[] getData();

    long getDataLength();

}
