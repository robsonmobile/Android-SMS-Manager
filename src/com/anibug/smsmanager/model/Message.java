package com.anibug.smsmanager.model;

import java.util.Date;


public class Message extends Model {
	public static final int STATUS_RECEIVED = 1;
	public static final int STATUS_SENT = 2;
	public static final int STATUS_DRAFT = 3;
	
	private String phoneNumber;
	private Date dateCreated;
	private String content;
	private int status;
	private int onlineId = -1;

	public Message() {
		
	}
	
	public Message(String phoneNumber, Date dateCreated, String content, int status) {
		this.phoneNumber = phoneNumber;
		this.dateCreated = dateCreated;
		this.content = content;
		this.status = status;
	}

	public Message(String phoneNumber, Date dateCreated, String content, int status, int onlineId) {
		this.phoneNumber = phoneNumber;
		this.dateCreated = dateCreated;
		this.content = content;
		this.status = status;
		this.onlineId = onlineId;
	}

	public final String getPhoneNumber() {
		return phoneNumber;
	}

	public final void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public final Date getDateCreated() {
		return dateCreated;
	}

	public final void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public final int getOnlineId() {
		return onlineId;
	}

	public final void setOnlineId(int onlineId) {
		this.onlineId = onlineId;
	}

	public final boolean isUploaded() {
		return onlineId >= 0;
	}

	public class DataBase {
		public static final String TABLE_NAME = "messages";
		public static final String PHONENUMBER = "phone_number";
		public static final String DATE_CREATED = "date_created";
		public static final String CONTENT = "content";
		public static final String STATUS = "status";
		public static final String ONLINE_ID = "online_id";
	}
}
