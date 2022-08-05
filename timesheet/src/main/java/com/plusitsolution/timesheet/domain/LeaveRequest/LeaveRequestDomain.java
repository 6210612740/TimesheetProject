package com.plusitsolution.timesheet.domain.LeaveRequest;

import com.plusitsolution.timesheet.domain.EnumDomain.LeaveStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.LeaveType;

public class LeaveRequestDomain {
	
	private String empID;
	private String empCode ;
	private String nickName ;
	private String dateReq;
	private String dateStart;
	private String dateEnd;
	private Double requestLeave;
	private Double currentLeave;
	private String note;
	private LeaveType leaveType;
	private LeaveStatus leaveStatus;
	
	public LeaveRequestDomain() {
		
	}

	public LeaveRequestDomain(String empID, String empCode, String nickName, String dateReq, String dateStart, String dateEnd, Double requestLeave,
			Double currentLeave, String note, LeaveType leaveType, LeaveStatus leaveStatus) {
		this.empID = empID;
		this.empCode = empCode;
		this.nickName = nickName;
		this.dateReq = dateReq;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.requestLeave = requestLeave;
		this.currentLeave = currentLeave;
		this.note = note;
		this.leaveType = leaveType;
		this.leaveStatus = leaveStatus;
	}

	
	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public Double getRequestLeave() {
		return requestLeave;
	}

	public void setRequestLeave(Double requestLeave) {
		this.requestLeave = requestLeave;
	}

	public Double getCurrentLeave() {
		return currentLeave;
	}

	public void setCurrentLeave(Double currentLeave) {
		this.currentLeave = currentLeave;
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

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getDateReq() {
		return dateReq;
	}

	public void setDateReq(String dateReq) {
		this.dateReq = dateReq;
	}
	
	
	
	
	
	
}
