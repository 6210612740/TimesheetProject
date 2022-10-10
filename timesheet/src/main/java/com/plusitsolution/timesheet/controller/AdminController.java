package com.plusitsolution.timesheet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plusitsolution.timesheet.domain.HolidayDomain;
import com.plusitsolution.timesheet.domain.SumDomain;
import com.plusitsolution.timesheet.domain.Display.OverviewDomain;
import com.plusitsolution.timesheet.domain.Display.SummaryByMonthDomain;
import com.plusitsolution.timesheet.domain.Display.SummaryByMonthValueDomain;
import com.plusitsolution.timesheet.domain.LeaveRequest.LeaveRequestDomain;
import com.plusitsolution.timesheet.domain.Medical.MedicalRequestDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsSummaryDomain;
import com.plusitsolution.timesheet.domain.wrapper.TimesheetSetTimesheetWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.UpdateUserProfileWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper.HolidayWrapper;
import com.plusitsolution.timesheet.domain.wrapper.LeaveWrapper.UpdateLeaveRequestsStatusWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalWrapper.MedicalIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalWrapper.UpdateMedicalRequestsStatusWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrganizeWrapper.OrgIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrganizeWrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrganizeWrapper.OrgIDYearWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrganizeWrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.service.AdminService;

@RestController
@CrossOrigin
@RequestMapping(value = "/secure/admin") //     /secure/admin
public class AdminController {
	
	@Autowired
	private AdminService service ;
	
//--------- Timesheet ---------------
	
	
//---------- Employee ---------------	
	@PostMapping("/registerEmployee")
	public void registerEmployee(@RequestBody RegisterEmployeeWrapper wrapper) {
		service.registerEmployee(wrapper);
	}
	
	@PostMapping("/updateUserProfile")
	public void updateUserProfile(@RequestBody UpdateUserProfileWrapper wrapper) {
		service.updateUserProfile(wrapper);
	}
	
	@PostMapping("/approveTimesheet")
	public void approveTimesheet(@RequestBody TimesheetSetTimesheetWrapper wrapper) {
		service.approveTimesheet(wrapper);
	}
	
	@PostMapping("/disabelEmp")
	public void disabelEmp(@RequestBody EmployeeIDWrapper wrapper) {
		service.disabelEmp(wrapper);
	}
	
//----------- Organization ----------------------	
	@PostMapping("/registerOrganize")
	public void registerOrg(@RequestBody OrgRegisterWrapper wrapper) {
		service.registerOrg(wrapper);
	}
	
//------------------ Display -----------------	
	@PostMapping("getOverView")
	public Map<String, OverviewDomain> getOverView(@RequestBody OrgIDYearWrapper wrapper) {
		return service.getOverView(wrapper);
	}
	
	@PostMapping("/getEveryOneLeaveDay")
	public Map<String , SummaryByMonthDomain> getEveryOneLeaveDay(@RequestBody OrgIDYearWrapper wrapper) {
		return service.getEveryOneLeaveDay(wrapper);
	}
	
	@PostMapping("/getEveryOneMedicalFees")
	public Map<String , SummaryByMonthDomain> getEveryOneMedicalFees(@RequestBody OrgIDYearWrapper wrapper) {
		return service.getEveryOneMedicalFees(wrapper);
	}
	
	@PostMapping("/getEveryOneTimesheetsSummary")
	public Map<String , TimesheetsSummaryDomain> getEveryOneTimesheetsSummary(@RequestBody OrgIDMonthWrapper wrapper) {
		return service.getEveryOneTimesheetsSummary(wrapper);
	}
	
	@PostMapping("/getEveryOneMedicalFeesRequests")
	public Map<String , MedicalRequestDomain> getEveryOneMedicalFeesRequests(@RequestBody OrgIDYearWrapper wrapper) {
		return service.getEveryOneMedicalFeesRequests(wrapper);
	}
	
	@PostMapping("/getEveryOneLeaveRequests")
	public Map<String , LeaveRequestDomain> getEveryOneLeaveRequests(@RequestBody OrgIDYearWrapper wrapper) {
		return service.getEveryOneLeaveRequests(wrapper);
	}

	
//-------- holiday -------------------
	@PostMapping("/createHolidayType")
	public void createHolidayType(@RequestBody HolidayWrapper wrapper) {
		service.createHolidayType(wrapper);
	}
	
	@PostMapping("/updateHolidayType")
	public void updateHolidayType(@RequestBody HolidayUpdateWrapper wrapper) {
		service.updateHolidayType(wrapper);
	}
	
//-------- Medical ------------------------
	@PostMapping("/updateMedicalRequestsStatus")
	public void updateMedicalRequestsStatus(@RequestBody UpdateMedicalRequestsStatusWrapper wrapper) {
		service.updateMedicalRequestsStatus(wrapper);
	}
	
	@PostMapping("/getMedicalRequestsDetails")
	public MedicalEntity getMedicalRequestsDetails(@RequestBody MedicalIDWrapper wrapper) {
		return service.getMedicalRequestsDetails(wrapper);
	}
	
	@PostMapping("/getAllHoliday")
	public Map<String , HolidayDomain> getAllHoliday(@RequestBody OrgIDWrapper wrapper) {
		return service.getAllHoliday(wrapper);
	}
	
	//---------------- Leave -------------------------
	@PostMapping("/updateLeaveRequestsStatus")
	public void updateLeaveRequestsStatus(@RequestBody UpdateLeaveRequestsStatusWrapper wrapper) {
		service.updateLeaveRequestsStatus(wrapper);
	}
	
	//----------------- excel -----------------------------
	@PostMapping("/createExcelAllSummary")
public HttpEntity<byte[]> createExcelAllSummary(@RequestBody OrgIDYearWrapper wrapper) throws Exception {
		return service.createExcelAllSummary(wrapper);
	}
	
	@PostMapping("/createExcelMyTimesheet")
	public HttpEntity<byte[]> createExcelMyTimesheet(@RequestBody EmployeeIDMonthWrapper wrapper) throws Exception {
		return service.createExcelMyTimesheet(wrapper);
	}

}
