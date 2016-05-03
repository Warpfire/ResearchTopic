package scrape;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;


import support.NewFileWriter;
import support.SupportFileReader;
/**
 * 
 * TODO Multithread?
 */
public class JobExecutor {

	public static void main(String[] args) {
		for(int i =0; i<args.length;i++){
			executeJob(i+1,args[i]);
		}
	}
	
	private static void executeJob(int jobID, String searchTerm){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		String contentBlock = " ";
		
		resultMap.put("serial", jobID);
		resultMap.put("status", "success");
		resultMap.put("id", searchTerm);
		ArrayList<String> individualResults = new ArrayList<String>();
		resultMap.put("results", individualResults);
		
		try{
		Iterator<String> results  = FetchProductListingFromSearch.FetchSearch(searchTerm).iterator();
		int i=0;
		while(results.hasNext()){
			i++;
			String url = results.next();
			HashMap<String,Object> individualResultMap = new HashMap<String, Object>();
			HashMap<String,String> JobMap = new HashMap<String, String>();
				JobMap.put("serial", ""+jobID);
				JobMap.put("id", searchTerm);
			individualResultMap.put("job", JobMap);
			individualResultMap.put("fetchDate", LocalDateTime.now().toString());
			individualResultMap.put("url", url);
			individualResultMap.put("status", "success");
			try{
				HashMap<String,String> cleanedHashMap = FetchProductListingFromSearch.initialClean(FetchProductListingFromSearch.getProductInfo(url));
				NewFileWriter.writeFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(individualResultMap))+"\n--\n"+SupportFileReader.prettyJSON(JSONObject.toJSONString(cleanedHashMap)), ""+jobID+"-"+i+".job");
				individualResults.add(""+jobID+"-"+i);

			}catch (Exception e) {
				resultMap.put("status", "failure");
				individualResults.add(""+jobID+"-"+i+" X");
				individualResultMap.put("status", "Failure");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				NewFileWriter.writeFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(individualResultMap))+"\n--\n"+e.toString()+"\n"+sw.toString(), ""+jobID+"-"+i+".job");
	        }
			
		}
		}catch (IOException e) {
			resultMap.put("status", "failure");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			contentBlock = contentBlock+e.toString()+"\n"+sw.toString();
        }
		NewFileWriter.writeFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(resultMap))+"\n--\n"+contentBlock, ""+jobID+".job");
	}

	
	
}
