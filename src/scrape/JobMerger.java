package scrape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;

import support.FileAppender;
import support.NewFileWriter;
import support.SupportFileReader;

public class JobMerger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		compileJobs(args);
	}
	/**

	 * @param jobSerials
	 */
	private static void compileJobs(String[] jobSerials){
		HashMap compileMatches = new HashMap();
		for(int i=0; i<jobSerials.length;i++){
			ArrayList searchResults = (ArrayList)SupportFileReader.readHeaderFromJob(jobSerials[i]+".job").get("results");
			for(int j=0;j<searchResults.size();j++){
				if(!searchResults.get(j).toString().contains(" X")){ //only read successful jobs. Failures are marked with " X".
				HashMap header =SupportFileReader.readHeaderFromJob(searchResults.get(j).toString()+".job");
				String url = header.get("url").toString();
				HashMap entry = (HashMap) compileMatches.get(url);
				if(entry==null){
					entry = new HashMap();
					entry.put("data", SupportFileReader.readDataFromJob(searchResults.get(j).toString()+".job"));
					ArrayList<String> _job_id = new ArrayList<String>();
					_job_id.add(((HashMap) header.get("job")).get("id").toString());	
					ArrayList<String> _job_serial = new ArrayList<String>();
					_job_serial.add(((HashMap) header.get("job")).get("serial").toString());
					entry.put("_job_id", _job_id);
					entry.put("_job_serial", _job_serial);
					compileMatches.put(url, entry);
				}
				else{
					((ArrayList)entry.get("_job_id")).add(((HashMap) header.get("job")).get("id").toString());
					((ArrayList)entry.get("_job_serial")).add(((HashMap) header.get("job")).get("serial").toString());
				}
			}}
		}
		
		//System.out.println(SupportFileReader.prettyJSON(JSONObject.toJSONString(compileMatches)));
		formatPrintCompiledJobs(compileMatches);
	}
	
	private static void formatPrintCompiledJobs(HashMap compiledJobs){
		NewFileWriter.writeFile("[\n", "compiledJobs.json");
		Iterator itr =  compiledJobs.entrySet().iterator();
		if(itr.hasNext()){
			Map.Entry entry = (Map.Entry) itr.next();
			HashMap json = (HashMap) ((HashMap) entry.getValue()).get("data");
					json.put("_job_serial", ((HashMap) entry.getValue()).get("_job_serial"));
					json.put("_job_id", ((HashMap) entry.getValue()).get("_job_id"));
					json.put("_url", entry.getKey());
			FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(json)), "compiledJobs.json");
		}

		while(itr.hasNext()){
			Map.Entry entry = (Map.Entry) itr.next();
			HashMap json = (HashMap) ((HashMap) entry.getValue()).get("data");
					json.put("_job_serial", ((HashMap) entry.getValue()).get("_job_serial"));
					json.put("_job_id", ((HashMap) entry.getValue()).get("_job_id"));
					json.put("_url", entry.getKey());
					FileAppender.appendToFile(",\n", "compiledJobs.json");
					FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(json)), "compiledJobs.json");
		}
		FileAppender.appendToFile("\n]", "compiledJobs.json");

	}

}
