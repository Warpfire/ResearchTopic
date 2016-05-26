package scrape;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
	
	/**
	 * Method which executes a search and initial clean based on a searchTerm.
	 * The result is then stored in a number of files with a name based on JobID.
	 * @param jobID
	 * @param searchTerm
	 */
	public static void executeJob(int jobID, String searchTerm){
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
				HashMap<String,List<String>> cleanedHashMap = FetchProductListingFromSearch.initialClean(FetchProductListingFromSearch.getProductInfo(url));
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
		}catch (Exception e) {
			resultMap.put("status", "failure");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			contentBlock = contentBlock+e.toString()+"\n"+sw.toString();
        }
		NewFileWriter.writeFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(resultMap))+"\n--\n"+contentBlock, ""+jobID+".job");
	}
	
	/**
	 * Method which executes a search, initial clean and productMatch based on a searchTerm with the product matching being done using dataSet.
	 * The result is then stored in a number of files with a name based on JobID.
	 * Outdated because it searched on a basis of Type/Dimension which did not provide a sufficient amount of identification.
	 * @param jobID
	 * @param dataSet A Map containing a product upon which matching is to be attempted. It is expected that the searchTerm is based somewhat on contents of the dataSet.
	 */
	public static void executeJobUsingDatasetOld(int jobID, HashMap<String,String> dataSet){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		String contentBlock = " ";
		resultMap.put("serial", jobID);
		resultMap.put("status", "success");
		resultMap.put("id", dataSet.get("Type/Dimension | EF001139"));
		ArrayList<String> individualResults = new ArrayList<String>();
		resultMap.put("results", individualResults);
		
		try{
		Iterator<String> results  = FetchProductListingFromSearch.FetchSearch(dataSet.get("Type/Dimension | EF001139")).iterator();
		int i=0;
		while(results.hasNext()){
			i++;
			String url = results.next();
			HashMap<String,Object> individualResultMap = new HashMap<String, Object>();
			HashMap<String,String> JobMap = new HashMap<String, String>();
				JobMap.put("serial", ""+jobID);
				JobMap.put("id", dataSet.get("Type/Dimension | EF001139"));
			individualResultMap.put("job", JobMap);
			individualResultMap.put("fetchDate", LocalDateTime.now().toString());
			individualResultMap.put("url", url);
			individualResultMap.put("status", "success");
			try{
				HashMap<String,List<String>> cleanedHashMap = ProductMatching.matchToDataFile(FetchProductListingFromSearch.initialClean(FetchProductListingFromSearch.getProductInfo(url)),dataSet);
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
		}catch (Exception e) {
			resultMap.put("status", "failure");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			contentBlock = contentBlock+e.toString()+"\n"+sw.toString();
        }
		NewFileWriter.writeFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(resultMap))+"\n--\n"+contentBlock, ""+jobID+".job");
	}
	
	
	/**
	 * Method which executes a search, initial clean and productMatch based on a dataSet.
	 * The result is then stored in a number of files with a name based on JobID.
	 * From the data set manitem, suppitem and EAN are extracted and each of these is used to determine searchTerms.
	 * A requirement for a search terms is that it has a minimum size of 4 or more characters.
	 * @param jobID
	 * @param searchTerm
	 * @param dataSet A Map containing a product upon which matching is to be attempted. It is expected that the searchTerm is based somewhat on contents of the dataSet.
	 */
	public static void executeJobUsingDataset(int jobID, HashMap<String,String> dataSet){
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		String contentBlock = " ";
		resultMap.put("serial", jobID);
		resultMap.put("status", "success");
		resultMap.put("id", dataSet.get("DESC1_EN"));
		ArrayList<String> individualResults = new ArrayList<String>();
		resultMap.put("results", individualResults);
		
		try{
		List<String> searchTerms = new ArrayList<String>();
		searchTerms.addAll(Arrays.asList(dataSet.get("Manitem").split("\\|")));
		searchTerms.addAll(Arrays.asList(dataSet.get("Suppitem").split("\\|")));
		searchTerms.addAll(Arrays.asList(dataSet.get("EAN").split("\\|")));
		HashSet<String> results = new HashSet<String>();
		for(String searchTerm : searchTerms)  {
			if(searchTerm.length()>3){
			results.addAll(FetchProductListingFromSearch.FetchSearch(searchTerm));
			}
			}	
		Iterator<String> resultsitr  = results.iterator();
		int i=0;
		while(resultsitr.hasNext()){
			i++;
			String url = resultsitr.next();
			HashMap<String,Object> individualResultMap = new HashMap<String, Object>();
			HashMap<String,String> JobMap = new HashMap<String, String>();
				JobMap.put("serial", ""+jobID);
				JobMap.put("id", dataSet.get("DESC1_EN"));
			individualResultMap.put("job", JobMap);
			individualResultMap.put("fetchDate", LocalDateTime.now().toString());
			individualResultMap.put("url", url);
			individualResultMap.put("status", "success");
			try{
				HashMap<String,List<String>> cleanedHashMap = ProductMatching.matchToDataFile(FetchProductListingFromSearch.initialClean(FetchProductListingFromSearch.getProductInfo(url)),dataSet);
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
		}catch (Exception e) {
			resultMap.put("status", "failure");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			contentBlock = contentBlock+e.toString()+"\n"+sw.toString();
        }
		NewFileWriter.writeFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(resultMap))+"\n--\n"+contentBlock, ""+jobID+".job");
	}
	
	
}
