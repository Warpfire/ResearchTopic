package analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Set;

import org.json.simple.JSONObject;

import support.Statistics;
import support.SupportFileReader;

public class PrecisionRecall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		matchesCount(403,"compiledJobs.json");
		SearchCount(403,"compiledJobs.json");
		matchesCountJob(403,"compiledJobs.json");
	}
	
	/**
	 * Matches per Initial dataset product
	 * @param inputNumber
	 * @param filename
	 */
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
		System.out.println("occurences" + ": " + "times this occurence appears");
		for (Integer temp : uniqueSet) {
			System.out.println(temp + ": " + Collections.frequency(matchesCount.values(), temp));
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		
		Statistics stats = new Statistics(collectionToArrayWithPadding(matchesCount.values(),0));
		System.out.println(matchesCount.values().size()+" initial dataset entries has a match of the total "+inputNumber+" initial dataset entries.");
		System.out.println(totalCount+" total matches found.");
		System.out.println(stats.getSum()+" sum Type/Dimension | EF001139.");
		System.out.println(stats.getMean()+" mean Type/Dimension | EF001139.");
		System.out.println(stats.getVariance()+" variance Type/Dimension | EF001139.");
		System.out.println(stats.getStdDev()+" SDev Type/Dimension | EF001139.");
		System.out.println(stats.getMedian()+" Median Type/Dimension | EF001139.");
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
		System.out.println(SupportFileReader.prettyJSON(JSONObject.toJSONString(matchesCount)));
		Set<Integer> uniqueSet = new HashSet<Integer>(matchesCount.values());
		int totalCount = 0;
		System.out.println("occurences" + ": " + "times this occurence appears");
		for (Integer temp : uniqueSet) {
			System.out.println(temp + ": " + Collections.frequency(matchesCount.values(), temp));
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		
		Statistics stats = new Statistics(collectionToArrayWithPadding(matchesCount.values(),0));
		System.out.println(matchesCount.values().size()+" initial dataset entries has a match of the total "+inputNumber+" initial dataset entries.");
		System.out.println(totalCount+" total matches found.");
		System.out.println(stats.getSum()+" sum _job_id.");
		System.out.println(stats.getMean()+" mean _job_id.");
		System.out.println(stats.getVariance()+" variance _job_id.");
		System.out.println(stats.getStdDev()+" SDev _job_id.");
		System.out.println(stats.getMedian()+" Median _job_id.");
	}
	
	/**
	 * Matches per Job
	 * @param inputNumber
	 * @param filename
	 */
	public static void matchesCountJob(int inputNumber,String filename){
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(filename).iterator();
		HashMap<String,Integer> matchesCount = new HashMap<String,Integer>();
		while(entriesIterator.hasNext()){
			
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("Type/Dimension | EF001139")!=null){
				
				for(int j=0;j<entry.get("Type/Dimension | EF001139").size();j++){
					for(int i=0;i<entry.get("_job_id").size();i++){
				if(matchesCount.containsKey(entry.get("_job_id").get(i))){
					matchesCount.put(entry.get("_job_id").get(i), matchesCount.get(entry.get("_job_id").get(i))+1);
				}
				else{
					matchesCount.put(entry.get("_job_id").get(i), 1);
				}
			}}}
		}
		System.out.println(SupportFileReader.prettyJSON(JSONObject.toJSONString(matchesCount)));
		Set<Integer> uniqueSet = new HashSet<Integer>(matchesCount.values());
		int totalCount = 0;
		System.out.println("occurences" + ": " + "times this occurence appears");
		for (Integer temp : uniqueSet) {
			System.out.println(temp + ": " + Collections.frequency(matchesCount.values(), temp));
			totalCount = totalCount + (temp *Collections.frequency(matchesCount.values(), temp));
		}
		
		Statistics stats = new Statistics(collectionToArrayWithPadding(matchesCount.values(),0));
		System.out.println(matchesCount.values().size()+" initial dataset entries has a match of the total "+inputNumber+" initial dataset entries.");
		System.out.println(totalCount+" total matches found.");
		System.out.println(stats.getSum()+" sum Matches.");
		System.out.println(stats.getMean()+" mean Matches per Job");
		System.out.println(stats.getVariance()+" Matches per Job.");
		System.out.println(stats.getStdDev()+" Matches per Job.");
		System.out.println(stats.getMedian()+" Matches per Job.");
	}
	
	
	private static double[] collectionToArray(Collection<Integer> collection){
		double[] array = new double[collection.size()];
		Integer[] collectionArray = collection.toArray(new Integer[0]);
		for(int i=0;i<collection.size();i++){
			array[i] = collectionArray[i].doubleValue();
		}
		return array;
	}
	
	private static double[] collectionToArrayWithPadding(Collection<Integer> collection,int paddedSize){
		double[] array;
		if(paddedSize>=collection.size()){
		array = new double[paddedSize];
		Integer[] collectionArray = collection.toArray(new Integer[0]);
		for(int i=0;i<collection.size();i++){
			array[i] = collectionArray[i].doubleValue();
		}
		for(int i=collection.size();i<paddedSize;i++){
			array[i] = 0.0;
		}
		}
		else{
			array = new double[collection.size()];
			Integer[] collectionArray = collection.toArray(new Integer[0]);
			for(int i=0;i<collection.size();i++){
				array[i] = collectionArray[i].doubleValue();
			}
		}
		
		return array;
	}
	

}
