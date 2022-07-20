package com.plusitsolution.timesheet.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.plusitsolution.timesheet.entity.HolidayEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;

public class HolidayDomain {
	
	private String holidayID ;
	private String orgID ;
	private String holidayName;
	private Map<String, TimesheetsDomain>  HOLIDAY_MAP =  new HashMap<>(); 
	
	public HolidayDomain() {
		
	}
	
	public HolidayDomain(String holidayName, String orgID,Map<String, TimesheetsDomain> hOLIDAY_MAP) {
		this.HOLIDAY_MAP = hOLIDAY_MAP;
		this.orgID = orgID;
		this.holidayName = holidayName;
	}
	
	public HolidayEntity toEntity() {
		HolidayEntity entity = new HolidayEntity();
		entity.setHOLIDAY_MAP(this.HOLIDAY_MAP);
		entity.setOrgID(this.orgID);
		entity.setHolidayName(this.holidayName);
		return entity;
	}
	
	public String getHolidayID() {
		return holidayID;
	}

	public void setHolidayID(String holidayID) {
		this.holidayID = holidayID;
	}
	
	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
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
