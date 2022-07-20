package com.plusitsolution.timesheet.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.EmployeeDomain;
import com.plusitsolution.timesheet.domain.HolidayDomain;
import com.plusitsolution.timesheet.domain.MedicalDomain;
import com.plusitsolution.timesheet.domain.OrganizeDomain;
import com.plusitsolution.timesheet.domain.OverviewDomain;
import com.plusitsolution.timesheet.domain.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.TimesheetsEnum.DateStatus;
import com.plusitsolution.timesheet.domain.TimesheetsEnum.EmpRole;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.domain.wrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

@Service
@EnableScheduling
public class AdminService {
	
	@Autowired
	private OrganizeRepository orgRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private UtilsService utilService;
	
	//----------- Organization ----------------------

	public void registerOrg(OrgRegisterWrapper wrapper) {
		//save on org DB
		//Map<String, EmpDetailDomain> EMP_MAP =  new HashMap<>();
		OrganizeEntity orgEntity = orgRepository.save(new OrganizeDomain(wrapper.getOrgNameTh(), wrapper.getOrgNameEng(), 
				wrapper.getShortName(), wrapper.getOrgAdress(), wrapper.getOrgPic(), new HashMap<String, EmpDetailDomain>()).toEntity());
		//go regis first admin
		RegisterEmployeeWrapper domain = new RegisterEmployeeWrapper(orgEntity.getOrgID(), wrapper.getPassword(), wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(),
				370, 10000000, "xxxxx", EmpRole.ADMIN);
		registerEmployee(domain);
		
	}
	
	//------------------ Display -----------------
	
	public Map<String, OverviewDomain> getOverView(OrgIDWrapper wrapper){
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , OverviewDomain> OVERVIEW_MAP = new HashMap<>();

		for (String i : EMP_MAP.keySet()) {
//			String empCode, String firstName, String lastName, double leaveLimit, double medFeeLimit,
//			double leaveUse, double medFeeUse, double leaveRemain, double medFeeRemain, LocalDate endContract
			String empCode = EMP_MAP.get(i).getEmpCode();
			OverviewDomain domain = new OverviewDomain(empCode, employeeRepository.findByEmpCode(empCode).getFirstName(), employeeRepository.findByEmpCode(empCode).getLastName(), 
					EMP_MAP.get(i).getLeaveLimit(), EMP_MAP.get(i).getMedFeeLimit(), 
					0.00, 0.00, 0.00, 0.00, "");
			
		}
		
		return OVERVIEW_MAP;
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
		// save to employee DB
		AtomicInteger counter = new AtomicInteger(1+orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP().size());
		String empCode = utilService.generateEmpCode(counter, wrapper.getOrgID());
		//Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		String hashPass = wrapper.getPassword();
		//Map<String , String> MEDFEEUSE_MAP = new HashMap<>();
		
		employeeRepository.save(new EmployeeDomain(wrapper.getOrgID(), empCode, 
				wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(), new HashMap<String , TimesheetsDomain>(), hashPass,new HashMap<String , String>()).toEntity());
		
		// go to add employee to OrgDB EMP_MAP
		OrganizeEntity entity = orgRepository.findById(wrapper.getOrgID()).get();
		
		Map<String, EmpDetailDomain> EMP_MAP =  orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP();
		EmpDetailDomain domain = new EmpDetailDomain(empCode, wrapper.getHolidayID(), wrapper.getLeaveLimit(), wrapper.getMedFeeLimit(), wrapper.getEmpRole(),
				"9999-01-01");
		EMP_MAP.put(employeeRepository.findByEmpCode(empCode).getEmpCode(), domain);
		entity.setEMP_MAP(EMP_MAP);
		orgRepository.save(entity);

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
	
	public void createHolidayType(HolidayWrapper wrapper) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		
		for(int i=0; i<wrapper.getHolidayList().size(); i++) {
			TimesheetsDomain domain = new TimesheetsDomain("-", "-", "-", "-", DateStatus.HOLIDAY);
			TIMESHEETS_MAP.put(wrapper.getHolidayList().get(i), domain);		
		}
		
		holidayRepository.save(new HolidayDomain(wrapper.getHolidayName(), TIMESHEETS_MAP).toEntity());
	}
	
	public void updateHolidayType() {
		
	}
	
	//-------- count leave medFee use
	
	
}
