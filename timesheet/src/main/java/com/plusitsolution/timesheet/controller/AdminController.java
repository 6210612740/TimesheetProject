package com.plusitsolution.timesheet.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plusitsolution.timesheet.domain.OverviewDomain;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.domain.wrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.service.AdminService;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
	
	@Autowired
	private AdminService service ;
	
//---------- Employee ---------------	
	@PostMapping("/registerEmployee")
	public void registerEmployee(@RequestBody RegisterEmployeeWrapper wrapper) {
		service.registerEmployee(wrapper);
	}
	
//----------- Organization ----------------------	
	@PostMapping("/registerOrganize")
	public void registerOrg(@RequestBody OrgRegisterWrapper wrapper) {
		service.registerOrg(wrapper);
	}
	
//------------------ Display -----------------	
	@PostMapping("/getOverView")
	public Map<String, OverviewDomain> getOverView(@RequestBody OrgIDWrapper wrapper) {
		return service.getOverView(wrapper);
	}
	
//-------- holiday -------------------
	@PostMapping("/createHolidayType")
	public void createHolidayType(@RequestBody HolidayWrapper wrapper) {
		service.createHolidayType(wrapper);
	}
	
}
