package com.plusitsolution.timesheet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeLoginWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeProfileDomain;
import com.plusitsolution.timesheet.domain.wrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalRequestWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateMyTimesheetsWrapper;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.service.AdminService;
import com.plusitsolution.timesheet.service.EmployeeService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/employee")
public class EmployeeController {
	
	@Autowired
	private AdminService service ;
	@Autowired
	private EmployeeService empService ;
	
	//---------------Employee
	@PostMapping("/getUserProfile")
	public EmployeeProfileDomain getUserProfile(@RequestBody EmployeeIDWrapper wrapper) {
		return empService.getUserProfile(wrapper);
	}
	
	@PostMapping("/loginEmp")
	public EmployeeProfileDomain loginEmp(@RequestBody EmployeeLoginWrapper wrapper) {
		return empService.loginEmp(wrapper);
	}
	
	
	//---------------- timesheet
	@PostMapping("/updateMyTimesheets")
	public void updateMyTimesheets(@RequestBody UpdateMyTimesheetsWrapper wrapper) {
		empService.updateMyTimesheets(wrapper);
	}
	
	@PostMapping("/getMyTimesheetMonth")
	public Map<String, TimesheetsDomain> getMyTimesheetMonth(@RequestBody EmployeeIDMonthWrapper wrapper) {
		return empService.getMyTimesheetMonth(wrapper);
	}
	
	//----------------- Medical
	@PostMapping("/addMedRequests")
	public void addMedRequests(@RequestBody MedicalRequestWrapper wrapper) {
		empService.addMedRequests(wrapper);
	}
	
	@PostMapping("/geMyMedRequests")
	public Map<String, MedicalEntity> geMyMedRequests(@RequestBody EmployeeIDWrapper wrapper) {
		return empService.geMyMedRequests(wrapper);
	}
	
	//------------ Excel
	

}
