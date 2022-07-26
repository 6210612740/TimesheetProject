package com.plusitsolution.timesheet.domain.Medical;

import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;

public class MedicalMyRequestDomain {

	private String medID;
	private String empID;
	private String orgID;
	private String slipPic;
	private Double amount;
	private String note;
	private String date;
	private MedStatus medStatus;
	private String empCode;
	private String nickName;
	
	public MedicalMyRequestDomain() {
		
	}
	
	public MedicalMyRequestDomain(String medID, String empID, String orgID, String slipPic, Double amount, String note,
			String date, MedStatus medStatus, String empCode, String nickName) {
		this.medID = medID;
		this.empID = empID;
		this.orgID = orgID;
		this.slipPic = slipPic;
		this.amount = amount;
		this.note = note;
		this.date = date;
		this.medStatus = medStatus;
		this.empCode = empCode;
		this.nickName = nickName;
	}
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
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
}
