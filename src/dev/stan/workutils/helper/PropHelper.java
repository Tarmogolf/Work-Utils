package dev.stan.workutils.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import dev.stan.workutils.exception.PropertyNotFoundException;

public class PropHelper {
	private static Properties dymaxProp = new Properties();
	private static String propFile = "./src/dymax.properties";
	
	static {
		FileInputStream input;
		try{
			input = new FileInputStream(propFile);
			dymaxProp.load(input);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("property file '" + propFile + "' not found in the classpath");
		}
	}
	
	public static String getPropertyValue(String key) throws PropertyNotFoundException{
		if(!dymaxProp.containsKey(key)){
			throw new PropertyNotFoundException(key +" not found in properties.");
		}else{
			return dymaxProp.getProperty(key);
		}
	}
}
