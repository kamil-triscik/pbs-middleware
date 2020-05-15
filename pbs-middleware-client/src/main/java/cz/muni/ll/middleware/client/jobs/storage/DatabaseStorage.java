package cz.muni.ll.middleware.client.jobs.storage;

import cz.muni.ll.middleware.client.jobs.Job;
import cz.muni.ll.middleware.client.jobs.JobState;
import cz.muni.ll.middleware.client.jobs.storage.exception.DatabaseConnectionFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.DatabaseCreationFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.DeleteFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.LoadingException;
import cz.muni.ll.middleware.client.jobs.storage.exception.SaveFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.TableCreationFailedException;
import cz.muni.ll.middleware.client.jobs.storage.exception.UpdateFailedException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
public class DatabaseStorage implements JobStorage {

    private static String TABLE_NAME = "jobs";
    private static String ID_COL = "id";
    private static String MIDDLEWARE_ID_COL = "middleware_id";
    private static String JOB_ID_COL = "job_id";
    private static String STATE_COL = "state";

    private final String path;

    public DatabaseStorage(String path) {
        this.path = path;
    }

    private Connection getConnection() {
        String url = "jdbc:sqlite:" + this.path;
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException(e);
        }
    }

    @Override
    public void store(Job job) {
        String sql = format("INSERT INTO %s(%s, %s, %s, %s) VALUES(?,?,?,?)",
                TABLE_NAME, ID_COL, MIDDLEWARE_ID_COL, JOB_ID_COL, STATE_COL);

        try (Connection connection = this.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, job.getId());
            prepareStatement.setString(2, job.getMiddlewareId().toString());
            prepareStatement.setString(3, job.getPbsJobId());
            prepareStatement.setString(4, job.getState().toString());
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SaveFailedException(e);
        }
    }

    @Override
    public Optional<Job> find(String id) {
        String sql = format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, ID_COL, id);

        try (Connection connection = this.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            Job job = null;
            while (rs.next()) {
                job = mapToJob(rs);
            }
            return Optional.ofNullable(job);
        } catch (SQLException e) {
            throw new LoadingException("Jobs loading failed", e);
        }
    }

    @Override
    public void setRunning(Job job) {
        setRunning(job.getId());
    }

    @Override
    public void setRunning(String id) {
        setState(id, JobState.RUNNING);
    }

    @Override
    public void setFinished(Job job) {
        setFinished(job.getId());
    }

    @Override
    public void setFinished(String id) {
        setState(id, JobState.FINISHED);
    }

    @Override
    public void setState(Job job, JobState state) {
        setState(job.getId(), state);
    }

    @Override
    public void setState(String id, JobState state) {
        String sql = format("UPDATE %s SET %s = ? WHERE %s = ?", TABLE_NAME, STATE_COL, ID_COL);

        try (Connection connection = this.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, state.toString());
            prepareStatement.setString(2, id);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UpdateFailedException(e);
        }
    }

    @Override
    public List<Job> getAll() {
        return getAllWithState("SELECT * FROM " + TABLE_NAME);
    }

    @Override
    public List<Job> getAllByState(JobState state) {
        return getAllWithState(format("SELECT * FROM %s WHERE %s = '%s'", TABLE_NAME, STATE_COL, state.toString()));
    }

    private List<Job> getAllWithState(String sql) {
        List<Job> jobs = new LinkedList<>();
        try (Connection connection = this.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Job job = mapToJob(rs);
                jobs.add(job);
            }
        } catch (SQLException e) {
            throw new LoadingException("Jobs loading failed", e);
        }
        return jobs;
    }

    @Override
    public List<Job> getAllRunning() {
        return getAllByState(JobState.RUNNING);
    }

    @Override
    public boolean deleteById(String id) {
        String sql = format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, ID_COL);

        try (Connection connection = this.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, id);
            return prepareStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DeleteFailedException(e);
        }
    }

    @Override
    public Long deleteAll() {
        String sql = format("DELETE FROM %s", TABLE_NAME);

        try (Connection connection = this.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            return (long) prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteFailedException(e);
        }
    }

    @Override
    public Long deleteAllWithState(JobState state) {
        String sql = format("DELETE FROM %s WHERE %s = ?", TABLE_NAME, STATE_COL);

        try (Connection connection = this.getConnection();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {

            prepareStatement.setString(1, state.toString());
            return (long) prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteFailedException(e);
        }
    }

    private Job mapToJob(ResultSet resultSet) throws SQLException {
        Job job = new Job();

        job.setId(resultSet.getString(ID_COL).replace("\n", ""));
        job.setMiddlewareId(UUID.fromString(resultSet.getString(MIDDLEWARE_ID_COL).replace("\n", "")));
        job.setPbsJobId(resultSet.getString(JOB_ID_COL));
        job.setState(JobState.valueOf(resultSet.getString(STATE_COL)));

        return job;
    }

    public static void createNewDatabase(String path) {

        String url = "jdbc:sqlite:" + path;

        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                log.info("The driver name is " + meta.getDriverName());
                log.info("A new database has been created.");
                createNewTable(connection);
            } else {
                throw new DatabaseCreationFailedException("Database not created!");
            }
        } catch (SQLException e) {
            throw new DatabaseCreationFailedException(e);
        }
    }

    private static void createNewTable(Connection connection) {
        String sql = format("CREATE TABLE IF NOT EXISTS %s ("
                + "    %s VARCHAR(100) PRIMARY KEY,"
                + "    %s VARCHAR(36) NOT NULL UNIQUE,"
                + "    %s VARCHAR(100) NOT NULL UNIQUE,"
                + "    %s varchar(12)"
                + ");", TABLE_NAME, ID_COL, MIDDLEWARE_ID_COL, JOB_ID_COL, STATE_COL);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new TableCreationFailedException(e);
        }
    }
}
