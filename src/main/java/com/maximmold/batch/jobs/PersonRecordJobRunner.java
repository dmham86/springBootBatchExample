package com.maximmold.batch.jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximmold.batch.model.Person;
import com.maximmold.batch.model.PersonRecord;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by davidhamilton on 6/6/17.
 */
@Component
public class PersonRecordJobRunner {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("createPersonRecordJob")
    private Job job;

    @PostConstruct
    public void runTasks(){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, JobParameter> jobParameterMap = new ConcurrentHashMap<>();
        try {
            jobParameterMap.put("Person", new JobParameter(objectMapper.writeValueAsString(new Person("David", "Hamilton"))));
            jobParameterMap.put("PersonRecord", new JobParameter(objectMapper.writeValueAsString(new PersonRecord("123", null))));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            JobExecution jobExecution = jobLauncher.run(job,new JobParameters(jobParameterMap));
            while(jobExecution.isRunning()) {
                // Wait for job to complete
            }
            if(jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                System.out.println("Person Record Job completed! Person Record ID: " + ((PersonRecord) jobExecution.getExecutionContext().get("personRecord")).getId() );
            }
            else {
                System.out.println("Person Record Job FAILED!");
            }
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
