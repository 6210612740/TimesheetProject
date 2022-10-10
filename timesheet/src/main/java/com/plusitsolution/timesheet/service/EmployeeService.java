package com.plusitsolution.timesheet.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xddf.usermodel.SystemColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.plusitsolution.timesheet.domain.SumDomain;
import com.plusitsolution.timesheet.domain.Employee.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.Employee.EmployeeDomain;
import com.plusitsolution.timesheet.domain.Timesheet.MyTimesheetExcelDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpRole;
import com.plusitsolution.timesheet.domain.EnumDomain.LeaveStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.MedStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.TimesheetsStatus;
import com.plusitsolution.timesheet.domain.LeaveRequest.LeaveDomain;
import com.plusitsolution.timesheet.domain.LeaveRequest.LeaveMyRequestDomain;
import com.plusitsolution.common.toolkit.PlusExcelUtils;
import com.plusitsolution.common.toolkit.PlusHashUtils;
import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.Medical.MedicalDomain;
import com.plusitsolution.timesheet.domain.Medical.MedicalMyRequestDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeLoginWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.EmployeeProfileDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeWrapper.UpdateMyTimesheetsWrapper;
import com.plusitsolution.timesheet.domain.wrapper.LeaveWrapper.LeaveRequestWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalWrapper.MedicalRequestWrapper;
import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.LeaveEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.LeaveRepository;
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
	private LeaveRepository leaveRepository ;
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
				employeeEntity.getLastName(), employeeEntity.getNickName(), employeeEntity.getUsername(), EMP_MAP.get(employeeEntity.getEmpID()).getHolidayID(), EMP_MAP.get(employeeEntity.getEmpID()).getLeaveLimit(),
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
	
	public Map<String, LeaveMyRequestDomain> geMyLeaveRequests(EmployeeIDWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		
		EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();
		
		Map<String, LeaveMyRequestDomain> MYLEAVE_MAP = new HashMap<>();
		for (String i : employeeEntity.getLEAVEREQ_MAP().keySet()) {
			
			LeaveEntity entity = leaveRepository.findById(employeeEntity.getLEAVEREQ_MAP().get(i)).get();

			LeaveMyRequestDomain domain = new LeaveMyRequestDomain(employeeEntity.getLEAVEREQ_MAP().get(i), entity.getEmpID(), entity.getOrgID(), entity.getDateReq(),
					entity.getDateStart(), entity.getDateEnd(), entity.getNote(), entity.getLeaveType(), entity.getLeaveStatus(),employeeRepository.findById(entity.getEmpID()).get().getEmpCode(), employeeRepository.findById(entity.getEmpID()).get().getNickName());
			
			MYLEAVE_MAP.put(i, domain);
		}
		
		return MYLEAVE_MAP;
	}
	
	public SumDomain getSumMyMonth(EmployeeIDMonthWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		throwService.checkMonth(wrapper.getMonth());
		throwService.checkYear(wrapper.getYear());

		SumDomain domain = new SumDomain(utilService.myLeaveDayThisMonth(wrapper.getEmpID(), wrapper.getMonth(), wrapper.getYear()),
				utilService.myWorkThisMonth(wrapper.getEmpID(), wrapper.getMonth(), wrapper.getYear()),
				utilService.myHolidayThisMonth(wrapper.getEmpID(), wrapper.getMonth(), wrapper.getYear()),
				utilService.myOTThisMonth(wrapper.getEmpID(), wrapper.getMonth(), wrapper.getYear()));
		
		return domain;
	
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
	
	//-------------- Leave Request
	
	public void addLeaveRequests(LeaveRequestWrapper wrapper) {
		throwService.checkEmployeeByEmpCode(wrapper.getEmpCode());
		
		
		EmployeeEntity employeeEntity = employeeRepository.findByEmpCode(wrapper.getEmpCode());
		LeaveDomain domain = new LeaveDomain(employeeEntity.getEmpID(), employeeEntity.getOrgID(), LocalDate.now().toString(), wrapper.getDateStart(), 
				wrapper.getDateEnd(), wrapper.getNote(), wrapper.getLeaveType(), LeaveStatus.INPROCESS);
		
		LeaveEntity leaveEntity = leaveRepository.save(domain.toEntity()) ;
		
		employeeEntity.getLEAVEREQ_MAP().put(LocalDateTime.now().toString(), leaveEntity.getLeaveID());
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
        		domain.setWorkingTime(0.0);
        	}
        	if(wrapper.getTIMESHEETS_MAP().get(i).getDateStatus().equals(DateStatus.HOLIDAY)) {
        		domain.setWorkingTime(0.0);
        	}
        	
        }
        
        Map<String , TimesheetsStatus> timesheetStatus_MAP  = employeeEntity.getTimesheetStatus_MAP();
        
        for(int i=0; i<12; i++) {
        	
        	if(employeeEntity.getTimesheetStatus_MAP().get(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01") == null ) {
        		timesheetStatus_MAP.put(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01", 
        				myTimesheetStatus(wrapper.getEmpID(), i+1, LocalDate.now().getYear()));
        	} else {
  
        		if((!(employeeEntity.getTimesheetStatus_MAP().get(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01").equals(TimesheetsStatus.APPROVE) || 
        				employeeEntity.getTimesheetStatus_MAP().get(LocalDate.now().getYear()+"-"+utilService.paddding(i+1)+"-01").equals(TimesheetsStatus.REJECT))) ) {
        
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
		} else {
			return DateStatus.LEAVE;
		}

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
	
	public HttpEntity<byte[]> createExcelMyTimesheet(EmployeeIDMonthWrapper wrapper) throws IOException{
		
//		String empID = "UHy-SIIBa0CUUmxeDRCp";
//		int month = 7;
//		int year = 2022;
		
		String[] headerArray = {"#","Date","Day", "Time In", "Time Out", "Project", "Activities"};
		
		EmployeeEntity empEntity = employeeRepository.findById(wrapper.getEmpID()).get();
		OrganizeEntity orgEntity = orgRepository.findById(empEntity.getOrgID()).get();
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = empEntity.getTIMESHEETS_MAP();
		
    	XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Timesheet");
        sheet.setColumnWidth(0, 1500);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 12000);  
        
        XSSFRow row = sheet.createRow(0);

        // font
        Font font12 = workbook.createFont();  
        font12.setFontHeightInPoints((short)12);
        font12.setFontName("CordiaUPC");
        
        Font font12B = workbook.createFont();  
        font12B.setFontHeightInPoints((short)12);
        font12B.setFontName("CordiaUPC");
        font12B.setBold(true);
    	
        Font font14 = workbook.createFont();  
        font14.setFontHeightInPoints((short)14);
        font14.setFontName("CordiaUPC");
        
        Font font14B = workbook.createFont();  
        font14B.setFontHeightInPoints((short)14);
        font14B.setFontName("CordiaUPC");
        font14B.setBold(true);
        
        Font font16 = workbook.createFont();  
        font16.setFontHeightInPoints((short)16);
        font16.setFontName("CordiaUPC");
        
        Font font16B = workbook.createFont();  
        font16B.setFontHeightInPoints((short)16);
        font16B.setFontName("CordiaUPC");
        font16B.setBold(true);
        
        Font font18 = workbook.createFont();  
        font18.setFontHeightInPoints((short)18);
        font18.setFontName("CordiaUPC");
        
        Font font18B = workbook.createFont();  
        font18B.setFontHeightInPoints((short)18);
        font18B.setFontName("CordiaUPC");
        font18B.setBold(true);
        
        // style
        XSSFCellStyle styleW = workbook.createCellStyle();
        styleW.setFont(font12);
        styleW.setAlignment(HorizontalAlignment.CENTER);
        styleW.setVerticalAlignment(VerticalAlignment.CENTER);
        styleW.setBorderTop(BorderStyle.NONE);
        styleW.setBorderRight(BorderStyle.NONE);
        styleW.setBorderBottom(BorderStyle.NONE);
        styleW.setBorderLeft(BorderStyle.NONE);
        styleW.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        styleW.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle styleR = workbook.createCellStyle();
        styleR.setFont(font12);
        styleR.setAlignment(HorizontalAlignment.CENTER);
        styleR.setVerticalAlignment(VerticalAlignment.CENTER);
        styleR.setBorderTop(BorderStyle.NONE);
        styleR.setBorderRight(BorderStyle.THIN);
        styleR.setBorderBottom(BorderStyle.THIN);
        styleR.setBorderLeft(BorderStyle.NONE);
        styleR.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        styleR.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font12);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style0 = workbook.createCellStyle();
        style0.setFont(font18B);
        style0.setAlignment(HorizontalAlignment.RIGHT);
        style0.setVerticalAlignment(VerticalAlignment.CENTER);
        style0.setBorderTop(BorderStyle.NONE);
        style0.setBorderRight(BorderStyle.THIN);
        style0.setBorderBottom(BorderStyle.NONE);
        style0.setBorderLeft(BorderStyle.NONE);
        style0.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style0.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style1 = workbook.createCellStyle();
        style1.setFont(font16B);
        style1.setAlignment(HorizontalAlignment.RIGHT);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        style1.setBorderTop(BorderStyle.NONE);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderBottom(BorderStyle.NONE);
        style1.setBorderLeft(BorderStyle.NONE);
        style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFont(font14B);
        style2.setAlignment(HorizontalAlignment.RIGHT);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setBorderTop(BorderStyle.NONE);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.NONE);
        style2.setBorderLeft(BorderStyle.NONE);
        style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFont(font14);
        style3.setAlignment(HorizontalAlignment.RIGHT);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setBorderTop(BorderStyle.NONE);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setBorderBottom(BorderStyle.NONE);
        style3.setBorderLeft(BorderStyle.NONE);
        style3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style4 = workbook.createCellStyle();
        style4.setFont(font16B);
        style4.setAlignment(HorizontalAlignment.CENTER);
        style4.setVerticalAlignment(VerticalAlignment.CENTER);
        style4.setBorderTop(BorderStyle.NONE);
        style4.setBorderRight(BorderStyle.THIN);
        style4.setBorderBottom(BorderStyle.NONE);
        style4.setBorderLeft(BorderStyle.NONE);
        style4.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        
        XSSFCellStyle style5 = workbook.createCellStyle();
        style5.setFont(font12B);
        style5.setAlignment(HorizontalAlignment.RIGHT);
        style5.setVerticalAlignment(VerticalAlignment.CENTER);
        style5.setBorderTop(BorderStyle.NONE);
        style5.setBorderRight(BorderStyle.NONE);
        style5.setBorderBottom(BorderStyle.NONE);
        style5.setBorderLeft(BorderStyle.NONE);
        style5.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style5C = workbook.createCellStyle();
        style5C.setFont(font12);
        style5C.setAlignment(HorizontalAlignment.LEFT);
        style5C.setVerticalAlignment(VerticalAlignment.CENTER);
        style5C.setBorderTop(BorderStyle.NONE);
        style5C.setBorderRight(BorderStyle.NONE);
        style5C.setBorderBottom(BorderStyle.NONE);
        style5C.setBorderLeft(BorderStyle.NONE);
        style5C.setFillForegroundColor(IndexedColors.WHITE1.getIndex());  //  <----------------------------------------?
        style5C.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style5G = workbook.createCellStyle();
        style5G.setFont(font12);
        style5G.setAlignment(HorizontalAlignment.LEFT);
        style5G.setVerticalAlignment(VerticalAlignment.CENTER);
        style5G.setBorderTop(BorderStyle.NONE);
        style5G.setBorderRight(BorderStyle.THIN);
        style5G.setBorderBottom(BorderStyle.NONE);
        style5G.setBorderLeft(BorderStyle.NONE);
        style5G.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style5G.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style8 = workbook.createCellStyle();
        style8.setFont(font12);
        style8.setAlignment(HorizontalAlignment.CENTER);
        style8.setVerticalAlignment(VerticalAlignment.CENTER);
        style8.setBorderTop(BorderStyle.THIN);
        style8.setBorderRight(BorderStyle.THIN);
        style8.setBorderBottom(BorderStyle.THIN);
        style8.setBorderLeft(BorderStyle.THIN);
        style8.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style8.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style40 = workbook.createCellStyle();
        style40.setFont(font12B);
        style40.setAlignment(HorizontalAlignment.CENTER);
        style40.setVerticalAlignment(VerticalAlignment.CENTER);
        style40.setBorderTop(BorderStyle.THIN);
        style40.setBorderRight(BorderStyle.THIN);
        style40.setBorderBottom(BorderStyle.THIN);
        style40.setBorderLeft(BorderStyle.THIN);
        style40.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style40.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style44 = workbook.createCellStyle();
        style44.setFont(font14B);
        style44.setAlignment(HorizontalAlignment.CENTER);
        style44.setVerticalAlignment(VerticalAlignment.CENTER);
        style44.setBorderTop(BorderStyle.NONE);
        style44.setBorderRight(BorderStyle.NONE);
        style44.setBorderBottom(BorderStyle.NONE);
        style44.setBorderLeft(BorderStyle.NONE);
        style44.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style44.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style44G = workbook.createCellStyle();
        style44G.setFont(font14B);
        style44G.setAlignment(HorizontalAlignment.CENTER);
        style44G.setVerticalAlignment(VerticalAlignment.CENTER);
        style44G.setBorderTop(BorderStyle.NONE);
        style44G.setBorderRight(BorderStyle.THIN);
        style44G.setBorderBottom(BorderStyle.NONE);
        style44G.setBorderLeft(BorderStyle.NONE);
        style44G.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        style44G.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style46 = workbook.createCellStyle();
        style46.setFont(font14);
        style46.setAlignment(HorizontalAlignment.CENTER);
        style46.setVerticalAlignment(VerticalAlignment.CENTER);
        style46.setBorderTop(BorderStyle.THIN);
        style46.setBorderRight(BorderStyle.NONE);
        style46.setBorderBottom(BorderStyle.NONE);
        style46.setBorderLeft(BorderStyle.NONE);
        style46.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style46.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style46G = workbook.createCellStyle();
        style46G.setFont(font14);
        style46G.setAlignment(HorizontalAlignment.CENTER);
        style46G.setVerticalAlignment(VerticalAlignment.CENTER);
        style46G.setBorderTop(BorderStyle.THIN);
        style46G.setBorderRight(BorderStyle.THIN);
        style46G.setBorderBottom(BorderStyle.NONE);
        style46G.setBorderLeft(BorderStyle.NONE);
        style46G.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style46G.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style47 = workbook.createCellStyle();
        style47.setAlignment(HorizontalAlignment.CENTER);
        style47.setVerticalAlignment(VerticalAlignment.CENTER);
        style47.setBorderTop(BorderStyle.NONE);
        style47.setBorderRight(BorderStyle.THIN);
        style47.setBorderBottom(BorderStyle.THIN);
        style47.setBorderLeft(BorderStyle.NONE);
        style47.setFillForegroundColor(IndexedColors.WHITE .getIndex());
        style47.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        InputStream inputStream = new URL(""+orgRepository.findById(employeeRepository.findById(wrapper.getEmpID()).get().getOrgID()).get().getOrgPic()).openStream();
        //Get the contents of an InputStream as a byte[].
        byte[] bytes = IOUtils.toByteArray(inputStream);
        //Adds a picture to the workbook
        int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
        //close the input stream
        inputStream.close();
      
        //Returns an object that handles instantiating concrete classes
        CreationHelper helper = workbook.getCreationHelper();
      
        //Creates the top-level drawing patriarch.
        Drawing drawing = sheet.createDrawingPatriarch();
      
        //Create an anchor that is attached to the worksheet
        ClientAnchor anchor = helper.createClientAnchor();
        //set top-left corner for the image
        anchor.setCol1(0);
        anchor.setRow1(0);
      
        //Creates a picture
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        //Reset the image to the original size
        pict.resize(6.0,3.25);
        
        sheet.addMergedRegion(new CellRangeAddress(0,3,0,5));
        sheet.addMergedRegion(new CellRangeAddress(4,4,0,6));
        sheet.addMergedRegion(new CellRangeAddress(5,5,0,1));
        sheet.addMergedRegion(new CellRangeAddress(5,5,2,4));
        sheet.addMergedRegion(new CellRangeAddress(6,6,0,1));
        sheet.addMergedRegion(new CellRangeAddress(6,6,2,6));
        sheet.addMergedRegion(new CellRangeAddress(39,39,0,6));
        sheet.addMergedRegion(new CellRangeAddress(40,40,0,2));
        sheet.addMergedRegion(new CellRangeAddress(40,40,4,6));
        sheet.addMergedRegion(new CellRangeAddress(41,41,0,2));
        sheet.addMergedRegion(new CellRangeAddress(41,41,4,6));
        sheet.addMergedRegion(new CellRangeAddress(42,42,0,2));
        sheet.addMergedRegion(new CellRangeAddress(42,42,4,6));
        sheet.addMergedRegion(new CellRangeAddress(43,43,0,6));
        sheet.addMergedRegion(new CellRangeAddress(44,44,0,3));
        sheet.addMergedRegion(new CellRangeAddress(44,44,4,5));
        sheet.addMergedRegion(new CellRangeAddress(45,45,0,6));
        sheet.addMergedRegion(new CellRangeAddress(46,46,0,3));
        sheet.addMergedRegion(new CellRangeAddress(46,46,4,5));
        sheet.addMergedRegion(new CellRangeAddress(47,47,0,6));

        
        XSSFCell cell=row.createCell(0);
   // row 0
        row = sheet.createRow(0);
        row.setHeight((short) 500);
        cell=row.createCell(6);
        cell.setCellValue(""+orgEntity.getOrgNameTh());
        cell.setCellStyle(style0);
        
    // row 1
        row = sheet.createRow(1);
        row.setHeight((short) 500);
        cell=row.createCell(6);
        cell.setCellValue(""+orgEntity.getOrgNameEng());
        cell.setCellStyle(style1);
        
    // row 2
        row = sheet.createRow(2);
        row.setHeight((short) 500);
        cell=row.createCell(6);
        cell.setCellValue(""+orgEntity.getOrgAdress());
        cell.setCellStyle(style2);
        
     // row 3
        row = sheet.createRow(3);
        row.setHeight((short) 100);
        cell=row.createCell(6);
        cell.setCellStyle(style3);
        
     // row 4
        row = sheet.createRow(4);
        row.setHeight((short) 500);
        cell=row.createCell(0);
        cell.setCellValue("ใบบันทึกเวลาทำงาน / TIME SHEET");
        cell.setCellStyle(style4);
        
        cell=row.createCell(6);
        cell.setCellStyle(styleR);
        
     // row 5
        row = sheet.createRow(5);
        cell=row.createCell(0);
        cell.setCellValue("Employee Name");
        cell.setCellStyle(style5);
        
        cell=row.createCell(2);
        cell.setCellValue(""+empEntity.getFirstName()+" "+empEntity.getLastName());
        cell.setCellStyle(style5C);
        
        cell=row.createCell(5);
        cell.setCellValue("Ref No");
        cell.setCellStyle(style5);
        
        cell=row.createCell(6);
        cell.setCellValue(wrapper.getYear()+"TS"+utilService.paddding(wrapper.getMonth())+""+empEntity.getEmpCode());
        cell.setCellStyle(style5G);
        
	// row 6
        row = sheet.createRow(6);
        row.setHeight((short)400);
        cell=row.createCell(0);
        cell.setCellValue("Employee Code");
        cell.setCellStyle(style5);
        
        cell=row.createCell(2);
        cell.setCellValue(""+empEntity.getEmpCode());
        cell.setCellStyle(style5C);
        
        cell=row.createCell(6);
        cell.setCellStyle(style5G);
        
    // row 7
        row = sheet.createRow(7);
        row.setHeight((short) 400);
        for(int i=0; i<headerArray.length; i++) {
        	cell=row.createCell(i);
        	cell.setCellValue(""+headerArray[i]);
	        cell.setCellStyle(style);
        }
        
     // row 8
        for(int i=0; i<MONTH_MAP.get(utilService.paddding(wrapper.getMonth())) ; i++) {
        	 
        	String keyDate = wrapper.getYear()+"-"+utilService.paddding(wrapper.getMonth())+"-"+utilService.paddding(i+1);
        	LocalDate date = LocalDate.parse(keyDate);
        	
        	row = sheet.createRow(8+i);
        	
        	for(int j=0; j<=6; j++) {
        		cell=row.createCell(j);
        		
        		if(TIMESHEETS_MAP.get(keyDate) == null) {
                	cell.setCellValue(" ");
                	if(j==0) {
            			cell.setCellValue(""+(i+1));
            		}
                	
                	if(j==1) {
            			cell.setCellValue((i+1)+" "+date.getMonth().toString().substring(0, 3)+ " "+wrapper.getYear());
            		}
    		
            		if(j==2) {
            			cell.setCellValue(" "+date.getDayOfWeek().toString().substring(0, 3));
            		}
            		cell.setCellStyle(style8);
                	
        		}else{
                		if(j==0) {
                			cell.setCellValue(""+(i+1));
                		}
        		
                		if(j==1) {
                			cell.setCellValue((i+1)+" "+date.getMonth().toString().substring(0, 3)+ " "+wrapper.getYear());
                		}
        		
                		if(j==2) {
                			cell.setCellValue(" "+date.getDayOfWeek().toString().substring(0, 3));
                		}
        		
                		if(j==3) {
                			cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getTimeIn());
                		}
        		
                		if(j==4) {
                			cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getTimeOut());
                		}
        		
                		if(j==5) {
                			cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getProject());
                		}
        		
                		if(j==6) {
                			cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getActivity());
                		}
                		
                		if(!TIMESHEETS_MAP.get(keyDate).getDateStatus().equals(DateStatus.HOLIDAY)){    
                			cell.setCellStyle(style8);
                		} else {
                			cell.setCellStyle(style);
                		}
        		}
        	}
        }
        
     // row 39
        row = sheet.createRow(39);
    	row.setHeight((short) 500);
        cell=row.createCell(6);
        cell.setCellStyle(styleR);   		
        
	 // row 40
        row = sheet.createRow(40);
        
        cell=row.createCell(4);
        cell.setCellStyle(styleW);
        
        cell=row.createCell(0);
    	cell.setCellValue(" ลาหยุด / Leave (วัน)");
        cell.setCellStyle(style40);
        
        cell=row.createCell(3);
        if(utilService.myLeaveDayThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()) == 0) {
        	cell.setCellValue("-");
        }else {
        	cell.setCellValue(utilService.myLeaveDayThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()));
        }
   
        cell.setCellStyle(style40);
        
        cell=row.createCell(6);
        cell.setCellStyle(styleR);  
        
     // row 41
        row = sheet.createRow(41);
        
        cell=row.createCell(4);
        cell.setCellStyle(styleW);
        
        cell=row.createCell(0);
    	cell.setCellValue("ล่วงเวลา / OT (ชม.)");
        cell.setCellStyle(style40);
        
        cell=row.createCell(3);
        if(utilService.myOTThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()) == 0) {
        	cell.setCellValue("-");
        }else {
        	cell.setCellValue(utilService.myOTThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()));
        }
        
        cell.setCellStyle(style40);
        
        cell=row.createCell(6);
        cell.setCellStyle(styleR);   		
        
	 // row 42
        row = sheet.createRow(42);
        
        cell=row.createCell(4);
        cell.setCellStyle(styleW);
        
        cell=row.createCell(0);
    	cell.setCellValue("ทำงาน / Working (วัน)");
        cell.setCellStyle(style40);
        
        cell=row.createCell(3);
        if(utilService.myWorkThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()) == 0) {
        	cell.setCellValue("-");
        }else {
//        	String formula = "-D41+D42+21";
        	cell.setCellValue(utilService.myWorkThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()));
