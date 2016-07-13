package scrape;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import support.NewFileWriter;
import support.SupportFileReader;


public class EscapeCharsTest {
	
	
	public static void main(String[] args) {
		List dataEntries = SupportFileReader.readJsonFromDataset(args[0]);
		HashMap<String, String> dataSet = (HashMap<String, String>) dataEntries.get(0);
		List<String> searchTerms = new ArrayList<String>();
		searchTerms.addAll(Arrays.asList(dataSet.get("Manitem").split("\\|")));
		searchTerms.addAll(Arrays.asList(dataSet.get("Suppitem").split("\\|")));
		searchTerms.addAll(Arrays.asList(dataSet.get("EAN").split("\\|")));
		System.out.println(searchTerms.toString());
		HashSet<String> results = new HashSet<String>();
		for(String searchTerm : searchTerms)  {
			try {
				if(searchTerm.length()>3){
					results.addAll(FetchProductListingFromSearch.FetchSearch(searchTerm));
					}
					}
			 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}	
		System.out.println(results.toString());
	}
	
	public static String prettyJson(String jsonString){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonString);
		return gson.toJson(je);
	}
}
