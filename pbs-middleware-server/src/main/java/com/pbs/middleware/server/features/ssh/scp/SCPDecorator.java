package com.pbs.middleware.server.features.ssh.scp;

import java.io.IOException;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class SCPDecorator implements SCP {

    private final CloseableSCP scp;

    @Override
    public void upload(byte[] data, Path remote) throws SCPException {
        try(scp) {
            this.scp.upload(data, remote);
        } catch (IOException exception) {
            throw new SCPException("SCP IO exception", exception);
        }
    }

    @Override
    public File download(Path file) throws SCPException {
        try(scp) {
            return this.scp.download(file);
        } catch (IOException exception) {
            throw new SCPException("SCP IO exception", exception);
        }
    }
}
