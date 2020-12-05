package apiOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import apiConfigs.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class OperationOnAPI  {
	static List<String> keys_of_woeid_list = null;
	static List<String> keys_of_date_list = null;
	static List<String> keys_of_country_woeid_list = null;
	int i=0;
	static String title ="";
	static int woeid =0;
	public static String weather_info_for_tomorrow_date(Response response) {		
		JSONObject jsonobj = null;
		// utility to get tomorrows date
		String tommorow_date = utils.utilities.get_tommorow_date();
		String resp_as_string= response.getBody().asString();
		JSONObject jobj = new JSONObject(resp_as_string);
		JSONArray jarr = new JSONArray(jobj.get("consolidated_weather").toString());
		for (int i=0;i< jarr.length();i++) {
			jsonobj = jarr.getJSONObject(i);
			String date_of_weather_report = (String) jsonobj.get("applicable_date");
			date_of_weather_report=date_of_weather_report.replace('-', '/');
			if(date_of_weather_report.contentEquals(tommorow_date)) {
				return jsonobj.toString();
			}
		}
		return "";
	}	

	// it checks if it has forecast for next five days
	public static int woeid_url_record_count(Response response) {
		int check_record_count_forecast = 0;
		String resp_as_string= response.getBody().asString();
		JSONObject jobj = new JSONObject(resp_as_string);
		JSONArray jarr = new JSONArray(jobj.get("consolidated_weather").toString());
		check_record_count_forecast = jarr.length();
		return check_record_count_forecast;
	}
	public static List<String> woeid_url_record_fields(Response response) {
		String resp_as_string= response.getBody().asString();
		JSONObject jobj = new JSONObject(resp_as_string);
		for(int i=0;i<jobj.length();i++) {
			keys_of_woeid_list = new ArrayList<String>(jobj.keySet());
			break;
		}
		return keys_of_woeid_list;
	}
	public static List<String> woeid_url_consolidated_weather_fields(Response response) {
		String resp_as_string= response.getBody().asString();
		JSONObject jobj = new JSONObject(resp_as_string);
		JSONArray jarry = new JSONArray(jobj.get("consolidated_weather").toString());
		for(int i=0;i<jarry.length();i++) {
			JSONObject jsonobj = jarry.getJSONObject(i) ;
			keys_of_woeid_list = new ArrayList<String>(jsonobj.keySet());
			break;
		}
		return keys_of_woeid_list;
	}
	public static List<String> date_url_fields(Response response) {
		String resp_as_string= response.getBody().asString();
		JSONArray jarry = new JSONArray(resp_as_string);
		for(int i=0;i<jarry.length();i++) {
			JSONObject jsonobj = jarry.getJSONObject(i) ;
			keys_of_date_list = new ArrayList<String>(jsonobj.keySet());
			break;
		}
		return keys_of_date_list;
	}

	//true if status code is 405
	public static boolean post_put_method_not_allow_code(Response response) {
		if(response.getStatusCode() == 405) {
			return true;
		}		
		return false;		
	}
	public static Set<String> check_response_for_country_woeid_url(int woeid) {
		String country_query_woeid_url = apiConfigs.ApiEndpoints.endpoints.GET_LOCATION_WEATHER + woeid;
		Response resp_of_country_woeid = RestAssured.given().get(country_query_woeid_url);
		String resp_as_string= resp_of_country_woeid.getBody().asString();
		JSONObject jobj = new JSONObject(resp_as_string);
		return jobj.keySet();
	}
	// returns woeid of the location passed from query url
	public static int get_woeid_from_query_url(Response response) {
		String response_string = response.asString();
		JSONArray jarry = new JSONArray(response_string);
		for(int i=0;i<jarry.length();i++) {
			JSONObject jobj = jarry.getJSONObject(i);
			woeid = jobj.getInt("woeid");
			return woeid;
		}
		return woeid;		
	}
	// returns woeid of the location passed from woeid url
	public static int get_woeid_from_woeid_url(Response response) {
		String response_string = response.asString();
		JSONObject jobj = new JSONObject(response_string);
		int woeid= jobj.getInt("woeid");
		return woeid;

	}
	// returns title of the location passed
	public static String get_title_from_query_url(Response response) {
		String response_string = response.asString();
		JSONArray jarry = new JSONArray(response_string);
		for(int i=0;i<jarry.length();i++) {
			JSONObject jobj = jarry.getJSONObject(i);
			title = jobj.getString("title");
			return title;
		}
		return title;		
	}
	//compare if the resultant response is given for entered woeid only so matching the "woeid" field value from query url and woeid url
	public static boolean compare_woeid_in_query_and_woeid_url(Response query_url_response,Response woeid_url_response) {
		String query_with_woeid = apiBuilder.BuildApi.api_url_with_woeid(query_url_response);
		String expected_woeid=query_with_woeid;
		expected_woeid=expected_woeid.replace("/api/location/", "");
		int query_url_woeid = Integer.parseInt(expected_woeid);
		int woeid_url_woeid = apiOperations.OperationOnAPI.get_woeid_from_woeid_url(woeid_url_response);
		if(query_url_woeid == woeid_url_woeid) {
			return true;
		}else {
			return false;
		}
	}
}
