package analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Set;

import org.json.simple.JSONObject;

import support.SupportFileReader;

public class PrecisionRecall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		matchesCount(403,"compiledJobs.json");
	}
	
	public static void matchesCount(int inputNumber,String filename){
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(filename).iterator();
		HashMap<String,Integer> matchesCount = new HashMap<String,Integer>();
		while(entriesIterator.hasNext()){
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("Type/Dimension | EF001139")!=null){
				for(int i=0;i<entry.get("Type/Dimension | EF001139").size();i++){
				if(matchesCount.containsKey(entry.get("Type/Dimension | EF001139").get(i))){
					matchesCount.put(entry.get("Type/Dimension | EF001139").get(i), matchesCount.get(entry.get("Type/Dimension | EF001139").get(i))+1);
				}
				else{
					matchesCount.put(entry.get("Type/Dimension | EF001139").get(i), 1);
				}
			}}
		}
		System.out.println(SupportFileReader.prettyJSON(JSONObject.toJSONString(matchesCount)));
		Set<Integer> uniqueSet = new HashSet<Integer>(matchesCount.values());
		int totalCount = 0;
		for (Integer temp : uniqueSet) {
			System.out.println(temp + ": " + Collections.frequency(matchesCount.values(), temp));
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		System.out.println(matchesCount.values().size()+" initial dataset entries has a match of the total "+inputNumber+" initial dataset entries.");
		System.out.println(totalCount+" total matches found.");
	}
	

}
