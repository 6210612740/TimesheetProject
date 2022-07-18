package com.plusitsolution.timesheet.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class OrganizeDomain {
	
	
	private String orgNameTh ;
	private String orgNameEng ;
	private String shortName ;
	private String orgAdress ;
	private String orgPic ;
	private Map<String , EmpDetailDomain> EMP_MAP =  new HashMap<>();
	
	
	
	public String getOrgNameTh() {
		return orgNameTh;
	}
	public void setOrgNameTh(String orgNameTh) {
		this.orgNameTh = orgNameTh;
	}
	public String getOrgNameEng() {
		return orgNameEng;
	}
	public void setOrgNameEng(String orgNameEng) {
		this.orgNameEng = orgNameEng;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getOrgAdress() {
		return orgAdress;
	}
	public void setOrgAdress(String orgAdress) {
		this.orgAdress = orgAdress;
	}
	public String getOrgPic() {
		return orgPic;
	}
	public void setOrgPic(String orgPic) {
		this.orgPic = orgPic;
	}
	public Map<String, EmpDetailDomain> getEMP_MAP() {
		return EMP_MAP;
	}
	public void setEMP_MAP(Map<String, EmpDetailDomain> eMP_MAP) {
		EMP_MAP = eMP_MAP;
	}
	
	

}
