package com.plusitsolution.timesheet.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plusitsolution.common.toolkit.PlusExcelUtils;
import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.domain.TestDomain;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;
import com.plusitsolution.zeencommon.helper.ExcelBuilder;
import com.plusitsolution.zeencommon.helper.ExcelUtils;

import org.apache.poi.util.IOUtils;


public class TestService {
	
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
    

	public static void main(String[] args) throws Exception{
		write();
	}
	
	public static void  write() throws Exception {
		
		String templateFile = "/home/itim/Desktop/Usagereport.xlsx";
		String outputPath = "/home/itim/Desktop/bbbbb.xlsx";
		
		FileInputStream file = new FileInputStream(new File(templateFile));
		
		List<String[]> read = PlusExcelUtils.read(templateFile, "Sheet1");
		System.out.println(PlusJsonUtils.convertToJsonString(read));

		Workbook wb = new XSSFWorkbook(file);
		Sheet sheet = wb.getSheet("Sheet1");

	
		
//		PlusExcelUtils.writeWorkbook(wb, outputPath);
		PlusExcelUtils.writeWorkbookFromTemplate(outputPath, wb, generateDomains(), "Sheet1", 7);
        
        System.out.println("completed");
		

	}
	
	private static List<TestDomain> generateDomains() {
		List<TestDomain> domains = new ArrayList<>();
		domains.add(new TestDomain("ss","dd","14"));
		domains.add(new TestDomain("pp","ooo","17"));
		return domains;
	}


}