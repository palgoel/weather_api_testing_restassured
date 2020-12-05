package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataProvider {
	XSSFWorkbook wb;
	public ExcelDataProvider() {
		File src = new File("./testData/QueryParams.xlsx");
		try {
			FileInputStream file = new FileInputStream(src);
			wb = new XSSFWorkbook(file);
			
		} catch (Exception e) {
			System.out.println("Unable to get excel file"+e.getMessage());
		}				
	}
	//get value of quersyring from 1st column of query sheet
	public String getQueryString() {
		int max_excel_rows = wb.getSheet("query").getLastRowNum();
		Random rand = new Random(); 
		int row = rand.nextInt(max_excel_rows);
		return wb.getSheet("query").getRow(row).getCell(0).getStringCellValue();
	}
	//get value of date from 2nd column of query sheet
	public String getQueryDate() {
		int max_excel_rows = wb.getSheet("query").getLastRowNum();
		Random rand = new Random(); 
		int row = rand.nextInt(max_excel_rows);
		return wb.getSheet("query").getRow(row).getCell(1).getStringCellValue();
	}

}
