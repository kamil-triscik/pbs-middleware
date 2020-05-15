package com.pbs.middleware.server.features.template.pbs.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbs.middleware.server.features.pbs.qstat.Qstat;
import com.pbs.middleware.server.features.pbs.qstat.QstatResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;


class StatusResponseDeserializerTest {

    String json = "{\n" +
            "    \"timestamp\":1566508658,\n" +
            "    \"pbs_version\":\"19.0.0\",\n" +
            "    \"pbs_server\":\"wagap-pro.cerit-sc.cz\",\n" +
            "    \"Jobs\":{\n" +
            "        \"3098156.wagap-pro.cerit-sc.cz\":{\n" +
            "            \"Job_Name\":\"testovaci_uloha\",\n" +
            "            \"Job_Owner\":\"kamil_triscik@META\",\n" +
            "            \"resources_used\":{\n" +
            "                \"cpupercent\":0,\n" +
            "                \"cput\":\"00:00:00\",\n" +
            "                \"mem\":\"5184kb\",\n" +
            "                \"ncpus\":1,\n" +
            "                \"vmem\":\"31756kb\",\n" +
            "                \"walltime\":\"00:00:26\"\n" +
            "            },\n" +
            "            \"job_state\":\"Q\",\n" +
            "            \"queue\":\"q_2h\",\n" +
            "            \"server\":\"wagap-pro.cerit-sc.cz\",\n" +
            "            \"Checkpoint\":\"u\",\n" +
            "            \"ctime\":\"Thu Aug 22 23:16:51 2019\",\n" +
            "            \"Error_Path\":\"zuphux.cerit-sc.cz:/storage/brno3-cerit/home/kamil_triscik/testovaci_uloha.e3098156\",\n" +
            "            \"exec_host\":\"zefron5/13\",\n" +
            "            \"exec_host2\":\"zefron5.cerit-sc.cz:15002/13\",\n" +
            "            \"exec_vnode\":\"(zefron5:ncpus=1:mem=409600kb)\",\n" +
            "            \"Hold_Types\":\"n\",\n" +
            "            \"Join_Path\":\"n\",\n" +
            "            \"Keep_Files\":\"n\",\n" +
            "            \"Mail_Points\":\"a\",\n" +
            "            \"mtime\":\"Thu Aug 22 23:17:25 2019\",\n" +
            "            \"Output_Path\":\"zuphux.cerit-sc.cz:/storage/brno3-cerit/home/kamil_triscik/testovaci_uloha.o3098156\",\n" +
            "            \"Priority\":0,\n" +
            "            \"qtime\":\"Thu Aug 22 23:16:51 2019\",\n" +
            "            \"Rerunable\":\"True\",\n" +
            "            \"Resource_List\":{\n" +
            "                \"mem\":\"400mb\",\n" +
            "                \"mpiprocs\":1,\n" +
            "                \"ncpus\":1,\n" +
            "                \"nodect\":1,\n" +
            "                \"place\":\"free\",\n" +
            "                \"select\":\"1:ncpus=1:mem=400mb:mpiprocs=1:ompthreads=1\",\n" +
            "                \"walltime\":\"00:10:00\"\n" +
            "            },\n" +
            "            \"stime\":\"Thu Aug 22 23:16:59 2019\",\n" +
            "            \"session_id\":8697,\n" +
            "            \"jobdir\":\"/storage/brno3-cerit/home/kamil_triscik\",\n" +
            "            \"substate\":42,\n" +
            "            \"Variable_List\":{\n" +
            "                \"PBS_O_SYSTEM\":\"Linux\",\n" +
            "                \"GROUP\":\"meta\",\n" +
            "                \"PBS_O_HOME\":\"/storage/brno3-cerit/home/kamil_triscik\",\n" +
            "                \"PBS_O_HOST\":\"zuphux.cerit-sc.cz\",\n" +
            "                \"TORQUE_RESC_MEM\":419430400,\n" +
            "                \"PBS_RESC_TOTAL_MEM\":419430400,\n" +
            "                \"JOBID\":\"3098156.wagap-pro.cerit-sc.cz\",\n" +
            "                \"PBS_O_LOGNAME\":\"kamil_triscik\",\n" +
            "                \"TORQUE_RESC_PROC\":1,\n" +
            "                \"PBS_O_LANG\":\"en_US.UTF-8\",\n" +
            "                \"USER\":\"kamil_triscik\",\n" +
            "                \"TORQUE_RESC_TOTAL_PROCS\":1,\n" +
            "                \"PBS_O_MAIL\":\"/var/spool/mail/kamil_triscik\",\n" +
            "                \"PBS_RESC_MEM\":419430400,\n" +
            "                \"PBS_NUM_PPN\":1,\n" +
            "                \"PBS_RESC_TOTAL_PROCS\":1,\n" +
            "                \"PBS_O_SHELL\":\"/bin/bash\",\n" +
            "                \"HOSTNAME\":\"zefron5.cerit-sc.cz\",\n" +
            "                \"PBS_O_QUEUE\":\"default\",\n" +
            "                \"PBS_O_WORKDIR\":\"/storage/brno3-cerit/home/kamil_triscik\",\n" +
            "                \"PROMENNA\":\"deset\",\n" +
            "                \"PBS_O_PATH\":\"/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/software/meta-utils/public:/opt/pbs/bin:/opt/puppetlabs/bin\",\n" +
            "                \"PBS_NCPUS\":1,\n" +
            "                \"TORQUE_RESC_TOTAL_WALLTIME\":600,\n" +
            "                \"PBS_RESC_TOTAL_WALLTIME\":600,\n" +
            "                \"PBS_NUM_NODES\":1,\n" +
            "                \"TORQUE_RESC_TOTAL_MEM\":419430400\n" +
            "            },\n" +
            "            \"comment\":\"Job run at Thu Aug 22 at 23:16 on (zefron5:ncpus=1:mem=409600kb)\",\n" +
            "            \"etime\":\"Thu Aug 22 23:16:51 2019\",\n" +
            "            \"run_count\":1,\n" +
            "            \"Exit_status\":0,\n" +
            "            \"eligible_time\":\"00:00:00\",\n" +
            "            \"Submit_arguments\":\"-l walltime=10:0 -l select=1:ncpus=1:mem=400mb -N testovaci_uloha -v PROMENNA=deset run.sh\",\n" +
            "            \"project\":\"_pbs_project_default\",\n" +
            "            \"krb_princ\":\"kamil_triscik@META\",\n" +
            "            \"Job_Host\":\"zuphux.cerit-sc.cz\"\n" +
            "        }\n" +
            "    }\n" +
            "}";

    String finished = "{\n" +
            "    \"timestamp\":1566508938,\n" +
            "    \"pbs_version\":\"19.0.0\",\n" +
            "    \"pbs_server\":\"wagap-pro.cerit-sc.cz\"\n" +
            "}\n";

    @Test
    void deserialize() throws JsonProcessingException {

            QstatResponse example = new ObjectMapper().readValue(json, QstatResponse.class);
//            PbsJobStatus example = new ObjectMapper().readValue(finished, PbsJobStatus.class);
            System.out.println();



    }
}