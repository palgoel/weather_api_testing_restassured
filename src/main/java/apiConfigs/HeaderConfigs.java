package apiConfigs;

import static org.testng.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public class HeaderConfigs {
	public static Map<String,String> defaultHeader(){
		Map<String,String> headerDefault = new HashMap<String,String>();
		headerDefault.put("Content-Type", "application/json");
		return headerDefault;
	}
	public static Map<String,String> headerWithToken(){
		Map<String,String> tokenheader = new HashMap<String,String>();
		tokenheader.put("Allow  headerValue", "GET, HEAD, OPTIONS");
		tokenheader.put("Vary", "Accept-Language, Cookie");
		return tokenheader;		
	}
	//get headers of response
	public static void getheaders(Response resp) {
		Headers allheaders = resp.headers();
		for(Header header: allheaders) {
			System.out.println("headerKey="+header.getName()+"  headerValue="+header.getValue());			
		}	
	}
}
