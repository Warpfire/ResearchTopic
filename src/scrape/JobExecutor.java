package scrape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;

import support.NewFileWriter;

public class JobExecutor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i =0; i<args.length;i++){
			executeJob(i+1,args[i]);
		}
	}
	
	public static void executeJob(int jobID, String searchTerm){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		String contentBlock = " ";
		
		resultMap.put("serial", jobID);
		resultMap.put("status", "success");
		resultMap.put("id", "searchTerm");
		ArrayList<String> individualResults = new ArrayList<String>();
		resultMap.put("results", individualResults);
		individualResults.add(""+jobID+"-1");
		individualResults.add(""+jobID+"-2");
		individualResults.add(""+jobID+"-3");
		
		try{
		Iterator<String> results  = FetchProductListingFromSearch.FetchSearch(searchTerm).iterator();
		int i=0;
		while(results.hasNext()){
			i++;
			FetchProductListingFromSearch.getProductInfo(results.next());
		}
		}catch (IOException e) {
			resultMap.put("status", "failure");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			contentBlock = contentBlock+e.toString()+"\n"+sw.toString();
        }
		
		
		NewFileWriter.writeFile(JSONObject.toJSONString(resultMap)+"\n--\n"+contentBlock, ""+jobID+".txt");
	}

}
