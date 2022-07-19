package com.plusitsolution.timesheet.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.plusitsolution.timesheet.domain.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.EmployeeDomain;
import com.plusitsolution.timesheet.domain.MedicalDomain;
import com.plusitsolution.timesheet.domain.OrganizeDomain;
import com.plusitsolution.timesheet.domain.OverviewDomain;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.domain.wrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

@Service
@EnableScheduling
public class AdminService {
	
	@Autowired
	private OrganizeRepository orgRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	
	//----------- Organization ----------------------

	public void registerOrg(OrgRegisterWrapper wrapper) {
		
		Map<String, EmpDetailDomain> EMP_MAP =  new HashMap<>();
		orgRepository.save(new OrganizeDomain(wrapper.getOrgNameTh(), wrapper.getOrgNameEng(), 
				wrapper.getShortName(), wrapper.getOrgAdress(), wrapper.getOrgPic(), EMP_MAP).toEntity());
		
//		employeeRepository.save(new EmployeeDomain());
	}
	
	//------------------ Display -----------------
	
	public Map<String, OverviewDomain> getOverView(OrgIDWrapper wrapper){
		
		return null;
	}
	
	public Map<String,EmpDetailDomain> getEveryOneTimesheetsSummary(OrgIDWrapper wrapper) {
		
		return null;
	}
	
	public void getEveryOneLeaveDay(OrgIDWrapper wrapper) {
		
	}
	
	public void getEveryOneMedicalFees(OrgIDWrapper wrapper) {
		
	}
	
	//---------- Employee ---------------
	
	public void registerEmployee() {
			
	}
	
	public void updateUserProfile() {
		
	}
	
	//---------- medical --------------------
	
	public void updateMedicalRequestsStatus() {
		
	}
	
	public MedicalDomain getMedicalRequestsDetails() {
		
		return null;
	}
	
	//-------- holiday -------------------
	
	public void createHolidayType() {
		
	}
	
	public void updateHolidayType() {
		
	}
	
	
	
}
