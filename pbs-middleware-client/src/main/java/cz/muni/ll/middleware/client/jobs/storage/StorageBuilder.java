package cz.muni.ll.middleware.client.jobs.storage;

public class StorageBuilder {

    public static StorageBuilder get() {
        return new StorageBuilder();
    }

    private StorageBuilder() {
    }

    public LocalFileStorageBuilder fileStorage(String storeDirectory) {
        return new LocalFileStorageBuilder(storeDirectory);
    }

    public DatabaseStorageBuilder databaseStorage(String url) {
        return new DatabaseStorageBuilder(url);
    }

    public static class LocalFileStorageBuilder {

        private final String storeDirectory;
        private String template = null;

        LocalFileStorageBuilder(String storeDirectory) {
            this.storeDirectory = storeDirectory;
        }

        public LocalFileStorageBuilder template(String template) {
            this.template = template;
            return this;
        }

        public JobStorage build() {
            return new LocalFileStorage(storeDirectory, template);
        }
    }

    public static class DatabaseStorageBuilder {

        private final String path;

        private boolean init = false;

        public DatabaseStorageBuilder(String path) {
            this.path = path;
        }

        public DatabaseStorageBuilder init() {
            this.init = true;
            return this;
        }

        public JobStorage build() {
            if (init) {
                DatabaseStorage.createNewDatabase(path);
            }
            return new DatabaseStorage(path);
        }
    }

}
