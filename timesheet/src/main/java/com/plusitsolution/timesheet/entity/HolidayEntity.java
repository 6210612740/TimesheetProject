package com.plusitsolution.timesheet.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.plusitsolution.timesheet.domain.TimesheetsDomain;



@Document(indexName = "holiday-index")
public class HolidayEntity {
	@Id
	@ReadOnlyProperty
	private String holidayID ;
	
	@Field(type = FieldType.Keyword)
	private Map<String, TimesheetsDomain>  HOLIDAY_MAP =  new HashMap<>(); 
	@Field(type = FieldType.Keyword)
	private String holidayName;
	
	
	public String getHolidayID() {
		return holidayID;
	}
	public void setHolidayID(String holidayID) {
		this.holidayID = holidayID;
	}
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
