package com.plusitsolution.timesheet.domain;

import java.time.LocalDate;

import com.plusitsolution.timesheet.domain.TimesheetsEnum.EmpRole;

public class EmpDetailDomain {
	
	private String empCode ;
	private String holidayID ;
	private double leaveLimit ;
	private double medFeeLimit ;
	private EmpRole empRole ;
	private LocalDate endContract ;
	
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getHolidayID() {
		return holidayID;
	}
	public void setHolidayID(String holidayID) {
		this.holidayID = holidayID;
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
	public EmpRole getEmpRole() {
		return empRole;
	}
	public void setEmpRole(EmpRole empRole) {
		this.empRole = empRole;
	}
	public LocalDate getEndContract() {
		return endContract;
	}
	public void setEndContract(LocalDate endContract) {
		this.endContract = endContract;
	}
	

}
