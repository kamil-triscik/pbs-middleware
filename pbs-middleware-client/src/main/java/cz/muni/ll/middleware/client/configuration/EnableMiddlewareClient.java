package cz.muni.ll.middleware.client.configuration;

import cz.muni.ll.middleware.client.download.DownloadClientConfiguration;
import cz.muni.ll.middleware.client.jobs.JobClientConfiguration;
import cz.muni.ll.middleware.client.upload.UploadClientConfiguration;
import org.springframework.context.annotation.Import;

@Import({
        JobClientConfiguration.class,
        DownloadClientConfiguration.class,
        UploadClientConfiguration.class})
public @interface EnableMiddlewareClient {
}
