package com.plusitsolution.timesheet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.plusitsolution.timesheet.domain.wrapper.HolidayIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.domain.wrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateMedicalRequestsStatusWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateUserProfileWrapper;
import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.HolidayEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
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
	private MedicalRepository medicalRepository;
	@Autowired
	private UtilsService utilService;
	
	//----------- Organization ----------------------

	public void registerOrg(OrgRegisterWrapper wrapper) {
		
		//save on org DB
		OrganizeEntity orgEntity = orgRepository.save(new OrganizeDomain(wrapper.getOrgNameTh(), wrapper.getOrgNameEng(), 
				wrapper.getShortName(), wrapper.getOrgAdress(), wrapper.getOrgPic(), new HashMap<String, EmpDetailDomain>()).toEntity());
		//add DEFUALT holiday to this org
		List<String> holidayList = new ArrayList<String>();
		holidayList.add(LocalDate.now().getYear()+"-01-01");
		holidayList.add(LocalDate.now().getYear()+"-12-31");
		createHolidayType(new HolidayWrapper("DEFAULT "+wrapper.getShortName(), orgEntity.getOrgID(), holidayList));

		//go regis first admin
		RegisterEmployeeWrapper domain = new RegisterEmployeeWrapper(orgEntity.getOrgID(), wrapper.getPassword(), wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(),
				370, 10000000, holidayRepository.findByHolidayName("DEFAULT "+wrapper.getShortName()).getHolidayID(), EmpRole.ADMIN);
		registerEmployee(domain);
		
	}
	
	//------------------ Display -----------------
	
	public Map<String, OverviewDomain> getOverView(OrgIDWrapper wrapper){
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , OverviewDomain> OVERVIEW_MAP = new HashMap<>();

		for (String i : EMP_MAP.keySet()) {
			String empCode = EMP_MAP.get(i).getEmpCode();
			OverviewDomain domain = new OverviewDomain(empCode, employeeRepository.findByEmpCode(empCode).getFirstName(), employeeRepository.findByEmpCode(empCode).getLastName(), 
					EMP_MAP.get(i).getLeaveLimit(), EMP_MAP.get(i).getMedFeeLimit(), 
					myLeaveDayThisYear(i, wrapper.getYear()), myMedfeeThisYear(i, wrapper.getYear()),
					EMP_MAP.get(i).getLeaveLimit()-myLeaveDayThisYear(i, wrapper.getYear()), EMP_MAP.get(i).getMedFeeLimit()-myMedfeeThisYear(i, wrapper.getYear()),
					EMP_MAP.get(i).getEndContract());
			OVERVIEW_MAP.put(i, domain);
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
		String hashPass = wrapper.getPassword();
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(holidayRepository.findById(wrapper.getHolidayID()).get().getHOLIDAY_MAP());
		
		EmployeeDomain empDomain = new EmployeeDomain(wrapper.getOrgID(), empCode, 
				wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(), TIMESHEETS_MAP, hashPass,new HashMap<String , String>());
		
		employeeRepository.save(empDomain.toEntity());
		
		// go to add employee to OrgDB EMP_MAP
		OrganizeEntity entity = orgRepository.findById(wrapper.getOrgID()).get();
		
		Map<String, EmpDetailDomain> EMP_MAP =  orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP();
		EmpDetailDomain domain = new EmpDetailDomain(empCode, wrapper.getHolidayID(), wrapper.getLeaveLimit(), wrapper.getMedFeeLimit(), wrapper.getEmpRole(),
				"9999-01-01");
		EMP_MAP.put(employeeRepository.findByEmpCode(empCode).getEmpID(), domain);
		entity.setEMP_MAP(EMP_MAP);
		orgRepository.save(entity);

	}
	
	public void updateUserProfile(UpdateUserProfileWrapper wrapper) {
        EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
        if (employeeEntity == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
        }

        employeeEntity.setFirstName(wrapper.getFirstName());
        employeeEntity.setLastName(wrapper.getLastName());
        employeeEntity.setNickName(wrapper.getNickName());

        OrganizeEntity orgEntity = orgRepository.findById(employeeEntity.getOrgID()).get();
        EmpDetailDomain domain =  orgEntity.getEMP_MAP().get(employeeEntity.getEmpID());
        domain.setEmpRole(wrapper.getEmpRole());
        domain.setHolidayID(wrapper.getHolidayID());
        domain.setLeaveLimit(wrapper.getLeaveLimit());
        domain.setMedFeeLimit(wrapper.getMedFeeLimit());

        employeeRepository.save(employeeEntity);
        orgRepository.save(orgEntity);
    }
	
	//---------- medical --------------------
	
    public void updateMedicalRequestsStatus(UpdateMedicalRequestsStatusWrapper wrapper) {
        MedicalEntity entity = medicalRepository.findById(wrapper.getEmpID()).get();
        if (entity == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
        }
        
        entity.setMedStatus(wrapper.getMedStatus());
        medicalRepository.save(entity);
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
		
		holidayRepository.save(new HolidayDomain(wrapper.getHolidayName(), wrapper.getOrgID(), TIMESHEETS_MAP).toEntity());
	}
	
	public void updateHolidayType(HolidayUpdateWrapper wrapper) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		
		for(int i=0; i<wrapper.getHolidayList().size(); i++) {
			TimesheetsDomain domain = new TimesheetsDomain("-", "-", "-", "-", DateStatus.HOLIDAY);
			TIMESHEETS_MAP.put(wrapper.getHolidayList().get(i), domain);		
		}
		
		HolidayEntity entity = holidayRepository.findById(wrapper.getHolidayID()).get();
		entity.setHOLIDAY_MAP(TIMESHEETS_MAP);
		holidayRepository.save(entity);
		
		// update to who use this holidayID
		updateHolidayAllEmpInOrg(holidayRepository.findById(wrapper.getHolidayID()).get().getOrgID(), wrapper.getHolidayID());
	}
	
	public void updateEmpHoliday(String empID, String holidayID) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP());
		TIMESHEETS_MAP.putAll(holidayRepository.findById(holidayID).get().getHOLIDAY_MAP());
		
		EmployeeEntity entity = employeeRepository.findById(empID).get();
		entity.setTIMESHEETS_MAP(TIMESHEETS_MAP);
		employeeRepository.save(entity);
	}
	
	public void updateHolidayAllEmpInOrg(String orgID, String holidayID) {
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(orgID).get().getEMP_MAP());
		
		for (String i : EMP_MAP.keySet()) {
			if(EMP_MAP.get(i).getHolidayID().equals(holidayID)) {
				updateEmpHoliday(i, holidayID);
			}
		}
		  
		
	}
	
	//-------- count leave medFee use
	
	public Integer myLeaveDayThisMonth(String empID, int month, int year) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP()); 
		
		int totalLeaveThisMonth = 0;
		for (String i : TIMESHEETS_MAP.keySet()) {
			LocalDate date = LocalDate.parse(i);
			if(date.getYear() == year && date.getMonthValue() == month) {
				if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.LEAVE)) {
					totalLeaveThisMonth += 1;
				}
			}
		}
		
		return totalLeaveThisMonth;
	}
	
	public Integer myLeaveDayThisYear(String empID, int year) {
		
		int totalLeave = 0;
		for(int i=1; i<13; i++) {
			totalLeave += myLeaveDayThisMonth(empID, i, year);
		}
		
		return totalLeave;
	}
	
	public Double myMedfeeThisMonth(String empID, int month, int year) {
		
		Map<String , String> MEDFEEUSE_MAP = new HashMap<>();
		MEDFEEUSE_MAP.putAll(employeeRepository.findById(empID).get().getMEDFEEUSE_MAP()); 
		
		Double totalMedfeeThisMonth = 0.00;
		for (String i : MEDFEEUSE_MAP.keySet()) {
			LocalDateTime date = LocalDateTime.parse(i);
			if(date.getYear() == year && date.getMonthValue() == month) {
				totalMedfeeThisMonth += medicalRepository.findById(MEDFEEUSE_MAP.get(i)).get().getAmount();
			}
		}
		
		return totalMedfeeThisMonth;
	}
	
	public Double myMedfeeThisYear(String empID, int year) {
		
		Double totalMedfee = 0.00;
		for(int i=1; i<13; i++) {
			totalMedfee += myMedfeeThisMonth(empID, i, year);
		}
		
		return totalMedfee;
	}
	
	
}
