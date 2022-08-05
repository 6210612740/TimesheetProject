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

import com.plusitsolution.timesheet.domain.SumDomain;
import com.plusitsolution.timesheet.domain.LeaveRequest.LeaveMyRequestDomain;
import com.plusitsolution.timesheet.domain.Medical.MedicalMyRequestDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeLoginWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeProfileDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.UpdateMyTimesheetsWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.LeaveWrapper.LeaveRequestWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalWrapper.MedicalRequestWrapper;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.service.AdminService;
import com.plusitsolution.timesheet.service.EmployeeService;

@RestController
@CrossOrigin
@RequestMapping(value = "/secure/employee") //    /secure/employee
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
	
	@PostMapping("/getSumMyMonth")
	public SumDomain getSumMyMonth(@RequestBody EmployeeIDMonthWrapper wrapper) {
		return empService.getSumMyMonth(wrapper);
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
	public Map<String, MedicalMyRequestDomain> geMyMedRequests(@RequestBody EmployeeIDWrapper wrapper) {
		return empService.geMyMedRequests(wrapper);
	}
	
	//------------ Leave Request

	@PostMapping("/addLeaveRequests")
	public void addLeaveRequests(@RequestBody LeaveRequestWrapper wrapper) {
		empService.addLeaveRequests(wrapper);
	}
	
	@PostMapping("/geMyLeaveRequests")
	public Map<String, LeaveMyRequestDomain> geMyLeaveRequests(@RequestBody EmployeeIDWrapper wrapper) {
		return empService.geMyLeaveRequests(wrapper);
	}

}
