package com.maximmold.batch.jobs;

import com.maximmold.batch.model.Person;
import com.maximmold.batch.model.PersonRecord;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by davidhamilton on 6/6/17.
 */
@Component
public class CreatePersonRecordTasklet implements Tasklet {
    Random random = new Random(System.currentTimeMillis());

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Creating PersonRecord");

        PersonRecordContext personRecordContext = new PersonRecordContext(chunkContext.getStepContext());

        Person person = personRecordContext.getPerson();
        PersonRecord personRecord = personRecordContext.getPersonRecord();
        personRecord.setPersonId(person.getId());
        // Make a service call to create the person record
        createPersonRecord(personRecord);
        System.out.println("Created PersonRecord");
        return null;
    }

    private void createPersonRecord(PersonRecord personrecord) throws InterruptedException {
        Thread.sleep(1500);
        personrecord.setId(Math.abs(random.nextLong()));
    }
}
