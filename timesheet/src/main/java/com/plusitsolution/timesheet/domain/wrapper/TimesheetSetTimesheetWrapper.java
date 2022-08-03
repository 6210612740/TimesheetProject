package com.plusitsolution.timesheet.domain.wrapper;

import com.plusitsolution.timesheet.domain.EnumDomain.TimesheetsStatus;

public class TimesheetSetTimesheetWrapper {

	private String empID ;
	private int month ;
	private int year ;
	private TimesheetsStatus timesheetStatus ;
	
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public TimesheetsStatus getTimesheetStatus() {
		return timesheetStatus;
	}
	public void setTimesheetStatus(TimesheetsStatus timesheetStatus) {
		this.timesheetStatus = timesheetStatus;
	}
	
	
}
