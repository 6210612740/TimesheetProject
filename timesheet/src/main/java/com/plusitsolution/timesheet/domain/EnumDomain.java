package com.plusitsolution.timesheet.domain;

public class EnumDomain {
	
	public enum DateStatus {
		LEAVE ,HALFDAY , WORK , HOLIDAY , OT, RECORD
	}
	
	public enum EmpRole {
		ADMIN , EMPLOYEE 
	}
	
	public enum EmpStatus {
		ACTIVE , INACTIVE 
	}
	
	public enum MedStatus {
		APPROVE , NOTAPPROVE , INPROCESS 
	}
	
	public enum LeaveStatus {
		APPROVE , NOTAPPROVE , INPROCESS 
	}
	
	public enum LeaveType {
		SICK , BUSINESS , VACATION 
	}
	
	public enum TimesheetsStatus {
		COMPLETED , INCOMPLETED , APPROVE , REJECT
	}
}
