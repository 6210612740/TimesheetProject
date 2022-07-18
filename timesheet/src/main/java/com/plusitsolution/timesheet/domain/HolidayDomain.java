package com.plusitsolution.timesheet.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class HolidayDomain {
	
	
	private Map<String, TimesheetsDomain>  HOLIDAY_MAP =  new HashMap<>(); 
	private String holidayName;
	
	
	public Map<String, TimesheetsDomain> getHOLIDAY_MAP() {
		return HOLIDAY_MAP;
	}
	public void setHOLIDAY_MAP(Map<String, TimesheetsDomain> hOLIDAY_MAP) {
		HOLIDAY_MAP = hOLIDAY_MAP;
	}
	public String getHolidayName() {
		return holidayName;
	}
	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

}
