package com.maximmold.batch.jobs;

import com.maximmold.batch.model.PersonRecord;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Example service call that can be run as an asynchronous step. Doesn't modify anything in the context
 *
 * Created by davidhamilton on 6/6/17.
 */
@Component
public class UpdateSuspenseTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Updating Suspense");

        PersonRecordContext personRecordContext = new PersonRecordContext(chunkContext.getStepContext());
        PersonRecord personRecord = personRecordContext.getPersonRecord();

        // Service Call to set suspense date
        updatePersonRecordSuspense(personRecord);
        System.out.println("Updated Suspense");
        return null;
    }

    private void updatePersonRecordSuspense(PersonRecord personRecord) throws InterruptedException {
        Thread.sleep(1500);
    }
}
