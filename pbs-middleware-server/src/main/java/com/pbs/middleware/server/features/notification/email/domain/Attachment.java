package com.pbs.middleware.server.features.notification.email.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Attachment {

    private String fileName;

    private String content;

    private String contentType;

}
