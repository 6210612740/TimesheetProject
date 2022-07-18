package com.plusitsolution.timesheet.domain;

import java.time.LocalDate;

public class MedicalDomain {
	
	private String medID;
	private String empID;
	private String orgID;
	private String slipPic;
	private Double amount;
	private String note;
	private LocalDate date;
	
	public String getMedID() {
		return medID;
	}
	public void setMedID(String medID) {
		this.medID = medID;
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
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
	
}
