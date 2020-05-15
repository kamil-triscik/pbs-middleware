package com.pbs.middleware.server.common.storage.temporary.local;

import com.pbs.middleware.server.common.storage.temporary.TemporaryEntity;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LocalTemporaryEntity implements TemporaryEntity {

    private final String id;

    private final byte[] data;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public long getDataLength() {
        return getData() != null ? getData().length : 0;
    }
}
