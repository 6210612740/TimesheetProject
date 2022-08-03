package com.plusitsolution.timesheet.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

import com.plusitsolution.common.toolkit.PlusExcelUtils;
import com.plusitsolution.common.toolkit.PlusHashUtils;
import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpRole;
import com.plusitsolution.timesheet.domain.EnumDomain.EmpStatus;
import com.plusitsolution.timesheet.domain.EnumDomain.TimesheetsStatus;
import com.plusitsolution.timesheet.domain.HolidayDomain;
import com.plusitsolution.timesheet.domain.OrganizeDomain;
import com.plusitsolution.timesheet.domain.SumDomain;
import com.plusitsolution.timesheet.domain.Display.OverviewDomain;
import com.plusitsolution.timesheet.domain.Display.SummaryByMonthDomain;
import com.plusitsolution.timesheet.domain.Employee.EmpDetailDomain;
import com.plusitsolution.timesheet.domain.Employee.EmployeeDomain;
import com.plusitsolution.timesheet.domain.Medical.MedicalRequestDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsDomain;
import com.plusitsolution.timesheet.domain.Timesheet.TimesheetsSummaryDomain;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.EmployeeIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayUpdateWrapper;
import com.plusitsolution.timesheet.domain.wrapper.HolidayWrapper;
import com.plusitsolution.timesheet.domain.wrapper.MedicalIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDMonthWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgIDYearWrapper;
import com.plusitsolution.timesheet.domain.wrapper.OrgRegisterWrapper;
import com.plusitsolution.timesheet.domain.wrapper.RegisterEmployeeWrapper;
import com.plusitsolution.timesheet.domain.wrapper.TimesheetSetTimesheetWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateMedicalRequestsStatusWrapper;
import com.plusitsolution.timesheet.domain.wrapper.UpdateUserProfileWrapper;
import com.plusitsolution.timesheet.entity.EmployeeEntity;
import com.plusitsolution.timesheet.entity.HolidayEntity;
import com.plusitsolution.timesheet.entity.MedicalEntity;
import com.plusitsolution.timesheet.entity.OrganizeEntity;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;
import com.plusitsolution.zeencommon.helper.ExcelBuilder;
import com.plusitsolution.zeencommon.helper.ExcelUtils;

@Service
@EnableScheduling
public class AdminService {
	
	@Autowired
	private OrganizeRepository orgRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private MedicalRepository medicalRepository;
	@Autowired
	private UtilsService utilService;
	@Autowired
	private ThrowService throwService;
	
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
	
	//----------- Organization ----------------------

	public void registerOrg(OrgRegisterWrapper wrapper) {
		throwService.checkUsernameAlreadyuse(wrapper.getUsername());
		throwService.checkShortName(wrapper.getShortName());
		throwService.checkName(wrapper.getFirstName(), wrapper.getLastName());
		
		
		//save on org DB
		OrganizeEntity orgEntity = orgRepository.save(new OrganizeDomain(wrapper.getOrgNameTh(), wrapper.getOrgNameEng(), 
				wrapper.getShortName(), wrapper.getOrgAdress(), wrapper.getOrgPic(), new HashMap<String, EmpDetailDomain>()).toEntity());
		//add DEFUALT holiday to this org
		List<String> holidayList = new ArrayList<String>();
		holidayList.add(LocalDate.now().getYear()+"-01-01");
		holidayList.add(LocalDate.now().getYear()+"-12-31");
		createHolidayType(new HolidayWrapper("DEFAULT "+wrapper.getShortName(), orgEntity.getOrgID(), holidayList));

		//go regis first admin
		RegisterEmployeeWrapper domain = new RegisterEmployeeWrapper(orgEntity.getOrgID(), wrapper.getPassword(), wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(),
				0, 0, holidayRepository.findByHolidayName("DEFAULT "+wrapper.getShortName()).getHolidayID(), EmpRole.ADMIN, wrapper.getUsername(), LocalDate.now().toString());
		registerEmployee(domain);
		
	}
	
	//------------------ Display -----------------
	
	public Map<String, OverviewDomain> getOverView(OrgIDYearWrapper wrapper){
		throwService.checkOrganize(wrapper.getOrgID());
		throwService.checkYear(wrapper.getYear());
		
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , OverviewDomain> OVERVIEW_MAP = new HashMap<>();

		for (String i : EMP_MAP.keySet()) {
			String empCode = EMP_MAP.get(i).getEmpCode();
			OverviewDomain domain = new OverviewDomain(empCode, employeeRepository.findByEmpCode(empCode).getFirstName(), employeeRepository.findByEmpCode(empCode).getLastName(), 
					EMP_MAP.get(i).getLeaveLimit(), EMP_MAP.get(i).getMedFeeLimit(), 
					utilService.myLeaveDayThisYear(i, wrapper.getYear()), utilService.myMedfeeThisYear(i, wrapper.getYear()),
					EMP_MAP.get(i).getLeaveLimit()- utilService.myLeaveDayThisYear(i, wrapper.getYear()), EMP_MAP.get(i).getMedFeeLimit()- utilService.myMedfeeThisYear(i, wrapper.getYear()),
					EMP_MAP.get(i).getEndContract(), employeeRepository.findByEmpCode(empCode).getNickName());
			OVERVIEW_MAP.put(i, domain);
		}
		
		return OVERVIEW_MAP;
	}

