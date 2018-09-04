package com.saran.testdataAccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.saran.testUtils.TestParameters;



public class MSExcelReader {



	private static String dataTablePath;
	private Workbook workBook;
	private Sheet workSheet;
	private String currentTestCase;


	
	 private static  MSExcelReader instance = new MSExcelReader();

	 private MSExcelReader() {
	 
	}

	public static  MSExcelReader getInstance() {
		
		return instance;
	}


	//Create an object for workbook
	public Workbook createobjectWorkbook(String dataTablePath) {


		try {
			FileInputStream	fis = new FileInputStream(new File(dataTablePath));
			try {
				workBook = new HSSFWorkbook(fis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return workBook;

	}

	private Sheet getSheet(String workSheetName) {
		Sheet workSheet = workBook.getSheet(workSheetName);
		return workSheet;
	}

	public int getColumnNum(String workSheetName,String ColumnName) {

		Sheet workSheet = getSheet(workSheetName);

		for(Row row:workSheet) {			
			for(Cell cell:row) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				int colIndex = cell.getColumnIndex();
				if(cell.getSheet().getRow(0).getCell(colIndex).getRichStringCellValue().toString().equals(ColumnName)) {
					return cell.getColumnIndex();
				}

			}
		}							
		return 0;		
	}

	public int getRowNum(String workSheetName,String currentTestcase) {

		Sheet workSheet = getSheet(workSheetName);
		Iterator<Row>iterator=workSheet.iterator();
		while(iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell>cellIterator=nextRow.cellIterator();
			while(cellIterator.hasNext()) {
				Cell cell=cellIterator.next();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if(cell.getRichStringCellValue().getString().trim().equals(currentTestcase)) {
					return cell.getRowIndex();
				}
			}
		}


		return 0;

	}
	
	public String getData(String workSheetName,String ColumnName) {
		String data = null;
		
		Sheet worksheet = getSheet(workSheetName);
		Row currentRow = worksheet.getRow(getRowNum(workSheetName,currentTestCase));
		int colNum = getColumnNum(workSheetName, ColumnName);
		
		Cell cell = currentRow.getCell(colNum);

		
		if(cell!=null){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			data = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			data = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			data = String.valueOf(cell.getNumericCellValue());
			break;
		}
		}
		return data;
		
		
	}
	
	public String getData(String workSheetName,int rowIndex ,String ColumnName) {
		String data = null;
		
		Sheet worksheet = getSheet(workSheetName);
		Row currentRow = worksheet.getRow(rowIndex);
		int colNum = getColumnNum(workSheetName, ColumnName);
		
		Cell cell = currentRow.getCell(colNum);

		
		if(cell!=null){
		cell.setCellType(Cell.CELL_TYPE_STRING);
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			data = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			data = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			data = String.valueOf(cell.getNumericCellValue());
			break;
		}
		}
		return data;
		
		
	}
	
	public  LinkedList<TestParameters>getRunMangerInfo(String Suite){
		
		LinkedList<TestParameters> runinfolist = new LinkedList<TestParameters>();
		Sheet worksheet= workBook.getSheet(Suite);
		int lastRowNum = worksheet.getLastRowNum();
		
		for(int i=0; i<=lastRowNum;i++) {
			TestParameters testparameters = new TestParameters();
			if(getData(Suite, i,"Execute").equalsIgnoreCase("Yes")) {
				
				testparameters.setCurrentTestcase(getData(Suite,i,"TC_ID"));
				testparameters.setTestRailId(getData(Suite,i,"TESTRAIL_ID"));
				testparameters.setDescription(getData(Suite,i,"Description"));
				testparameters.setExecuteCurrentTestcase(getData(Suite,i,"Execute"));
				testparameters.setBrowser(getData(Suite,i,"Browser"));
				runinfolist.add(testparameters);
			}
										
		}				
		return runinfolist;			
	}

	public void setCurrentRow(String currentTestcase) {
		this.currentTestCase = currentTestcase;
	
	}
	
	
	public LinkedHashMap<String,String>getRowData(String workSheetName ,String currentTestCase){
		LinkedHashMap<String,String>rowData=new LinkedHashMap<String,String>();
		Sheet sheet = workBook.getSheet(workSheetName);
		Row colName = sheet.getRow(0);	
		Row currentRow = sheet.getRow(getRowNum(workSheetName,currentTestCase));
		int lastRowNum = currentRow.getLastCellNum();
		
		for(int col=0; col<lastRowNum;col++) {
				
			Cell key = colName.getCell(col, Row.RETURN_BLANK_AS_NULL);
			Cell value = currentRow.getCell(col, Row.RETURN_BLANK_AS_NULL);
			
			if(key==null) {
				
			}
			else if(value!=null){
				switch (value.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					rowData.put(key.getStringCellValue(), value.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					rowData.put(String.valueOf(key.getBooleanCellValue()), String.valueOf(value.getBooleanCellValue()));
					break;
				case Cell.CELL_TYPE_NUMERIC:
					rowData.put(String.valueOf(key.getNumericCellValue()), String.valueOf(value.getNumericCellValue()));
					break;
				}
				
			}
		}
		return rowData;
		
	}

}
