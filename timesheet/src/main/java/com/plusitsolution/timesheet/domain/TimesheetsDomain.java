package com.plusitsolution.timesheet.domain;

import com.plusitsolution.timesheet.domain.TimesheetsEnum.DateStatus;

public class TimesheetsDomain {
	
	private String timeIn ;
	private String timeOut ;
	private String project ;
	private String activity ;
	private DateStatus dateStatus ;
	
	public TimesheetsDomain() {
		
	}
	
	public TimesheetsDomain(String timeIn, String timeOut, String project, String activity, DateStatus dateStatus) {
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.project = project;
		this.activity = activity;
		this.dateStatus = dateStatus;
	}
	
	public String getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}
	public String getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public DateStatus getDateStatus() {
		return dateStatus;
	}
	public void setDateStatus(DateStatus dateStatus) {
		this.dateStatus = dateStatus;
	}
	
	
	

}
