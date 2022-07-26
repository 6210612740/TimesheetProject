package com.plusitsolution.timesheet.domain.Display;

public class SummaryByMonthDomain {
	
	private String empCode;
	private String nickName;
	private Double total;
	private Double jan;
	private Double feb;
	private Double mar;
	private Double apr;
	private Double may;
	private Double jun;
	private Double jul;
	private Double aug;
	private Double sep;
	private Double oct;
	private Double nov;
	private Double dec;

	
	public SummaryByMonthDomain() {
		
	}

	public SummaryByMonthDomain(String empCode, String nickName, Double total,Double jan, Double feb, Double mar, Double apr, Double may, Double jun, Double jul,
			Double aug, Double sep, Double oct, Double nov, Double dec) {
		this.empCode = empCode;
		this.nickName = nickName;
		this.total = total;
		this.jan = jan;
		this.feb = feb;
		this.mar = mar;
		this.apr = apr;
		this.may = may;
		this.jun = jun;
		this.jul = jul;
		this.aug = aug;
		this.sep = sep;
		this.oct = oct;
		this.nov = nov;
		this.dec = dec;
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
	

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Double getJan() {
		return jan;
	}

	public void setJan(Double jan) {
		this.jan = jan;
	}

	public Double getFeb() {
		return feb;
	}

	public void setFeb(Double feb) {
		this.feb = feb;
	}

	public Double getMar() {
		return mar;
	}

	public void setMar(Double mar) {
		this.mar = mar;
	}

	public Double getApr() {
		return apr;
	}

	public void setApr(Double apr) {
		this.apr = apr;
	}

	public Double getMay() {
		return may;
	}

	public void setMay(Double may) {
		this.may = may;
	}

	public Double getJun() {
		return jun;
	}

	public void setJun(Double jun) {
		this.jun = jun;
	}

	public Double getJul() {
		return jul;
	}

	public void setJul(Double jul) {
		this.jul = jul;
	}

	public Double getAug() {
		return aug;
	}

	public void setAug(Double aug) {
		this.aug = aug;
	}

	public Double getSep() {
		return sep;
	}

	public void setSep(Double sep) {
		this.sep = sep;
	}

	public Double getOct() {
		return oct;
	}

	public void setOct(Double oct) {
		this.oct = oct;
	}

	public Double getNov() {
		return nov;
	}

	public void setNov(Double nov) {
		this.nov = nov;
	}

	public Double getDec() {
		return dec;
	}

	public void setDec(Double dec) {
		this.dec = dec;
	}
	
	
	
	
	

}
