package com.plusitsolution.timesheet.domain;

import com.plusitsolution.timesheet.domain.TimesheetsEnum.TimesheetsStatus;

public class TimesheetsSummaryDomain {
	
	private String empCode ;
	private String firstName ;
	private String lastName ;
	private TimesheetsStatus timesheetsStatus ;
	private double leaveUse ;
	private double totalOT ;
	private double totalWork ;
	
	
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public TimesheetsStatus getTimesheetsStatus() {
		return timesheetsStatus;
	}
	public void setTimesheetsStatus(TimesheetsStatus timesheetsStatus) {
		this.timesheetsStatus = timesheetsStatus;
	}
	public double getLeaveUse() {
		return leaveUse;
	}
	public void setLeaveUse(double leaveUse) {
		this.leaveUse = leaveUse;
	}
	public double getTotalOT() {
		return totalOT;
	}
	public void setTotalOT(double totalOT) {
		this.totalOT = totalOT;
	}
	public double getTotalWork() {
		return totalWork;
	}
	public void setTotalWork(double totalWork) {
		this.totalWork = totalWork;
	}
	
	
	

}
