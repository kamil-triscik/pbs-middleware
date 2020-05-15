package com.pbs.middleware.server.features.ssh.shell;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
abstract class JschShell implements CloseableShell {

    protected final String pbsHost;

    protected final String login;


    private final String sshHost;

    protected Session session = null;

    protected ChannelExec channel;

    protected InputStream stdout = null;
    protected InputStream stderr = null;

    protected abstract void initSshSession() throws ShellException;

    protected void createSession(JSch jSch) throws JSchException {
        String[] host = this.sshHost.split(":");
        this.session = jSch.getSession(this.login, host[0], host.length > 1 ? Integer.parseInt(host[1]) : 22);
    }

    @Override
    public Result executeCommand(String command) throws ShellException {
        try {
            log.debug("Executing shell command: " + command);
            this.initSshSession();
            session.connect(3000); // todo move to app. properties
            this.channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ofNullable(pbsHost).ifPresent(pbsHost -> channel.setEnv("PBS_SERVER", pbsHost));
            channel.setInputStream(null);
            stdout = channel.getInputStream();
            stderr = channel.getErrStream();

            channel.connect();
            Result result = new Result(channel.getExitStatus(), IOUtils.toString(new InputStreamReader(stdout)), IOUtils.toString(new InputStreamReader(stderr)));
            log.debug("Sell command response:" + result.toString());
            return result;
        } catch (JSchException exception) {
            throw new ShellException("SSH channel exception: " + exception.getLocalizedMessage());
        } catch (IOException exception) {
            throw new ShellException("SSH IO exception: " + exception.getLocalizedMessage());
        }
    }

    @Override
    public InputStream getInputStream() throws ShellException {
        try {
            return channel != null ? channel.getInputStream() : null;
        } catch (IOException exception) {
            throw new ShellException("SSH IO exception: " + exception.getLocalizedMessage());
        }
    }

    @Override
    public OutputStream getOutputStream() throws ShellException {
        try {
            return channel != null ? channel.getOutputStream() : null;
        } catch (IOException exception) {
            throw new ShellException("SSH IO exception: " + exception.getLocalizedMessage());
        }
    }

    @Override
    public void close() {
        ofNullable(channel).filter(Channel::isConnected).ifPresent(Channel::disconnect);
        ofNullable(session).filter(Session::isConnected).ifPresent(Session::disconnect);
        ofNullable(stdout).ifPresent(stream -> {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("STDOUT stream closing exception", e);
            }
        });
        ofNullable(stderr).ifPresent(stream -> {
            try {
                stream.close();
            } catch (IOException e) {
                log.error("STDERR stream closing exception", e);
            }
        });
    }
}
