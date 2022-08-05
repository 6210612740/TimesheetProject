package com.plusitsolution.timesheet.domain.wrapper.MedicalWrapper;

public class MedicalRequestWrapper {
	
	private String empCode ;
	private Double amount ;
	private String note ;
	private String slipPic ;
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
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
