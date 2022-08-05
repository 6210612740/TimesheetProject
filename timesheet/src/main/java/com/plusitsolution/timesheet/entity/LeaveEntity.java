package com.plusitsolution.timesheet.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.plusitsolution.timesheet.domain.EnumDomain.LeaveStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.LeaveType;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;

@Document(indexName = "leaverequest-index")
public class LeaveEntity {
	
	@Id
	@ReadOnlyProperty	
	private String leaveID;
	
	@Field(type = FieldType.Keyword)
	private String empID;
	
	@Field(type = FieldType.Keyword)
	private String orgID;
	
	private String dateReq;
	private String dateStart;
	private String dateEnd;
	private String note;
	private LeaveType leaveType;
	private LeaveStatus leaveStatus;
	
	public String getLeaveID() {
		return leaveID;
	}
	public void setLeaveID(String leaveID) {
		this.leaveID = leaveID;
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
	
	

}
