package analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Set;

import org.json.simple.JSONObject;

import support.FileAppender;
import support.NewFileWriter;
import support.Statistics;
import support.SupportFileReader;

public class PrecisionRecall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NewFileWriter.writeFile("", "statistics.txt");
		SearchCount(Integer.parseInt(args[1]),args[0]);
		matchesCountInitial(Integer.parseInt(args[1]),args[0]);
		matchesCountFound(Integer.parseInt(args[1]),args[0]);
	}
	
	/**
	 * Matches per Initial dataset product
	 * @param inputNumber
	 * @param filename
	 */
	public static void matchesCountInitial(int inputNumber,String filename){
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(filename).iterator();
		HashMap<String,Integer> matchesCount = new HashMap<String,Integer>();
		while(entriesIterator.hasNext()){
			
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("DESC1_EN")!=null){
				for(int i=0;i<entry.get("DESC1_EN").size();i++){
				if(matchesCount.containsKey(entry.get("id").get(0)+"|"+entry.get("DESC1_EN").get(i))){
					matchesCount.put(entry.get("id").get(0)+"|"+entry.get("DESC1_EN").get(i), matchesCount.get(entry.get("id").get(0)+"|"+entry.get("DESC1_EN").get(i))+1);
				}
				else{
					matchesCount.put(entry.get("id").get(0)+"|"+entry.get("DESC1_EN").get(i), 1);
				}
			}}
		}
		FileAppender.appendToFile("Matches per Initial dataset product\n\n", "statistics.txt");
		FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(matchesCount)), "statistics.txt");
		Set<Integer> uniqueSet = new HashSet<Integer>(matchesCount.values());
		int totalCount = 0;
		FileAppender.appendToFile("\noccurences" + ": " + "times this occurence appears\n", "statistics.txt");
		for (Integer temp : uniqueSet) {
			FileAppender.appendToFile(temp + ": " + Collections.frequency(matchesCount.values(), temp)+"\n", "statistics.txt");
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		
		Statistics stats = new Statistics(collectionToArray(matchesCount.values()));
		FileAppender.appendToFile(matchesCount.values().size()+" initial dataset entries of the "+inputNumber+" initial dataset entries had at least 1 match found from the simplyBearings Products.\n", "statistics.txt");
		FileAppender.appendToFile(totalCount+" total matches found.\n", "statistics.txt");
		FileAppender.appendToFile(stats.getSum()+" sum\n", "statistics.txt");
		FileAppender.appendToFile(stats.getMean()+" mean\n", "statistics.txt");
		FileAppender.appendToFile(stats.getVariance()+" variance\n", "statistics.txt");
		FileAppender.appendToFile(stats.getStdDev()+" sDev\n", "statistics.txt");
		FileAppender.appendToFile(stats.getMedian()+" Median\n\n", "statistics.txt");
	}
	
	/**
	 * Search Results per Job
	 * @param inputNumber
	 * @param filename
	 */
	public static void SearchCount(int inputNumber,String filename){
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(filename).iterator();
		HashMap<String,Integer> matchesCount = new HashMap<String,Integer>();
		while(entriesIterator.hasNext()){
			
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("_job_id")!=null){
				for(int i=0;i<entry.get("_job_id").size();i++){
				if(matchesCount.containsKey(entry.get("_job_id").get(i))){
					matchesCount.put(entry.get("_job_id").get(i), matchesCount.get(entry.get("_job_id").get(i))+1);
				}
				else{
					matchesCount.put(entry.get("_job_id").get(i), 1);
				}
			}}
		}
		FileAppender.appendToFile("Search Results per Job\\initial dataset product\n\n", "statistics.txt");
		FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(matchesCount)), "statistics.txt");
		Set<Integer> uniqueSet = new HashSet<Integer>(matchesCount.values());
		int totalCount = 0;
		FileAppender.appendToFile("\noccurences" + ": " + "times this occurence appears\n", "statistics.txt");
		for (Integer temp : uniqueSet) {
			FileAppender.appendToFile(temp + ": " + Collections.frequency(matchesCount.values(), temp)+"\n", "statistics.txt");
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		
		Statistics stats = new Statistics(collectionToArray(matchesCount.values()));
		FileAppender.appendToFile(totalCount+" total matches found.\n", "statistics.txt");
		FileAppender.appendToFile(stats.getSum()+" sum\n", "statistics.txt");
		FileAppender.appendToFile(stats.getMean()+" mean\n", "statistics.txt");
		FileAppender.appendToFile(stats.getVariance()+" variance\n", "statistics.txt");
		FileAppender.appendToFile(stats.getStdDev()+" SDev\n", "statistics.txt");
		FileAppender.appendToFile(stats.getMedian()+" Median\n\n", "statistics.txt");
	}
	
	/**
	 * Matches per simplyBearingsProduct
	 * @param inputNumber
	 * @param filename
	 */
	public static void matchesCountFound(int inputNumber,String filename){
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(filename).iterator();
		HashMap<String,Integer> matchesCount = new HashMap<String,Integer>();
		while(entriesIterator.hasNext()){
			
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("DESC1_EN")!=null){
					matchesCount.put(entry.get("_trackingID").get(0)+"|"+entry.get("DESC2_EN").get(0), entry.get("DESC1_EN").size());
				}

		}
		FileAppender.appendToFile("Matches per simplyBearings Product\n\n", "statistics.txt");
		FileAppender.appendToFile(SupportFileReader.prettyJSON(JSONObject.toJSONString(matchesCount)), "statistics.txt");
		Set<Integer> uniqueSet = new HashSet<Integer>(matchesCount.values());
		int totalCount = 0;
		FileAppender.appendToFile("\noccurences" + ": " + "times this occurence appears\n", "statistics.txt");
		for (Integer temp : uniqueSet) {
			FileAppender.appendToFile(temp + ": " + Collections.frequency(matchesCount.values(), temp)+"\n", "statistics.txt");
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		
		Statistics stats = new Statistics(collectionToArray(matchesCount.values()));
		FileAppender.appendToFile(matchesCount.values().size()+" simplyBearings Products had at least 1 match with the "+inputNumber+" initial dataset entries.\n", "statistics.txt");
		FileAppender.appendToFile(totalCount+" total matches found.\n", "statistics.txt");
		FileAppender.appendToFile(stats.getSum()+" sum\n", "statistics.txt");
		FileAppender.appendToFile(stats.getMean()+" mean\n", "statistics.txt");
		FileAppender.appendToFile(stats.getVariance()+" Variance\n", "statistics.txt");
		FileAppender.appendToFile(stats.getStdDev()+" sDev\n", "statistics.txt");
		FileAppender.appendToFile(stats.getMedian()+" Median\n\n", "statistics.txt");
	}
	
	
	private static double[] collectionToArray(Collection<Integer> collection){
		double[] array = new double[collection.size()];
		Integer[] collectionArray = collection.toArray(new Integer[0]);
		for(int i=0;i<collection.size();i++){
			array[i] = collectionArray[i].doubleValue();
		}
		return array;
	}
	

	

}
