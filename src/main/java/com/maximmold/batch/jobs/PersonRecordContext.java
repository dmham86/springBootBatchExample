package com.maximmold.batch.jobs;

import com.maximmold.batch.model.Person;
import com.maximmold.batch.model.PersonRecord;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;

import java.util.Map;

/**
 * Created by davidhamilton on 6/7/17.
 */
public class PersonRecordContext {
    private ExecutionContext stepExecutionContext;
    private Map<String, Object> jobExecutionContext;

    private static final String PERSON = "person";
    private static final String PERSON_RECORD = "personRecord";

    public PersonRecordContext(StepContext stepContext) {
        stepExecutionContext = stepContext.getStepExecution().getExecutionContext();
        jobExecutionContext = stepContext.getJobExecutionContext();
    }

    public Person getPerson(){
        return (Person) jobExecutionContext.get(PERSON);
    }

    public void putPerson(Person person) {
        stepExecutionContext.put(PERSON, person);
    }

    public PersonRecord getPersonRecord() {
        return (PersonRecord) jobExecutionContext.get(PERSON_RECORD);
    }

    public void putPersonRecord(PersonRecord personRecord) {
        stepExecutionContext.put(PERSON_RECORD, personRecord);
    }
}
