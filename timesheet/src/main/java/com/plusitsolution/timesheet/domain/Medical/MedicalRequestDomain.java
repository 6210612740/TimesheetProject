package com.plusitsolution.timesheet.domain.Medical;

import java.time.LocalDate;

import com.plusitsolution.timesheet.domain.EnumDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;

public class MedicalRequestDomain {
	
	private String empCode ;
	private String nickName ;
	private String date;
	private double requestMedicalFee ;
	private double currentMedicalFee ;
	private MedStatus medStatus ;
	
	public MedicalRequestDomain() {
		
	}
	
	public MedicalRequestDomain(String empCode, String nickName, String date, double requestMedicalFee,
			double currentMedicalFee, MedStatus medStatus) {
		this.empCode = empCode;
		this.nickName = nickName;
		this.date = date;
		this.requestMedicalFee = requestMedicalFee;
		this.currentMedicalFee = currentMedicalFee;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getRequestMedicalFee() {
		return requestMedicalFee;
	}
	public void setRequestMedicalFee(double requestMedicalFee) {
		this.requestMedicalFee = requestMedicalFee;
	}
	public double getCurrentMedicalFee() {
		return currentMedicalFee;
	}
	public void setCurrentMedicalFee(double currentMedicalFee) {
		this.currentMedicalFee = currentMedicalFee;
	}
	public MedStatus getMedStatus() {
		return medStatus;
	}
	public void setMedStatus(MedStatus medStatus) {
		this.medStatus = medStatus;
	}
	
	

}