	public Map<String,TimesheetsSummaryDomain> getEveryOneTimesheetsSummary(OrgIDMonthWrapper wrapper) {	
		throwService.checkOrganize(wrapper.getOrgID());
		throwService.checkMonth(wrapper.getMonth());
		throwService.checkYear(wrapper.getYear());
		
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , TimesheetsSummaryDomain> EveryOneTimesheetsSummary_MAP = new HashMap<>();
		
		for (String i : EMP_MAP.keySet()) {
			EmployeeEntity entity = employeeRepository.findById(i).get() ;

			TimesheetsStatus status = entity.getTimesheetStatus_MAP().get(wrapper.getYear()+"-"+utilService.paddding(wrapper.getMonth())+"-01");
			if(status == null) {
				status = TimesheetsStatus.INCOMPLETED;
			}
			
			TimesheetsSummaryDomain domain = new TimesheetsSummaryDomain(EMP_MAP.get(i).getEmpCode(), entity.getFirstName(), entity.getLastName(), status,
					utilService.myLeaveDayThisMonth(i, wrapper.getMonth(), wrapper.getYear()), utilService.myOTThisMonth(i, wrapper.getMonth(), wrapper.getYear()), utilService.myWorkThisMonth(i, wrapper.getMonth(), wrapper.getYear()), 
					entity.getNickName() ,holidayRepository.findById(EMP_MAP.get(i).getHolidayID()).get().getHolidayName());
			EveryOneTimesheetsSummary_MAP.put(i, domain);
		}
		
		return EveryOneTimesheetsSummary_MAP;
	}
	
	public Map<String , SummaryByMonthDomain> getEveryOneLeaveDay(OrgIDYearWrapper wrapper) {
		throwService.checkOrganize(wrapper.getOrgID());
		throwService.checkYear(wrapper.getYear());
		
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , SummaryByMonthDomain> EveryOneSummaryDay_MAP = new HashMap<>();
		
		for (String i : EMP_MAP.keySet()) {
			SummaryByMonthDomain domain = new SummaryByMonthDomain(employeeRepository.findById(i).get().getEmpCode(), employeeRepository.findById(i).get().getNickName(), utilService.myLeaveDayThisYear(i, wrapper.getYear()),
					utilService.myLeaveDayThisMonth(i, 1, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 2, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 3, wrapper.getYear()),
					utilService.myLeaveDayThisMonth(i, 4, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 5, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 6, wrapper.getYear()),
					utilService.myLeaveDayThisMonth(i, 7, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 8, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 9, wrapper.getYear()),
					utilService.myLeaveDayThisMonth(i, 10, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 11, wrapper.getYear()), utilService.myLeaveDayThisMonth(i, 12, wrapper.getYear()));
			EveryOneSummaryDay_MAP.put(i, domain);
		}
		
		return EveryOneSummaryDay_MAP;
	}
	
	public Map<String , SummaryByMonthDomain> getEveryOneMedicalFees(OrgIDYearWrapper wrapper) {
		throwService.checkOrganize(wrapper.getOrgID());
		throwService.checkYear(wrapper.getYear());
		
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP());
		
		Map<String , SummaryByMonthDomain> EveryOneSummaryDay_MAP = new HashMap<>();
		
