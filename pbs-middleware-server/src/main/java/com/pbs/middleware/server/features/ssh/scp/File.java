package com.pbs.middleware.server.features.ssh.scp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class File {

    String name;

    Long size;

    byte[] data;

}
