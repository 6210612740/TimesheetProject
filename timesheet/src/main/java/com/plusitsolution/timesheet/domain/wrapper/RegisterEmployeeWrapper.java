package com.plusitsolution.timesheet.domain.wrapper;

import com.plusitsolution.timesheet.domain.TimesheetsEnum.EmpRole;

public class RegisterEmployeeWrapper {
	
	private String orgID ;
	private String password ;
	private String firstName;
	private String lastName;
	private String nickName ;
	private double leaveLimit ;
	private double medFeeLimit;
	private String holidayID;
	private EmpRole empRole ;
	
	public RegisterEmployeeWrapper() {
		
	}
	
	public RegisterEmployeeWrapper(String orgID, String password, String firstName, String lastName,
			String nickName, double leaveLimit, double medFeeLimit, String holidayID, EmpRole empRole) {
		this.orgID = orgID;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.leaveLimit = leaveLimit;
		this.medFeeLimit = medFeeLimit;
		this.holidayID = holidayID;
		this.empRole = empRole;
	}
	
	public String getOrgID() {
		return orgID;
	}
	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public double getLeaveLimit() {
		return leaveLimit;
	}
	public void setLeaveLimit(double leaveLimit) {
		this.leaveLimit = leaveLimit;
	}
	public double getMedFeeLimit() {
		return medFeeLimit;
	}
	public void setMedFeeLimit(double medFeeLimit) {
		this.medFeeLimit = medFeeLimit;
	}
	public String getHolidayID() {
		return holidayID;
	}
	public void setHolidayID(String holidayID) {
		this.holidayID = holidayID;
	}
	public EmpRole getEmpRole() {
		return empRole;
	}
	public void setEmpRole(EmpRole empRole) {
		this.empRole = empRole;
	} 
	
	
	

}
