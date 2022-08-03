package com.plusitsolution.timesheet.service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.xddf.usermodel.SystemColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.timesheet.domain.Employee.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.Employee.EmployeeDomain;
import com.plusitsolution.timesheet.domain.Timesheet.MyTimesheetExcelDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpRole;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.TimesheetsStatus;
import com.plusitsolution.common.toolkit.PlusExcelUtils;
import com.plusitsolution.common.toolkit.PlusHashUtils;
import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.Medical.MedicalDomain;
import com.plusitsolution.timesheet.domain.Medical.MedicalMyRequestDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeLoginWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeProfileDomain;
import com.plusitsolution.timesheet.domain.wrapper.MedicalRequestWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateMyTimesheetsWrapper;
import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;
import com.plusitsolution.zeencommon.helper.ExcelBuilder;
import com.plusitsolution.zeencommon.helper.ExcelUtils;

@Service
@EnableScheduling
public class EmployeeService {
	
	@Autowired
	private OrganizeRepository orgRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private MedicalRepository medicalRepository ;
	@Autowired
	private UtilsService utilService;
	@Autowired
	private ThrowService throwService ;
	
	Map<String, Integer> MONTH_MAP = new HashMap<String, Integer>();
	
	@PostConstruct
	public void monthMap() {
		MONTH_MAP.put("01", 31);
		MONTH_MAP.put("02", 28);
		MONTH_MAP.put("03", 31);
		MONTH_MAP.put("04", 30);
		MONTH_MAP.put("05", 31);
		MONTH_MAP.put("06", 30);
		MONTH_MAP.put("07", 31);
		MONTH_MAP.put("08", 31);
		MONTH_MAP.put("09", 30);
		MONTH_MAP.put("10", 31);
		MONTH_MAP.put("11", 30);
		MONTH_MAP.put("12", 31);
	}
	
	//----------------------- emp ------------------
	
