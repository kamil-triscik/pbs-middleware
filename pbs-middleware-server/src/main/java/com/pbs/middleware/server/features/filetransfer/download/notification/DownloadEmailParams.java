package com.pbs.middleware.server.features.filetransfer.download.notification;

import com.pbs.middleware.server.features.notification.email.configuration.TemplateType;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class DownloadEmailParams {

    private UUID downloadId;

    private String recipient;

    private String subject;

    private TemplateType templateType;

    private Map<String, Object> parameters;

    private Map<String, String> exceptions;

}
