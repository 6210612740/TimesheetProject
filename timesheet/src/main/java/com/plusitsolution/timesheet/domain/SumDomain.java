package com.plusitsolution.timesheet.domain;

public class SumDomain {
	private Double totalLeave;
	private Double totalHalfday;
	private Double totalWork;
	private Double totalHoliday;
	private Double totalOT;
	
	public SumDomain() {
		
	}
	
	public SumDomain(Double totalLeave, Double totalHalfday, Double totalWork, Double totalHoliday, Double totalOT) {
		this.totalLeave = totalLeave;
		this.totalHalfday = totalHalfday;
		this.totalWork = totalWork;
		this.totalHoliday = totalHoliday;
		this.totalOT = totalOT;
	}
	
	public Double getTotalLeave() {
		return totalLeave;
	}
	public void setTotalLeave(Double totalLeave) {
		this.totalLeave = totalLeave;
	}
	public Double getTotalHalfday() {
		return totalHalfday;
	}
	public void setTotalHalfday(Double totalHalfday) {
		this.totalHalfday = totalHalfday;
	}
	public Double getTotalWork() {
		return totalWork;
	}
	public void setTotalWork(Double totalWork) {
		this.totalWork = totalWork;
	}
	public Double getTotalHoliday() {
		return totalHoliday;
	}
	public void setTotalHoliday(Double totalHoliday) {
		this.totalHoliday = totalHoliday;
	}
	public Double getTotalOT() {
		return totalOT;
	}
	public void setTotalOT(Double totalOT) {
		this.totalOT = totalOT;
	}
	
	

}
