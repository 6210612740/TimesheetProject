package com.plusitsolution.timesheet.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

@Service
public class UtilsService {
	@Autowired
	private OrganizeRepository orgRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private MedicalRepository medicalRepository;
	
	
	public String padZeroLeft(String input, int padLength) {
        return padding(input, padLength, '0', false, true);
    }
	
	 public String padding(String input, int padLength, char padChar, boolean isPadRight, boolean isCutOverPadded) {
	        StringBuilder str = new StringBuilder();
	        if (isEmpty(input)) {
	            str.append("");
	        } else {
	            str.append(input);
	        }

	        if (str.length() < padLength) {
	            while(str.length() < padLength) {
	                if (isPadRight) {
	                    str.append(padChar);
	                } else {
	                    str.insert(0, padChar);
	                }
	            }
	        } else if (isCutOverPadded && str.length() > padLength) {
	            if (isPadRight) {
	                str.delete(padLength, str.length());
	            } else {
	                str.delete(0, str.length() - padLength);
	            }
	        }

	        return str.toString();
	    }
	 
	 public boolean isEmpty(String input) {
	        return input == null || input.equals("");
	    }
	 
	 public double round(double amount) {
         return Math.round(amount*100.0)/100.0;
     }
	 
	 public String generateEmpCode(AtomicInteger counter , String orgID) {
		 OrganizeEntity orgEntity = orgRepository.findById(orgID).get();
		 return orgEntity.getShortName()+padZeroLeft(String.valueOf(counter.getAndAdd(1)), 4);
	 }
	 
	 public String paddding(int counter) {

		 return padZeroLeft(String.valueOf(counter), 2);
	 }
	 
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
					if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.HALFDAY)) {
						totalLeaveThisMonth += 0.5;
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
		
		public Double myHalfdayThisMonth(String empID, int month, int year) {
			
			Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
			TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP()); 
			
			Double totalHalfdayThisMonth = 0.0;
			for (String i : TIMESHEETS_MAP.keySet()) {
				LocalDate date = LocalDate.parse(i);
				if(date.getYear() == year && date.getMonthValue() == month) {
					if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.HALFDAY)) {
						totalHalfdayThisMonth += 1;
					}
				}
			}
			
			return totalHalfdayThisMonth;
		}
		
		public Double myHolidayThisMonth(String empID, int month, int year) {
			
			Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
			TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP()); 
			
			Double totalHolidayThisMonth = 0.0;
			for (String i : TIMESHEETS_MAP.keySet()) {
				LocalDate date = LocalDate.parse(i);
				if(date.getYear() == year && date.getMonthValue() == month) {
					if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.HOLIDAY)) {
						totalHolidayThisMonth += 1;
					}
				}
			}
			
			return totalHolidayThisMonth;
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
		

		
		public int compareTime(String timeIn, String timeOut) {
			
			LocalTime in = LocalTime.parse(timeIn);
			LocalTime out = LocalTime.parse(timeOut);
//			int x = out.getHour()-in.getHour();
//			int y = out.getMinute()-in.getMinute();
			
			return ((out.getHour()-in.getHour())*60 + out.getMinute()-in.getMinute())/60;
		}
	 


}
