package com.plusitsolution.timesheet.domain.wrapper.LeaveWrapper;

import com.plusitsolution.timesheet.domain.EnumDomain.LeaveType;

public class LeaveRequestWrapper {
	
	private String empCode ;
	private String dateStart;
	private String dateEnd;
	private String note;
	private LeaveType leaveType;
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public LeaveType getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
	

}
