package cz.muni.ll.middleware.client.jobs.domain.submitRequest;

import com.pbs.middleware.api.job.JobSubmit;
import com.pbs.middleware.common.pbs.Chunk;
import com.pbs.middleware.common.pbs.Resources;
import com.pbs.middleware.common.pbs.Walltime;
import cz.muni.ll.middleware.client.rest.exceptions.NotSupported;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class SubmitJobBuilder {

    private String templateId = null;

    private String group = null;

    private String jobName = null;

    private String connection = null;

    private String handleStatusScript = null;

//    private String jobStateCheckInterval = null;

    private Resources resources = null;

    private String queue = null;

    private Map<String, String> variables = new HashMap<>();

    private String workdir = null;

    private String qsubPrefix = null;

    private String jobScript = null;

    private String qsubSuffix = null;

    private String stdout = null;

    private String stderr = null;

    private List<String> arguments = new LinkedList<>();

    /**
     * Set id of job template used for submitting nad handling job.
     *
     * @param       templateId  d of job template.
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code templateId} is {@code null}
     * @exception   IllegalArgumentException in case {@code templateId} is blank string
     */
    public SubmitJobBuilder templateId(String templateId) {
        if (templateId == null) {
            throw new IllegalArgumentException("templateId can not be null");
        }
        if (templateId.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("templateId can not be empty string");
        }
        this.templateId = templateId;
        return this;
    }

    /**
     * Set job name. Job name is optional.
     *
     * @param       jobName name of job which will be submitted.
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code jobName} is {@code null}
     * @exception   IllegalArgumentException in case {@code jobName} is blank string
     */
    public SubmitJobBuilder name(String jobName) {
        if (jobName == null) {
            throw new IllegalArgumentException("jobName can not be null");
        }
        if (jobName.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("jobName can not be empty string");
        }
        this.jobName = jobName;
        return this;
    }

    /**
     * Set job group. Jobs can be aggregated into groups. Setting job group is not mandatory.
     *
     * @param       group name of job group.
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code group} is {@code null}
     * @exception   IllegalArgumentException in case {@code group} is blank string
     */
    public SubmitJobBuilder group(String group) {
        if (group == null) {
            throw new IllegalArgumentException("group can not be null");
        }
        if (group.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("group can not be empty string");
        }
        this.group = group;
        return this;
    }

    /**
     * Set id of connection which contain info how to connect to the server, where job should be launched.
     * Mandatory in case, templateId is not set.
     *
     * @param       connection  id of connection
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code connection} is {@code null}
     * @exception   IllegalArgumentException in case {@code connection} is blank string
     */
    public SubmitJobBuilder connection(String connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection can not be null");
        }
        if (connection.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("connection can not be empty string");
        }
        this.connection = connection;
        return this;
    }

    /**
     * Set id of handling script which will be used by middleware for handling changes of submitted job status.
     * Optional.
     *
     * @param       handleStatusScript  id of handle status script
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code handleStatusScript} is {@code null}
     * @exception   IllegalArgumentException in case {@code handleStatusScript} is blank string
     */
    public SubmitJobBuilder handleStatusScript(String handleStatusScript) {
        if (handleStatusScript == null) {
            throw new IllegalArgumentException("handleScript can not be null");
        }
        if (handleStatusScript.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("handleScript can not be empty string");
        }
        this.handleStatusScript = handleStatusScript;
        return this;
    }

    /**
     * Set job state check interval. It interval in seconds how often should middleware check the status of submitted job.
     * Optional. In case interval is not set, server default interval will be used.
     * In case interval is lower than default server interval, default server interval will be used.
     * <p>
     * Provided value will be transformed to the Duration object.
     *
     * @param       jobStateCheckInterval  job state check interval which will be
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code jobStateCheckInterval} is {@code null}
     * @exception   IllegalArgumentException in case {@code jobStateCheckInterval} is blank string
     * @exception   IllegalArgumentException in case {@code jobStateCheckInterval} is in invalid duration string format
     * @see         java.time.Duration#parse(CharSequence)
     */
    @Deprecated
    @NotSupported(reason = "Deprecated. Not implemented on server side!")
    public SubmitJobBuilder jobStateCheckInterval(String jobStateCheckInterval) {
        if (handleStatusScript == null) {
            throw new IllegalArgumentException("Job state check interval can not be null");
        }
        if (handleStatusScript.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("handleScript can not be empty string");
        }
        try {
            return jobStateCheckInterval(Duration.parse(jobStateCheckInterval));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid duration provided for job state check interval");
        }
    }

    /**
     * Set job state check interval. It interval how often should middleware check the status of submitted job.
     * Optional. In case interval is not set, server default interval will be used.
     * In case interval is lower than default server interval, default server interval will be used.
     *
     * @param       jobStateCheckInterval  job state check interval which will be
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code jobStateCheckInterval} is {@code null}
     * @exception   IllegalArgumentException in case {@code jobStateCheckInterval} hsd negative value
     * @exception   IllegalArgumentException in case {@code jobStateCheckInterval} has zero value
     */
    @Deprecated
    @NotSupported(reason = "Deprecated. Not implemented on server side!")
    public SubmitJobBuilder jobStateCheckInterval(Duration jobStateCheckInterval) {
        if (jobStateCheckInterval == null) {
            throw new IllegalArgumentException("Job state check interval can not be null");
        }
        if (jobStateCheckInterval.isNegative() || jobStateCheckInterval.isZero()) {
            throw new IllegalArgumentException("Job state check interval can not be negative or equals to 0!");
        }
//        this.jobStateCheckInterval = jobStateCheckInterval.toString();
        return this;
    }

    /**
     * Set job queue. If we want to specified, to which queue we want to new submit job. Optional.
     *
     * @param       queue  jobs queue
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code queue} is {@code null}
     * @exception   IllegalArgumentException in case {@code queue} is blank string
     */
    public SubmitJobBuilder queue(String queue) {
        if (queue == null) {
            throw new IllegalArgumentException("queue can not be null");
        }
        if (queue.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("queue can not be empty string");
        }
        this.queue = queue;
        return this;
    }

    /**
     * Associates the specified variable value with the specified variable key in variables map.
     * Variables are used for building QSUB command. Optional.
     *
     * @param       key variable key with which the specified variable value is to be associated
     * @param       value variable value to be associated with the specified variable key
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code key} is {@code null}
     * @exception   IllegalArgumentException in case {@code value} is {@code null}
     * @exception   IllegalArgumentException in case {@code key} is blank string
     * @exception   IllegalArgumentException in case {@code value} is blank string
     */
    public SubmitJobBuilder variable(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("variable key can not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("variable value can not be null");
        }
        if (key.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("variable key can not be empty string");
        }
        if (value.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("variable value can not be empty string");
        }
        if (this.variables == null) {
            this.variables = new HashMap<>();
        }
        variables.put(key, value);
        return this;
    }

    /**
     * Associates the specified variable value with the specified variable key in variables map.
     * Variables are used for building QSUB command. Optional.
     *
     * @param       variables new variables map to be added to variables map
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code variables} is {@code null}
     * @exception   IllegalArgumentException in case {@code variables.keySet()} contain {@code null}
     * @exception   IllegalArgumentException in case {@code variables.keySet()} contain blank string
     * @exception   IllegalArgumentException in case {@code variables.values()} contain {@code null}
     * @exception   IllegalArgumentException in case {@code variables.values()} contain blank string
     */
    public SubmitJobBuilder variables(Map<String, String> variables) {
        if (variables == null) {
            throw new IllegalArgumentException("variables map can not be null");
        }
        if (variables.keySet().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("variables map can not contain null key");
        }
        if (variables.keySet().stream().map(key -> key.replace(" ", "")).anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("variables map can not contain empty as key");
        }
        if (variables.values().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("variables map can not contain null value");
        }
        if (variables.values().stream().map(key -> key.replace(" ", "")).anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("variables map can not contain empty value");
        }
        if (this.variables == null) {
            this.variables = new HashMap<>();
        }
        this.variables.putAll(variables);

        return this;
    }

    /**
     * Server directory in which job will be submitted. Optional.
     *
     * @param       workdir  job working directory
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code workdir} is {@code null}
     * @exception   IllegalArgumentException in case {@code workdir} is blank string
     */
    public SubmitJobBuilder workingDirectory(String workdir) {
        if (workdir == null) {
            throw new IllegalArgumentException("workdir can not be null");
        }
        if (workdir.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("workdir can not be empty string");
        }
        this.workdir = workdir;
        return this;
    }

    /**
     * Set qsub command prefix. Optional.
     * <p>
     * Added to enable user launch some (init) logic, before job script will be launched.
     * {@code ". /etc/profile && "} for example.
     *
     * @param       qsubPrefix  QSUB command prefix
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code qsubPrefix} is {@code null}
     * @exception   IllegalArgumentException in case {@code qsubPrefix} is blank string
     */
    public SubmitJobBuilder qsubPrefix(String qsubPrefix) {
        if (qsubPrefix == null) {
            throw new IllegalArgumentException("qsubPrefix can not be null");
        }
        if (qsubPrefix.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("qsubPrefix can not be empty string");
        }
        this.qsubPrefix = qsubPrefix;
        return this;
    }

    /**
     * Set submitted job script.
     * Mandatory in case, templateId is not set.
     *
     * @param       jobScript  submitted job script.
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code jobScript} is {@code null}
     * @exception   IllegalArgumentException in case {@code jobScript} is blank string
     */
    public SubmitJobBuilder jobScript(String jobScript) {
        if (jobScript == null) {
            throw new IllegalArgumentException("jobScript can not be null");
        }
        if (jobScript.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("jobScript can not be empty string");
        }
        this.jobScript = jobScript;
        return this;
    }

    /**
     * Set qsub command suffix. Optional.
     * <p>
     * Added to enable user launch some (post) logic, after job script finished.
     * {@code " && echo hello > /tmp/a"} for example.
     *
     * @param       qsubSuffix  QSUB command suffix
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code qsubSuffix} is {@code null}
     * @exception   IllegalArgumentException in case {@code qsubSuffix} is blank string
     */
    public SubmitJobBuilder qsubSuffix(String qsubSuffix) {
        if (qsubSuffix == null) {
            throw new IllegalArgumentException("qsubSuffix can not be null");
        }
        if (qsubSuffix.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("qsubSuffix can not be empty string");
        }
        this.qsubSuffix = qsubSuffix;
        return this;
    }

    /**
     * Path to file into which standard output of launched job script should be redirected.
     *
     * @param       stdout  STDOUT file path
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code stdout} is {@code null}
     * @exception   IllegalArgumentException in case {@code stdout} is blank string
     */
    public SubmitJobBuilder stdout(String stdout) {
        if (stdout == null) {
            throw new IllegalArgumentException("stdout can not be null");
        }
        if (stdout.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("stdout can not be empty string");
        }
        this.stdout = stdout;
        return this;
    }

    /**
     * Path to file into which standard error output from launched job script should be redirected.
     *
     * @param       stderr  STDERR file path
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code stderr} is {@code null}
     * @exception   IllegalArgumentException in case {@code stderr} is blank string
     */
    public SubmitJobBuilder stderr(String stderr) {
        if (stderr == null) {
            throw new IllegalArgumentException("stderr can not be null");
        }
        if (stderr.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("stderr can not be empty string");
        }
        this.stderr = stderr;
        return this;
    }

    /**
     * Set Qsub argument.
     *
     * @param       argument  qsub argument
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code argument} is {@code null}
     * @exception   IllegalArgumentException in case {@code argument} is blank string
     */
    public SubmitJobBuilder argument(String argument) {
        if (argument == null) {
            throw new IllegalArgumentException("argument can not be null");
        }
        if (argument.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("argument can not be empty string");
        }
        if (this.arguments == null) {
            this.arguments = new LinkedList<>();
        }
        this.arguments.add(argument);
        return this;
    }

    /**
     * Set Qsub arguments.
     *
     * @param       arguments  qsub arguments collection
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code arguments} is {@code null}
     * @exception   IllegalArgumentException in case {@code arguments} contain {@code null}
     * @exception   IllegalArgumentException in case {@code arguments} contain blank string
     */
    public SubmitJobBuilder arguments(List<String> arguments) {
        if (arguments == null) {
            throw new IllegalArgumentException("arguments can not be null");
        }
        if(arguments.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("arguments can not contain null");
        }
        if(arguments.stream().map(arg -> arg.replace(" ", "")).anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("arguments can not contain empty string");
        }
        if (this.arguments == null) {
            this.arguments = new LinkedList<>();
        }
        this.arguments.addAll(arguments);
        return this;
    }

    /**
     * Set Qsub walltime. Expected time, for how long job will be running.
     *
     * @param       weeks  the number of weeks
     * @param       days  the number of days
     * @param       hours  the number of hours
     * @param       minutes  the number of minutes
     * @param       seconds  the number of weeks
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code weeks} is negative value
     * @exception   IllegalArgumentException in case {@code days} is negative value
     * @exception   IllegalArgumentException in case {@code hours} is negative value
     * @exception   IllegalArgumentException in case {@code minutes} is negative value
     * @exception   IllegalArgumentException in case {@code seconds} is negative value
     */
    public SubmitJobBuilder walltime(int weeks, int days, int hours, int minutes, int seconds) {
        return walltime(new Walltime(weeks, days, hours, minutes, seconds));
    }

    /**
     * Set Qsub walltime. Expected time, for how long job will be running.
     *
     * @param       walltime object containing representing walltime
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code walltime} is {@code null}
     */
    public SubmitJobBuilder walltime(Walltime walltime) {
        if (walltime == null) {
            throw new IllegalArgumentException("walltime can not be null");
        }
        if (resources == null) {
            this.resources = new Resources();
        }
        this.resources.setWalltime(walltime);
        return this;
    }

    /**
     * Associates the specified custom resource value with the specified key in custom resources map.
     * Custom resources are used for building of QSUB command. Optional.
     *
     * @param       key custom resource key with which the specified custom resource value is to be associated
     * @param       value custom resource value to be associated with the specified custom resource key
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code key} is {@code null}
     * @exception   IllegalArgumentException in case {@code value} is {@code null}
     * @exception   IllegalArgumentException in case {@code key} is blank string
     * @exception   IllegalArgumentException in case {@code value} is blank string
     */
    public SubmitJobBuilder customResource(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("custom resource key can not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("custom resource value can not be null");
        }
        if (key.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("custom resource key can not be empty string");
        }
        if (value.replace(" ", "").isEmpty()) {
            throw new IllegalArgumentException("custom resource value can not be empty string");
        }
        if (resources == null) {
            this.resources = new Resources();
        }
        if (this.resources.getCustomResources() == null) {
            this.resources.setCustomResources(new HashMap<>());
        }
        this.resources.getCustomResources().put(key, value);
        return this;
    }

    /**
     * Associates the specified custom resource value with the specified key in custom resources map.
     * Custom resources are used for building of QSUB command. Optional.
     *
     * @param       customResources new custom resources map to be added to custom resources map
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code customResources} is {@code null}
     * @exception   IllegalArgumentException in case {@code customResources.keySet()} contain {@code null}
     * @exception   IllegalArgumentException in case {@code customResources.keySet()} contain blank string
     * @exception   IllegalArgumentException in case {@code customResources.values()} contain {@code null}
     * @exception   IllegalArgumentException in case {@code customResources.values()} contain blank string
     */
    public SubmitJobBuilder customResources(Map<String, String> customResources) {
        if (customResources == null) {
            throw new IllegalArgumentException("custom resources map can not be null");
        }
        if (customResources.keySet().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("custom resources can not contain null key");
        }
        if (customResources.keySet().stream().map(key -> key.replace(" ", "")).anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("custom resources can not contain empty as key");
        }
        if (customResources.values().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("custom resources can not contain null value");
        }
        if (customResources.values().stream().map(key -> key.replace(" ", "")).anyMatch(String::isEmpty)) {
            throw new IllegalArgumentException("custom resources map can not contain empty value");
        }
        if (resources == null) {
            this.resources = new Resources();
        }
        if (this.resources.getCustomResources() == null) {
            this.resources.setCustomResources(new HashMap<>());
        }
        this.resources.getCustomResources().putAll(customResources);
        return this;
    }

    /**
     * This method create ChunkBuilder associated with SubmitJobBuilder for adding chunk into collection of chunks.
     * After {@link SubmitJobBuilder.ChunkBuilder#build()}
     * is called, reference to current a submitJobBuilder is returned.
     *
     * @param       id new chunk id;
     * @return      ChunkBuilder instance.
     * @exception   IllegalArgumentException in case {@code id} is {@code null}
     * @exception   IllegalArgumentException in case {@code id} is negative value
     */
    public ChunkBuilder chunk(Integer id) {
        if (id == null) {
            throw new IllegalStateException("Chunk id can not be null!");
        }
        if (id < 0) {
            throw new IllegalStateException("Chunk id can not be negative value!");
        }
        return chunk(id.toString());
    }

    /**
     * This method create ChunkBuilder associated with SubmitJobBuilder for adding chunk into collection of chunks.
     * After {@link SubmitJobBuilder.ChunkBuilder#build()}
     * is called, reference to current a submitJobBuilder is returned.
     *
     * @param       id new chunk id;
     * @return      ChunkBuilder instance.
     * @exception   IllegalArgumentException in case {@code id} is {@code null}
     * @exception   IllegalArgumentException in case {@code id} is empty string
     */
    public ChunkBuilder chunk(String id) {
        if (id == null) {
            throw new IllegalStateException("Chunk id can not be null!");
        }
        if (id.replace("", " ").isEmpty()) {
            throw new IllegalStateException("Chunk id can not be empty string!");
        }
        return new ChunkBuilder(this, id);
    }

    /**
     * Method build {@code SubmitRequest} from values setup by builder.
     *
     * @return      SubmitRequest object.
     * @exception   IllegalArgumentException in case {@code template} and {@code connection} are {@code null}. At least must be set.
     * @exception   IllegalArgumentException in case {@code template} and {@code jobScript} are {@code null}. At least must be set.
     */
    public JobSubmit build() {
        if (templateId == null) {
            if (connection == null) {
                throw new IllegalStateException("Connection is mandatory if template id is not set!");
            }
            if (jobScript == null) {
                throw new IllegalStateException("Job script is mandatory if template id is not set!");
            }
        }

        if (this.resources == null) {
            this.resources = new Resources();
        }

        if (this.resources.getCustomResources() == null) {
            this.resources.setCustomResources(new HashMap<>());
        }

        return JobSubmit
                .builder()
                .templateId(this.templateId)
                .resources(this.resources)
                .queue(this.queue)
                .variables(this.variables)
                .workdir(this.workdir)
                .qsubPrefix(this.qsubPrefix)
                .jobScript(this.jobScript)
                .qsubSuffix(this.qsubSuffix)
                .stdout(this.stdout)
                .stderr(this.stderr)
                .arguments(this.arguments)
                .group(this.group)
                .jobName(this.jobName)
                .connection(this.connection)
                .handleStatusScript(this.handleStatusScript)
//                .jobStateCheckInterval(this.jobStateCheckInterval)
                .build();
    }

    /**
     * Add chunk object to the list of chunks.
     *
     * @param       chunk object, which will be added into collection of chunks
     * @return      a reference to this object.
     * @exception   IllegalArgumentException in case {@code chunk} is {@code null}
     * @exception   IllegalArgumentException in case collection of chunks already contain chunk with {@code chunk.getId()}
     */
    public SubmitJobBuilder addChunk(Chunk chunk) {
        if (chunk == null) {
            throw new IllegalStateException("Chunk can not be null!");
        }
        if (resources == null) {
            this.resources = new Resources();
        }
        if (this.resources.getChunks() == null) {
            this.resources.setChunks(new LinkedList<>());
        }
        if (this.resources.getChunks().stream().anyMatch(it -> it.getId().equals(chunk.getId()))) {
            throw new IllegalStateException("Chunk with id \"" + chunk.getId() + "\" already exists!");
        }
        this.resources.getChunks().add(chunk);
        return this;
    }

    public static class ChunkBuilder {

        private final SubmitJobBuilder jobBuilder;

        private final String id;

        private Long count = null;

        private Long ncpus = null;

        private String mem = null;

        private Map<String, String> customResources = new HashMap<>();

        ChunkBuilder(SubmitJobBuilder jobBuilder, String id) {
            this.jobBuilder = jobBuilder;
            this.id = id;
        }

        /**
         * Set chunk nodes count value.
         *
         * @param       count number of used nodes
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code count} is {@code null}
         * @exception   IllegalArgumentException in case {@code count} is negative value
         */
        public ChunkBuilder count(Integer count) {
            count(count.longValue());
            return this;
        }

        /**
         * Set chunk nodes count value.
         *
         * @param       count number of used nodes
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code count} is {@code null}
         * @exception   IllegalArgumentException in case {@code count} is negative value
         */
        public ChunkBuilder count(Long count) {
            if (count == null) {
                throw new IllegalArgumentException("Count can not be null");
            }
            if (count < 0) {
                throw new IllegalArgumentException("Count must be positive number");
            }
            this.count = count;
            return this;
        }

        /**
         * Set chunk CPUs count value.
         *
         * @param       ncpus number of used CPUs
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code ncpus} is {@code null}
         * @exception   IllegalArgumentException in case {@code ncpus} is negative value
         */
        public ChunkBuilder ncpus(Integer ncpus) {
            ncpus(ncpus.longValue());
            return this;
        }

        /**
         * Set chunk CPUs count value.
         *
         * @param       ncpus number of used CPUs
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code ncpus} is {@code null}
         * @exception   IllegalArgumentException in case {@code ncpus} is negative value
         */
        public ChunkBuilder ncpus(Long ncpus) {
            if (ncpus == null) {
                throw new IllegalArgumentException("ncpus can not be null");
            }
            if (ncpus < 1) {
                throw new IllegalArgumentException("Number of CPUs must be positive number");
            }
            this.ncpus = ncpus;
            return this;
        }

        /**
         * Set chunk request memory amount.
         *
         * @param       mem requested memory
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code mem} is {@code null}
         * @exception   IllegalArgumentException in case {@code mem} is empty string
         * @exception   IllegalArgumentException in case invalid {@code mem} value
         * @see <a href="https://linux.die.net/man/7/pbs_resources_linux">PBS resources</a>
         */
        public ChunkBuilder mem(String mem) {
            if (mem == null) {
                throw new IllegalArgumentException("mem can not be null");
            }
            if (mem.replace(" ", "").isEmpty()) {
                throw new IllegalArgumentException("mem can not be empty string");
            }
            if (!Pattern.compile("(\\d+)(b|kb|mb|gb|)").matcher(mem.toLowerCase()).matches()) {
                throw new IllegalArgumentException("mem does not match pattern <value><b|kb|mb|gb|>");
            }
            this.mem = mem;
            return this;
        }

        /**
         * Associates the specified chunk custom resource value with the specified key in chunk custom resources map.
         * Custom resources are used for building of QSUB command. Optional.
         *
         * @param       key chunk custom resource key with which the specified chunk custom resource value is to be associated
         * @param       value chunk custom resource value to be associated with the specified chunk custom resource key
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code key} is {@code null}
         * @exception   IllegalArgumentException in case {@code value} is {@code null}
         * @exception   IllegalArgumentException in case {@code key} is blank string
         * @exception   IllegalArgumentException in case {@code value} is blank string
         */
        public ChunkBuilder customResource(String key, String value) {
            if (key == null) {
                throw new IllegalArgumentException("custom resource key can not be null");
            }
            if (value == null) {
                throw new IllegalArgumentException("custom resource value can not be null");
            }
            if (key.replace(" ", "").isEmpty()) {
                throw new IllegalArgumentException("custom resource key can not be empty string");
            }
            if (value.replace(" ", "").isEmpty()) {
                throw new IllegalArgumentException("custom resource value can not be empty string");
            }
            if (this.customResources == null) {
                this.customResources = new HashMap<>();
            }
            this.customResources.put(key, value);
            return this;
        }

        /**
         * Associates the specified chunk custom  resource value with the specified key in chunk custom  resources map.
         * Chunk custom  resources are used for building of QSUB command. Optional.
         *
         * @param       customResources new chunk custom  resources map to be added to chunk custom resources map
         * @return      a reference to this object.
         * @exception   IllegalArgumentException in case {@code customResources} is {@code null}
         * @exception   IllegalArgumentException in case {@code customResources.keySet()} contain {@code null}
         * @exception   IllegalArgumentException in case {@code customResources.keySet()} contain blank string
         * @exception   IllegalArgumentException in case {@code customResources.values()} contain {@code null}
         * @exception   IllegalArgumentException in case {@code customResources.values()} contain blank string
         */
        public ChunkBuilder customResources(Map<String, String> customResources) {
            if (customResources == null) {
                throw new IllegalArgumentException("chunk custom resources map can not be null");
            }
            if (customResources.keySet().stream().anyMatch(Objects::isNull)) {
                throw new IllegalArgumentException("custom resources can not contain null key");
            }
            if (customResources.keySet().stream().map(key -> key.replace(" ", "")).anyMatch(String::isEmpty)) {
                throw new IllegalArgumentException("chunk custom resources can not contain empty as key");
            }
            if (customResources.values().stream().anyMatch(Objects::isNull)) {
                throw new IllegalArgumentException("chunk custom resources can not contain null value");
            }
            if (customResources.values().stream().map(key -> key.replace(" ", "")).anyMatch(String::isEmpty)) {
                throw new IllegalArgumentException("chunk custom resources map can not contain empty value");
            }
            if (this.customResources == null) {
                this.customResources = new HashMap<>();
            }
            this.customResources.putAll(customResources);
            return this;
        }

        /**
         * Build new Chunk instance. And add it to the associated SubmitJobBuilder.
         *
         * @return          associated SubmitJobBuilder
         * @exception   IllegalArgumentException in case collection of chunks already contain chunk with {@code chunk.getId()}
         */
        public SubmitJobBuilder build() {
            Chunk newChunk = new Chunk(id, count, ncpus, mem, customResources);
            jobBuilder.addChunk(newChunk);
            return jobBuilder;
        }
    }

}
