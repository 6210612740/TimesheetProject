package com.plusitsolution.timesheet.domain;

import java.time.LocalDate;

public class OverviewDomain {
	
	private String empCode ;
	private String firstName ;
	private String lastName ;
	private double leaveLimit ;
	private double medFeeLimit ;
	private double leaveUse;
	private double medFeeUse ;
	private double leaveRemain ;
	private double medFeeRemain ;
	private LocalDate endContract ;
	
	
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
	public double getLeaveUse() {
		return leaveUse;
	}
	public void setLeaveUse(double leaveUse) {
		this.leaveUse = leaveUse;
	}
	public double getMedFeeUse() {
		return medFeeUse;
	}
	public void setMedFeeUse(double medFeeUse) {
		this.medFeeUse = medFeeUse;
	}
	public double getLeaveRemain() {
		return leaveRemain;
	}
	public void setLeaveRemain(double leaveRemain) {
		this.leaveRemain = leaveRemain;
	}
	public double getMedFeeRemain() {
		return medFeeRemain;
	}
	public void setMedFeeRemain(double medFeeRemain) {
		this.medFeeRemain = medFeeRemain;
	}
	public LocalDate getEndContract() {
		return endContract;
	}
	public void setEndContract(LocalDate endContract) {
		this.endContract = endContract;
	}
	

}
