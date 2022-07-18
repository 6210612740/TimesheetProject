package com.plusitsolution.timesheet.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.plusitsolution.timesheet.domain.EmpDetailDomain;

@Document(indexName = "organize-index")
public class OrganizeEntity {
	@Id
	@ReadOnlyProperty
	private String orgID ;
	
	@Field(type = FieldType.Keyword)
	private String orgNameTh ;
	@Field(type = FieldType.Keyword)
	private String orgNameEng ;
	@Field(type = FieldType.Keyword)
	private String shortName ;
	private String orgAdress ;
	private String orgPic ;
	
	private Map<String , EmpDetailDomain> EMP_MAP =  new HashMap<>();
	
	
	
	public String getOrgID() {
		return orgID;
	}
	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}
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
