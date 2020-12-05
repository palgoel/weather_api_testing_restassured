****RUN****

*To test with the current configurations:*

run pom.xml file at root with giving goal as "clean install"  
Right click on "pom.xml"--> Select"Maven build"--> enter the value "clean install" in "Goals" field and click "Run" button.  
**OR**													
run testng.xml file at root  
Right click on "testng.xml"--> Select"Run As"--> Select"TestNG Suite"
												

****Result****

To check results of test executed you can see Extent Report.  
In root directory click "Reports"--> click "Extent_Rep.html"


****SCENARIO****

**Scenario 1**

To test with different querystring(location or latt,long).

**Actions : You can perform any of Action1 or Action2**

**Action1:**

Double click on "testng.xml" file in root.Change the value of "Nottingham" to your desired value(location or latt,long)
`<parameter name="querystring" value="Nottingham"></parameter>`

after changing value from Nottingham to Pune,the line will look as:

`<parameter name="querystring" value="Pune"></parameter>`                   					
**OR**      					
after changing value from Nottingham to 36.96,-122.02,the line will look as:

`<parameter name="querystring" value="36.96,-122.02"></parameter>`


**Action2:**
Uncomment the given line in below @BeforeTest method by removing **//**

This will override the querystring value from testng.xml to random value from excel sheet

`public void response_to_get_woeid_from_api(String querystring) {
		//	querystring =excel.getQueryString();`
		
**Scenario 2**

Give value of querystring as empty like below in testng.xml, Then this will give error status code 403 and all  tests will fail.    
`<parameter name="querystring" value=""></parameter>`


**Scenario 3**

To test with different dates.

**Actions : You can perform any of Action1 or Action2**

**Action1:**

Double click on "testng.xml" file in root.Change the value of "2018/10/17" to your desired value(date in yyyy/mm/dd format)  
`<parameter name="date" value="2018/10/17"></parameter>`


after changing value from 2018/10/17 to 2019/07/25,the line will look as  
`<parameter name="date" value="2019/07/25"></parameter>`

**Action2:**
Uncomment  // from below line in @Test, for whatever Test you want to use value from Excel.  
This will override the date from testng.xml to random date from excel sheet.  
eg:if you have uncommented the line only in single test say "Testdate", then for only "Testdate", the date value will be taken from excel sheet, other tests will still use value of date in testng.xml  
`@Test`    
`//    date = excel.getQueryDate();`	

***Testcases***
1. *weather_report_for_tomorrow_for_entered_location* ----      Description: Get response for tommorow's date for entered query
2. *weather_report_for_anydate_for_entered_location* ----       Description: Get response for any date passed
3. *check_number_of_forecast_with_woeid_url* ----               Description: Verify if forcast is there for next 5 days
4. *check_fields_for_api_with_woeid* ----                       Description: Verify the fields in woeid api
5. *check_fields_for_consolidated_weather* ----  				Description: Verify fields for consolidate weather
6. *check_fields_for_api_with_date* ----        				Description: Verify the fields in date api
7. *check_response_for_country_woeid_url* ----  				Description: Verify the response generated if woeid is of country
8. *check_get_after_post_req_woeid_url* ----     				Description: Verify if get works correctly after post is done
9. *check_post_req_query_url * ----       						Description: Verify post req not allowed with query url
10. *check_post_req_woeid_url* ----   							Description: Verify post req not allowed with woeid url
11. *check_post_req_date_url* ----      						Description: Verify post req not allowed with date url
12. *check_put_req_query_url* ----    							Description: Verify put req not allowed with woeid url
13. *check_put_req_woeid_url* ----    							Description: Verify put req not allowed with woeid url
14. *check_put_req_date_url* ----   							Description: Verify put req not allowed with date url
15. *check_head_req_query_url* ----  							Description: Verify status code,response body and header is valid for head req with query url
16. *check_head_req_woeid_url* ----   							Description: Verify status code,response body and header is valid for head req with woeid url
17. *check_head_req_date_url* ----  							Description: Verify status code,response body and header is valid for head req with date url
18. *check_options_req_query_url* ----       					Description: Verify status code,response body and header is valid for options req with query url
19. *check_options_req_woeid_url* ----      					Description: Verify status code,response body and header is valid for options req with woeid url
20. *check_options_req_date_url* ----    						Description: Verify status code,response body and header is valid for options req with date url