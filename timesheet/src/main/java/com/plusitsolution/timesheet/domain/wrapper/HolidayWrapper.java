package com.plusitsolution.timesheet.domain.wrapper;

import java.util.ArrayList;
import java.util.List;

public class HolidayWrapper {
	
	private String holidayName;
	private String orgID;
	private List<String> holidayList = new ArrayList<String>();
	
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}
	public List<String> getHolidayList() {
		return holidayList;
	}
	public void setHolidayList(List<String> holidayList) {
		this.holidayList = holidayList;
	}
	public String getOrgID() {
		return orgID;
	}
	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}
	
	
	

}
