package com.plusitsolution.timesheet.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;

@Document(indexName = "medical-index")
public class MedicalEntity {
	
	@Id
	@ReadOnlyProperty	
	private String medID;
	
	@Field(type = FieldType.Keyword)
	private String empID;
	
	@Field(type = FieldType.Keyword)
	private String orgID;
	
	private String slipPic;
	private Double amount;
	private String note;
	private String date;
	private MedStatus medStatus ;
	
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
	

}
