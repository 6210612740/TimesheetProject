package com.plusitsolution.timesheet.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;import com.plusitsolution.common.toolkit.PlusHashUtils;
import com.plusitsolution.common.toolkit.PlusJsonUtils;
import com.plusitsolution.timesheet.domain.Employee.EmpDetailDomain;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

@Service
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
	
    
    	
    

}
