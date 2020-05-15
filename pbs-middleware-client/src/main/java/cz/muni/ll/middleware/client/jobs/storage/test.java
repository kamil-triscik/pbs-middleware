package cz.muni.ll.middleware.client.jobs.storage;

public class test {

    public static void main(String[] args) {
//        file();
//        db();
    }

    private static void file() {
        //        JobStorage storage = new LocalFileStorage("/home/kamil/xxt", "g1");
//        JobStorage storage = StorageBuilder.get()
//                .fileStorage("/home/kamil/xxt")
//                .template("g1")
//                .build();
//        Long c = storage.deleteAll();
//        Long c = storage.deleteAllJobs(JobState.FINISHED);
//        Job job = new Job(UUID.randomUUID(), "dhasgdja");
//        storage.store(job);
//        List<Job> jobs = storage.getAllJobs();
//        List<Job> runn = storage.getAllRunningJobs();

//        storage.setRunning(UUID.fromString("0b016d31-d658-42c8-80bb-3a8a49764f6c"));
//        System.out.println();
    }

    private static void db() {
//        DatabaseStorage.createNewDatabase("/home/kamil/xxt/lite.db");
//        JobStorage storage = StorageBuilder.get()
//                .databaseStorage("/home/kamil/xxt/lite.db")
//                .build();
//        JobStorage storage = StorageBuilder.get()
//                .fileStorage("/home/kamil/xxt/files")
//                .template("g1")
//                .build();

//        Job j1 = new Job();
//        j1.setId("j1");
//        j1.setMiddlewareId(UUID.randomUUID());
//        j1.setPbsJobId("jobId1");
//        j1.setState(JobState.RUNNING);

//        Optional<Job> f = storage.find("j1");
//        Optional<Job> fe = storage.find("jx");
//
//        storage.setRunning(j1);
//        storage.setFinished(j1);
//        storage.setState(j1, JobState.INITIALIZING);
//
//        Job j2 = new Job();
//        j2.setId("j2");
//        j2.setMiddlewareId(UUID.randomUUID());
//        j2.setPbsJobId("jobId2");
//        j2.setState(JobState.RUNNING);
//
//        Job j3 = new Job();
//        j3.setId("j3");
//        j3.setMiddlewareId(UUID.randomUUID());
//        j3.setPbsJobId("jobId3");
//        j3.setState(JobState.RUNNING);
//
//        storage.store(j1);
//        storage.store(j2);
//        storage.store(j3);

//        List<Job> all = storage.getAll();
//        List<Job> run = storage.getAllRunning();
//        List<Job> st = storage.getAllByState(JobState.INITIALIZING);
//
//        storage.deleteById("j2");
//        storage.deleteAllWithState(JobState.INITIALIZING);
//        storage.deleteAll();


//        System.out.printf("");
    }


}
