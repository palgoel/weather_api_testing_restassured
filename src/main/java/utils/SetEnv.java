package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SetEnv {
	public static Map<String,String> configmap = new HashMap<String,String>();
	public static Properties prop = new Properties();
	
	public static Map<String,String> envsetup() {
		try {
				FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/testData/config.properties");
		        prop.load(fis);
		        configmap.put("BASEURL", prop.getProperty("BASEURL"));		
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return configmap;
    }
	public  static Map<String,String> confignull(){
		if (configmap == null) {
			configmap = envsetup();		//configmap is object of hashmap and envsetup () is the function above
		}
		return configmap;
	}
	public static String get_value_from_config_file(String key) {
		String key_value="";
		try {
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/testData/config.properties");
	        prop.load(fis);
	        key_value = prop.getProperty(key);		
        
	} catch (Exception e) {
		e.printStackTrace();
	} 		
		return key_value;		
	}
}
