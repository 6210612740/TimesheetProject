package com.plusitsolution.timesheet.service;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.LeaveRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

@Service
public class ThrowService {
	
	@Autowired
	private OrganizeRepository orgRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private MedicalRepository medicalRepository;
	@Autowired
	private LeaveRepository leaveRepository;

	public void checkUsernameAlreadyuse(String username) {
		if (employeeRepository.findByUsername(username) != null ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this username is already use ");
		}
	}
	
	public void checkEmployee(String empID) {
		if (employeeRepository.findById(empID).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
		}
	}
	
	public void checkOrganize(String orgID) {
		if (orgRepository.findById(orgID).isEmpty() ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this organize is't exist"); 
		}
	}
	
	public void checkEmployeeByEmpCode(String empCode) {
		if (employeeRepository.findByEmpCode(empCode) == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this employee is't exist"); 
		}
	}
	
	public void checkMonth(int month) {
		if (month < 1 ||  month > 12) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "please input mouth in 01-12"); 
		}
	}
	
	public void checkYear(int year) {
		if (year < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "year must be positive"); 
		}
	}
	
	public void checkAmount(Double amount) {
		if (amount < 0.0 ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount is POSITIVE"); 
		
		}
	}
	
	public void checkLeaveLimit(Double leaveLimit) {
		if (leaveLimit < 0.0 ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "leaveLimit is POSITIVE "); 
		
		}
	}
	
	public void checkMedFeeLimit(Double medFeeLimit) {
		if (medFeeLimit < 0.0 ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "medFeeLimit is POSITIVE "); 
		
		}
	}
	
	public void checkHoliday(String holidayID) {
		if (holidayRepository.findById(holidayID).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Don't have  this Holiday ");
		}
	}
	
	public void checkMedical(String medID) {
		if (medicalRepository.findById(medID).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this medicalRequest is't exist"); 
        }
	}
	
	public void checkLeaveRequest(String leaveID) {
		if (leaveRepository.findById(leaveID).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this leaveRequest is't exist"); 
        }
	}
	
	
	
	 public void checkName(String firstName , String lastName) {
	        if(!(Pattern.matches("[A-Za-z]{1,20}", firstName))) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your firstName have only character."); 
	        }
	        if(!(Pattern.matches("[A-Za-z]{1,20}", lastName))) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your lastName have only character."); 
	        }
	    }
	
	public void checkShortName(String shortName) {
		if(!(Pattern.matches("[A-Za-z0-9]{1,6}", shortName))) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your OrgShortName have only character."); 
	    }
		
		if(orgRepository.findByShortName(shortName) != null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this ShortName is already use "); 
	    }
	}
	
	public void checkUsername(String username) {
		if (employeeRepository.findByUsername(username) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this username is't exist"); 
        }
	}
	


	
	
	
	
}
