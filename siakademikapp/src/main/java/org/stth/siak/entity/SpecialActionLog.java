package org.stth.siak.entity;

import java.util.Date;

public class SpecialActionLog {
	private int id;
	private String object;
	private DosenKaryawan performedBy;
	private Date performedDate;
	private String log;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public DosenKaryawan getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(DosenKaryawan performedBy) {
		this.performedBy = performedBy;
	}
	public Date getPerformedDate() {
		return performedDate;
	}
	public void setPerformedDate(Date performedDate) {
		this.performedDate = performedDate;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	
	

}
