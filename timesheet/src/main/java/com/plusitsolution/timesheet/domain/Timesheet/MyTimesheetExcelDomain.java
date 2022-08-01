package com.plusitsolution.timesheet.domain.Timesheet;

public class MyTimesheetExcelDomain {
	
	private int index;
	private String date;
	private String timeIn ;
	private String timeOut ;
	private String project ;
	private String activity ;
	
	public MyTimesheetExcelDomain() {
	
	}

	public MyTimesheetExcelDomain(int index, String date, String timeIn, String timeOut, String project,
			String activity) {
		this.index = index;
		this.date = date;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.project = project;
		this.activity = activity;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
	
	
	

}
