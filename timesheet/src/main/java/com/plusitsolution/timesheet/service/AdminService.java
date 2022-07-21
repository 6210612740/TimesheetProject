package com.plusitsolution.timesheet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.beanutils.WrapDynaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.HolidayDomain;
import com.plusitsolution.timesheet.domain.Display.OverviewDomain;
import com.plusitsolution.timesheet.domain.Display.SummaryLeaveByMonthDomain;
import com.plusitsolution.timesheet.domain.Display.SummaryMedfeeByMonthDomain;
import com.plusitsolution.timesheet.domain.OrganizeDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpRole;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.TimesheetsStatus;
import com.plusitsolution.timesheet.domain.Medical.MedicalDomain;
import com.plusitsolution.timesheet.domain.Medical.MedicalRequestDomain;
import com.plusitsolution.timesheet.domain.Employee.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.Employee.EmployeeDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsSummaryDomain;
import com.plusitsolution.timesheet.domain.wrapper.HolidayIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDYearWrapper;
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
	
	public Map<String, OverviewDomain> getOverView(OrgIDYearWrapper wrapper){
		
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

	public Map<String,TimesheetsSummaryDomain> getEveryOneTimesheetsSummary(OrgIDMonthWrapper wrapper) {
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , TimesheetsSummaryDomain> EveryOneTimesheetsSummary_MAP = new HashMap<>();
		
		for (String i : EMP_MAP.keySet()) {
			EmployeeEntity entity = employeeRepository.findById(i).get() ;
//			String empCode, String firstName, String lastName, TimesheetsStatus timesheetsStatus,
//			double leaveUse, double totalOT, double totalWork
			TimesheetsSummaryDomain domain = new TimesheetsSummaryDomain(EMP_MAP.get(i).getEmpCode(), entity.getFirstName(), entity.getLastName(), TimesheetsStatus.INCOMPLETED,
					myLeaveDayThisMonth(i, wrapper.getMonth(), wrapper.getYear()), myOTThisMonth(i, wrapper.getMonth(), wrapper.getYear()), myWorkThisMonth(i, wrapper.getMonth(), wrapper.getYear()));
			EveryOneTimesheetsSummary_MAP.put(i, domain);
		}
		
		return EveryOneTimesheetsSummary_MAP;
	}
	
	public Map<String , SummaryLeaveByMonthDomain> getEveryOneLeaveDay(OrgIDYearWrapper wrapper) {
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , SummaryLeaveByMonthDomain> EveryOneSummaryDay_MAP = new HashMap<>();
		
		for (String i : EMP_MAP.keySet()) {
			SummaryLeaveByMonthDomain domain = new SummaryLeaveByMonthDomain(myLeaveDayThisMonth(i, 1, wrapper.getYear()), myLeaveDayThisMonth(i, 2, wrapper.getYear()), myLeaveDayThisMonth(i, 3, wrapper.getYear()),
					myLeaveDayThisMonth(i, 4, wrapper.getYear()), myLeaveDayThisMonth(i, 5, wrapper.getYear()), myLeaveDayThisMonth(i, 6, wrapper.getYear()),
					myLeaveDayThisMonth(i, 7, wrapper.getYear()), myLeaveDayThisMonth(i, 8, wrapper.getYear()), myLeaveDayThisMonth(i, 9, wrapper.getYear()),
					myLeaveDayThisMonth(i, 10, wrapper.getYear()), myLeaveDayThisMonth(i, 11, wrapper.getYear()), myLeaveDayThisMonth(i, 12, wrapper.getYear()));
			EveryOneSummaryDay_MAP.put(i, domain);
		}
		
		return EveryOneSummaryDay_MAP;
	}
	
	public Map<String , SummaryMedfeeByMonthDomain> getEveryOneMedicalFees(OrgIDYearWrapper wrapper) {
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , SummaryMedfeeByMonthDomain> EveryOneSummaryDay_MAP = new HashMap<>();
		
		for (String i : EMP_MAP.keySet()) {
			SummaryMedfeeByMonthDomain domain = new SummaryMedfeeByMonthDomain(myMedfeeThisMonth(i, 1, wrapper.getYear()), myMedfeeThisMonth(i, 2, wrapper.getYear()), myMedfeeThisMonth(i, 3, wrapper.getYear()),
					myMedfeeThisMonth(i, 4, wrapper.getYear()), myMedfeeThisMonth(i, 5, wrapper.getYear()), myMedfeeThisMonth(i, 6, wrapper.getYear()),
					myMedfeeThisMonth(i, 7, wrapper.getYear()), myMedfeeThisMonth(i, 8, wrapper.getYear()), myMedfeeThisMonth(i, 9, wrapper.getYear()),
					myMedfeeThisMonth(i, 10, wrapper.getYear()), myMedfeeThisMonth(i, 11, wrapper.getYear()), myMedfeeThisMonth(i, 12, wrapper.getYear()));
			EveryOneSummaryDay_MAP.put(i, domain);
		}
		return EveryOneSummaryDay_MAP;
	}
	
	public List<MedicalRequestDomain> getEveryOneMedicalFeesRequests(OrgIDYearWrapper wrapper) {
		
		List<MedicalEntity> medList = new ArrayList<MedicalEntity>();
		medList.addAll(medicalRepository.findByOrgID(wrapper.getOrgID())) ;
		System.out.println(PlusJsonUtils.convertToJsonString(medList));
		
		List<MedicalRequestDomain> everyoneList = new ArrayList<MedicalRequestDomain>();

		
		for(int i=0 ; i<medList.size() ; i++) {
			if(LocalDate.parse(medList.get(i).getDate()).getYear() == wrapper.getYear()) {
				

				MedicalRequestDomain domain = new MedicalRequestDomain(employeeRepository.findById(medList.get(i).getEmpID()).get().getEmpCode(),
						employeeRepository.findById(medList.get(i).getEmpID()).get().getNickName(), medList.get(i).getDate(), medList.get(i).getAmount(),
						orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP().get(medList.get(i).getEmpID()).getLeaveLimit()- myMedfeeThisYear(medList.get(i).getEmpID(), wrapper.getYear()), medList.get(i).getMedStatus());
				
				everyoneList.add(domain); 
			}
		}
		return everyoneList;
		
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
        
        updateHolidayToEmp(wrapper.getEmpID(), wrapper.getHolidayID());
    }
	
	//---------- medical --------------------
	
    public void updateMedicalRequestsStatus(UpdateMedicalRequestsStatusWrapper wrapper) {
    	
        MedicalEntity entity = medicalRepository.findById(wrapper.getMedID()).get();
        if (entity == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
        }
        
        entity.setMedStatus(wrapper.getMedStatus());
        medicalRepository.save(entity);
    }
	
	public MedicalEntity getMedicalRequestsDetails(MedicalIDWrapper wrapper) {
		
		MedicalEntity entity = medicalRepository.findById(wrapper.getMedID()).get();
		return entity;
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
	
	public void updateHolidayAllEmpInOrg(String orgID, String holidayID) {
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(orgID).get().getEMP_MAP());
		
		for (String i : EMP_MAP.keySet()) {
			if(EMP_MAP.get(i).getHolidayID().equals(holidayID)) {
				updateHolidayToEmp(i, holidayID);
			}
		}
	}
	
	public void updateHolidayToEmp(String empID, String holidayID) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP());

		List<String> mapKeyList = new ArrayList<String>();
		
		for (String i : TIMESHEETS_MAP.keySet()) {
			System.out.println("1");
			if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.HOLIDAY)) {
				mapKeyList.add(i);
			}
		}
		
		for(int i=0; i<mapKeyList.size() ;i++) {
			TIMESHEETS_MAP.remove(mapKeyList.get(i));
		}
		
		TIMESHEETS_MAP.putAll(holidayRepository.findById(holidayID).get().getHOLIDAY_MAP());
		
		EmployeeEntity entity = employeeRepository.findById(empID).get();
		entity.setTIMESHEETS_MAP(TIMESHEETS_MAP);
		employeeRepository.save(entity);
	}
	
	public Map<String , HolidayDomain> getAllHoliday(OrgIDWrapper wrapper){
		
		List<HolidayEntity> holidayList = holidayRepository.findByOrgID(wrapper.getOrgID());
		Map<String , HolidayDomain> HOLIDAY_MAP = new HashMap<>();
		
		for(int i=0 ; i<holidayList.size() ; i++) {
			HolidayDomain domain = new HolidayDomain(holidayList.get(i).getHolidayName(), holidayList.get(i).getOrgID(), holidayList.get(i).getHOLIDAY_MAP(), holidayList.get(i).getHolidayID());
			
			HOLIDAY_MAP.put(holidayList.get(i).getHolidayID(), domain);
		}
		
		return HOLIDAY_MAP;
	}
	
