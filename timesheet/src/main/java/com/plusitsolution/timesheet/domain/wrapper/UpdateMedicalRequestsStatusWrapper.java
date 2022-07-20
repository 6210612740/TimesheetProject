package com.plusitsolution.timesheet.domain.wrapper;

import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;

public class UpdateMedicalRequestsStatusWrapper {
	
	private String empID ;
	private MedStatus medStatus ;
	
	
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public MedStatus getMedStatus() {
		return medStatus;
	}
	public void setMedStatus(MedStatus medStatus) {
		this.medStatus = medStatus;
	}
	
	

}
