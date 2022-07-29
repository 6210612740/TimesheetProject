package com.plusitsolution.timesheet.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plusitsolution.timesheet.domain.EnumDomain.DateStatus;
import com.plusitsolution.timesheet.repository.EmployeeRepository;
import com.plusitsolution.timesheet.repository.HolidayRepository;
import com.plusitsolution.timesheet.repository.MedicalRepository;
import com.plusitsolution.timesheet.repository.OrganizeRepository;

import org.apache.poi.util.IOUtils;

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
    

//    public void asdasd() throws IOException{
//	
//	String orgID = "PHwbQ4IBa0CUUmxehAuY";
//	int month = 7;
//	int year = 2022;
//    	
//    	XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("Timesheet");
//        
//        //ไม่มีขอบ บน ล่าง ขวา
//        CellStyle style = workbook.createCellStyle();  
//        style.setBorderBottom(BorderStyle.THICK);  
//        style.setBottomBorderColor(IndexedColors.WHITE.getIndex());  
//        style.setBorderRight(BorderStyle.THIN);  
//        style.setRightBorderColor(IndexedColors.WHITE.getIndex());  
//        style.setBorderTop(BorderStyle.THICK);
//        style.setTopBorderColor(IndexedColors.WHITE.getIndex()); 
//        //ไม่มีขอบ บน ล่าง
//        CellStyle style1 = workbook.createCellStyle();
//        style1.setAlignment(HorizontalAlignment.RIGHT);
//        style1.setVerticalAlignment(VerticalAlignment.BOTTOM);
//        style1.setBorderBottom(BorderStyle.THICK);  
//        style1.setBottomBorderColor(IndexedColors.WHITE.getIndex());    
//        style1.setBorderTop(BorderStyle.THICK);
//        style1.setTopBorderColor(IndexedColors.WHITE.getIndex());
//        
//        //กลาง
//        XSSFCellStyle style2 = workbook.createCellStyle();
//        style2.setAlignment(HorizontalAlignment.CENTER);
//        style2.setVerticalAlignment(VerticalAlignment.CENTER);
//        
//        //ไม่มีขอบ ขวา ชิดขวา
//        XSSFCellStyle style3 = workbook.createCellStyle();
//        style3.setAlignment(HorizontalAlignment.RIGHT);
////        style2.setVerticalAlignment(VerticalAlignment.CENTER);
//        style3.setBorderRight(BorderStyle.THIN);  
//        style3.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        
//        //ไม่มีขอบ บน ขวา
//        XSSFCellStyle style4 = workbook.createCellStyle();
//        style4.setBorderTop(BorderStyle.THIN);
//        style4.setTopBorderColor(IndexedColors.WHITE.getIndex());
//        style4.setBorderRight(BorderStyle.THIN);  
//        style4.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        
//        //ไม่มีขอบ บน
//        XSSFCellStyle style5 = workbook.createCellStyle();
//        style5.setBorderTop(BorderStyle.THIN);
//        style5.setTopBorderColor(IndexedColors.WHITE.getIndex());
//        
//        //ไม่มีขอบ ล่าง ขวา ชิดขวา
//        XSSFCellStyle style6 = workbook.createCellStyle();
//        style6.setBorderBottom(BorderStyle.THICK);  
//        style6.setBottomBorderColor(IndexedColors.WHITE.getIndex());   
//        style6.setAlignment(HorizontalAlignment.RIGHT);
////        style6.setVerticalAlignment(VerticalAlignment.CENTER);
//        style6.setBorderRight(BorderStyle.THIN);  
//        style6.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        //เทา กลาง
//        XSSFCellStyle style7 = workbook.createCellStyle();
//        style7.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        style7.setFillPattern(FillPatternType.BIG_SPOTS);
//        style7.setAlignment(HorizontalAlignment.CENTER);
//        style7.setVerticalAlignment(VerticalAlignment.CENTER);
//        
//        //ไม่มีขอบ ขวา ล่าง
//        XSSFCellStyle style8 = workbook.createCellStyle();
//
//        style8.setBorderRight(BorderStyle.THIN);  
//        style8.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        style8.setBorderBottom(BorderStyle.THIN);  
//        style8.setBottomBorderColor(IndexedColors.WHITE.getIndex());
//        
//        CellStyle style9 = workbook.createCellStyle();
//        style9.setAlignment(HorizontalAlignment.CENTER);
//        style9.setBorderBottom(BorderStyle.THICK);  
//        style9.setBottomBorderColor(IndexedColors.WHITE.getIndex());  
//        style9.setBorderRight(BorderStyle.THIN);  
//        style9.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        style9.setVerticalAlignment(VerticalAlignment.CENTER);
//        
//        
//        
//        CellStyle style10 = workbook.createCellStyle();
//        style10.setAlignment(HorizontalAlignment.CENTER);
//        style10.setBorderBottom(BorderStyle.THICK);  
//        style10.setBottomBorderColor(IndexedColors.WHITE.getIndex()); 
//        
//        XSSFCellStyle style11 = workbook.createCellStyle();
//        style11.setAlignment(HorizontalAlignment.CENTER);
//        style11.setBorderBottom(BorderStyle.THICK);  
//        style11.setBottomBorderColor(IndexedColors.WHITE.getIndex());
//        style11.setBorderRight(BorderStyle.THIN);  
//        style11.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        
//        //ชิดขวา
//        CellStyle style12 = workbook.createCellStyle();
//        style12.setAlignment(HorizontalAlignment.RIGHT);
//        //ล่างหนา ไม่มีขอบขวา
//        CellStyle style13 = workbook.createCellStyle();
//        style13.setBorderBottom(BorderStyle.THIN);  
//        style13.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        style13.setBorderRight(BorderStyle.THIN);  
//        style13.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        
//        //ล่างหนา ไม่มีขอบขวา
//        CellStyle style14 = workbook.createCellStyle();
//        style14.setBorderBottom(BorderStyle.THIN);  
//        style14.setBottomBorderColor(IndexedColors.BLACK.getIndex());
//        //ไม่มีขอบล่าง
//        CellStyle style15 = workbook.createCellStyle();
//        style15.setBorderBottom(BorderStyle.THIN);  
//        style15.setBottomBorderColor(IndexedColors.WHITE.getIndex());
//        
//        
//        // ------------------------ ใส่รูปภาพ
//        
//        InputStream inputStream = new FileInputStream("/home/itim/Desktop/zeen.png");
//        //Get the contents of an InputStream as a byte[].
//        byte[] bytes = IOUtils.toByteArray(inputStream);
//        //Adds a picture to the workbook
//        int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
//        //close the input stream
//        inputStream.close();
//      
//        //Returns an object that handles instantiating concrete classes
//        CreationHelper helper = workbook.getCreationHelper();
//      
//        //Creates the top-level drawing patriarch.
//        Drawing drawing = sheet.createDrawingPatriarch();
//      
//        //Create an anchor that is attached to the worksheet
//        ClientAnchor anchor = helper.createClientAnchor();
//        //set top-left corner for the image
//        anchor.setCol1(0);
//        anchor.setRow1(0);
//      
//        //Creates a picture
//        Picture pict = drawing.createPicture(anchor, pictureIdx);
//        //Reset the image to the original size
//        pict.resize();
//        
//        // ----------------------------------------------------
//        
//        Row row = sheet.createRow(0);
//        Row row1 = sheet.createRow(1);
//        Row row2 = sheet.createRow(2); 
//        
//        
//        for(int i = 0 ; i < 6 ;i ++ ) {
//        	Cell cell = row.createCell(i);
//        	Cell cell1 = row1.createCell(i);
//        	Cell cell2 = row2.createCell(i);
//        	cell.setCellStyle(style);
//        	cell1.setCellStyle(style);
//        	cell2.setCellStyle(style);
//        }
//        
//        Cell cell6 = row.createCell(6);
//        Cell cell7 = row1.createCell(6);
//        Cell cell8 = row2.createCell(6);
//
//        
//        //row 4
//        Row row3 = sheet.createRow(3);
//        row3.setHeight((short) 100);
//        for(int i = 0 ; i < 6 ;i ++ ) {
//        	Cell cellEmpty = row3.createCell(i);
//        	cellEmpty.setCellStyle(style4);
//        }
//        row3.createCell(6).setCellStyle(style5);
//        
//        
//    
//        cell6.setCellStyle(style1);
//        cell7.setCellStyle(style1);
//        cell8.setCellStyle(style1);
//        sheet.setColumnWidth(6, 9000);  
//        sheet.setColumnWidth(5, 5000);
//        sheet.setColumnWidth(0, 1000);
//        
//        cell6.setCellValue("บริษัท ซีน อินโนเวชั่น จำกัด");
//        cell7.setCellValue("ZEEN Innovation Co., Ltd.");
//        
////        sheet.addMergedRegion(new CellRangeAddress(2,2,5,6));
//        sheet.addMergedRegion(new CellRangeAddress(0,2,0,4));
//        row2.createCell(6).setCellValue("เลขที่ 64/22 ตรอกวัดสวนพลู (เจริญกรุง 42/1) แขวงบางรัก เขตบางรัก กรุงเทพมหานคร 10500");
//        sheet.addMergedRegion(new CellRangeAddress(4,4,0,6));
//        Cell cellTS = sheet.createRow(4).createCell(0);
//        cellTS.setCellValue("ใบบันทึกเวลาทำงาน / TIME SHEET");
//        cellTS.setCellStyle(style7);
//        // Row 5 -6
//        sheet.addMergedRegion(new CellRangeAddress(5,5,0,1));
//        sheet.addMergedRegion(new CellRangeAddress(6,6,0,1));
//        sheet.addMergedRegion(new CellRangeAddress(5,5,2,4));
//        sheet.addMergedRegion(new CellRangeAddress(6,6,2,4));
//        
//        Row row5 = sheet.createRow(5);
//        Row row6 = sheet.createRow(6);
//        Cell cellTName = row5.createCell(0);
//        cellTName.setCellValue("Employee Name");
//        cellTName.setCellStyle(style6);
//        
//        row5.createCell(1).setCellStyle(style3);
//        Cell cellTCode = row6.createCell(0);
//        cellTCode.setCellValue("Employee Code");
//        cellTCode.setCellStyle(style3);
//        row6.createCell(1).setCellStyle(style3);
//        Cell cellName = row5.createCell(2);
//        cellName.setCellValue("Tharm");
//        Cell cellCode = row6.createCell(2);
//        cellCode.setCellValue("ZEEN112");
//        cellCode.setCellStyle(style5);
//        
//        Cell cellRefNo = row5.createCell(5);
//        cellRefNo.setCellValue("Ref No");
//        cellRefNo.setCellStyle(style6);
//        
//        Cell cellRefNoData = row5.createCell(6);
//        cellRefNoData.setCellValue("2022TS06ZEEN");
//        
//        // จัดการเส้นขอบ
//        row6.createCell(5).setCellStyle(style3);
//        row5.createCell(4).setCellStyle(style3);
//        row6.createCell(4).setCellStyle(style3);
//        row6.createCell(6).setCellStyle(style5);
//       
//        Row row7 = sheet.createRow(7);
//        Cell cellNo = row7.createCell(0);
//        Cell cellDate = row7.createCell(1);
//        Cell cellDay = row7.createCell(2);
//        Cell cellTI = row7.createCell(3);
//        Cell cellTO = row7.createCell(4);
//        Cell cellPro = row7.createCell(5);
//        Cell cellAct = row7.createCell(6);
//        cellNo.setCellValue("#");
//        cellNo.setCellStyle(style7);
//        cellDate.setCellValue("Date");
//        cellDate.setCellStyle(style7);
//        cellDay.setCellValue("Day");
//        cellDay.setCellStyle(style7);
//        cellTI.setCellValue("Time In");
//        cellTI.setCellStyle(style7);
//        cellTO.setCellValue("Time out");
//        cellTO.setCellStyle(style7);
//        cellPro.setCellValue("Project");
//        cellPro.setCellStyle(style7);
//        cellAct.setCellValue("Activities");
//        cellAct.setCellStyle(style7);
//        
//        int x = 1 ;
//        for(int i = 8 ; i < 38 ; i ++ ) {
//        	
//        	Row rowData = sheet.createRow(i);
//        	for(int j = 0 ; j < 7 ; j ++ ) {
//        		Cell cellData = rowData.createCell(j);
//        		if(j == 0) {
//        			if(j != 6) {
//        				cellData.setCellValue(x);
//        				cellData.setCellStyle(style2);
//        				x = x + 1;
//        			}else {
//        				cellData.setCellValue("-");
//        			}
//        			
//        		}else {
//        			if(j != 6) {
//        				cellData.setCellValue("-");
//        				cellData.setCellStyle(style2);
//        			}
//        			else {
//        				cellData.setCellValue("-");
//        			}
//        			
//        		}
//        	}
//        }
//        Row row39 = sheet.createRow(39);
//        row39.setHeight((short) 300);
//        
//        
//        sheet.addMergedRegion(new CellRangeAddress(39,39,0,6));
//        
//        
//        sheet.addMergedRegion(new CellRangeAddress(40,40,0,2));
//        sheet.addMergedRegion(new CellRangeAddress(41,41,0,2));
//        sheet.addMergedRegion(new CellRangeAddress(42,42,0,2));
//        Row row40 = sheet.createRow(40);
//        Row row41 = sheet.createRow(41);
//        Row row42 = sheet.createRow(42);
//        Cell cellLeave = row40.createCell(0);
//        Cell cellOT = row41.createCell(0);
//        Cell cellWorking = row42.createCell(0);
//        
//        cellLeave.setCellValue("ลาหยุด / Leave");
//        cellLeave.setCellStyle(style12);
//        cellOT.setCellValue("ล่วงเวลา / OT");
//        cellOT.setCellStyle(style12);
//        cellWorking.setCellValue("ทำงาน / Working");
//        cellWorking.setCellStyle(style12);
//        //ช่องว่างข้างๆ
//        sheet.addMergedRegion(new CellRangeAddress(40,43,4,6));
//        
//        row40.createCell(4).setCellStyle(style5);
//        row40.createCell(5).setCellStyle(style5);
//        row40.createCell(6).setCellStyle(style5);
//        
//        
//        Row row43 = sheet.createRow(43);
//        
//        row43.setHeight((short) 600);
//        for(int i = 0 ; i < 6 ;i ++ ) {
//        	Cell cellEmpty = row43.createCell(i);
//        	cellEmpty.setCellStyle(style8);
//        }
//        row43.createCell(6).setCellStyle(style5);
//        
//        sheet.addMergedRegion(new CellRangeAddress(44,44,0,3));
//        Row row44 = sheet.createRow(44);
//        Cell cellApprover = row44.createCell(0);
//        cellApprover.setCellValue("ผู้อนุมัติ / Approver");
//        cellApprover.setCellStyle(style11);
//        row44.createCell(1).setCellStyle(style11);
//        row44.createCell(2).setCellStyle(style11);
//        row44.createCell(3).setCellStyle(style11);
//        Cell cellEmployee = row44.createCell(6);
//        cellEmployee.setCellValue("พนักงาน / Employee");
//        cellEmployee.setCellStyle(style10);
//        
//        sheet.addMergedRegion(new CellRangeAddress(45,45,0,3));
//        Row row45 = sheet.createRow(45);
//        row45.setHeight((short) 700);
//        row45.createCell(0).setCellStyle(style13);
//        row45.createCell(1).setCellStyle(style13);
//        row45.createCell(2).setCellStyle(style13);
//        row45.createCell(3).setCellStyle(style13);
//        row45.createCell(6).setCellStyle(style14);
//        
//        
//        sheet.addMergedRegion(new CellRangeAddress(46,46,0,3));
//        Row row46 = sheet.createRow(46);
//        Cell cell46 = row46.createCell(0);
//        
//        cell46.setCellValue("(                                        )");
//        cell46.setCellStyle(style11);
//        row46.createCell(1).setCellStyle(style11);
//        row46.createCell(2).setCellStyle(style11);
//        row46.createCell(3).setCellStyle(style11);
//        
//        
//        
//        Cell cell46Employee = row46.createCell(6);
//        cell46Employee.setCellValue("(firstname Lastname)");
//        cell46Employee.setCellStyle(style10);
//        
//        Row row47 = sheet.createRow(47);
//        row47.setHeight((short) 200);
//        
//        for(int i = 0 ; i < 6 ;i ++ ) {
//        	Cell cellEmpty = row47.createCell(i);
//        	cellEmpty.setCellStyle(style3);
//        }
//        
//        
//        sheet.addMergedRegion(new CellRangeAddress(44,47,4,5));
//        
//        row44.createCell(5).setCellStyle(style3);
//        row45.createCell(5).setCellStyle(style3);
//        row46.createCell(5).setCellStyle(style3);
//        row47.createCell(5).setCellStyle(style3);
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
////        cell=row.createCell(0);
////    	cell.setCellValue(""+(i+1));
////    	if(TIMESHEETS_MAP.get(keyDate).getDateStatus().equals(DateStatus.HOLIDAY)) {
////    		style16.setFont(font12);
////    		cell.setCellStyle(style16);
////    	} else {
////    		style2.setFont(font12);
////    		cell.setCellStyle(style2);
////    	}
////    	
////        cell=row.createCell(1);
////    	cell.setCellValue((i+1)+" "+date.getMonth()+ " "+year);
////    	style2.setFont(font12);
////        cell.setCellStyle(style2);
////        
////        cell=row.createCell(2);
////    	cell.setCellValue(" "+date.getDayOfWeek().toString().substring(0, 3));
////    	style2.setFont(font12);
////        cell.setCellStyle(style2);
////        
////        cell=row.createCell(3);
////        if(TIMESHEETS_MAP.get(keyDate) == null) {
////        	cell.setCellValue(" ");
////        }else {
////        	cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getTimeIn());
////        }
////    	style2.setFont(font12);
////        cell.setCellStyle(style2);
////        
////        cell=row.createCell(4);
////        if(TIMESHEETS_MAP.get(keyDate) == null) {
////        	cell.setCellValue(" ");
////        }else {
////        	cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getTimeOut());
////        }
////    	style2.setFont(font12);
////        cell.setCellStyle(style2);
////        
////        cell=row.createCell(5);
////        if(TIMESHEETS_MAP.get(keyDate) == null) {
////        	cell.setCellValue(" ");
////        }else {
////        	cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getProject());
////        }
////    	style2.setFont(font12);
////        cell.setCellStyle(style2);
////        
////        cell=row.createCell(6);
////        if(TIMESHEETS_MAP.get(keyDate) == null) {
////        	cell.setCellValue(" ");
////        }else {
////        	cell.setCellValue(""+TIMESHEETS_MAP.get(keyDate).getActivity());
////        }
////    	style2.setFont(font12);
////        cell.setCellStyle(style2);
////        
////        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        
//
//        try {
//            FileOutputStream out =  new FileOutputStream(new File("/home/itim/Desktop/as.xlsx"));
//            workbook.write(out);
//            out.close();
//            System.out.println("Excel with foumula cells written successfully");
//              
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        System.out.println("------ create timesheet sucess -------");
//	
//        
//    
//    }
	
	public static void main(String[] args) 
	{
	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("Calculate Simple Interest");
	  
	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("Pricipal");
	    header.createCell(1).setCellValue("RoI");
	    header.createCell(2).setCellValue("T");
	    header.createCell(3).setCellValue("Interest (P r t)");
	      
	    Row dataRow = sheet.createRow(1);
	    dataRow.createCell(0).setCellValue(14500d);
	    dataRow.createCell(1).setCellValue(9.25);
	    dataRow.createCell(2).setCellValue(3d);
	  
	    dataRow.createCell(3).setCellFormula("A2*B2*C2");
	      
	    try {
	        FileOutputStream out =  new FileOutputStream(new File("/home/itim/Desktop/formu.xlsx"));
	        workbook.write(out);
	        out.close();
	        System.out.println("Excel with foumula cells written successfully");
	          
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
    

    
    
    
    

}