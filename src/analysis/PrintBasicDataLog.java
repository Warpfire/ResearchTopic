package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

import support.FileAppender;
import support.NewFileWriter;
import support.SupportFileReader;

public class PrintBasicDataLog {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		printBasicDataLog(args[0]);
	}
	
	public static void printBasicDataLog(String filename){
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(filename).iterator();
		ArrayList<String> dataLogFacts = new ArrayList<String>();
		while(entriesIterator.hasNext()){
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("DESC1_EN")!=null){
				for(int i=0;i<entry.get("DESC_EN1").size();i++){
				dataLogFacts.add("match("+entry.get("DESC_EN1").get(i)+", "+entry.get("DESC2_EN").get(0)+").\n");
			}}
		}
		NewFileWriter.writeFile("", "dataLog");
		Iterator<String> dataLogFactsIterator = dataLogFacts.iterator();
		while(dataLogFactsIterator.hasNext()){
			
			FileAppender.appendToFile(dataLogFactsIterator.next(), "dataLog");
		}
		
	}

}
