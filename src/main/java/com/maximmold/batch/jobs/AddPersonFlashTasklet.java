package com.maximmold.batch.jobs;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * Example service call that can be run as an asynchronous step. Doesn't modify anything in the context
 *
 * Created by davidhamilton on 6/6/17.
 */
@Component
public class AddPersonFlashTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Adding Person Flash");
        System.out.println("Added Person Flash");
        return null;
    }
}
