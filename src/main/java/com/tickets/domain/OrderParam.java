package com.tickets.domain;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class OrderParam {
	
	private Train train;
	
	private List<Person> persons;
	

	public OrderParam(Train train, List<Person> persons) {
		super();
		this.train = train;
		this.persons = persons;
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
}
