package com.pbs.middleware.server.features.ssh.shell;

import java.io.Closeable;

interface CloseableShell extends Shell, Closeable {
}
