package com.pbs.middleware.server.common.info;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class JobsStatusInfoContributor implements InfoContributor {

    @Override
    public void contribute(Builder builder) {
        Map<String, Object> jobsStatus  = new HashMap<>();
        jobsStatus.put("runningJobs", new HashMap<String, Object>() {{
            put("queued", new Random().nextInt(40));
            put("running", new Random().nextInt(40));
            put("held", new Random().nextInt(40));

        }});

        jobsStatus.put("week", new HashMap<String, Object>() {{
            put("days", LocalDate.now().minusDays(7).datesUntil(LocalDate.now()).map(date -> date.getDayOfMonth() + "." + date.getMonth()).toArray());
            put("values", new Random().ints(7,1,1000).toArray());

        }});

        jobsStatus.put("month", new HashMap<>() {{
            put("days",  LocalDate.now().minusDays(37).datesUntil(LocalDate.now().minusDays(7)).map(date -> date.getDayOfMonth() + "." + date.getMonth()).toArray());
            put("values",  new Random().ints(30,1,1000).toArray());

        }});
        jobsStatus.put("year", new HashMap<>() {{
            put("months",  LocalDate.now().minusMonths(12).datesUntil(LocalDate.now()).map(date -> date.getMonth() + "." + date.getYear()).collect(Collectors.toSet()));
            put("values",  new Random().ints(12,1,1000).toArray());

        }});

        builder.withDetail("jobs", jobsStatus);
    }
}