		for (String i : EMP_MAP.keySet()) {
			SummaryByMonthDomain domain = new SummaryByMonthDomain(employeeRepository.findById(i).get().getEmpCode(), employeeRepository.findById(i).get().getNickName(), utilService.myMedfeeThisYear(i, wrapper.getYear()),
					utilService.myMedfeeThisMonth(i, 1, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 2, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 3, wrapper.getYear()),
					utilService.myMedfeeThisMonth(i, 4, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 5, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 6, wrapper.getYear()),
					utilService.myMedfeeThisMonth(i, 7, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 8, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 9, wrapper.getYear()),
					utilService.myMedfeeThisMonth(i, 10, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 11, wrapper.getYear()), utilService.myMedfeeThisMonth(i, 12, wrapper.getYear()));
			EveryOneSummaryDay_MAP.put(i, domain);
		}
		return EveryOneSummaryDay_MAP;
	}
	
	public Map<String , MedicalRequestDomain> getEveryOneMedicalFeesRequests(OrgIDYearWrapper wrapper) {
		throwService.checkOrganize(wrapper.getOrgID());
		throwService.checkYear(wrapper.getYear());
		
		
		List<MedicalEntity> medList = new ArrayList<MedicalEntity>();
		medList.addAll(medicalRepository.findByOrgID(wrapper.getOrgID())) ;
		
//		List<MedicalRequestDomain> everyoneList = new ArrayList<MedicalRequestDomain>();
		Map<String , MedicalRequestDomain> EveryOneSummary_MAP = new HashMap<>();
		
		for(int i=0 ; i<medList.size() ; i++) {
			if(LocalDate.parse(medList.get(i).getDate()).getYear() == wrapper.getYear()) {
				

				MedicalRequestDomain domain = new MedicalRequestDomain(employeeRepository.findById(medList.get(i).getEmpID()).get().getEmpCode(),
						employeeRepository.findById(medList.get(i).getEmpID()).get().getNickName(), medList.get(i).getDate(), medList.get(i).getAmount(),
						orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP().get(medList.get(i).getEmpID()).getLeaveLimit()- utilService.myMedfeeThisYear(medList.get(i).getEmpID(), wrapper.getYear()), medList.get(i).getMedStatus());
				
//				everyoneList.add(domain);
				EveryOneSummary_MAP.put(medList.get(i).getMedID(), domain);
			}
		}
		return EveryOneSummary_MAP;
		
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
	
	//---------- Employee ---------------
	

	public void registerEmployee(RegisterEmployeeWrapper wrapper) {	
		throwService.checkUsernameAlreadyuse(wrapper.getUsername());
		throwService.checkOrganize(wrapper.getOrgID());
		throwService.checkLeaveLimit(wrapper.getLeaveLimit());
		throwService.checkMedFeeLimit(wrapper.getMedFeeLimit());
		throwService.checkHoliday(wrapper.getHolidayID());
		throwService.checkName(wrapper.getFirstName(), wrapper.getLastName());

		// save to employee DB
		AtomicInteger counter = new AtomicInteger(1+orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP().size());
		String empCode = utilService.generateEmpCode(counter, wrapper.getOrgID());
		String hashPass = PlusHashUtils.hash(wrapper.getPassword());
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(holidayRepository.findById(wrapper.getHolidayID()).get().getHOLIDAY_MAP());
		
		EmployeeDomain empDomain = new EmployeeDomain(wrapper.getOrgID(), empCode, 
				wrapper.getFirstName(), wrapper.getLastName(), wrapper.getNickName(), TIMESHEETS_MAP, hashPass,new HashMap<String , String>(), wrapper.getUsername(), new HashMap<String , TimesheetsStatus>());
		
		employeeRepository.save(empDomain.toEntity());
		
		// go to add employee to OrgDB EMP_MAP
		OrganizeEntity entity = orgRepository.findById(wrapper.getOrgID()).get();
		
		Map<String, EmpDetailDomain> EMP_MAP =  orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP();
		EmpDetailDomain domain = new EmpDetailDomain(empCode, wrapper.getHolidayID(), wrapper.getLeaveLimit(), wrapper.getMedFeeLimit(), wrapper.getEmpRole(),
				"9999-01-01", wrapper.getStartDate(), EmpStatus.ACTIVE);
		EMP_MAP.put(employeeRepository.findByEmpCode(empCode).getEmpID(), domain);
		entity.setEMP_MAP(EMP_MAP);
		orgRepository.save(entity);

	}
	
	public void updateUserProfile(UpdateUserProfileWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		throwService.checkName(wrapper.getFirstName(), wrapper.getLastName());
		throwService.checkLeaveLimit(wrapper.getLeaveLimit());
		throwService.checkMedFeeLimit(wrapper.getMedFeeLimit());
		throwService.checkHoliday(wrapper.getHolidayID());
		     
        EmployeeEntity employeeEntity = employeeRepository.findById(wrapper.getEmpID()).get();

        employeeEntity.setFirstName(wrapper.getFirstName());
        employeeEntity.setLastName(wrapper.getLastName());
        employeeEntity.setNickName(wrapper.getNickName());

        OrganizeEntity orgEntity = orgRepository.findById(employeeEntity.getOrgID()).get();
        EmpDetailDomain domain =  orgEntity.getEMP_MAP().get(employeeEntity.getEmpID());
        domain.setEmpRole(wrapper.getEmpRole());
        domain.setHolidayID(wrapper.getHolidayID());
        domain.setLeaveLimit(wrapper.getLeaveLimit());
        domain.setMedFeeLimit(wrapper.getMedFeeLimit());
        
        employeeRepository.save(employeeEntity);

        orgRepository.save(orgEntity);
        
        updateHolidayToEmp(wrapper.getEmpID(), wrapper.getHolidayID());
    }
	
	public void disabelEmp(EmployeeIDWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		
		OrganizeEntity orgEntity = orgRepository.findById(employeeRepository.findById(wrapper.getEmpID()).get().getOrgID()).get();
		EmpDetailDomain domain =  orgEntity.getEMP_MAP().get(wrapper.getEmpID());
		
		if(domain.getEmpStatus().equals(EmpStatus.ACTIVE)) {
			domain.setEmpStatus(EmpStatus.INACTIVE);
		} else {
			domain.setEmpStatus(EmpStatus.ACTIVE);
		}
		
		orgRepository.save(orgEntity);
	}
	
	public void approveTimesheet(TimesheetSetTimesheetWrapper wrapper) {
		throwService.checkEmployee(wrapper.getEmpID());
		
		EmployeeEntity empEntity = employeeRepository.findById(wrapper.getEmpID()).get();
		Map<String , TimesheetsStatus> timesheetStatus_MAP  = new HashMap<>();
		timesheetStatus_MAP.putAll(empEntity.getTimesheetStatus_MAP());
		timesheetStatus_MAP.put(wrapper.getYear()+"-"+utilService.paddding(wrapper.getMonth())+"-01", wrapper.getTimesheetStatus());
		
		empEntity.setTimesheetStatus_MAP(timesheetStatus_MAP);
		employeeRepository.save(empEntity);
	}
	
	//---------- medical --------------------
	
    public void updateMedicalRequestsStatus(UpdateMedicalRequestsStatusWrapper wrapper) {
    	throwService.checkMedical(wrapper.getMedID());
    	
        MedicalEntity entity = medicalRepository.findById(wrapper.getMedID()).get();
 
        entity.setMedStatus(wrapper.getMedStatus());
        medicalRepository.save(entity);
    }
	
	public MedicalEntity getMedicalRequestsDetails(MedicalIDWrapper wrapper) {
		throwService.checkMedical(wrapper.getMedID());
		MedicalEntity entity = medicalRepository.findById(wrapper.getMedID()).get();
		return entity;
	}
	
	//-------- holiday -------------------
	
	public void createHolidayType(HolidayWrapper wrapper) {
		throwService.checkOrganize(wrapper.getOrgID());
		
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		for(int i=0; i<wrapper.getHolidayList().size(); i++) {
			TimesheetsDomain domain = new TimesheetsDomain("", "", "", "Public Holiday", DateStatus.HOLIDAY, 0.0);
			TIMESHEETS_MAP.put(wrapper.getHolidayList().get(i), domain);		
		}
		
		holidayRepository.save(new HolidayDomain(wrapper.getHolidayName(), wrapper.getOrgID(), TIMESHEETS_MAP).toEntity());
	}
	
	public void updateHolidayType(HolidayUpdateWrapper wrapper) {
		throwService.checkHoliday(wrapper.getHolidayID());
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(holidayRepository.findById(wrapper.getHolidayID()).get().getHOLIDAY_MAP());
		
		for(int i=0; i<wrapper.getHolidayList().size(); i++) {
			if(TIMESHEETS_MAP.containsKey(wrapper.getHolidayList().get(i))) {
				if(TIMESHEETS_MAP.get(wrapper.getHolidayList().get(i)).getDateStatus().equals(DateStatus.HOLIDAY)) {
					TIMESHEETS_MAP.remove(wrapper.getHolidayList().get(i));
				}
	
			} else {
					TimesheetsDomain domain = new TimesheetsDomain("", "", "", "Public Holiday", DateStatus.HOLIDAY, 0.0);
					TIMESHEETS_MAP.put(wrapper.getHolidayList().get(i), domain);
			}	
		}
		
		HolidayEntity entity = holidayRepository.findById(wrapper.getHolidayID()).get();
		entity.setHOLIDAY_MAP(TIMESHEETS_MAP);
		holidayRepository.save(entity);
		
		// update to who use this holidayID
		updateHolidayAllEmpInOrg(holidayRepository.findById(wrapper.getHolidayID()).get().getOrgID(), wrapper.getHolidayID());
	}
	
	public void updateHolidayAllEmpInOrg(String orgID, String holidayID) {
		
		Map<String , EmpDetailDomain> EMP_MAP = new HashMap<>();
		EMP_MAP.putAll(orgRepository.findById(orgID).get().getEMP_MAP());
		
		for (String i : EMP_MAP.keySet()) {
			if(EMP_MAP.get(i).getHolidayID().equals(holidayID)) {
				updateHolidayToEmp(i, holidayID);
			}
		}
	}
	
	public void updateHolidayToEmp(String empID, String holidayID) {
		
		Map<String , TimesheetsDomain> TIMESHEETS_MAP = new HashMap<>();
		TIMESHEETS_MAP.putAll(employeeRepository.findById(empID).get().getTIMESHEETS_MAP());

		List<String> mapKeyList = new ArrayList<String>();
		
		for (String i : TIMESHEETS_MAP.keySet()) {
			if(TIMESHEETS_MAP.get(i).getDateStatus().equals(DateStatus.HOLIDAY)) {
				mapKeyList.add(i);
			}
		}
		
		for(int i=0; i<mapKeyList.size() ;i++) {
			TIMESHEETS_MAP.remove(mapKeyList.get(i));
		}
		
		TIMESHEETS_MAP.putAll(holidayRepository.findById(holidayID).get().getHOLIDAY_MAP());
		
		EmployeeEntity entity = employeeRepository.findById(empID).get();
		entity.setTIMESHEETS_MAP(TIMESHEETS_MAP);
		employeeRepository.save(entity);
	}
	
	public Map<String , HolidayDomain> getAllHoliday(OrgIDWrapper wrapper){
		throwService.checkOrganize(wrapper.getOrgID());
		
		List<HolidayEntity> holidayList = holidayRepository.findByOrgID(wrapper.getOrgID());
		Map<String , HolidayDomain> HOLIDAY_MAP = new HashMap<>();
		
		for(int i=0 ; i<holidayList.size() ; i++) {
			HolidayDomain domain = new HolidayDomain(holidayList.get(i).getHolidayName(), holidayList.get(i).getOrgID(), holidayList.get(i).getHOLIDAY_MAP(), holidayList.get(i).getHolidayID());
			
			HOLIDAY_MAP.put(holidayList.get(i).getHolidayID(), domain);
		}
		
		return HOLIDAY_MAP;
	}
	
	//------------- New Excel ss2 create
    public HttpEntity<byte[]> createExcelAllSummary(OrgIDYearWrapper wrapper) throws IOException{
    	
//    	String orgID = "IXyuSIIBa0CUUmxedhCX";
//    	int year= 2022;
    	
    	Map<String , EmpDetailDomain> EMP_MAP = orgRepository.findById(wrapper.getOrgID()).get().getEMP_MAP();
    	
    	String[] headerArray = {"EmpID","Nickname","Leave Limited","Medical Fee Limited", "Total Leave", "Total Medical", 
    	"Remaining Leave", "Remaining Medical Fee", "End Contract"};
    	
    	XSSFWorkbook workbook = new XSSFWorkbook();
    	XSSFSheet sheet = workbook.createSheet("Summary");
    	sheet.setColumnWidth(0, 4000); 
    	sheet.setColumnWidth(1, 4000); 
    	sheet.setColumnWidth(2, 4000); 
    	sheet.setColumnWidth(3, 5000); 
    	sheet.setColumnWidth(4, 4000); 
    	sheet.setColumnWidth(5, 4000);
    	sheet.setColumnWidth(6, 4000);
    	sheet.setColumnWidth(7, 6000);
    	sheet.setColumnWidth(8, 4000);

        Font font = workbook.createFont();  
        font.setFontHeightInPoints((short)12);
        font.setFontName("CordiaUPC");
        
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
    	XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFont(font);  
        style2.setAlignment(HorizontalAlignment.RIGHT);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
    	style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
    	style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
       	XSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFont(font);  
        style3.setAlignment(HorizontalAlignment.RIGHT);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
       	XSSFCellStyle style4 = workbook.createCellStyle();
        style4.setFont(font);
        style4.setAlignment(HorizontalAlignment.RIGHT);
        style4.setVerticalAlignment(VerticalAlignment.CENTER);
        style4.setBorderTop(BorderStyle.THIN);
        style4.setBorderRight(BorderStyle.THIN);
        style4.setBorderBottom(BorderStyle.THIN);
        style4.setBorderLeft(BorderStyle.THIN);
        style4.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style5 = workbook.createCellStyle();
        style5.setFont(font);
        style5.setAlignment(HorizontalAlignment.CENTER);
        style5.setVerticalAlignment(VerticalAlignment.CENTER);
        style5.setBorderTop(BorderStyle.THIN);
        style5.setBorderRight(BorderStyle.THIN);
        style5.setBorderBottom(BorderStyle.THIN);
        style5.setBorderLeft(BorderStyle.THIN);
        style5.setFillForegroundColor(IndexedColors.RED.getIndex());
    	style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    	
        XSSFCellStyle style5R = workbook.createCellStyle();
        style5R.setFont(font);
        style5R.setAlignment(HorizontalAlignment.RIGHT);
        style5R.setVerticalAlignment(VerticalAlignment.CENTER);
        style5R.setBorderTop(BorderStyle.THIN);
        style5R.setBorderRight(BorderStyle.THIN);
        style5R.setBorderBottom(BorderStyle.THIN);
        style5R.setBorderLeft(BorderStyle.NONE);
        style5R.setFillForegroundColor(IndexedColors.RED.getIndex());
        style5R.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style6 = workbook.createCellStyle();
        style6.setFont(font);
        style6.setAlignment(HorizontalAlignment.CENTER);
        style6.setVerticalAlignment(VerticalAlignment.CENTER);
        style6.setBorderTop(BorderStyle.THIN);
        style6.setBorderRight(BorderStyle.NONE);
        style6.setBorderBottom(BorderStyle.THIN);
        style6.setBorderLeft(BorderStyle.THIN);
        style6.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style6R = workbook.createCellStyle(); // <-----------------------------------------------
        style6R.setFont(font);
        style6R.setAlignment(HorizontalAlignment.RIGHT);
        style6R.setVerticalAlignment(VerticalAlignment.CENTER);
        style6R.setBorderTop(BorderStyle.THIN);
        style6R.setBorderRight(BorderStyle.THIN);
        style6R.setBorderBottom(BorderStyle.THIN);
        style6R.setBorderLeft(BorderStyle.THIN);
        style6R.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style6R.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        

    	XSSFRow row = sheet.createRow(0);
    	row.setHeight((short)500);
        
      //---row 0
        XSSFCell cell=row.createCell(0);
        for(int i=0; i<headerArray.length; i++) {
        	cell=row.createCell(i);
    		cell.setCellValue(""+headerArray[i]);
    		cell.setCellStyle(style);
        }
      //---row 1	
		row = sheet.createRow(1);
		cell=row.createCell(0);
		cell.setCellValue("COUNT "+EMP_MAP.size()+" EMP");
		cell.setCellStyle(style5);
		
		sheet.addMergedRegion(new CellRangeAddress(1,1,1,8));
        cell=row.createCell(1);
    	cell.setCellStyle(style5);
    	
        cell=row.createCell(8);
    	cell.setCellStyle(style5R);
        
      //---row 2-end
		int count=0;
			
		for (String i : EMP_MAP.keySet()) {
			
        		row = sheet.createRow(count+2);
        		
        		if(EMP_MAP.get(i).getEmpStatus().equals(EmpStatus.INACTIVE)) {
        			
        			cell = row.createCell(0);
            		cell.setCellValue(""+employeeRepository.findById(i).get().getEmpCode());
            		cell.setCellStyle(style6);
            		
            		cell = row.createCell(1);
            		cell.setCellValue(""+employeeRepository.findById(i).get().getNickName());
            		cell.setCellStyle(style6);
            		
            		cell = row.createCell(2);
            		cell.setCellValue(""+EMP_MAP.get(i).getLeaveLimit());
            		cell.setCellStyle(style6R);
            		
            		cell = row.createCell(3);
            		cell.setCellValue(""+EMP_MAP.get(i).getMedFeeLimit());
            		cell.setCellStyle(style6R);
            		
            		cell = row.createCell(4);
            		cell.setCellValue(""+utilService.myLeaveDayThisYear(i, wrapper.getYear()));
            		cell.setCellStyle(style6R);
            		
            		cell = row.createCell(5);
            		cell.setCellValue(""+utilService.myMedfeeThisYear(i, wrapper.getYear()));
            		cell.setCellStyle(style6R);
            		
            		cell = row.createCell(6);
            		cell.setCellValue(EMP_MAP.get(i).getLeaveLimit() - utilService.myLeaveDayThisYear(i, wrapper.getYear()) );
            		cell.setCellStyle(style6R);
            		
            		cell = row.createCell(7);
            		cell.setCellValue( EMP_MAP.get(i).getMedFeeLimit()-utilService.myMedfeeThisYear(i, wrapper.getYear()));
            		cell.setCellStyle(style6R);
            		
            		cell = row.createCell(8);
            		cell.setCellValue(""+EMP_MAP.get(i).getEndContract());
            		cell.setCellStyle(style6R);
        			
        		} else {
        			cell = row.createCell(0);
        			cell.setCellValue(""+employeeRepository.findById(i).get().getEmpCode());
        			cell.setCellStyle(style);
        		
        			cell = row.createCell(1);
        			cell.setCellValue(""+employeeRepository.findById(i).get().getNickName());
        			cell.setCellStyle(style);
        		
        			cell = row.createCell(2);
        			cell.setCellValue(""+EMP_MAP.get(i).getLeaveLimit());
        			cell.setCellStyle(style2);
        		
        			cell = row.createCell(3);
        			cell.setCellValue(""+EMP_MAP.get(i).getMedFeeLimit());
        			cell.setCellStyle(style2);
        		
        			cell = row.createCell(4);
        			cell.setCellValue(""+utilService.myLeaveDayThisYear(i, wrapper.getYear()));
        			cell.setCellStyle(style3);
        		
        			cell = row.createCell(5);
        			cell.setCellValue(""+utilService.myMedfeeThisYear(i, wrapper.getYear()));
        			cell.setCellStyle(style3);
        		
        			cell = row.createCell(6);
        			cell.setCellValue(EMP_MAP.get(i).getLeaveLimit() - utilService.myLeaveDayThisYear(i, wrapper.getYear()) );
        			cell.setCellStyle(style4);
        		
        			cell = row.createCell(7);
        			cell.setCellValue( EMP_MAP.get(i).getMedFeeLimit()-utilService.myMedfeeThisYear(i, wrapper.getYear()));
        			cell.setCellStyle(style4);
        		
        			cell = row.createCell(8);
        			cell.setCellValue(""+EMP_MAP.get(i).getEndContract());
        		
        			cell.setCellStyle(style4);
        		}
        		count++;
        	
        }
		
		leaveSummary(wrapper.getOrgID(), wrapper.getYear(), workbook);
		medicalSummary(wrapper.getOrgID(), wrapper.getYear(), workbook);
		
    	String keyDate = wrapper.getYear()+"-"+utilService.paddding(1)+"-"+utilService.paddding(1);
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
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; charset=UTF-8; filename="+"EmployeeBenefit_"+orgRepository.findById(wrapper.getOrgID()).get().getShortName()+""
        		+date.getMonth().toString().substring(0, 3)+year.substring(2, 4)+".xlsx");
        header.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        header.add(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
              
//    	FileOutputStream file = new FileOutputStream("/home/itim/Desktop/wwww.xlsx");
//    	workbook.write(file);
//    	file.close();

    	System.out.println("--------------create Summary sucess----------");
    	return new HttpEntity<byte[]>(content, header);
                   
    }
	
	public void leaveSummary(String orgID, int year, XSSFWorkbook workbook) {

    	Map<String , EmpDetailDomain> EMP_MAP = orgRepository.findById(orgID).get().getEMP_MAP();
		
    	String[] headerArray = {"EmpID","Nickname","Total Leave", "Jan "+year, "Feb "+year, "Mar "+year, "Apr "+year,
    			 "May "+year, "Jun "+year, "Jul "+year, "Aug "+year,
    			 "Sep "+year, "Oct "+year, "Nov "+year, "Dec "+year,};  
    	
    	Double[] totalArray = {0.00, 0.00, 0.00, 0.00, 0.00,
    			0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,};  
		
		XSSFSheet sheet = workbook.createSheet("Leave");
    	sheet.setColumnWidth(0, 4000); 
    	sheet.setColumnWidth(1, 4000); 
    	sheet.setColumnWidth(2, 4000); 
    	sheet.setColumnWidth(3, 3000); 
    	sheet.setColumnWidth(4, 3000); 
    	sheet.setColumnWidth(5, 3000);
    	sheet.setColumnWidth(6, 3000);
    	sheet.setColumnWidth(7, 3000);
    	sheet.setColumnWidth(8, 3000);
    	sheet.setColumnWidth(9, 3000);
    	sheet.setColumnWidth(10, 3000);
    	sheet.setColumnWidth(11, 3000);
    	sheet.setColumnWidth(12, 3000);
    	sheet.setColumnWidth(13, 3000);
    	sheet.setColumnWidth(14, 3000);
    	sheet.setColumnWidth(15, 3000);
    
    	XSSFRow row = sheet.createRow(0);
    	row.setHeight((short)500);
    	
        Font font = workbook.createFont();  
        font.setFontHeightInPoints((short)12);
        font.setFontName("CordiaUPC");
    	
    	XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        
       	XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFont(font);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
       	XSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFont(font);
        style3.setAlignment(HorizontalAlignment.CENTER);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setFillForegroundColor(IndexedColors.RED.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
       	XSSFCellStyle style4 = workbook.createCellStyle();
       	style4.setFont(font);
       	style4.setAlignment(HorizontalAlignment.RIGHT);
       	style4.setVerticalAlignment(VerticalAlignment.CENTER);
       	style4.setBorderTop(BorderStyle.THIN);
       	style4.setBorderRight(BorderStyle.THIN);
       	style4.setBorderBottom(BorderStyle.THIN);
       	style4.setBorderLeft(BorderStyle.THIN);
       	style4.setFillForegroundColor(IndexedColors.RED1.getIndex());
       	style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style6 = workbook.createCellStyle();
        style6.setFont(font);
        style6.setAlignment(HorizontalAlignment.CENTER);
        style6.setVerticalAlignment(VerticalAlignment.CENTER);
        style6.setBorderTop(BorderStyle.THIN);
        style6.setBorderRight(BorderStyle.THIN);
        style6.setBorderBottom(BorderStyle.THIN);
        style6.setBorderLeft(BorderStyle.THIN);
        style6.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //---row 0
        XSSFCell cell=row.createCell(0);
        for(int i=0; i<headerArray.length; i++) {
        	cell=row.createCell(i);
    		cell.setCellValue(""+headerArray[i]);
    		cell.setCellStyle(style);
        }

        //---row 2-end
  		int count=0;
  			//--- colume 0-2
  		for (String i : EMP_MAP.keySet()) {
  			
  			if(EMP_MAP.get(i).getEmpStatus().equals(EmpStatus.INACTIVE)) {
  				
  				row = sheet.createRow(count+2);
  	    		
  				cell = row.createCell(0);
  				cell.setCellValue(""+employeeRepository.findById(i).get().getEmpCode());
  				cell.setCellStyle(style6);
    		
    			cell = row.createCell(1);
    			cell.setCellValue(""+employeeRepository.findById(i).get().getNickName());
    			cell.setCellStyle(style6);
    			
    			cell = row.createCell(2);
    			if(utilService.myLeaveDayThisYear(i, year) == 0) {
    				cell.setCellValue("-");
        			cell.setCellStyle(style6);
    			} else {
    				cell.setCellValue(utilService.myLeaveDayThisYear(i, year));
        			cell.setCellStyle(style6);
    			}

    			//--- colume 3-14
    			for(int j=0 ; j<12; j++) {
        			cell = row.createCell(j+3);
        			cell.setCellStyle(style6);
        		
        			if(utilService.myLeaveDayThisMonth(i, j+1, year) == 0 && LocalDate.now().getMonthValue() >= j+1) {
        				cell.setCellValue("-");
        				cell.setCellStyle(style6);
        			} 
        			else if(LocalDate.now().getMonthValue() >= j+1){
        				cell.setCellValue(utilService.myLeaveDayThisMonth(i, j+1, year));
        				cell.setCellStyle(style6);
        				totalArray[0] += utilService.myLeaveDayThisMonth(i, j+1, year);
        				totalArray[j+1] += utilService.myLeaveDayThisMonth(i, j+1, year);	
        			} 
        			else {
        				cell.setCellValue("");
        				cell.setCellStyle(style6);
        			}
    			}
  				
  			} else {
  			
  				row = sheet.createRow(count+2);
    		
  				cell = row.createCell(0);
  				cell.setCellValue(""+employeeRepository.findById(i).get().getEmpCode());
  				cell.setCellStyle(style);
    		
    			cell = row.createCell(1);
    			cell.setCellValue(""+employeeRepository.findById(i).get().getNickName());
    			cell.setCellStyle(style);
    		
    			cell = row.createCell(2);
    			if(utilService.myLeaveDayThisYear(i, year) == 0) {
    				cell.setCellValue("-");
        			cell.setCellStyle(style);
    			} else {
    				cell.setCellValue(utilService.myLeaveDayThisYear(i, year));
        			cell.setCellStyle(style);
    			}

    			//--- colume 3-14
    			for(int j=0 ; j<12; j++) {
        			cell = row.createCell(j+3);
        			cell.setCellStyle(style2);
        		
        			if(utilService.myLeaveDayThisMonth(i, j+1, year) == 0 && LocalDate.now().getMonthValue() >= j+1) {
        				cell.setCellValue("-");
        				cell.setCellStyle(style);
        			} 
        			else if(LocalDate.now().getMonthValue() >= j+1){
        				cell.setCellValue(utilService.myLeaveDayThisMonth(i, j+1, year));
        				cell.setCellStyle(style);
        				totalArray[0] += utilService.myLeaveDayThisMonth(i, j+1, year);
        				totalArray[j+1] += utilService.myLeaveDayThisMonth(i, j+1, year);	
        			} 
        			else {
        				cell.setCellValue("");
        				cell.setCellStyle(style2);
        			}
    			}
  			}
    			count++;
  		}
  		
        //---row 1
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,1)); 
        
        row = sheet.createRow(1);
        cell=row.createCell(0);
        cell.setCellValue("SUM ");
        cell.setCellStyle(style4);

        
        cell=row.createCell(2);
        cell.setCellValue(totalArray[0]);
        cell.setCellStyle(style3);
        
        for(int i=1; i<totalArray.length; i++) {

        	if(LocalDate.now().getMonthValue() < i) {
        		cell=row.createCell(i+2);
        		cell.setCellValue("");
        		cell.setCellStyle(style3);
        	} 
        	else if(totalArray[i] == 0){
        		cell=row.createCell(i+2);
        		cell.setCellValue("-");
        		cell.setCellStyle(style3);
        	} 
        	else{
        		cell=row.createCell(i+2);
        		cell.setCellValue(totalArray[i]);
        		cell.setCellStyle(style3);
        		} 
        	}

		System.out.println("------------- create Leave sucess ----------");
}
	
	
	public void medicalSummary(String orgID, int year, XSSFWorkbook workbook) {
		
		Map<String , EmpDetailDomain> EMP_MAP = orgRepository.findById(orgID).get().getEMP_MAP();
		
    	String[] headerArray = {"EmpID","Nickname","Total Medical", "Jan "+year, "Feb "+year, "Mar "+year, "Apr "+year,
    			 "May "+year, "Jun "+year, "Jul "+year, "Aug "+year,
    			 "Sep "+year, "Oct "+year, "Nov "+year, "Dec "+year,};  
    	
    	Double[] totalArray = {0.00, 0.00, 0.00, 0.00, 0.00,
    			0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,};  
		
		XSSFSheet sheet = workbook.createSheet("Medical");
		sheet.setColumnWidth(0, 4000); 
    	sheet.setColumnWidth(1, 4000); 
    	sheet.setColumnWidth(2, 4000); 
    	sheet.setColumnWidth(3, 3000); 
    	sheet.setColumnWidth(4, 3000); 
    	sheet.setColumnWidth(5, 3000);
    	sheet.setColumnWidth(6, 3000);
    	sheet.setColumnWidth(7, 3000);
    	sheet.setColumnWidth(8, 3000);
    	sheet.setColumnWidth(9, 3000);
    	sheet.setColumnWidth(10, 3000);
    	sheet.setColumnWidth(11, 3000);
    	sheet.setColumnWidth(12, 3000);
    	sheet.setColumnWidth(13, 3000);
    	sheet.setColumnWidth(14, 3000);
    	sheet.setColumnWidth(15, 3000);
    
    	XSSFRow row = sheet.createRow(0);
    	row.setHeight((short)500);
    	
        Font font = workbook.createFont();  
        font.setFontHeightInPoints((short)12);
        font.setFontName("CordiaUPC");
       
    	XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        
       	XSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFont(font);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
       	XSSFCellStyle style3 = workbook.createCellStyle();
        style3.setFont(font);
        style3.setAlignment(HorizontalAlignment.CENTER);
        style3.setVerticalAlignment(VerticalAlignment.CENTER);
        style3.setBorderTop(BorderStyle.THIN);
        style3.setBorderRight(BorderStyle.THIN);
        style3.setBorderBottom(BorderStyle.THIN);
        style3.setBorderLeft(BorderStyle.THIN);
        style3.setFillForegroundColor(IndexedColors.RED.getIndex());
        style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
       	XSSFCellStyle style4 = workbook.createCellStyle();
       	style4.setFont(font);
       	style4.setAlignment(HorizontalAlignment.RIGHT);
       	style4.setVerticalAlignment(VerticalAlignment.CENTER);
       	style4.setBorderTop(BorderStyle.THIN);
       	style4.setBorderRight(BorderStyle.THIN);
       	style4.setBorderBottom(BorderStyle.THIN);
       	style4.setBorderLeft(BorderStyle.THIN);
       	style4.setFillForegroundColor(IndexedColors.RED1.getIndex());
       	style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFCellStyle style6 = workbook.createCellStyle();
        style6.setFont(font);
        style6.setAlignment(HorizontalAlignment.CENTER);
        style6.setVerticalAlignment(VerticalAlignment.CENTER);
        style6.setBorderTop(BorderStyle.THIN);
        style6.setBorderRight(BorderStyle.THIN);
        style6.setBorderBottom(BorderStyle.THIN);
        style6.setBorderLeft(BorderStyle.THIN);
        style6.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    	
        //---row 0
        XSSFCell cell=row.createCell(0);
        for(int i=0; i<headerArray.length; i++) {
        	cell=row.createCell(i);
    		cell.setCellValue(""+headerArray[i]);
    		cell.setCellStyle(style);
        }

        //---row 2-end
  		int count=0;
  			//--- colume 0-2
  		for (String i : EMP_MAP.keySet()) {
  			
  			if(EMP_MAP.get(i).getEmpStatus().equals(EmpStatus.INACTIVE)) {
  				
  				row = sheet.createRow(count+2);
  	    		
  				cell = row.createCell(0);
    			cell.setCellValue(""+employeeRepository.findById(i).get().getEmpCode());
    			cell.setCellStyle(style6);
    		
    			cell = row.createCell(1);
    			cell.setCellValue(""+employeeRepository.findById(i).get().getNickName());
    			cell.setCellStyle(style6);
    		
    			cell = row.createCell(2);
    			if(utilService.myMedfeeThisYear(i, year) == 0) {
    				cell.setCellValue("-");
    				cell.setCellStyle(style6);
    			} else {
    				cell.setCellValue(utilService.myMedfeeThisYear(i, year));
        			cell.setCellStyle(style6);
    			}

    			//--- colume 3-14
    			for(int j=0 ; j<12; j++) {
        			cell = row.createCell(j+3);
        			cell.setCellStyle(style6);
        		
        			if(utilService.myMedfeeThisMonth(i, j+1, year) == 0 && LocalDate.now().getMonthValue() >= j+1) {
        				cell.setCellValue("-");
        				cell.setCellStyle(style6);
        			} 
        			else if(LocalDate.now().getMonthValue() >= j+1){
        				cell.setCellValue(utilService.myMedfeeThisMonth(i, j+1, year));
        				cell.setCellStyle(style6);
        				totalArray[0] += utilService.myMedfeeThisMonth(i, j+1, year);
        				totalArray[j+1] += utilService.myMedfeeThisMonth(i, j+1, year);	
        			} 
        			else {
        				cell.setCellValue("");
        				cell.setCellStyle(style6);
        			}
    			}
  				
  			} else {
  			
  				row = sheet.createRow(count+2);
    		
  				cell = row.createCell(0);
    			cell.setCellValue(""+employeeRepository.findById(i).get().getEmpCode());
    			cell.setCellStyle(style);
    		
    			cell = row.createCell(1);
    			cell.setCellValue(""+employeeRepository.findById(i).get().getNickName());
    			cell.setCellStyle(style);
    		
    			cell = row.createCell(2);
    			if(utilService.myMedfeeThisYear(i, year) == 0) {
    				cell.setCellValue("-");
    				cell.setCellStyle(style);
    			} else {
    				cell.setCellValue(utilService.myMedfeeThisYear(i, year));
        			cell.setCellStyle(style);
    			}

    			//--- colume 3-14
    			for(int j=0 ; j<12; j++) {
        			cell = row.createCell(j+3);
        			cell.setCellStyle(style2);
        		
        			if(utilService.myMedfeeThisMonth(i, j+1, year) == 0 && LocalDate.now().getMonthValue() >= j+1) {
        				cell.setCellValue("-");
        				cell.setCellStyle(style);
        			} 
        			else if(LocalDate.now().getMonthValue() >= j+1){
        				cell.setCellValue(utilService.myMedfeeThisMonth(i, j+1, year));
        				cell.setCellStyle(style);
        				totalArray[0] += utilService.myMedfeeThisMonth(i, j+1, year);
        				totalArray[j+1] += utilService.myMedfeeThisMonth(i, j+1, year);	
        			} 
        			else {
        				cell.setCellValue("");
        				cell.setCellStyle(style2);
        			}
    			}
  			}
    		
    		count++;
  		}
  		
        //---row 1
        sheet.addMergedRegion(new CellRangeAddress(1,1,0,1)); 
        
        row = sheet.createRow(1);
        cell=row.createCell(0);
        cell.setCellValue("SUM " );
        cell.setCellStyle(style4);
        
        cell=row.createCell(2);
        cell.setCellValue(totalArray[0]);
        cell.setCellStyle(style3);
        
        for(int i=1; i<totalArray.length; i++) {

        	if(LocalDate.now().getMonthValue() < i) {
        		cell=row.createCell(i+2);
        		cell.setCellValue("");
        		cell.setCellStyle(style3);
        	} 
        	else if(totalArray[i] == 0){
        		cell=row.createCell(i+2);
        		cell.setCellValue("-");
        		cell.setCellStyle(style3);
        	} 
        	else{
        		cell=row.createCell(i+2);
        		cell.setCellValue(totalArray[i]);
        		cell.setCellStyle(style3);
        		}
        	}
		
		System.out.println("--------------create Medical sucess----------"); 
	}
	
	 public HttpEntity<byte[]> createExcelMyTimesheet(EmployeeIDMonthWrapper wrapper) throws IOException{
			
//			String empID = "UHy-SIIBa0CUUmxeDRCp";
//			int month = 7;
//			int year = 2022;
			
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
	        
	        InputStream inputStream = new URL("https://www.planetware.com/wpimages/2020/02/france-in-pictures-beautiful-places-to-photograph-eiffel-tower.jpg").openStream();
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
//	        	String formula = "-D41+D42+21";
	        	cell.setCellValue(utilService.myWorkThisMonth(wrapper.getEmpID(), wrapper.getMonth(),  wrapper.getYear()));
//	        	cell.setCellFormula(formula);
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
	        
//	    	FileOutputStream file = new FileOutputStream("/home/itim/Desktop/timesheet.xlsx");
//	    	workbook.write(file);
//	    	file.close();
	        
	    	System.out.println("--------------create Timesheet sucess----------"); 
	        return new HttpEntity<byte[]>(content, header);
	 }

	
}
