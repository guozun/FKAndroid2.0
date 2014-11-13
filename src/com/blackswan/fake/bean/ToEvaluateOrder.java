package com.blackswan.fake.bean;

public class ToEvaluateOrder {
	private String barberHeadUrl;
	public String getBarberHeadUrl() {
		return barberHeadUrl;
	}
	public void setBarberHeadUrl(String barberHeadUrl) {
		this.barberHeadUrl = barberHeadUrl;
	}
	public String getBarbershopName() {
		return barbershopName;
	}
	public void setBarbershopName(String barbershopName) {
		this.barbershopName = barbershopName;
	}
	public String getBarberName() {
		return barberName;
	}
	public void setBarberName(String barberName) {
		this.barberName = barberName;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	private String barbershopName;
	private String barberName;
	private String operationName;
	private String orderTime;
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
}
