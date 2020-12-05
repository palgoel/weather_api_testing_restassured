package testcases;

import java.util.List;
import java.util.Set;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class TestAPI extends BaseTest {
	static String weather_report = "";
	static String header_key="";
	static String header_value="";
	int status_code=0;
	Response post_response = null;
	Response delete_response = null;
	Response patch_response = null;
	Response put_response = null;
	public boolean check_status_code(Response response) {
		status_code = response.getStatusCode();
		if (status_code==200) {		
			return true;
		}else {
			logger.log(LogStatus.INFO, "The status code returned is="+status_code +" and not 200");
			return false;
		}
	}
	// Check that response is not empty
	public static boolean check_respose_from_api(Response response) {
		String resp_as_string= response.getBody().asString().toString();
		int response_len = resp_as_string.length();
		if(response_len>=2) {
			return true;
		}else {
			logger.log(LogStatus.INFO, "The response is empty.");
			return false;
		}
	}
	// Check headers of response
	public static boolean check_headers_of_response(Response response) {
		int count=0;
		Headers allheaders = response.headers();
		for(Header header: allheaders) {
			if(header.getName().contentEquals("Content-Type")) {
				header_key = "Content-Type";
				header_value = header.getValue();
				if(!header_value.contentEquals("application/json")) {
					count=count+1;
					break;
				}			
			}
			if(header.getName().contentEquals("Content-Language")) {
				header_key = "Content-Type";
				header_value = header.getValue();
				if(!header_value.contentEquals("en")) {
					count=count+1;
					break;
				}			
			} 			
		}
		if(count ==0) {
			return true;			
		} else {
			logger.log(LogStatus.INFO, "The value for header ="+header_key+ "  is not valid. Valid value = "+header_value);
			return false;
		}
	}

	//Test to get response for tommorow's date for entered query
	@Test(enabled = true)
	public void weather_report_for_tomorrow_for_entered_location () {	
		logger.setDescription("This test is to get the weather report for tommorow for the location passed.");
		Response resp_to_get_report = apiBuilder.BuildApi.get_response_of_url_with_woeid(resp_to_get_woeid);
		//check for valid status code, header and basic response body
		if(!check_headers_of_response(resp_to_get_report) || !check_status_code(resp_to_get_report)||!check_respose_from_api(resp_to_get_report)) {
			Assert.assertTrue(false);
		}
		if(!apiOperations.OperationOnAPI.compare_woeid_in_query_and_woeid_url(resp_to_get_woeid, resp_to_get_report)) {
			logger.log(LogStatus.INFO, "The woeid sent in url and woeid from response generated does not match");
			Assert.assertTrue(false);
		}
		String weather_report = apiOperations.OperationOnAPI.weather_info_for_tomorrow_date(resp_to_get_report);
		if(weather_report=="") {
			logger.log(LogStatus.INFO, "The weather reportis empty");
			Assert.assertTrue(false);				
		}		
		try {
			utils.utilities.generate_file(weather_report);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.log(LogStatus.INFO, "This is the weather report for the location for tomorrows date:\n"+weather_report);
		Assert.assertTrue(true);
	}
	//Test  to get response for any date passed
	@Test(enabled = true)
	@Parameters("date")//send date in format yyyy/mm/dd
	public void weather_report_for_anydate_for_entered_location (String date) {	
		//date = excel.getQueryDate();// takes data from 2nd column of excel
		logger.setDescription("This test is to get the weather report for given date for the location passed.");		
		Response resp_to_get_report = apiBuilder.BuildApi.get_response_of_url_with_date(resp_to_get_woeid, date);
		//check for valid status code, header and basic response body
		if(!check_headers_of_response(resp_to_get_report) || !check_status_code(resp_to_get_report)||!check_respose_from_api(resp_to_get_report)) {
			Assert.assertTrue(false);
		}
		String weather_report = resp_to_get_report.getBody().asString();
		if(weather_report=="") {
			Assert.assertTrue(false,"The weather report is empty");	
		}
		logger.log(LogStatus.INFO, "This is the weather report for the entered date for given location:\n"+weather_report);		
		Assert.assertTrue(true);
	}

	// verify if forcast is there for next 5 days.
	@Test(enabled = true)
	public void check_number_of_forecast_with_woeid_url() {		
		logger.setDescription("This test is to check if for the woeid entere there are forecast for next five days");		
		Response resp_to_get_report = apiBuilder.BuildApi.get_response_of_url_with_woeid(resp_to_get_woeid);//get response for the API with woeid
		//check for valid status code, header and basic response body
		if(!check_headers_of_response(resp_to_get_report) || !check_status_code(resp_to_get_report)||!check_respose_from_api(resp_to_get_report)) {
			Assert.assertTrue(false);
		}
		int count_forecast_dates = apiOperations.OperationOnAPI.woeid_url_record_count(resp_to_get_report);
		if (count_forecast_dates==6) {
			logger.log(LogStatus.INFO, "API return forecast for next 5 days");
			Assert.assertTrue(true);			
		} else {
			logger.log(LogStatus.INFO, "API does not return forecast for next 5 days");
			Assert.assertTrue(false);
		}
	}
	// verify the fields in woeid api
	@Test(enabled = true)
	public void check_fields_for_api_with_woeid() {		
		logger.setDescription("This test is to check the fields of response from the api with woeid");		
		Response resp_to_get_report = apiBuilder.BuildApi.get_response_of_url_with_woeid(resp_to_get_woeid);
		//check for valid status code, header and basic response body
		if(!check_headers_of_response(resp_to_get_report) || !check_status_code(resp_to_get_report)||!check_respose_from_api(resp_to_get_report)) {
			Assert.assertTrue(false);
		}
		//Actual Fields in the woeid url 
		List<String> actual_list_of_fields = apiOperations.OperationOnAPI.woeid_url_record_fields(resp_to_get_report);
		if(actual_list_of_fields == null) {
			logger.log(LogStatus.INFO, "URL with woeid does not return expected fields.");
			Assert.assertTrue(false);
		}
		//Actual fields that should be present
		String expected_fields = utils.SetEnv.get_value_from_config_file("FIELDS_IN_WOEID_URL");
		List<String> list_of_expected_fields = utils.utilities.convert_String_to_list(expected_fields);
		if(list_of_expected_fields.toString().contentEquals(actual_list_of_fields.toString())) {
			logger.log(LogStatus.INFO, "The fields of response from the api with woeid URL = "+actual_list_of_fields);
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "The fields of response from the api with woeid URL does not match.");
			Assert.assertTrue(false);
		}					
	}
	//check fields for consolidate weather
	@Test(enabled = true)
	public void check_fields_for_consolidated_weather() {		
		logger.setDescription("This test is to check the fields of 'consolidated weather response from the api with woeid");		
		Response resp_to_get_report = apiBuilder.BuildApi.get_response_of_url_with_woeid(resp_to_get_woeid);
		//check for valid status code, header and basic response body
		if(!check_headers_of_response(resp_to_get_report) || !check_status_code(resp_to_get_report)||!check_respose_from_api(resp_to_get_report)) {
			Assert.assertTrue(false);
		}
		List<String> actual_list_of_fields = apiOperations.OperationOnAPI.woeid_url_consolidated_weather_fields(resp_to_get_report);
		if(actual_list_of_fields == null) {
			logger.log(LogStatus.INFO, "URL with woeid does not return expected fields.");
			Assert.assertTrue(false);
		}
		//Actual fields that should be present
		String expected_fields = utils.SetEnv.get_value_from_config_file("FIELDS_IN_CONSOLIDATED_WEATHER_FIELD_IN_WOEID_AND_DATE_URL_ARE_SAME");
		List<String> list_of_expected_fields = utils.utilities.convert_String_to_list(expected_fields);
		if(list_of_expected_fields.toString().contentEquals(actual_list_of_fields.toString())) {
			logger.log(LogStatus.INFO, "The fields of consolidated weather section in URL with woeid="+list_of_expected_fields);
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "The fields of 'consolidated weather'response from the api with woeid URL does not match.");
			Assert.assertTrue(false);
		}			
	}
	// verify the fields in date api
	@Test(enabled = true)
	@Parameters("date")
	public void check_fields_for_api_with_date(String date) { 
		//date = excel.getQueryDate();// takes data from 2nd column of excel
		//Currently is takes date from testng.xml if above is uncommented it waill take date from excel
		logger.setDescription("This test is to check the fields of response from the api with date");		
		Response resp_to_get_report = apiBuilder.BuildApi.get_response_of_url_with_date(resp_to_get_woeid, date);
		//check for valid status code, header and basic response body
		if(!check_headers_of_response(resp_to_get_report) || !check_status_code(resp_to_get_report)||!check_respose_from_api(resp_to_get_report)) {
			Assert.assertTrue(false);
		}
		List<String> actual_list_of_fields = apiOperations.OperationOnAPI.date_url_fields(resp_to_get_report);
		if(actual_list_of_fields == null) {
			logger.log(LogStatus.INFO, "URL with date does not return any fields.");
			Assert.assertTrue(false);
		}
		//Actual fields that should be present
		String expected_fields = utils.SetEnv.get_value_from_config_file("FIELDS_IN_CONSOLIDATED_WEATHER_FIELD_IN_WOEID_AND_DATE_URL_ARE_SAME");
		List<String> list_of_expected_fields = utils.utilities.convert_String_to_list(expected_fields);
		if(list_of_expected_fields.toString().contentEquals(actual_list_of_fields.toString())) {
			logger.log(LogStatus.INFO, "The fields of response from the api with date URL="+list_of_expected_fields);
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "The fields of response from the api with date URL does not match.");
			Assert.assertTrue(false);
		}			
	}
	//Test to check the response generated if woeid is of country.
	@Test(enabled = true)
	@Parameters("country_woeid")
	public void check_response_for_country_woeid_url(String country_woeid) {	
		logger.setDescription("This test is check for response when country woeid is given");
		Set<String> country_keys = apiOperations.OperationOnAPI.check_response_for_country_woeid_url(Integer.parseInt(country_woeid));
		if(country_keys.contains("children")) {
			logger.log(LogStatus.INFO, "Country woeid response is correct");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "Country woeid response is not correct");
			Assert.assertTrue(false);
		}
	}
	//Check if get works correctly after post is done
	@Test(enabled = true)
	public void check_get_after_post_req_woeid_url() throws InterruptedException { 
		logger.setDescription("This test is to check the get request after post request is performed");		
		Response post_resp =apiBuilder.BuildApi.post_response_of_url_with_woeid(resp_to_get_woeid);
		Response get_after_post_resp = apiBuilder.BuildApi.get_response_of_url_with_woeid(resp_to_get_woeid);
		if(!check_status_code(get_after_post_resp)){		
			Assert.assertTrue(false);
		}	
		logger.log(LogStatus.INFO, "GET works fine after post is done");
		Assert.assertTrue(true);	
	}
	//Check status code of post req with query url is 405
	@Test(enabled = true)
	public void check_post_req_query_url() { 
		logger.setDescription("This test is to check the post request response for query url");	
		Response post_response =  apiBuilder.BuildApi.post_response_of_url_with_location_title(resp_to_get_woeid);
		if(apiOperations.OperationOnAPI.post_put_method_not_allow_code(post_response)) {
			logger.log(LogStatus.INFO, "Post method not allowed");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "There is some issue with post method");
			Assert.assertTrue(false);
		}
	}
	//Check status code of post req with woeid url is 405
	@Test(enabled = true)
	public void check_post_req_woeid_url() throws InterruptedException { 
		logger.setDescription("This test is to check the post request response for woeid url");		
		Response post_response = apiBuilder.BuildApi.post_response_of_url_with_woeid(resp_to_get_woeid);
		if(apiOperations.OperationOnAPI.post_put_method_not_allow_code(post_response)) {
			logger.log(LogStatus.INFO, "Post method not allowed");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "There is some issue with post method");
			Assert.assertTrue(false);
		}
	}
	//Check status code of post req with date url is 405
	@Test(enabled = true)
	@Parameters("date")
	public void check_post_req_date_url(String date) { //here date is from @Parameters if u want date from xl, add as first line in below func " date = excel.getQueryDate();"// takes data from 2nd column of excel
		//date = excel.getQueryDate();// takes data from 2nd column of excel
		logger.setDescription("This test is to check the post request response for date url");				
		Response post_response = apiBuilder.BuildApi.post_response_of_url_with_date(resp_to_get_woeid,date);
		if(apiOperations.OperationOnAPI.post_put_method_not_allow_code(post_response)) {
			logger.log(LogStatus.INFO, "Post method not allowed");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "There is some issue with post method");
			Assert.assertTrue(false);
		}
	}
	//Check status code of put req with query url is 405
	@Test(enabled = true)
	public void check_put_req_query_url() { 
		logger.setDescription("This test is to check the put request response for query url");	
		Response put_response =  apiBuilder.BuildApi.put_response_of_url_with_location_title(resp_to_get_woeid);
		if(apiOperations.OperationOnAPI.post_put_method_not_allow_code(put_response)) {
			logger.log(LogStatus.INFO, "put method not allowed");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "There is some issue with put method");
			Assert.assertTrue(false);
		}
	}
	//Check status code of put req with woeid url is 405
	@Test(enabled = true)
	public void check_put_req_woeid_url() throws InterruptedException { 
		logger.setDescription("This test is to check the put request response for woeid url");		
		Response put_response = apiBuilder.BuildApi.put_response_of_url_with_woeid(resp_to_get_woeid);
		if(apiOperations.OperationOnAPI.post_put_method_not_allow_code(put_response)) {
			logger.log(LogStatus.INFO, "put method not allowed");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "There is some issue with put method");
			Assert.assertTrue(false);
		}
	}
	//Check status code of put req with date url is 405
	@Test(enabled = true)
	@Parameters("date")
	public void check_put_req_date_url(String date) { //here date is from @Parameters if u want date from xl, add as first line in below func " date = excel.getQueryDate();"// takes data from 2nd column of excel
		//date = excel.getQueryDate();// takes data from 2nd column of excel
		logger.setDescription("This test is to check the put request response for date url");		
		Response put_response = apiBuilder.BuildApi.put_response_of_url_with_date(resp_to_get_woeid,date);
		if(apiOperations.OperationOnAPI.post_put_method_not_allow_code(put_response)) {
			logger.log(LogStatus.INFO, "put method not allowed");
			Assert.assertTrue(true);
		}else {
			logger.log(LogStatus.INFO, "There is some issue with put method");
			Assert.assertTrue(false);
		}
	}
	//Check status code of head req with query url is 200
	@Test(enabled = true)
	public void check_head_req_query_url() { 
		logger.setDescription("This test is to check the head request response for query url");	
		Response head_response =  apiBuilder.BuildApi.head_response_of_url_with_location_title(resp_to_get_woeid);
		if(!check_status_code(head_response)) {
			logger.log(LogStatus.INFO, "There is some issue with head method");
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	//Check status code of head req with woeid url is 200
	@Test(enabled = true)
	public void check_head_req_woeid_url() throws InterruptedException { 
		logger.setDescription("This test is to check the head request response for woeid url");		
		Response head_response = apiBuilder.BuildApi.head_response_of_url_with_woeid(resp_to_get_woeid);
		if(!check_status_code(head_response)) {
			logger.log(LogStatus.INFO, "There is some issue with head method");
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	//Check status code of head req with date url is 200
	@Test(enabled = true)
	@Parameters("date")
	public void check_head_req_date_url(String date) { //here date is from @Parameters if u want date from xl, add as first line in below func " date = excel.getQueryDate();"// takes data from 2nd column of excel
		//date = excel.getQueryDate();// takes data from 2nd column of excellogger.setDescription("This test is to check the head request response for date url");		
		Response head_response = apiBuilder.BuildApi.head_response_of_url_with_date(resp_to_get_woeid,date);
		if(!check_status_code(head_response)) {
			logger.log(LogStatus.INFO, "There is some issue with head method");
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	//Check status code,response body and header is valid for options req with query url
	@Test(enabled = true)
	public void check_options_req_query_url() { 
		logger.setDescription("This test is to check the options request response for query url");	
		Response options_response =  apiBuilder.BuildApi.options_response_of_url_with_location_title(resp_to_get_woeid);
		if(!check_headers_of_response(options_response) || !check_status_code(options_response)||!check_respose_from_api(options_response)) {
			logger.log(LogStatus.INFO, "There is some issue with options method");
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	//Check status code,response body and header is valid for options req with woeid url
	@Test(enabled = true)
	public void check_options_req_woeid_url() throws InterruptedException { 
		logger.setDescription("This test is to check the options request response for woeid url");		
		Response options_response = apiBuilder.BuildApi.options_response_of_url_with_woeid(resp_to_get_woeid);
		if(!check_headers_of_response(options_response) || !check_status_code(options_response)||!check_respose_from_api(options_response)) {
			logger.log(LogStatus.INFO, "There is some issue with options method");
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
	//Check status code,response body and header is valid for options req with date url
	@Test(enabled = true)
	@Parameters("date")
	public void check_options_req_date_url(String date) { //here date is from @Parameters if u want date from xl, add as first line in below func " date = excel.getQueryDate();"// takes data from 2nd column of excel
		//date = excel.getQueryDate();// takes data from 2nd column of excellogger.setDescription("This test is to check the options request response for date url");		
		Response options_response = apiBuilder.BuildApi.options_response_of_url_with_date(resp_to_get_woeid,date);
		if(!check_headers_of_response(options_response) || !check_status_code(options_response)||!check_respose_from_api(options_response)) {
			logger.log(LogStatus.INFO, "There is some issue with options method");
			Assert.assertTrue(false);
		}
		Assert.assertTrue(true);
	}
}

