package com.plusitsolution.timesheet.domain.wrapper.MedicalWrapper;

import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;

public class UpdateMedicalRequestsStatusWrapper {
	
	private String medID ;
	private MedStatus medStatus ;
	
	public String getMedID() {
		return medID;
	}
	public void setMedID(String medID) {
		this.medID = medID;
	}
	public MedStatus getMedStatus() {
		return medStatus;
	}
	public void setMedStatus(MedStatus medStatus) {
		this.medStatus = medStatus;
	}
	
	

	
	

}
