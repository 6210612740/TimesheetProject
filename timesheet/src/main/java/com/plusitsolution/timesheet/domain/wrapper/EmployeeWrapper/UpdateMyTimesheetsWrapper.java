package com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper;

import java.util.HashMap;
import java.util.Map;

import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;

public class UpdateMyTimesheetsWrapper {
	
	private String empID ;
	private Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
	
	
	public String getEmpID() {
		return empID;
	}
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	public Map<String, TimesheetsDomain> getTIMESHEETS_MAP() {
		return TIMESHEETS_MAP;
	}
	public void setTIMESHEETS_MAP(Map<String, TimesheetsDomain> tIMESHEETS_MAP) {
		TIMESHEETS_MAP = tIMESHEETS_MAP;
	}
	

}