	public EmployeeProfileDomain loginEmp(EmployeeLoginWrapper wrapper) {
		throwService.checkUsername(wrapper.getUsername());
		if (!PlusHashUtils.hash(wrapper.getPassword()).equals(employeeRepository.findByUsername(wrapper.getUsername()).getPassword()) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
		}
		
		EmployeeEntity employeeEntity = employeeRepository.findByUsername(wrapper.getUsername());
		
		Map<String , EmpDetailDomain> EMP_MAP = orgRepository.findById(employeeEntity.getOrgID()).get().getEMP_MAP();
		
		EmployeeProfileDomain domain = new EmployeeProfileDomain(employeeEntity.getEmpID(), employeeEntity.getOrgID(), employeeEntity.getEmpCode(), employeeEntity.getFirstName(),
				employeeEntity.getLastName(), employeeEntity.getNickName(), employeeEntity.getUsername(),EMP_MAP.get(employeeEntity.getEmpID()).getHolidayID(), EMP_MAP.get(employeeEntity.getEmpID()).getLeaveLimit(),
				EMP_MAP.get(employeeEntity.getEmpID()).getMedFeeLimit(), EMP_MAP.get(employeeEntity.getEmpID()).getEmpRole(), EMP_MAP.get(employeeEntity.getEmpID()).getEndContract(),
				utilService.myLeaveDayThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()), utilService.myMedfeeThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()),
				EMP_MAP.get(employeeEntity.getEmpID()).getLeaveLimit()- utilService.myLeaveDayThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()),
				EMP_MAP.get(employeeEntity.getEmpID()).getMedFeeLimit()- utilService.myMedfeeThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()), 
				orgRepository.findById(employeeEntity.getOrgID()).get().getOrgNameEng(), orgRepository.findById(employeeEntity.getOrgID()).get().getOrgNameTh());
		
		
		return domain;
	}
	
	public EmployeeProfileDomain getUserProfile(EmployeeIDWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());

		throwService.checkEmployee(wrapper.getEmpID());
		EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
		Map<String , EmpDetailDomain> EMP_MAP = orgRepository.findById(employeeEntity.getOrgID()).get().getEMP_MAP();
		
		EmployeeProfileDomain domain = new EmployeeProfileDomain(employeeEntity.getEmpID(), employeeEntity.getOrgID(), employeeEntity.getEmpCode(), employeeEntity.getFirstName(),
				employeeEntity.getLastName(), employeeEntity.getNickName(), employeeEntity.getUsername(), EMP_MAP.get(employeeEntity.getEmpID()).getHolidayID(), EMP_MAP.get(employeeEntity.getEmpID()).getLeaveLimit(),
				EMP_MAP.get(employeeEntity.getEmpID()).getMedFeeLimit(), EMP_MAP.get(employeeEntity.getEmpID()).getEmpRole(), EMP_MAP.get(employeeEntity.getEmpID()).getEndContract(),
				utilService.myLeaveDayThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()), utilService.myMedfeeThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()),
				EMP_MAP.get(employeeEntity.getEmpID()).getLeaveLimit()- utilService.myLeaveDayThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()),
				EMP_MAP.get(employeeEntity.getEmpID()).getMedFeeLimit()- utilService.myMedfeeThisYear(employeeEntity.getEmpID(), LocalDate.now().getYear()), 
				orgRepository.findById(employeeEntity.getOrgID()).get().getOrgNameEng(), orgRepository.findById(employeeEntity.getOrgID()).get().getOrgNameTh());

		
		return domain;
	}
	
	public Map<String, TimesheetsDomain> getMyTimesheetMonth(EmployeeIDMonthWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		throwService.checkMonth(wrapper.getMonth());
		throwService.checkYear(wrapper.getYear());
		
		EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
		Map<String, TimesheetsDomain> MYTIMESHEETS_MAP = new HashMap<>();
		
		for (String i : employeeEntity.getTIMESHEETS_MAP().keySet()) {
			LocalDate myObj = LocalDate.parse(i);
			if (myObj.getMonthValue() == wrapper.getMonth() && myObj.getYear() == wrapper.getYear()){
				
				TimesheetsDomain timesheetsDomain = employeeEntity.getTIMESHEETS_MAP().get(i)	;	
				MYTIMESHEETS_MAP.put(i, timesheetsDomain);	
			}
		}
		return MYTIMESHEETS_MAP;
	}
	
	public Map<String, MedicalMyRequestDomain> geMyMedRequests(EmployeeIDWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		
		EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
		
		Map<String, MedicalMyRequestDomain> MYMEDFEE_MAP = new HashMap<>();
		for (String i : employeeEntity.getMEDFEEUSE_MAP().keySet()) {
			
			MedicalEntity entity = medicalRepository.findById(employeeEntity.getMEDFEEUSE_MAP().get(i)).get();

			MedicalMyRequestDomain domain = new MedicalMyRequestDomain(employeeEntity.getMEDFEEUSE_MAP().get(i), entity.getEmpID(), entity.getOrgID(), entity.getSlipPic(),
					entity.getAmount(), entity.getNote(), entity.getDate(), entity.getMedStatus(), employeeRepository.findById(entity.getEmpID()).get().getEmpCode(), employeeRepository.findById(entity.getEmpID()).get().getNickName());
			
			MYMEDFEE_MAP.put(i, domain);
		}
		
		return MYMEDFEE_MAP;
	}
	
	//-------------- Medical
	
	public void addMedRequests(MedicalRequestWrapper wrapper) {
		throwService.checkEmployeeByEmpCode(wrapper.getEmpCode());
		throwService.checkAmount(wrapper.getAmount());
		
		
		EmployeeEntity employeeEntity = employeeRepository.findByEmpCode(wrapper.getEmpCode());
		MedicalDomain medicalDomain = new MedicalDomain(employeeEntity.getEmpID() , employeeEntity.getOrgID() , wrapper.getSlipPic() , wrapper.getAmount() , 
				wrapper.getNote() , LocalDate.now().toString() , MedStatus.INPROCESS );
		
		MedicalEntity medicalEntity = medicalRepository.save(medicalDomain.toEntity()) ;
		
		employeeEntity.getMEDFEEUSE_MAP().put(LocalDateTime.now().toString(), medicalEntity.getMedID());
		employeeRepository.save(employeeEntity);
		
	}
	
	//------------ Timesheet
	
	public void updateMyTimesheets(UpdateMyTimesheetsWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		
        EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
 
        for (String i : wrapper.getTIMESHEETS_MAP().keySet()) {
        	 TimesheetsDomain domain = wrapper.getTIMESHEETS_MAP().get(i);
        	if(wrapper.getTIMESHEETS_MAP().get(i).getDateStatus().equals(DateStatus.RECORD)) {
        		domain.setDateStatus(checkdateStatus(domain.getTimeIn(), domain.getTimeOut()));
        		domain.setWorkingTime((double)utilService.compareTime(domain.getTimeIn(), domain.getTimeOut()));
        	}
        	if(wrapper.getTIMESHEETS_MAP().get(i).getDateStatus().equals(DateStatus.LEAVE)) {
        		domain.setActivity("leave");
        		domain.setTimeIn("-");
        		domain.setTimeOut("-");
        		domain.setProject("-");
        	}
        	
        }
        
        Map<String , TimesheetsStatus> timesheetStatus_MAP  = employeeEntity.getTimesheetStatus_MAP();
        
        for(int i=0; i<12; i++) {
        	
        	if(!(employeeEntity.getTimesheetStatus_MAP().get(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01").equals(TimesheetsStatus.APPROVE) || 
        			employeeEntity.getTimesheetStatus_MAP().get(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01").equals(TimesheetsStatus.REJECT))) {
        
        		if(employeeEntity.getTimesheetStatus_MAP().get(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01").equals(null)) {
        			timesheetStatus_MAP.put(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01", 
        					TimesheetsStatus.INCOMPLETED);
        		} else {
        			timesheetStatus_MAP.put(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01",
        					myTimesheetStatus(wrapper.getEmpID(), i+1, LocalDate.now().getYear()));
        		}
        	}
        }
        
        employeeEntity.setTimesheetStatus_MAP(timesheetStatus_MAP);
        employeeEntity.getTIMESHEETS_MAP().putAll(wrapper.getTIMESHEETS_MAP());
        employeeRepository.save(employeeEntity);
        System.out.println("------------- update my timesheet ---------------------");
    }
	
	//------------- cal
	
	public DateStatus checkdateStatus(String timein, String timeout) {
		
		if( LocalTime.parse(timeout).getHour() - LocalTime.parse(timein).getHour() > 1) {
			return DateStatus.WORK;
//		} else if(LocalTime.parse(timeout).getHour() - LocalTime.parse(timein).getHour() < 8 && LocalTime.parse(timeout).getHour() - LocalTime.parse(timein).getHour() > 4) {
//			return DateStatus.HALFDAY;
//		} else if(LocalTime.parse(timeout).getHour() - LocalTime.parse(timein).getHour() > 8) {
//			return DateStatus.OT;
		} else 
			return DateStatus.LEAVE;

	}
	
	
	public TimesheetsStatus myTimesheetStatus(String empID, int month, int year) {
			
					Map<String , TimesheetsDomain> TIMESHEETS_MAP = employeeRepository.findById(empID).get().getTIMESHEETS_MAP();
		
					int count = 0;
					for (String i : TIMESHEETS_MAP.keySet()) {

						if(LocalDate.parse(i).getYear() == year && LocalDate.parse(i).getMonthValue() == month) {
							count++;
						}
					}
					
					int montDate = MONTH_MAP.get(""+utilService.paddding(month));
					if(year % 4 == 0 && month == 2) {
						montDate += 1;
					}

					if(count == montDate) {
						return TimesheetsStatus.COMPLETED;
					} else {
						return TimesheetsStatus.INCOMPLETED;
					}

	}
			
	
	
}