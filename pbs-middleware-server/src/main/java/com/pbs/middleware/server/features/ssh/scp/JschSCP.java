package com.pbs.middleware.server.features.ssh.scp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
abstract class JschSCP implements CloseableSCP {

    protected final String login;

    protected final String sshHost;

    protected Session session = null;

    protected Channel channel;

    protected FileInputStream fileInputStream;

    protected abstract void initSshSession() throws SCPException;

    protected void createSession(JSch jSch) throws JSchException {
        String[] host = this.sshHost.split(":");
        this.session = jSch.getSession(this.login, host[0], host.length > 1 ? Integer.parseInt(host[1]) : 22);
    }

    @Override
    public void upload(byte[] data, Path remote) throws SCPException {
        File _lfile = null;

        try {
            this.initSshSession();

            String command = "scp " + "-p" + " -t " + remote.toAbsolutePath();

            session.connect(30000);
            this.channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            _lfile = File.createTempFile("temp", null);
            Files.write(_lfile.toPath(), data);

            String from = _lfile.toPath().toAbsolutePath().toString();

            command = "T" + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += " " + (_lfile.lastModified() / 1000) + " 0\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            long filesize = _lfile.length();
            log.info("SCP uploading {}B", filesize);
            command = "C0644 " + filesize + " ";
            if (from.lastIndexOf('/') > 0) {
                command += from.substring(from.lastIndexOf('/') + 1);
            } else {
                command += from;
            }

            command += "\n";
            out.write(command.getBytes());
            out.flush();

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fileInputStream = new FileInputStream(from);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fileInputStream.read(buf, 0, buf.length);
                if (len <= 0) break;
                out.write(buf, 0, len);
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            int ack = checkAck(in);
            if (ack != 0) {
                throw new SCPException("SCP upload exception. Ack = " + ack);
            }

            out.close();

        } catch (Exception e) {
            throw new SCPException("SCP upload exception", e);
        } finally {
            if (_lfile != null) {
                try {
                    Files.deleteIfExists(_lfile.toPath());
                } catch (IOException e) {
                    log.warn("Temp SCP file removing filed", e);
                }
            }
        }
    }

    @Override
    public com.pbs.middleware.server.features.ssh.scp.File download(Path file) throws SCPException {
        this.initSshSession();


        String command = "scp -f " + file.toString();

        byte[] data = new byte[]{};

        try {
            session.connect(30000);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            long finalSize = 0;
            long filesize = 0L;
            String filename = "";
            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    break;
                }

                // read '0644 '
                in.read(buf, 0, 5);
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ') break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                buf = new byte[1024];
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        filename = new String(buf, 0, i);
                        break;
                    }
                }
                finalSize = filesize;
                log.info("Downloading " + filename + " with size " + filesize + "B");

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                int size;
                while (true) {
                    if (buf.length < filesize) size = buf.length;
                    else size = (int) filesize;
                    size = in.read(buf, 0, size);
                    if (size < 0) {
                        // error
                        break;
                    }
                    data = ArrayUtils.addAll(data, Arrays.copyOfRange(buf, 0, size));
                    filesize -= size;
                    if (filesize == 0L) break;
                }

                int ack = checkAck(in);
                if (ack != 0) {
                    throw new SCPException("SCP download exception. Ack = " + ack);
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }
            log.info("SCP downloaded file {} with size {}B", filename, finalSize);
            return new com.pbs.middleware.server.features.ssh.scp.File(filename, finalSize, data);
        } catch (Exception e) {
            throw new SCPException("SCP download exception", e);
        }
    }

    @Override
    public void close() throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
        }

        ofNullable(channel).filter(Channel::isConnected).ifPresent(Channel::disconnect);
        ofNullable(session).filter(Session::isConnected).ifPresent(Session::disconnect);
    }

    private int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            do {
                c = in.read();
                stringBuilder.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(stringBuilder.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(stringBuilder.toString());
            }
        }
        return b;
    }
}
