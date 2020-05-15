package com.pbs.middleware.server.features.ssh.scp;

import java.nio.file.Path;

public interface SCP {

    void upload(byte[] data, Path remote) throws SCPException;

    File download(Path file) throws SCPException;

}
