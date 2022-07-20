package com.plusitsolution.timesheet.domain.Employee;

import java.util.HashMap;
import java.util.Map;

import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;

public class EmployeeDomain {
	
	private String empID ; 
	private String orgID ;
	private String empCode ;
	private String firstName;
	private String lastName;
	private String nickName ;
	private Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
	private String password ;
	private Map<String , String> MEDFEEUSE_MAP = new HashMap<>();
	
	public EmployeeDomain() {
		
	}
	
	public EmployeeDomain(String orgID, String empCode, String firstName, String lastName,
			String nickName, Map<String, TimesheetsDomain> TIMESHEETS_MAP, String password,
			Map<String, String> MEDFEEUSE_MAP) {
		this.orgID = orgID;
		this.empCode = empCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickName = nickName;
		this.TIMESHEETS_MAP = TIMESHEETS_MAP;
		this.password = password;
		this.MEDFEEUSE_MAP = MEDFEEUSE_MAP;
	}
	
	public EmployeeEntity toEntity() {
		EmployeeEntity entity = new EmployeeEntity();
		entity.setOrgID(this.orgID);
		entity.setEmpCode(this.empCode);
		entity.setFirstName(this.firstName);
		entity.setLastName(this.lastName);
		entity.setNickName(this.nickName);
		entity.setTIMESHEETS_MAP(this.TIMESHEETS_MAP);
		entity.setPassword(this.password);
		entity.setMEDFEEUSE_MAP(this.MEDFEEUSE_MAP);
		return entity;
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
