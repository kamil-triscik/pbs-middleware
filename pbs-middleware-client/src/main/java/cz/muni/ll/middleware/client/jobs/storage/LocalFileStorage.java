package cz.muni.ll.middleware.client.jobs.storage;

import cz.muni.ll.middleware.client.jobs.Job;
import cz.muni.ll.middleware.client.jobs.JobState;
import cz.muni.ll.middleware.client.jobs.storage.exception.AlreadyExistsException;
import cz.muni.ll.middleware.client.jobs.storage.exception.DeleteFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.LoadingException;
import cz.muni.ll.middleware.client.jobs.storage.exception.NotFoundException;
import cz.muni.ll.middleware.client.jobs.storage.exception.SaveFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.UpdateFailedException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class LocalFileStorage implements JobStorage {

    private static Logger log = LoggerFactory.getLogger(LocalFileStorage.class);

    private final static String PBS_LINE = "pbs";
    private final static String MIDDLEWARE_LINE = "middleware";

    private final Function<File, Job> fileToJob = file -> {
        Job job = new Job();

        job.setId(getId(file));
        job.setMiddlewareId(getMiddleWareId(file));
        job.setPbsJobId(getJobId(file));
        job.setState(getState(file));

        return job;
    };

    private final String storeDirectoryPath;
    private final File storeDirectory;
    private final String nameTemplate;

    public LocalFileStorage(String storeDirectoryPath, String nameTemplate) {
        this.storeDirectoryPath = storeDirectoryPath;
        this.storeDirectory = new File(storeDirectoryPath);
        this.nameTemplate = ofNullable(nameTemplate)
                .filter(temp -> !temp.replace(" ", "").isEmpty())
                .map(template -> template + "_").orElse("");

        if (!this.storeDirectory.exists()) {
            throw new IllegalArgumentException(format("Provided directory [%s] does not exists!", storeDirectoryPath));
        }

        if (!this.storeDirectory.isDirectory()) {
            throw new IllegalArgumentException(format("Provided directory [%s] is not directory!", storeDirectoryPath));
        }

        if (!this.storeDirectory.canRead()) {
            throw new IllegalArgumentException(format("Missing read permission for directory %s!", storeDirectoryPath));
        }

        if (!this.storeDirectory.canWrite()) {
            throw new IllegalArgumentException(format("Missing write permission for directory %s!", storeDirectoryPath));
        }
    }

    @Override
    public void store(Job job) throws SaveFailedException, AlreadyExistsException {
        Path file = Paths.get(getStoreDirectoryPath(), getFileName(job.getId(), job.getState()));
        if (Files.exists(file)) {
            throw new AlreadyExistsException(file);
        }
        String data = PBS_LINE + "=" + job.getPbsJobId() + "\n" + MIDDLEWARE_LINE + "=" + job.getMiddlewareId().toString();
        try {
            Files.write(file, data.getBytes());
        } catch (IOException e) {
            throw new SaveFailedException(e);
        }
    }

    @Override
    public Optional<Job> find(String id) {
        return findFileForId(id).map(fileToJob);
    }

    @Override
    public void setRunning(Job job) {
        setRunning(job.getId());
    }

    @Override
    public void setRunning(String id) {
        setFileState(id, JobState.RUNNING);
    }

    @Override
    public void setFinished(Job job) {
        setFinished(job.getId());
    }

    @Override
    public void setFinished(String id) {
        setFileState(id, JobState.FINISHED);
    }

    @Override
    public void setState(Job job, JobState state) {
        setState(job.getId(), state);
    }

    @Override
    public void setState(String id, JobState state) {
        setFileState(id, state);
    }

    @Override
    public List<Job> getAll() {
        File[] files = getStoreDirectory().listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(files)
                .map(fileToJob)
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> getAllByState(JobState state) {
        File[] files = getStoreDirectory().listFiles((dir, name) -> name.contains(state.toString()));
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(files)
                .map(fileToJob)
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> getAllRunning() {
        return getAllByState(JobState.RUNNING);
    }

    @Override
    public boolean deleteById(String id) {
        return findFileForId(id)
                .map(file ->
                {
                    try {
                        return Files.deleteIfExists(file.toPath());
                    } catch (IOException e) {
                        throw new DeleteFailedException(e);
                    }
                }).orElse(false);
    }

    @Override
    public Long deleteAll() {
        File[] files = getStoreDirectory().listFiles();
        if (files == null) {
            throw new LoadingException("Can not load files!");
        }
        return deleteFiles(files);
    }

    @Override
    public Long deleteAllWithState(JobState state) {
        File[] files = getStoreDirectory().listFiles((dir, name) -> name.contains(state.toString()));
        if (files == null) {
            throw new LoadingException("Can not load files!");
        }
        return deleteFiles(files);
    }

    private String getStoreDirectoryPath() {
        return this.storeDirectoryPath;
    }

    private File getStoreDirectory() {
        return this.storeDirectory;
    }

    private String getFileName(String id, JobState state) {
        return id + "_" + this.nameTemplate + state.toString();
    }

    private String getId(File file) {
        return file.getName().split("_")[0];
    }

    private UUID getMiddleWareId(File file) {
        try {
            return FileUtils
                    .readLines(file, StandardCharsets.US_ASCII)
                    .stream()
                    .filter(line -> line.startsWith(MIDDLEWARE_LINE))
                    .findFirst()
                    .map(line -> line.substring(MIDDLEWARE_LINE.length() + 1))
                    .map(UUID::fromString)
                    .orElseThrow(() -> new LoadingException("Middleware id loading failed"));
        } catch (IOException e) {
            throw new LoadingException("Can not load middleware id!", e);
        }
    }

    private JobState getState(File file) {
        String[] name = file.getName().split("_");
        return JobState.valueOf(name[name.length - 1]);
    }

    private String getJobId(File file) {
        try {
            return FileUtils
                    .readLines(file, StandardCharsets.US_ASCII)
                    .stream()
                    .filter(line -> line.startsWith(PBS_LINE))
                    .findFirst()
                    .map(line -> line.substring(PBS_LINE.length()))
                    .orElseThrow(() -> new LoadingException("PBS id loading failed"));
        } catch (IOException e) {
            throw new LoadingException("Can not load job id!", e);
        }
    }

    private Optional<File> findFileForId(String id) {
        File[] files = getStoreDirectory().listFiles((dir, name) -> name.startsWith(id));
        if (files == null) {
            throw new LoadingException("Can not load files!");
        }
        if (files.length > 1) {
            throw new IllegalStateException("Founded files duplicities for job " + id);
        } else if (files.length == 1) {
            return of(files[0]);
        } else {
            return empty();
        }
    }

    public Long deleteFiles(File[] files) {
        AtomicLong removedFilesCount = new AtomicLong();
        Arrays.stream(files).map(File::toPath).forEach(path -> {
            try {
                if (Files.deleteIfExists(path)) {
                    removedFilesCount.getAndIncrement();
                }
            } catch (IOException e) {
                log.error(format("File[%s] deleting failed!", path.toString()), e);
            }
        });

        if (removedFilesCount.get() != files.length) {
            throw new DeleteFailedException(format("Not all files was deleted: Founded: %d, Deleted: %d", files.length, removedFilesCount.get()));
        }

        return removedFilesCount.get();
    }

    private void setFileState(String id, JobState state) {
        Path file = findFileForId(id)
                .map(File::toPath)
                .orElseThrow(() -> new NotFoundException(id));
        String newName = getFileName(id, state);
        try {
            Files.move(file, file.resolveSibling(newName));
        } catch (IOException e) {
            throw new UpdateFailedException(e);
        }

    }
}
