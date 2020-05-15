package cz.muni.ll.middleware.client.jobs.storage.configuration;

import cz.muni.ll.middleware.client.jobs.storage.DatabaseStorage;
import cz.muni.ll.middleware.client.jobs.storage.JobStorage;
import cz.muni.ll.middleware.client.jobs.storage.LocalFileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Value("${middleware.storage.enabled:true}")
    private boolean storageEnabled;

    @Value("${middleware.storage.type}")
    private String storageType;

    @Value("${middleware.storage.file.directory:}")
    private String fileDirectory;

    @Value("${middleware.storage.file.template:}")
    private String fileTemplate;

    @Value("${middleware.storage.db.path:}")
    private String dbPath;

    @Value("${middleware.storage.db.init:false}")
    private boolean init;

    @Bean
    public JobStorage jobStorage() {
        if (!storageEnabled) {
            return null;
        }
        if ("FILE".equals(storageType.toUpperCase())) {
            return new LocalFileStorage(fileDirectory, fileTemplate);
        } else if ("DATABASE".equals(storageType.toUpperCase())) {
            if (init) {
                DatabaseStorage.createNewDatabase(dbPath);
            }
            return new DatabaseStorage(dbPath);
        }
        throw new IllegalArgumentException("middleware.storage property could contain only values: [FILE, DATABASE]");
    }

}
