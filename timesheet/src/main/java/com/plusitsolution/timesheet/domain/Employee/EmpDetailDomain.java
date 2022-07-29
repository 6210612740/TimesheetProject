package com.plusitsolution.timesheet.domain.Employee;

import java.time.LocalDate;

import com.plusitsolution.timesheet.domain.EnumDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpRole;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpStatus;

public class EmpDetailDomain {
	
	private String empCode ;
	private String holidayID ;
	private double leaveLimit ;
	private double medFeeLimit ;
	private EmpRole empRole ;
	private String endContract ;
	private String startContract;
	private EmpStatus empStatus ;
	
	public EmpDetailDomain() {
		
	}
	
	public EmpDetailDomain(String empCode, String holidayID, double leaveLimit, double medFeeLimit, EmpRole empRole,
			String endContract, String startContract, EmpStatus empStatus) {
		this.empCode = empCode;
		this.holidayID = holidayID;
		this.leaveLimit = leaveLimit;
		this.medFeeLimit = medFeeLimit;
		this.empRole = empRole;
		this.endContract = endContract;
		this.startContract = startContract;
		this.empStatus = empStatus;
	}

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
	public String getEndContract() {
		return endContract;
	}
	public void setEndContract(String endContract) {
		this.endContract = endContract;
	}

	public String getStartContract() {
		return startContract;
	}

	public void setStartContract(String startContract) {
		this.startContract = startContract;
	}

	public EmpStatus getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(EmpStatus empStatus) {
		this.empStatus = empStatus;
	}
	
	

}
