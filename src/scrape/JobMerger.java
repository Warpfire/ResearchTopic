package scrape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import support.FileAppender;
import support.NewFileWriter;
import support.SupportFileReader;

public class JobMerger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args[0].equals("all")){
			String[] jobs = new String[args.length-1];
			for(int i =1; i<args.length;i++){
				JobExecutor.executeJob(i,args[i]);
				jobs[i-1]=""+i;
			}
			formatPrintCompiledJobs(compileJobs(jobs),"compiledJobs");
		}
		else{
			formatPrintCompiledJobs(compileJobs(args),"compiledJobs");}
	}
	
	
	/**
	 * Method which compiles jobs into a single compiledJobs.json file which contains each entry as an JSON array.
	 * Each entry is a compiled version of a product which has been matched based on URL contained in the header of the .job files.
	 * All data from all jobs should be added.
	 * @param jobSerials Array of Strings which when .job is added to the Strings form filenames to the various jobs to be compiled
	 * @return A Map which uses URL's as keys and contains the data in a HashMap contained within found via the key data and header information.
	 */
	protected static HashMap compileJobs(String[] jobSerials){
		HashMap compileMatches = new HashMap();
		for(int i=0; i<jobSerials.length;i++){
			ArrayList searchResults = (ArrayList)SupportFileReader.readHeaderFromJob(jobSerials[i]+".job").get("results");
			for(int j=0;j<searchResults.size();j++){
				if(!searchResults.get(j).toString().contains(" X")){ //only read successful jobs. Failures are marked with " X".
				HashMap header =SupportFileReader.readHeaderFromJob(searchResults.get(j).toString()+".job");
				String url = header.get("url").toString();
				HashMap entry = (HashMap) compileMatches.get(url);
				if(entry==null){
					System.out.println("Entry Null");
					entry = new HashMap();
					entry.put("data", SupportFileReader.readDataFromJob(searchResults.get(j).toString()+".job"));
					entry.put("_job_id", new ArrayList(Arrays.asList(((HashMap) header.get("job")).get("id").toString())));
					entry.put("_job_serial", new ArrayList(Arrays.asList(((HashMap) header.get("job")).get("serial").toString())));
					compileMatches.put(url, entry);
				}
				else{
					System.out.println("Entry Not Null");
					//new match might contain match with initial dataset, as might the already exisiting entry.
					Iterator<Entry<String, Object>> toAdd = SupportFileReader.readDataFromJob(searchResults.get(j).toString()+".job").entrySet().iterator();
					while(toAdd.hasNext()){
						Entry<String, Object> entryAdd = toAdd.next();
						if(!((HashMap) entry.get("data")).containsKey(entryAdd.getKey())){
							((HashMap) entry.get("data")).put(entryAdd.getKey(), new ArrayList(Arrays.asList(entryAdd.getValue())));
						}
						else{
							//Data entry exists but the new information contained in toAdd may be a new varation and not contained in the List.
							//check this and if it is new add it to the entry.
							if(!((List)((HashMap) entry.get("data")).get(entryAdd.getKey())).contains(((List)entryAdd.getValue()).get(0))){
								//we are only checking if the first entry of the list is unique because only AlsoKnownAs should be able to 
								//get multiple entries at previous stages and this should match fully because this only comes from simplybearings.
								((List)((HashMap) entry.get("data")).get(entryAdd.getKey())).add(((List)entryAdd.getValue()).get(0));
								System.out.println("entry Already existed so added "+entryAdd.toString()+" as "+((HashMap) entry.get("data")).get(entryAdd.getKey()));
							}
							
						}
					}
					((List)entry.get("_job_id")).add(((HashMap) header.get("job")).get("id").toString());
					((List)entry.get("_job_serial")).add(((HashMap) header.get("job")).get("serial").toString());
				}
			}}
		}

		//System.out.println(SupportFileReader.prettyJSON(JSONObject.toJSONString(compileMatches)));
		return compileMatches;
	}
	
	/**
	 * Sub-Method of compileJobs which formats it into JSONarray objects, adds a internal tracking number and print it to a new file.
	 * @param compiledJobs A Map which uses URL's as keys and contains the data in a HashMap contained within found via the key data and header information (specifically using the keys _job_serial and _job_id).
	 * @param filename name of the file .json will be used as extension
	 */
	static void formatPrintCompiledJobs(HashMap compiledJobs, String filename){
		int trackingNumber = 0;
		NewFileWriter.writeFile("[\n", filename+".json");
		Iterator itr =  compiledJobs.entrySet().iterator();
		if(itr.hasNext()){
			Map.Entry entry = (Map.Entry) itr.next();
			HashMap json = (HashMap) ((HashMap) entry.getValue()).get("data");
					json.put("_job_serial", ((HashMap) entry.getValue()).get("_job_serial"));
					json.put("_job_id", ((HashMap) entry.getValue()).get("_job_id"));
					json.put("_url", new ArrayList(Arrays.asList(entry.getKey())));
					json.put("_trackingID", ""+trackingNumber);
					trackingNumber++;
			FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(json)), filename+".json");
		}

		while(itr.hasNext()){
			Map.Entry entry = (Map.Entry) itr.next();
			HashMap json = (HashMap) ((HashMap) entry.getValue()).get("data");
					json.put("_job_serial", ((HashMap) entry.getValue()).get("_job_serial"));
					json.put("_job_id", ((HashMap) entry.getValue()).get("_job_id"));
					json.put("_url", new ArrayList(Arrays.asList(entry.getKey())));
					json.put("_trackingID", new ArrayList(Arrays.asList(""+trackingNumber)));
					trackingNumber++;
					FileAppender.appendToFile(",\n", filename+".json");
					FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(json)), filename+".json");
		}
		FileAppender.appendToFile("\n]", filename+".json");

	}

}
