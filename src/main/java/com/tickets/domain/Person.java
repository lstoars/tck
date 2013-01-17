package com.tickets.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Person {
	private String name;
	
	private String cardCode;
	
	private String phone;
	
	private String seatType;
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	
	
}
