package com.pbs.middleware.server.features.notification.email.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pbs.middleware.server.features.job.email.JobEmailFactory;
import com.pbs.middleware.server.features.job.email.domain.EmailEntity;
import com.pbs.middleware.server.features.job.email.repository.EmailRepository;
import com.pbs.middleware.server.features.notification.email.domain.Email;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.pbs.middleware.server.features.job.email.domain.EmailState.SEND;
import static com.pbs.middleware.server.features.job.email.domain.EmailState.WAITING;

@Component
@RequiredArgsConstructor
public class EmailProcessor {

    @NonNull
    private final EmailRepository emailRepository;

    @NonNull
    private final JobEmailFactory jobEmailFactory;

    @NonNull
    private final EmailService emailService;


    @Transactional
    @Scheduled(fixedRateString = "${middleware.server.mail.processor.delay:600}000")
    public void sendEmail() {
        Multimap<String, EmailEntity> emailsMap = ArrayListMultimap.create();

        emailRepository.findAllByState(WAITING)
                .forEach(emailEntity -> emailsMap.put(emailEntity.getRecipient(), emailEntity));

        emailsMap.keySet().forEach(recipient -> {
            try {
                Email email = jobEmailFactory.get(recipient, "Job notification", getBody(emailsMap.get(recipient)));
                emailService.send(email);
                emailsMap.get(recipient).forEach(emailEntity -> {
                    emailEntity.setSendDate(new Date().getTime());
                    emailEntity.setState(SEND);
                });
                emailRepository.saveAll(emailsMap.get(recipient));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        emailRepository.deleteOlderThan(getDateWeekAgo().getTime());
    }

    private Date getDateWeekAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return calendar.getTime();
    }

    private Map<String, Object> getBody(Collection<EmailEntity> emails) {
        Map<String, Object> body = new HashMap<>();

        emails.forEach(emailEntity -> body.put(getName(emailEntity.getSubject(), body), emailEntity.getBody()));

        return body;
    }

    /**
     * To solve problem of multiple email for the same recipient and subject
     */
    private String getName(String subject, Map<String, Object> body) {
        return body.containsKey(subject) ? getName(subject + " ", body) : subject;
    }

}
