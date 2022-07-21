package com.plusitsolution.timesheet.domain;

public class EnumDomain {
	
	public enum DateStatus {
		LEAVE ,HALFDAY , WORK , HOLIDAY , OT, RECORD
	}
	
	public enum EmpRole {
		ADMIN , EMPLOYEE 
	}
	
	public enum MedStatus {
		APPROVE , NOTAPPROVE , INPROCESS 
	}
	
	public enum TimesheetsStatus {
		COMPLETED , INCOMPLETED
	}
}
