package com.tickets.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * G6012#03:27#07:12#6i000G601202#IOQ#CWQ#10:39#深圳北#长沙南#01#07#O*****0005M*****0032P*****0007#83A58E24B35ECCC90B748C05CEAADA826340220288ADEE128E6AD1D6#Q7
 * @author sahala
 *
 */
public class Train {
	private String trainCode;
	private String lishi;
	private String startTime;
	private String arriveTime;
	
	private String trainNo;
	private String fromStationCode;
	private String toStationCode;
	
	private String fromStationName;
	private String toStationName;
	
	private String fromStationNo;
	private String toStationNo;
	private String ypInfo;
	
	private SeatType seatType;
	
	private String date;
	
	private String mmStr;
	
	private String locationCode;
	
	public Train(){
		
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
	public Train(String strInfo){
		if(strInfo==null || strInfo.trim().length()==0){
			throw new IllegalArgumentException("strInfo is empty");
		}
		String[] infoArr = strInfo.split("#");
		if(infoArr.length !=14){
			throw new IllegalArgumentException("strInfo format error;strInfo:"+infoArr);
		}
		//G6012#03:27#07:12#6i000G601202#IOQ#CWQ#10:39#深圳北#长沙南#01#07#O*****0005M*****0032P*****0007#83A58E24B35ECCC90B748C05CEAADA826340220288ADEE128E6AD1D6#Q7
		setTrainCode(infoArr[0]);
		setLishi(infoArr[1]);
		setStartTime(infoArr[2]);
		setTrainNo(infoArr[3]);
		setFromStationCode(infoArr[4]);
		setToStationCode(infoArr[5]);
		setArriveTime(infoArr[6]);
		setFromStationName(infoArr[7]);
		setToStationName(infoArr[8]);
		setFromStationNo(infoArr[9]);
		setToStationNo(infoArr[10]);
		setYpInfo(infoArr[11]);
		setMmStr(infoArr[12]);
		setLocationCode(infoArr[13]);
	}

	public String getTrainCode() {
		return trainCode;
	}

	public void setTrainCode(String trainCode) {
		this.trainCode = trainCode;
	}

	public String getLishi() {
		return lishi;
	}

	public void setLishi(String lishi) {
		this.lishi = lishi;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public String getFromStationCode() {
		return fromStationCode;
	}

	public void setFromStationCode(String fromStationCode) {
		this.fromStationCode = fromStationCode;
	}

	public String getToStationCode() {
		return toStationCode;
	}

	public void setToStationCode(String toStationCode) {
		this.toStationCode = toStationCode;
	}

	public String getFromStationName() {
		return fromStationName;
	}

	public void setFromStationName(String fromStationName) {
		this.fromStationName = fromStationName;
	}

	public String getToStationName() {
		return toStationName;
	}

	public void setToStationName(String toStationName) {
		this.toStationName = toStationName;
	}

	public String getYpInfo() {
		return ypInfo;
	}

	public void setYpInfo(String ypInfo) {
		this.ypInfo = ypInfo;
	}
	
	public static void main(String[] args) {
		Train r = new Train("T82#02:29#08:30#7100000T8200#XHH#SNH#10:59#杭州南#上海南#1002900000300800003540120000001002903002#1ECA57C0EBE225D6665EE8C8D7966368A5392E1C88D77756403A39F1");
		System.out.println(r);
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMmStr() {
		return mmStr;
	}

	public void setMmStr(String mmStr) {
		this.mmStr = mmStr;
	}

	public String getFromStationNo() {
		return fromStationNo;
	}

	public void setFromStationNo(String fromStationNo) {
		this.fromStationNo = fromStationNo;
	}

	public String getToStationNo() {
		return toStationNo;
	}

	public void setToStationNo(String toStationNo) {
		this.toStationNo = toStationNo;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	
	
	
	
}
