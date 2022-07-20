package com.plusitsolution.timesheet.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.timesheet.domain.Employee.EmployeeDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;
import com.plusitsolution.timesheet.domain.Medical.MedicalDomain;
import com.plusitsolution.timesheet.domain.wrapper.MedicalRequestWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateMyTimesheetsWrapper;
import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

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
	
	public EmployeeDomain loginEmp() {
		
		return null;
	}
	
	public EmployeeEntity getUserProfile(String empCode , String password) {
		
		EmployeeEntity employeeEntity = employeeRepository.findByEmpCode(empCode);
		if (employeeEntity == null ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
		}
		
		return employeeEntity;
	}
	
	public Map<String, TimesheetsDomain> getMyTimesheetMonth(String empID , int month , int year) {
		EmployeeEntity employeeEntity = employeeRepository.findById(empID).get();
		if (employeeEntity == null ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
		}
		
		Map<String, TimesheetsDomain> MYTIMESHEETS_MAP = new HashMap<>();
		
		for (String i : employeeEntity.getTIMESHEETS_MAP().keySet()) {
			LocalDate myObj = LocalDate.parse(i);
			if (myObj.getMonthValue() == month && myObj.getYear() == year){
				
				TimesheetsDomain timesheetsDomain = employeeEntity.getTIMESHEETS_MAP().get(i)	;	
				MYTIMESHEETS_MAP.put(i, timesheetsDomain);	
			}
		}
		return MYTIMESHEETS_MAP;
	}
	
	public Map<String, MedicalEntity> geMyMedRequests(String empID) {
		EmployeeEntity employeeEntity = employeeRepository.findById(empID).get();
		if (employeeEntity == null ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
		}
		Map<String, MedicalEntity> MYMEDFEE_MAP = new HashMap<>();
		for (String i : employeeEntity.getMEDFEEUSE_MAP().keySet()) {
			String medID = employeeEntity.getMEDFEEUSE_MAP().get(i);
			
			MedicalEntity medicalEntity = medicalRepository.findById(medID).get();
			MYMEDFEE_MAP.put(i, medicalEntity);
		}
		
		return MYMEDFEE_MAP;
	}
	
	public void addMedRequests(MedicalRequestWrapper wrapper) {
		
		EmployeeEntity employeeEntity = employeeRepository.findByEmpCode(wrapper.getEmpCode());
		if (employeeEntity == null ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
		}
		
		MedicalDomain medicalDomain = new MedicalDomain(employeeEntity.getEmpID() , employeeEntity.getOrgID() , wrapper.getSlipPic() , wrapper.getAmount() , 
				wrapper.getNote() , LocalDate.now() , MedStatus.INPROCESS );
		
		medicalRepository.save(medicalDomain.toEntity()) ;
		
	}
	
	public void updateMyTimesheets(UpdateMyTimesheetsWrapper wrapper) {
        EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
        if (employeeEntity == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
        }

        employeeEntity.getTIMESHEETS_MAP().putAll(wrapper.getTIMESHEETS_MAP());
        employeeRepository.save(employeeEntity);
    }
	
}