//	public void updateEmpHoliday2(String empID, String holidayID) {
//		
//		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
//		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP());
//		
//		for (String i : TIMESHEETS_MAP.keySet()) {
//			if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.HOLIDAY)) {
//				TIMESHEETS_MAP.remove(i);
//			}
//		}
//		
//		TIMESHEETS_MAP.putAll(holidayRepository.findById(holidayID).get().getHOLIDAY_MAP());
//		
//		EmployeeEntity entity = employeeRepository.findById(empID).get();
//		entity.setTIMESHEETS_MAP(TIMESHEETS_MAP);
//	}
	
	//-------- count leave medFee use
	
	public Double myLeaveDayThisMonth(String empID, int month, int year) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP()); 
		
		Double totalLeaveThisMonth = 0.0;
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
	
	public Double myLeaveDayThisYear(String empID, int year) {
		
		Double totalLeave = 0.0;
		for(int i=1; i<13; i++) {
			totalLeave += myLeaveDayThisMonth(empID, i, year);
		}
		
		return totalLeave;
	}
	
	public Double myOTThisMonth(String empID, int month, int year) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP()); 
		
		Double totalOTThisMonth = 0.0;
		for (String i : TIMESHEETS_MAP.keySet()) {
			LocalDate date = LocalDate.parse(i);
			if(date.getYear() == year && date.getMonthValue() == month) {
				if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.OT)) {
					totalOTThisMonth += 1;
				}
			}
		}
		
		return totalOTThisMonth;
	}
	
	public Double myWorkThisMonth(String empID, int month, int year) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP()); 
		
		Double totalWorkThisMonth = 0.0;
		for (String i : TIMESHEETS_MAP.keySet()) {
			LocalDate date = LocalDate.parse(i);
			if(date.getYear() == year && date.getMonthValue() == month) {
				if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.WORK)) {
					totalWorkThisMonth += 1;
				}
			}
		}
		
		return totalWorkThisMonth;
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
