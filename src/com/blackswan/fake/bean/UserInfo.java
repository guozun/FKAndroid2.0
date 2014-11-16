package com.blackswan.fake.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private String phoneNumber; // 用户注册手机号
	private String pwd; // 用户登录密码
	private String name; // 用户登录密码
	private String disp; // 用户登录密码
	private String headUrl; // 用户登录密码
	private Boolean sex; // 用户登录密码

	public UserInfo() {
	}

	public UserInfo(String name, String phone, String pwd, String disp,
			String head, Boolean sex) {
		phoneNumber = phone;
		this.pwd = pwd;
		this.name = name;
		this.disp = disp;
		this.headUrl = head;
		this.sex = sex;

	}

	public String getHeadUrl() {
		return headUrl;
	}

	public Boolean getSex() {
		return sex;
	}

	public String getSexLocal() {
		if (sex) {
			return "男";
		} else {
			return "女";
		}

	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisp() {
		return disp;
	}

	public void setDisp(String disp) {
		this.disp = disp;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
