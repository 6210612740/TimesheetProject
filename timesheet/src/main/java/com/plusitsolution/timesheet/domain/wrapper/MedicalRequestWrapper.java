package com.plusitsolution.timesheet.domain.wrapper;

public class MedicalRequestWrapper {
	
	private String empCode ;
	private String firstName ;
	private String lastName ;
	private Double amount ;
	private String note ;
	private String slipPic ;
	
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
	public String getSlipPic() {
		return slipPic;
	}
	public void setSlipPic(String slip) {
		this.slipPic = slip;
	}
	
	

}
