package com.plusitsolution.timesheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plusitsolution.timesheet.domain.wrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateMyTimesheetsWrapper;
import com.plusitsolution.timesheet.service.AdminService;
import com.plusitsolution.timesheet.service.EmployeeService;

@RestController
@RequestMapping(value = "/api/v1/employee")
public class EmployeeController {
	
	@Autowired
	private AdminService service ;
	@Autowired
	private EmployeeService empService ;
	
	//---------------- timesheet
	@PostMapping("/updateMyTimesheets")
	public void updateMyTimesheets(@RequestBody UpdateMyTimesheetsWrapper wrapper) {
		empService.updateMyTimesheets(wrapper);
	}

}
