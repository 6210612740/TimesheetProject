package com.plusitsolution.timesheet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plusitsolution.timesheet.domain.wrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.domain.wrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.service.AdminService;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
	
	@Autowired
	private AdminService service ;
	
	@PostMapping("/registerEmployee")
	public void registerEmployee(@RequestBody RegisterEmployeeWrapper wrapper) {
		service.registerEmployee(wrapper);
	}
	
	@PostMapping("/registerOrganize")
	public void registerOrg(@RequestBody OrgRegisterWrapper wrapper) {
		service.registerOrg(wrapper);
	}

}
