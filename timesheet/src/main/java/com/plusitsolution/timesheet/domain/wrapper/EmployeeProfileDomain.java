package com.plusitsolution.timesheet.domain.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.plusitsolution.timesheet.domain.EnumDomain.EmpRole;
import com.plusitsolution.timesheet.entity.OrganizeEntity;

public class EmployeeProfileDomain {

	private String empID ; 
	private String orgID ;
	private String empCode ;
	private String firstName;
	private String lastName;
	private String nickName ;
	private String username;

	private String holidayID ;
	private double leaveLimit ;
	private double medFeeLimit ;
	private EmpRole empRole ;
	private String endContract ;
	
	private Double leaveUse;
	private Double medFeeUse;
	private Double leaveRemain;
	private Double medFeeRemain;
	
	private String orgNameEng;
	private String orgNameTh;
	
	
	public EmployeeProfileDomain() {
		
	}
	
	public EmployeeProfileDomain(String empID, String orgID, String empCode, String firstName, String lastName,
			String nickName, String username, String holidayID, double leaveLimit, double medFeeLimit,
			EmpRole empRole, String endContract, Double leaveUse, Double medFeeUse, Double leaveRemain,
			Double medFeeRemain, String orgNameEng, String orgNameTh) {
		this.empID = empID;
		this.orgID = orgID;
		this.empCode = empCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.username = username;
		this.holidayID = holidayID;
		this.leaveLimit = leaveLimit;
		this.medFeeLimit = medFeeLimit;
		this.empRole = empRole;
		this.endContract = endContract;
		this.leaveUse = leaveUse;
		this.medFeeUse = medFeeUse;
		this.leaveRemain = leaveRemain;
		this.medFeeRemain = medFeeRemain;
		this.orgNameEng = orgNameEng;
		this.orgNameTh = orgNameTh;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	public String getEndContract() {
		return endContract;
	}
	public void setEndContract(String endContract) {
		this.endContract = endContract;
	}
	public Double getLeaveUse() {
		return leaveUse;
	}
	public void setLeaveUse(Double leaveUse) {
		this.leaveUse = leaveUse;
	}
	public Double getMedFeeUse() {
		return medFeeUse;
	}
	public void setMedFeeUse(Double medFeeUse) {
		this.medFeeUse = medFeeUse;
	}
	public Double getLeaveRemain() {
		return leaveRemain;
	}
	public void setLeaveRemain(Double leaveRemain) {
		this.leaveRemain = leaveRemain;
	}
	public Double getMedFeeRemain() {
		return medFeeRemain;
	}
	public void setMedFeeRemain(Double medFeeRemain) {
		this.medFeeRemain = medFeeRemain;
	}

	public String getOrgNameEng() {
		return orgNameEng;
	}

	public void setOrgNameEng(String orgNameEng) {
		this.orgNameEng = orgNameEng;
	}

	public String getOrgNameTh() {
		return orgNameTh;
	}

	public void setOrgNameTh(String orgNameTh) {
		this.orgNameTh = orgNameTh;
	}

	
	
	
	
}
