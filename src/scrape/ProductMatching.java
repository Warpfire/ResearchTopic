package scrape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import support.SupportFileReader;

public class ProductMatching {

	public static final int WORKER_NUMBER = 4;
	
	/**
	 * 
	 * @param args first argument for .json file containing the initial dataset. use the word "threaded" as the second argument to mutlithread the operation with a number of workers equal to WORKER_NUMBER
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
			List dataEntries = SupportFileReader.readJsonFromDataset(args[0]);
			
			String[] jobs = new String[dataEntries.size()];
			
			if(args[1].equals("threaded")){
			
				Thread[] workers= new Thread[WORKER_NUMBER];
			for(int i=0;i<WORKER_NUMBER;i++){
				Thread T1 = new Thread(new JobExecutorRunnable(dataEntries.subList(((int)Math.floor(i*(dataEntries.size()/4.0))),((int)Math.floor((i+1)*(dataEntries.size()/4.0)))),((int)Math.floor(i*(dataEntries.size()/4.0)))));
			      T1.start();
			      workers[i]=T1;
			}
			for(int i =0; i<dataEntries.size();i++){
				jobs[i]=""+(i+1);
			
			}
			for(int i = 0; i<WORKER_NUMBER;i++){
				try {
					workers[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			else{
			for(int i =0; i<dataEntries.size();i++){
				JobExecutor.executeJobUsingDataset(i+1, (HashMap<String, String>) dataEntries.get(i));
				jobs[i]=""+(i+1);
			}}
			JobMerger.formatPrintCompiledJobs(JobMerger.compileJobs(jobs),"compiledJobs");
		
		//System.out.println(matchToDataFile(SupportFileReader.readDataFromJob("1-2.job"),SupportFileReader.readJsonFromDataset("deep-groove-ball-bearing.json").get(0)));
		
	}

	
	
	/**
	 * Will attempt to match found entries with entries from the origonal dataset.
	 * @param resultMap
	 * @param dataSet
	 * @return
	 */
	public static HashMap<String,List<String>> matchToDataFile(HashMap<String,List<String>> resultMap, HashMap<String,String> dataSet){
		HashMap<String,List<String>> returnMap = resultMap;
		if(resultMap.get("Brand").get(0).equals(dataSet.get("Brand | EGTF_0008"))&&resultMap.get("Outside Diameter | mm").get(0).equals(dataSet.get("Outer diameter | EF000015"))&&resultMap.get("Width | mm").get(0).equals(dataSet.get("Width | EF000008"))&&resultMap.get("Inside Diameter | mm").get(0).equals(dataSet.get("Inner diameter | EF000065"))){
			//sizes and brand matches
			if(resultMap.get("DESC2_EN").toString().replaceAll("[^A-Za-z0-9]", "").contains((dataSet.get("Type/Dimension | EF001139")).replaceAll("[^A-Za-z0-9]", ""))||(resultMap.get("AlsoKnownAs")!=null&&resultMap.get("AlsoKnownAs").toString().replaceAll("[^A-Za-z0-9]", "").contains((dataSet.get("Type/Dimension | EF001139")).replaceAll("[^A-Za-z0-9]", "")))){
				//name possibly matches as well
				//for some reason a 2 is sometimes used instead of a -
				Iterator<Entry<String, String>> toAdd = dataSet.entrySet().iterator();
				while(toAdd.hasNext()){
					Entry<String, String> entry = toAdd.next();
					if(!returnMap.containsKey(entry.getKey())){
						//System.out.println(entry.getKey());
					returnMap.put(entry.getKey(), new ArrayList(Arrays.asList(entry.getValue())));
					}
					else{
						System.out.println("entry Already existed? "+entry.toString()+" as "+resultMap.get(entry.getKey()));
					}
				}
			}
			else{
				System.out.println("potentialMatch"+resultMap.get("DESC2_EN")+" and "+dataSet.get("DESC_EN"));
				if(resultMap.get("DESC2_EN").toString().replaceAll("[^A-Za-z0-9]", "").replaceAll("2", "").contains((dataSet.get("Type/Dimension | EF001139")).replaceAll("[^A-Za-z0-9]", "").replaceAll("2", ""))||(resultMap.get("AlsoKnownAs")!=null&&resultMap.get("AlsoKnownAs").toString().replaceAll("[^A-Za-z0-9]", "").replaceAll("2", "").contains((dataSet.get("Type/Dimension | EF001139")).replaceAll("[^A-Za-z0-9]", "").replaceAll("2", "")))){
					System.out.println("potentialMatch"+resultMap.get("DESC2_EN")+" and "+dataSet.get("DESC_EN")+" matched when filtering 2/-");
					//name possibly matches as well
					//for some reason a 2 is sometimes used instead of a -
					Iterator<Entry<String, String>> toAdd = dataSet.entrySet().iterator();
					while(toAdd.hasNext()){
						Entry<String, String> entry = toAdd.next();
						if(!returnMap.containsKey(entry.getKey())){
							//System.out.println(entry.getKey());
						returnMap.put(entry.getKey(), new ArrayList(Arrays.asList(entry.getValue())));
						}
						else{
							System.out.println("entry Already existed? "+entry.toString()+" as "+resultMap.get(entry.getKey()));
						}
					}
				}
			}
		}
		return returnMap;
	}
}
