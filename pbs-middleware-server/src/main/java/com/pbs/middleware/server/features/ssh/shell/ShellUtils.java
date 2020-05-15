package com.pbs.middleware.server.features.ssh.shell;

import java.nio.file.Path;

public class ShellUtils {

    private ShellUtils() { }

    public static String removeFileCmd(Path file) {
        return removeFileCmd(file.toString());
    }

    public static String removeFileCmd(String file) {
        return " rm " + file;
    }

}
