package com.plusitsolution.timesheet.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.plusitsolution.timesheet.domain.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.EmployeeDomain;
import com.plusitsolution.timesheet.domain.MedicalDomain;
import com.plusitsolution.timesheet.domain.OrganizeDomain;
import com.plusitsolution.timesheet.domain.OverviewDomain;
import com.plusitsolution.timesheet.domain.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.TimesheetsEnum.EmpRole;
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
	@Autowired
	private UtilsService utilService;
	
	//----------- Organization ----------------------

	public void registerOrg(OrgRegisterWrapper wrapper) {
		
		Map<String, EmpDetailDomain> EMP_MAP =  new HashMap<>();
		orgRepository.save(new OrganizeDomain(wrapper.getOrgNameTh(), wrapper.getOrgNameEng(), 
				wrapper.getShortName(), wrapper.getOrgAdress(), wrapper.getOrgPic(), EMP_MAP).toEntity());
		
		
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
	
	public void registerEmployee(RegisterEmployeeWrapper wrapper) {
		AtomicInteger counter = new AtomicInteger(1);
		String empCode = utilService.generateEmpCode(counter, wrapper.getOrgID());
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		String hashPass = wrapper.getPassword();
		Map<String , String> MEDFEEUSE_MAP = new HashMap<>();
		
		employeeRepository.save(new EmployeeDomain(orgRepository.findByOrgNameTh(wrapper.getOrgID()).getOrgID(), empCode, 
				wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(), TIMESHEETS_MAP, hashPass, MEDFEEUSE_MAP).toEntity());
		
		OrganizeEntity entity = orgRepository.findById(wrapper.getOrgID()).get();
		
		Map<String, EmpDetailDomain> EMP_MAP =  orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP();
		EmpDetailDomain domain = new EmpDetailDomain(empCode, wrapper.getHolidayID(), wrapper.getLeaveLimit(), wrapper.getMedFeeLimit(), wrapper.getEmpRole(),
				LocalDate.parse("9999-01-01"));
		EMP_MAP.put(employeeRepository.findByEmpCode(empCode).getEmpCode(), domain);
		
		entity.setEMP_MAP(EMP_MAP);
		
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
