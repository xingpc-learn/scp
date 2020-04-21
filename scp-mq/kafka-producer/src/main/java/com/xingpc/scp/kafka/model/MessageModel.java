package com.xingpc.scp.kafka.model;

import java.util.Date;

public class MessageModel {

	// id
	private Long id;

	// 消息
	private String msg;

	// 时间戳
	private Date sendTime;



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public Date getSendTime() {
		return sendTime;
	}



	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

}