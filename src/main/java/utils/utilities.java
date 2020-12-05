package utils;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import io.restassured.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;    
public class utilities {  
	static String query_with_woeid;
	public static String get_tommorow_date() {
		String date = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		LocalDate now = LocalDate.now();  
		LocalDate tomorrow = now.plusDays(1);
		date = (String)dtf.format(tomorrow);
		return date;  
	}    
	public static void generate_file(String content) throws Exception {
		try {  
			FileWriter myWriter = new FileWriter("./Reports/weather_report.txt");
			myWriter.write(content);
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} 
	}

	public static List<String> convert_String_to_list(String expected_fields) {
		String[] split_list = expected_fields.split(",");
		List<String> list_of_expected_fields = new ArrayList<String>();
		for (int i=0;i<split_list.length;i++) {
			list_of_expected_fields.add(split_list[i].strip());
		}
		return list_of_expected_fields;
	}	
}    