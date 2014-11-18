package com.blackswan.fake.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Advertisement implements Serializable {

	private String imgUrl;
	private String adverUrl;
	private int duration;	//持续时间，该广告的展示时间

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getAdverUrl() {
		return adverUrl;
	}

	public void setAdverUrl(String adverUrl) {
		this.adverUrl = adverUrl;
	}

	public Advertisement(String url) {
		imgUrl = url;
	}
}
