package com.plusitsolution.timesheet.domain.Timesheet;

import com.plusitsolution.timesheet.domain.EnumDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.TimesheetsStatus;

public class TimesheetsSummaryDomain {
	
	private String empCode ;
	private String firstName ;
	private String lastName ;
	private TimesheetsStatus timesheetsStatus ;
	private double leaveUse ;
	private double totalOT ;
	private double totalWork ;
	private String nickName;
	private String holidayName;
	
	public TimesheetsSummaryDomain() {
		
	}
	
	
	public TimesheetsSummaryDomain(String empCode, String firstName, String lastName, TimesheetsStatus timesheetsStatus,
			double leaveUse, double totalOT, double totalWork, String nickName, String holidayName) {
		this.empCode = empCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.timesheetsStatus = timesheetsStatus;
		this.leaveUse = leaveUse;
		this.totalOT = totalOT;
		this.totalWork = totalWork;
		this.nickName = nickName;
		this.holidayName = holidayName;
	}



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


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getHolidayName() {
		return holidayName;
	}


	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	
	
	
	

}
