package com.ronnie.ipl_dashboard.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("✅ JOB FINISHED! Time to verify the results:");

            jdbcTemplate.query(
                "SELECT team1, team2, date FROM match",
                (rs, rowNum) -> "Team 1: " + rs.getString("team1")
                             + ", Team 2: " + rs.getString("team2")
                             + ", Date: " + rs.getString("date")
            ).forEach(result -> System.out.println(result));
        }
    }
}
