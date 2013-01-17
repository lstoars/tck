package com.tickets.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * ��������
 * ,--,--,--,--,--,��,��,--,��,��,--,
 * @author sahala
 *
 */
public class SeatType {
	
	public SeatType(){
		
	}
	
	public SeatType(String info){
		if(info == null || info.trim().length() ==0){
			throw new IllegalArgumentException("SeatType info is empty");
		}
		info = info.substring(1, info.length()-1);
		String[] arr = info.split(",");
		if(arr.length != 11){
			throw new IllegalArgumentException("SeatType info is error,is:"+info);
		}
		setShangwu(arr[0]);
		setTedeng(arr[1]);
		setYideng(arr[2]);
		setErdeng(arr[3]);
		setGaojiruanwo(arr[4]);
		setRuanwo(arr[5]);
		setYingwo(arr[6]);
		setRuanzuo(arr[7]);
		setYingzuo(arr[8]);
		setWuzuo(arr[9]);
		setQita(arr[10]);
	}
	
	public static void main(String[] args) {
		SeatType s = new SeatType(",--,--,--,--,--,��,��,--,��,��,--,");
		String str = s.toString();
		System.out.println(str);
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
	/** ������ */
	private String shangwu;
	
	/** �ص��� */
	private String tedeng;
	
	/** һ�� */
	private String yideng;
	
	/** ���� */
	private String erdeng;
	
	/** �߼����� */
	private String gaojiruanwo;
	
	/** ���� */
	private String ruanwo;
	
	/** Ӳ�� */
	private String yingwo;
	
	/** ���� */
	private String ruanzuo;
	
	/** Ӳ�� */
	private String yingzuo;
	
	/** ���� */
	private String wuzuo;
	
	/** ���� */
	private String qita;

	public String getShangwu() {
		return shangwu;
	}

	public void setShangwu(String shangwu) {
		this.shangwu = shangwu;
	}

	public String getTedeng() {
		return tedeng;
	}

	public void setTedeng(String tedeng) {
		this.tedeng = tedeng;
	}

	public String getYideng() {
		return yideng;
	}

	public void setYideng(String yideng) {
		this.yideng = yideng;
	}

	public String getErdeng() {
		return erdeng;
	}

	public void setErdeng(String erdeng) {
		this.erdeng = erdeng;
	}

	public String getGaojiruanwo() {
		return gaojiruanwo;
	}

	public void setGaojiruanwo(String gaojiruanwo) {
		this.gaojiruanwo = gaojiruanwo;
	}

	public String getRuanwo() {
		return ruanwo;
	}

	public void setRuanwo(String ruanwo) {
		this.ruanwo = ruanwo;
	}

	public String getYingwo() {
		return yingwo;
	}

	public void setYingwo(String yingwo) {
		this.yingwo = yingwo;
	}

	public String getRuanzuo() {
		return ruanzuo;
	}

	public void setRuanzuo(String ruanzuo) {
		this.ruanzuo = ruanzuo;
	}

	public String getYingzuo() {
		return yingzuo;
	}

	public void setYingzuo(String yingzuo) {
		this.yingzuo = yingzuo;
	}

	public String getWuzuo() {
		return wuzuo;
	}

	public void setWuzuo(String wuzuo) {
		this.wuzuo = wuzuo;
	}

	public String getQita() {
		return qita;
	}

	public void setQita(String qita) {
		this.qita = qita;
	}
	
	
	
}
