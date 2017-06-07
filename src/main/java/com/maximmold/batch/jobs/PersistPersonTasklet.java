package com.maximmold.batch.jobs;

import com.maximmold.batch.model.Person;
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
public class PersistPersonTasklet implements Tasklet {
    private Random random = new Random(System.currentTimeMillis());

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Updating Person");

        PersonRecordContext personRecordContext = new PersonRecordContext(chunkContext.getStepContext());

        Person person = personRecordContext.getPerson();
        // Make a service call to update the person
        updatePerson(person);
        System.out.println("Updated Person");
        return null;
    }

    private void updatePerson(Person person) throws InterruptedException {
        Thread.sleep(1000);
        person.setId(Math.abs(random.nextLong()));
    }
}
