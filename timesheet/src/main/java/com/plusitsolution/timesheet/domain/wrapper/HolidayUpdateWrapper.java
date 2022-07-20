package com.plusitsolution.timesheet.domain.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;

public class HolidayUpdateWrapper {

	private String holidayID;
	private List<String> holidayList = new ArrayList<String>();
	
	public String getHolidayID() {
		return holidayID;
	}
	public void setHolidayID(String holidayID) {
		this.holidayID = holidayID;
	}
	public List<String> getHolidayList() {
		return holidayList;
	}
	public void setHolidayList(List<String> holidayList) {
		this.holidayList = holidayList;
	}


	
}
