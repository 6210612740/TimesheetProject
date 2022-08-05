package com.plusitsolution.timesheet.domain.Medical;

import java.time.LocalDate;

import com.plusitsolution.timesheet.domain.EnumDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;
import com.plusitsolution.timesheet.entity.MedicalEntity;

public class MedicalDomain {
	
	private String empID;
	private String orgID;
	private String slipPic;
	private Double amount;
	private String note;
	private String date;
	private MedStatus medStatus ;
	
	public MedicalDomain() {
		
	}
	
	public MedicalDomain(String empID, String orgID, String slipPic, Double amount, String note,
			String date,MedStatus medStatus ) {
		
		this.empID = empID;
		this.orgID = orgID;
		this.slipPic = slipPic;
		this.amount = amount;
		this.note = note;
		this.date = date;
		this.medStatus = medStatus ;
	}
	
	public MedicalEntity toEntity() {
		MedicalEntity entity = new MedicalEntity() ;
		entity.setEmpID(this.empID);
		entity.setOrgID(this.orgID);
		entity.setSlipPic(this.slipPic);
		entity.setAmount(this.amount);
		entity.setNote(this.note);
		entity.setDate(this.date);
		entity.setMedStatus(this.medStatus);
		
		return entity ;
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
	public String getSlipPic() {
		return slipPic;
	}
	public void setSlipPic(String slipPic) {
		this.slipPic = slipPic;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public MedStatus getMedStatus() {
		return medStatus;
	}

	public void setMedStatus(MedStatus medStatus) {
		this.medStatus = medStatus;
	}
	
	
	
}