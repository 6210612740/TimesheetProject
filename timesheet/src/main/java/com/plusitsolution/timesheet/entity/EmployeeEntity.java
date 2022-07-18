package com.plusitsolution.timesheet.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Document;

import com.plusitsolution.timesheet.domain.TimesheetsDomain;
@Document(indexName = "employee-index")
public class EmployeeEntity {
	
	private String empID ; 
	private String orgID ;
	private String empCode ;
	private String firstName;
	private String lastName;
	private String nickName ;
	private Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
	private String username ;
	private String password ;
	private Map<String , String> MEDFEEUSE_MAP = new HashMap<>();
	
	
	
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Map<String, TimesheetsDomain> getTIMESHEETS_MAP() {
		return TIMESHEETS_MAP;
	}
	public void setTIMESHEETS_MAP(Map<String, TimesheetsDomain> tIMESHEETS_MAP) {
		TIMESHEETS_MAP = tIMESHEETS_MAP;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Map<String, String> getMEDFEEUSE_MAP() {
		return MEDFEEUSE_MAP;
	}
	public void setMEDFEEUSE_MAP(Map<String, String> mEDFEEUSE_MAP) {
		MEDFEEUSE_MAP = mEDFEEUSE_MAP;
	}

	
	

}
