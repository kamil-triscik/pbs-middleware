package cz.muni.ll.middleware.client.jobs.storage.configuration;

import org.springframework.context.annotation.Import;

@Import({StorageConfiguration.class})
public @interface EnableClientStorage {
}
