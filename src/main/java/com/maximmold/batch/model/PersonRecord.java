package com.maximmold.batch.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by davidhamilton on 6/7/17.
 */
public class PersonRecord implements Serializable {
    long id;
    String recordCode;
    Long personId;

    public PersonRecord() {
    }

    public PersonRecord(String recordCode, Long personId) {
        this.recordCode = recordCode;
        this.personId = personId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
}
