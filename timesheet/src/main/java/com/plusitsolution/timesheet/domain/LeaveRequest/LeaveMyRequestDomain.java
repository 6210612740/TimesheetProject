package com.plusitsolution.timesheet.domain.LeaveRequest;

import com.plusitsolution.timesheet.domain.EnumDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.LeaveStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.LeaveType;
import com.plusitsolution.timesheet.entity.LeaveEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;

public class LeaveMyRequestDomain {
	
	private String leaveID;
	private String empID;
	private String orgID;
	private String dateReq;
	private String dateStart;
	private String dateEnd;
	private String note;
	private LeaveType leaveType;
	private LeaveStatus leaveStatus;
	private String empCode;
	private String nickName;
	
	public LeaveMyRequestDomain() {
		
	}

	

	public LeaveMyRequestDomain(String leaveID, String empID, String orgID, String dateReq, String dateStart,
			String dateEnd, String note, LeaveType leaveType, LeaveStatus leaveStatus, String empCode,
			String nickName) {
		this.leaveID = leaveID;
		this.empID = empID;
		this.orgID = orgID;
		this.dateReq = dateReq;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.note = note;
		this.leaveType = leaveType;
		this.leaveStatus = leaveStatus;
		this.empCode = empCode;
		this.nickName = nickName;
	}



	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getDateReq() {
		return dateReq;
	}

	public void setDateReq(String dateReq) {
		this.dateReq = dateReq;
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

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}


	public String getLeaveID() {
		return leaveID;
	}


	public void setLeaveID(String leaveID) {
		this.leaveID = leaveID;
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
	
	

}
