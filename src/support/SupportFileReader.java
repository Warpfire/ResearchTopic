package support;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class SupportFileReader {

	
	
	@SuppressWarnings("rawtypes")
	public static HashMap readHeaderFromJob(String filename){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Temp\\"+filename))) {
		    
		    String line = br.readLine();

		    while (line != null && ! line.equals("--")) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser();
		HashMap returnValue = null;
		try {
			returnValue = (HashMap) parser.parse(sb.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap readDataFromJob(String filename){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Temp\\"+filename))) {
		    
		    String line = br.readLine();

		    while (line != null && ! line.equals("--")) {
		        line = br.readLine();
		    }
		    line = br.readLine();
		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser();
		HashMap returnValue = null;
		try {
			returnValue = (HashMap) parser.parse(sb.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;
	}
	
	/**
	 * Method which from a given filename builds a list of maps.
	 * @param filename A filename including extension which leads to a .json file containing an array of maps.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<HashMap> readJsonFromDataset(String filename){
		List<HashMap> resultList = new ArrayList();
		JSONParser parser = new JSONParser();
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Temp\\"+filename))) {
		    
		    String line = br.readLine();
		    while (line != null) {
		    	resultList.add((HashMap) parser.parse(line));
		    	resultList.get(resultList.size()-1).put("id", Long.toString((long) resultList.get(resultList.size()-1).get("id")));
		        line = br.readLine();
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 
	 * @param filename A filename including extension which leads to a .json file containing an array.
	 * @return The array contained in the file cast to a List.
	 */
	@SuppressWarnings("rawtypes")
	public static List readcompiledJobs(String filename){
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader("C:\\Temp\\"+filename))) {
		    
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser();
		List returnValue = null;
		try {
			returnValue = (List) parser.parse(sb.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnValue;
	}
	
	/**
	 * Method which takes a JSON string as input and returns a JSON string with formatting applied for readability.
	 * @param jsonString A JSON String.
	 * @return A JSON string with formatting applied.
	 */
	public static String prettyJSON(String jsonString){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonString);
		return gson.toJson(je);
	}
}
