package com.maximmold.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobFlowExecutor;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.AsyncTaskExecutor;

import javax.sql.DataSource;

/**
 * @author davidhamilton
 */
@Configuration
public class PersonRecordBatchConfiguration {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @Autowired
    private PersistPersonTasklet persistPersonTasklet;

    @Autowired
    private CreatePersonRecordTasklet createPersonRecordTasklet;

    @Autowired
    private PersonRecordReaderTasklet personRecordReaderTasklet;

    @Autowired
    private UpdateSuspenseTasklet updateSuspenseTasklet;

    @Autowired
    private AddPersonFlashTasklet addPersonFlashTasklet;

    @Autowired
    private ExecutionContextPromotionListener personRecordPromotionListener;

    @Bean
    public Job createPersonRecordJob(){
        return jobBuilderFactory.get("createPersonRecordJob")
                .incrementer(new RunIdIncrementer())
                .start(readPersonRecordFlow())
                .build().build();
    }

    @Bean
    public Flow readPersonRecordFlow() {
        return new FlowBuilder<SimpleFlow>("readPersonFlow")
                .start(stepBuilderFactory.get("readPersonRecordStep")
                    .tasklet(personRecordReaderTasklet)
                    .listener(personRecordPromotionListener)
                    .build()
                ).on("*").to(persistPersonFlow())
                .on("FAILURE").fail()
                .build();
    }

    @Bean
    public Flow persistPersonFlow() {
        return new FlowBuilder<SimpleFlow>("persistPersonFlow")
                .start(stepBuilderFactory.get("persistPersonStep")
                        .tasklet(persistPersonTasklet)
                        .build()
                ).on("*").to(createPersonRecordFlow())
                .on("FAILURE").fail()
                .build();
    }

    @Bean
    public Flow rollbackPersonFlow() {
        return new FlowBuilder<SimpleFlow>("rollbackPersonFlow")
                .start(stepBuilderFactory.get("rollbackPersonStep")
                        //TODO: Would need to pass arguments here or evaluate the step successes inside the tasklet?
                        .tasklet(persistPersonTasklet)
                        .build()
                ).on("*").end("ROLLEDBACK")
                .on("FAILURE").end("FAILEDROLLBACK")
                .build();
    }

    @Bean
    public Flow createPersonRecordFlow() {
        return new FlowBuilder<SimpleFlow>("createPersonRecordFlow")
                .start(stepBuilderFactory.get("createPersonRecordStep")
                        .tasklet(createPersonRecordTasklet)
                        .build()
                ).on("*").to(postPersonRecordCreatedFlow())
                .on("FAILURE").to(rollbackPersonFlow())
                .build();
    }

    @Bean
    public Flow rollbackPersonRecordFlow() {
        return new FlowBuilder<SimpleFlow>("rollbackPersonRecordFlow")
                .start(stepBuilderFactory.get("rollbackPersonRecordStep")
                        //TODO: Would need to pass arguments here or evaluate the step successes inside the tasklet?
                        .tasklet(createPersonRecordTasklet)
                        .build()
                ).on("*").to(rollbackPersonFlow())
                .on("FAILURE").end("FAILEDROLLBACK")
                .build();
    }

    @Bean
    public Flow postPersonRecordCreatedFlow() {
        return new FlowBuilder<SimpleFlow>("Split Flow")
                .split(asyncTaskExecutor)
                .add(new FlowBuilder<Flow>("updateSuspenseFlow").start(updateSuspenseStep()).end(),
                        new FlowBuilder<Flow>("addFlashFlow").start(addPersonFlashStep()).end())
                .build();
    }

    @Bean
    public Step updateSuspenseStep() {
        return stepBuilderFactory.get("updateSuspenseStep")
                .tasklet(updateSuspenseTasklet)
                .build();
    }

    @Bean
    public Step addPersonFlashStep() {
        return stepBuilderFactory.get("addPersonFlashStep")
                .tasklet(addPersonFlashTasklet)
                .build();
    }
}
