package com.maximmold.batch.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximmold.batch.model.Person;
import com.maximmold.batch.model.PersonRecord;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * Created by davidhamilton on 6/7/17.
 */
@Component
public class PersonRecordReaderTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PersonRecordContext personRecordContext = new PersonRecordContext(chunkContext.getStepContext());

        System.out.println("Reading Person Information into Context");

        personRecordContext.putPerson(objectMapper.readValue(chunkContext.getStepContext().getJobParameters().get("Person").toString(), Person.class));
        personRecordContext.putPersonRecord(objectMapper.readValue(chunkContext.getStepContext().getJobParameters().get("PersonRecord").toString(), PersonRecord.class));

        System.out.println("Read Person Information into Context");
        return null;
    }
}
