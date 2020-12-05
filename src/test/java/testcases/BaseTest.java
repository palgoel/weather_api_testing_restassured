package testcases;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.SetEnv;

public class BaseTest {
	protected static RequestSpecification requestSpecification;
	public static Response resp_to_get_woeid;
	static ExtentReports report;
	public static ExtentTest logger;
	public utils.ExcelDataProvider excel;
	String methodname="";
	@BeforeSuite
	public void setupSuite() {
		report = new ExtentReports("./Reports/Extent_Rep.html");
		excel = new utils.ExcelDataProvider();
	}
	@BeforeTest
	@Parameters("querystring")
	public void response_to_get_woeid_from_api(String querystring) {
		//		querystring =excel.getQueryString();// takes data from 1st column of excel	if we want to run suite for different location/lattlong
		//Currently is takes data from testng.xml if above is uncommented it will take location/latlong from excel.
		RestAssured.baseURI = SetEnv.envsetup().get("BASEURL");	
		RequestSpecification request = RestAssured.given();
		if(querystring.contains(",")) {
			resp_to_get_woeid = request.queryParam("lattlong",querystring)
					.get(apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_SEARCH);
		} else {
			resp_to_get_woeid = request.queryParam("query",querystring)
					.get(apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_SEARCH);
		}	
		int status_code = resp_to_get_woeid.getStatusCode();
		int response_length = resp_to_get_woeid.getBody().asString().toString().length();
		if(!resp_to_get_woeid.getHeaders().getValue("Content-Type").contentEquals("application/json")) {
			report.startTest("The response header value for Content-Type is not valid");
			report.flush();
			throw new NullPointerException("The response header not correct.");	
		}
		if (status_code!=200) {
			report.startTest("The response code = "+status_code+".....You might have passed empty querystring");
			report.flush();
			throw new NullPointerException("The response is empty");		
		}
		if (response_length<=2) {
			report.startTest("The response is empty for entered querystring="+querystring+"...Please pass valid location or lat/long");
			report.flush();
			throw new NullPointerException("The response is null");		
		}


	}	

	// using RequestSpecification
	//	@BeforeClass
	//	public void setupRequestSpecification()
	//	{
	//		 requestSpecification = RestAssured.given()
	//		.baseUri(SetEnv.envsetup().get("BASEURL"));
	//	}

	@BeforeMethod
	public void method_name(Method method) { //to get name of method of test
		methodname = method.getName();
		logger = report.startTest(methodname);
	}

	@AfterMethod
	public void tear(ITestResult result) {
		if (result.getStatus()==ITestResult.FAILURE) {
			logger.log(LogStatus.FAIL,logger.getTest().getName()+" is failed");
		}

		if (result.getStatus()==ITestResult.SUCCESS) {
			logger.log(LogStatus.PASS, logger.getTest().getName()+" is passed");
		}
		report.flush();
	}

	@AfterSuite
	public void stop_test() {
		report.endTest(logger);
	}
	//	
}