//        	cell.setCellFormula(formula);
        }
        
        cell.setCellStyle(style40);
        
        cell=row.createCell(6);
        cell.setCellStyle(styleR);   	
        
    // row 43
        row = sheet.createRow(43);
    	row.setHeight((short) 600);
        cell=row.createCell(6);
        cell.setCellStyle(styleR);   

	// row 44
        row = sheet.createRow(44);
        row.setHeight((short)500);
        cell=row.createCell(0);
    	cell.setCellValue("ผู้อนุมัติ / Approver");
        cell.setCellStyle(style44);
        
        cell=row.createCell(4);
        cell.setCellStyle(styleW);
        
        cell=row.createCell(6);
    	cell.setCellValue("พนักงาน / Employee");
        cell.setCellStyle(style44G);
        
	// row 45
        row = sheet.createRow(45);
        row.setHeight((short) 800);
        cell=row.createCell(6);
        cell.setCellStyle(styleR); 
        
    // row 46
        row = sheet.createRow(46);
        
        cell=row.createCell(4);
        cell.setCellStyle(styleW);
        
        row.setHeight((short)400);
        cell=row.createCell(0);
        cell.setCellValue("(                                           )");
        cell.setCellStyle(style46);
        
        cell=row.createCell(6);
        cell.setCellValue("(   "+empEntity.getFirstName()+" "+empEntity.getLastName()+"   )");
        cell.setCellStyle(style46G);
      	        
	// row 47
        row = sheet.createRow(47);
        row.setHeight((short) 800);
        
        cell=row.createCell(0);
        cell.setCellStyle(style47); 
        
        cell=row.createCell(6);
        cell.setCellStyle(style47); 
        
    	String keyDate = wrapper.getYear()+"-"+utilService.paddding(wrapper.getMonth())+"-"+utilService.paddding(1);
    	LocalDate date = LocalDate.parse(keyDate);
    	String year = ""+date;
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        
        byte[] content = bos.toByteArray();
        HttpHeaders header = new HttpHeaders();
        header.set("charset", "UTF-8");
        header.set(HttpHeaders.CONTENT_ENCODING, "UTF-8");
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        header.set(HttpHeaders.CONTENT_TYPE, "multipart/form-data; charset=UTF-8;");
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; charset=UTF-8; filename="+empEntity.getEmpCode()+"_"
        		+date.getMonth().toString().substring(0, 3)+year.substring(2, 4)+".xlsx");
        header.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        header.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        
//    	FileOutputStream file = new FileOutputStream("/home/itim/Desktop/timesheet.xlsx");
//    	workbook.write(file);
//    	file.close();
        
    	System.out.println("--------------create Timesheet sucess----------"); 
        return new HttpEntity<byte[]>(content, header);
 }
			
	
	
}