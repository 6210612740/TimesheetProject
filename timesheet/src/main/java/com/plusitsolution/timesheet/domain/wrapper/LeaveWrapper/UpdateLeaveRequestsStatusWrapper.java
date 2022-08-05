package com.plusitsolution.timesheet.domain.wrapper.LeaveWrapper;

import com.plusitsolution.timesheet.domain.EnumDomain.LeaveStatus;

public class UpdateLeaveRequestsStatusWrapper {
	
	private String leaveID;
	private LeaveStatus leaveStatus;
	
	public String getLeaveID() {
		return leaveID;
	}
	public void setLeaveID(String leaveID) {
		this.leaveID = leaveID;
	}
	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}
	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
	
	

}
