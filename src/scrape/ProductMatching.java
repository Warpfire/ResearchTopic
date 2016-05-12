package scrape;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import support.SupportFileReader;

public class ProductMatching {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
			List dataEntries = SupportFileReader.readJsonFromDataset(args[0]);
			String[] jobs = new String[dataEntries.size()];
			for(int i =0; i<dataEntries.size();i++){
				JobExecutor.executeJobUsingDataset(i+1, (HashMap<String, String>) dataEntries.get(i));
				jobs[i]=""+(i+1);
			}
			JobMerger.compileJobsWithDataset(jobs);
		
		//System.out.println(matchToDataFile(SupportFileReader.readDataFromJob("1-2.job"),SupportFileReader.readJsonFromDataset("deep-groove-ball-bearing.json").get(0)));
		
	}

	
	
	public static HashMap<String,List<String>> matchToDataFile(HashMap<String,List<String>> resultMap, HashMap<String,String> dataSet){
		HashMap<String,List<String>> returnMap = resultMap;
		if(resultMap.get("Brand").get(0).equals(dataSet.get("Brand | EGTF_0008"))&&resultMap.get("Outside Diameter | mm").get(0).equals(dataSet.get("Outer diameter | EF000015"))&&resultMap.get("Width | mm").get(0).equals(dataSet.get("Width | EF000008"))&&resultMap.get("Inside Diameter | mm").get(0).equals(dataSet.get("Inner diameter | EF000065"))){
			//sizes and brand matches
			if(resultMap.get("DESC2_EN").toString().contains((dataSet.get("Type/Dimension | EF001139")))||resultMap.get("AlsoKnownAs").toString().contains((dataSet.get("Type/Dimension | EF001139")))||resultMap.get("DESC2_EN").toString().replaceAll("[^A-Za-z0-9]", "").contains((dataSet.get("Type/Dimension | EF001139")).replaceAll("[^A-Za-z0-9]", ""))||resultMap.get("AlsoKnownAs").toString().replaceAll("[^A-Za-z0-9]", "").contains((dataSet.get("Type/Dimension | EF001139")).replaceAll("[^A-Za-z0-9]", ""))){
				//name possibly matches as well
				Iterator<Entry<String, String>> toAdd = dataSet.entrySet().iterator();
				while(toAdd.hasNext()){
					Entry<String, String> entry = toAdd.next();
					if(!returnMap.containsKey(entry.getKey())){
						//System.out.println(entry.getKey());
					returnMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
					}
					else{
						System.out.println("entry Already existed? "+entry.toString()+" as "+resultMap.get(entry.getKey()));
					}
				}
			}
			else{
				System.out.println("potentialMatch"+resultMap.get("DESC2_EN")+" and "+dataSet.get("DESC_EN"));
			}
		}
		return returnMap;
	}
}
