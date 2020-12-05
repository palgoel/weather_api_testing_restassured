package apiBuilder;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.log.SysoCounter;

import apiConfigs.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BuildApi {
	static RequestSpecification request = RestAssured.given();
	static String url_with_woeid =null;
	static String url_with_date =null;
	static JSONObject post_body = null;
	static JSONObject put_body = null;
	static String post_url="";
	static String put_url="";
	static String head_url="";
	static String loc_type="";
	static String options_url="";


	//this is dummy body to be passed
	public static JSONObject request_body() {
		JSONObject postobj = new JSONObject();
		postobj.put("title", "anyLocation");
		return postobj;
	}

	// BUILD the URLs

	//return the api formed with query to get woeid
	public static String api_url_with_query(Response response) {
		int woeid = apiOperations.OperationOnAPI.get_woeid_from_query_url(response);
		url_with_woeid = ApiEndpoints.endpoints.GET_LOCATION_WEATHER+woeid;	
		return url_with_woeid;		
	}

	//return the api formed with woeid to get the weather forecast of 5 days 
	public static String api_url_with_woeid(Response response) {
		int woeid = apiOperations.OperationOnAPI.get_woeid_from_query_url(response);
		url_with_woeid = ApiEndpoints.endpoints.GET_LOCATION_WEATHER+woeid;	
		return url_with_woeid;		
	}
	//return the api formed with date to get the weather forecast of particulat date
	public static String api_url_with_date(Response response, String date) {
		url_with_date = api_url_with_woeid(response)+"/"+date;//query with date is query string for woeid and date
		return url_with_date;		
	}
	// BUILD ALL POST REQUEST

	//post request for url with location title
	public static Response post_response_of_url_with_location_title(Response response) {
		post_body = request_body();
		String query = apiOperations.OperationOnAPI.get_title_from_query_url(response);// get title from the response returned from query
		post_url= apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_SEARCH + "?query="+query;
		Response resp_of_post = request.body(post_body.toString())
				.header("Content-Type", "application/json").post(post_url);
		return resp_of_post;
	}

	//post request for url with woeid
	public static Response post_response_of_url_with_woeid(Response response) throws InterruptedException {
		post_body = request_body();
		post_url = api_url_with_woeid(response)+ "/";
		Response resp_of_post = request.body(post_body.toString())
				.header("Content-Type", "application/json").redirects().follow(true).redirects().max(100)
				.post(post_url);
		return resp_of_post;
	}
	//post request for url with date
	public static Response post_response_of_url_with_date(Response response, String date) {
		post_body = request_body();
		post_url = api_url_with_date(response,date)+"/";
		Response resp_of_post = request.body(post_body.toString())
				.header("Content-Type", "application/json").post(post_url);
		return resp_of_post;
	}

	// BUILD ALL PUT REQUEST

	//put request for url with location title
	public static Response put_response_of_url_with_location_title(Response response) {
		put_body = request_body();
		String query = apiOperations.OperationOnAPI.get_title_from_query_url(response);// get title from the response returned from query
		put_url= apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_SEARCH + "?query="+query;
		Response resp_of_put = request.body(put_body.toString())
				.header("Content-Type", "application/json").put(put_url);
		return resp_of_put;
	}

	//put request for url with woeid
	public static Response put_response_of_url_with_woeid(Response response) throws InterruptedException {
		put_body = request_body();
		put_url = api_url_with_woeid(response)+ "/";
		Response resp_of_put = request.body(put_body.toString())
				.header("Content-Type", "application/json").put(put_url);
		return resp_of_put;
	}
	//put request for url with date
	public static Response put_response_of_url_with_date(Response response, String date) {
		put_body = request_body();
		put_url = api_url_with_date(response,date)+"/";
		Response resp_of_put = request.body(put_body.toString())
				.header("Content-Type", "application/json").put(put_url);
		return resp_of_put;
	}

	// BUILD ALL HEAD REQUEST

	//head request for url with location title
	public static Response head_response_of_url_with_location_title(Response response) {
		String query = apiOperations.OperationOnAPI.get_title_from_query_url(response);// get title from the response returned from query
		head_url= apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_SEARCH + "?query="+query;
		Response resp_of_head = request.head(head_url);
		return resp_of_head;
	}
	//head request for url with woeid
	public static Response head_response_of_url_with_woeid(Response response) throws InterruptedException {
		head_url = api_url_with_woeid(response)+ "/";
		Response resp_of_head = request.head(head_url);
		return resp_of_head;
	}
	//head request for url with date
	public static Response head_response_of_url_with_date(Response response, String date) {
		head_url = api_url_with_date(response,date)+"/";
		Response resp_of_head = request.head(head_url);
		return resp_of_head;
	}	
	// BUILD ALL options REQUEST

		//options request for url with location title
		public static Response options_response_of_url_with_location_title(Response response) {
			String query = apiOperations.OperationOnAPI.get_title_from_query_url(response);// get title from the response returned from query
			options_url= apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_SEARCH + "?query="+query;
			Response resp_of_options = request.options(options_url);
			return resp_of_options;
		}
		//options request for url with woeid
		public static Response options_response_of_url_with_woeid(Response response) throws InterruptedException {
			options_url = api_url_with_woeid(response)+ "/";
			Response resp_of_options = request.options(options_url);
			return resp_of_options;
		}
		//options request for url with date
		public static Response options_response_of_url_with_date(Response response, String date) {
			options_url = api_url_with_date(response,date)+"/";
			Response resp_of_options = request.options(options_url);
			return resp_of_options;
		}	
	// RETURN ALL RESPONSES

	//return response of API with woeid
	public static Response get_response_of_url_with_woeid(Response response) {
		String query_with_woeid = api_url_with_woeid(response);
		Response resp_to_get_report = RestAssured.given().header("Content-Type", "application/json").when().get(query_with_woeid);
		return resp_to_get_report;
	}

	//return response of API with woeid and date
	public static Response get_response_of_url_with_date(Response response,String date) {
		String query_with_date = api_url_with_date(response, date);//query with date is query string for woeid and date
		Response resp_to_get_report = RestAssured.given().when().get(query_with_date);		
		return resp_to_get_report;
	}
}
