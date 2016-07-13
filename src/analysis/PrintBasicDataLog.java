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
		printBasicDataLog(args[0],args[1]);
	}
	
	public static void printBasicDataLog(String matches, String dataset){
		NewFileWriter.writeFile("", "productMatching.graphml");
		FileAppender.appendToFile("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"\n"
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
				+ "xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns\n"
				+ "http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">\n"
				+ "<key id=\"d0\" for=\"node\" attr.name=\"color\" attr.type=\"string\">\n"
				+ "<default>yellow</default>\n"
				+ "</key>\n<graph id=\"G\" edgedefault=\"undirected\">\n", "productMatching.graphml");
		
		Iterator datasetIterator =  SupportFileReader.readJsonFromDataset(dataset).iterator();
		while(datasetIterator.hasNext()){
			HashMap entry = (HashMap) datasetIterator.next();
			if(entry.get("DESC1_EN")!=null){
				FileAppender.appendToFile("<node id=\""+((String)entry.get("DESC1_EN")).replaceAll("&", "")+"\">"
						+ "<data key=\"d0\">yellow</data>"
						+ "</node>\n", "productMatching.graphml");
				}
		}
		Iterator entriesIterator = SupportFileReader.readcompiledJobs(matches).iterator();
		ArrayList<String> dataLogFacts = new ArrayList<String>();
		while(entriesIterator.hasNext()){
			HashMap<String,List<String>> entry = (HashMap<String, List<String>>) entriesIterator.next();
			if(entry.get("DESC2_EN")!=null){
				if(entry.get("DESC1_EN")!=null){
					FileAppender.appendToFile("<node id=\""+((String)entry.get("DESC2_EN").get(0)).replaceAll("&", "")+"\">"
							+ "<data key=\"d0\">green</data>"
							+ "</node>\n", "productMatching.graphml");
					for(int i=0;i<entry.get("DESC1_EN").size();i++){
						dataLogFacts.add("<edge source=\""+((String)entry.get("DESC1_EN").get(i)).replaceAll("&", "")+"\" target=\""+((String)entry.get("DESC2_EN").get(0)).replaceAll("&", "")+"\"/>\n");
				}}
				else{
					FileAppender.appendToFile("<node id=\""+((String)entry.get("DESC2_EN").get(0)).replaceAll("&", "")+"\">"
							+ "<data key=\"d0\">red</data>"
							+ "</node>\n", "productMatching.graphml");
				}
			}
		}
		Iterator<String> dataLogFactsIterator = dataLogFacts.iterator();
		while(dataLogFactsIterator.hasNext()){
			FileAppender.appendToFile(dataLogFactsIterator.next(), "productMatching.graphml");
		}
		FileAppender.appendToFile("</graph>\n"
				+ "</graphml>", "productMatching.graphml");

		
	}

}
