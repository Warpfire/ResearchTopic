package scrape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchProductListingFromSearch {

	/*public static void main(String[] args) {

		Iterator<String> results  = FetchSearch("6304Z").iterator();
		while(results.hasNext()){
			String url = results.next();
			System.out.println(url);
            getProductInfo(url);
		}
	}*/
	
	/**
	 * Collect the set of product info URLs found on simplybearings.co.uk from a single keyword.
	 * 
	 * 
	 * @throws Exception 
	 * @throws throws IOException 
	 */
	public static HashSet<String> FetchSearch(String keyword) throws Exception {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    HashSet<String> foundURLs = new HashSet<String>();
	    int totalNumber = 0;
	    try {
	        url = new URL("http://simplybearings.co.uk/shop/product_listing_search_ajax.php?dd_data=&keywords="+keyword);
	        is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));
	        while ((line = br.readLine()) != null) {
	            if(line.contains("product_info.html")){
	            	line = line.replace('"', ' ');
	            	String[] words = line.split(" ");
	            	for(int i=0;i<words.length;i++){
	            		if (words[i].contains("product_info.html"))
	            			//System.out.println(words[i]);
	            			foundURLs.add(words[i]);
	            			
	            	}
	            	
	            }
	            else if(Jsoup.parse(line).text().contains("Displaying 1 to")){
	            	String[] splitLine = Jsoup.parse(line).text().split(" ");
	            	totalNumber = (int) Double.parseDouble(splitLine[splitLine.length-2]);
	            	int numberOfPages = (int) Math.ceil(totalNumber/25.0);
	            	for(int j=2;j<=numberOfPages;j++){
	            		foundURLs.addAll(FetchSearchExtra(keyword,j));
	            	}
	            }
	        }
	    } catch (MalformedURLException mue) {
	         throw mue;
	    } catch (IOException ioe) {
	    	throw ioe;
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    if(totalNumber!=0&& totalNumber!=foundURLs.size()){
	    	throw new Exception("Incorrect Number of Results: From search with keyword "+keyword+"\n"
					+ " expected number of results "+totalNumber+" but got "+ foundURLs.size());
	    }
	    return foundURLs;
	}
	
	/**
	 * Collect the set of product info URLs found on simplybearings.co.uk from a single keyword on a specified page of the search results.
	 * 
	 * TODO check number of results
	 * @throws throws IOException 
	 */
	private static HashSet<String> FetchSearchExtra(String keyword,int page) throws IOException {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    HashSet<String> foundURLs = new HashSet<String>();
	    try {
	        url = new URL("http://simplybearings.co.uk/shop/product_listing_search_ajax.php?dd_data=&keywords="+keyword+"&page="+page);
	        is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));
	        while ((line = br.readLine()) != null) {
	            if(line.contains("product_info.html")){
	            	line = line.replace('"', ' ');
	            	String[] words = line.split(" ");
	            	for(int i=0;i<words.length;i++){
	            		if (words[i].contains("product_info.html"))
	            			//System.out.println(words[i]);
	            			foundURLs.add(words[i]);
	            			
	            	}
	            	
	            }
	        }
	    } catch (MalformedURLException mue) {
	         throw mue;
	    } catch (IOException ioe) {
	    	throw ioe;
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    return foundURLs;
	}
	
	/**
	 * Extract Product information from a URL found on simplybearings.co.uk.
	 * Extraction is done on the basis of the formatting used to find the correct locations to extract data from.
	 * 
	 * TODO Formatting is table based so check if extraction based on the table is possible to get something better
	 * than the currently used hack.
	 * @throws IOException 
	 */
	public static HashMap<String,List<String>> getProductInfo(String url) throws IOException{
		HashMap<String,List<String>> results = new HashMap<String,List<String>>();
		

	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    try {
	        URL url2 = new URL(url);
	        is = url2.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));
	        
	        while ((line = br.readLine()) != null) {
	            if(line.contains("<td align=\"left\" class=\"pinfopageHeading\" width=\"75%\" valign=\"top\">")){
	            	//matches title and possibly Also Known As
	            	//remove formatting html
	            	line = Jsoup.parse(line).text();
	            	if(line.contains("Also Known As:")){
	            		//contains Also Known As
	            		
	            		//System.out.println("DESC_EN : "+ line.substring(0, line.indexOf("Also Known As:")));
	            		results.put("DESC_EN", Arrays.asList(line.substring(0, line.indexOf("Also Known As:"))));
	            		//System.out.println("AlsoKnownAs : "+ line.substring(line.indexOf("Also Known As:")+16));
	            		results.put("AlsoKnownAs", Arrays.asList(line.substring(line.indexOf("Also Known As:")+16).split(" ")));
	            	}else{
	            		//only contains title so remainder of line should be the title/description
	            		//System.out.println("DESC_EN : "+ line);
	            		results.put("DESC_EN",Arrays.asList(line) );
	            	}
	            		
	            }
	            else if(line.contains("productDataTable-Cell-key")){
	            	//technical data
	            	String attributeName = Jsoup.parse(line).text().replace("\u00a0", "");
	            	//read 2 lines to get data value
	            	br.readLine();
	            	line= br.readLine();
	            	String attributeValue = Jsoup.parse(line).text().replace("\u00a0", "");
	            	//System.out.println(attributeName+" : "+ attributeValue);
	            	results.put(attributeName, Arrays.asList(attributeValue));
	            }
	            else if(line.contains("<td class=\"main\"><table border=\"0\" cellspacing=\"1\" cellpadding=\"2\">")){
	            	//general product information
	            	line = br.readLine(); //read <tr>
	            	while (line.contains("<tr>")){
	            		line = br.readLine();
	            		String attributeName = Jsoup.parse(line).text().replace("\u00a0", "");
	            		attributeName = attributeName.substring(0, attributeName.length()-1); //consistently pickup : at the end of each general product information.
	            		line = br.readLine();
	            		String attributeValue = Jsoup.parse(line).text().replace("\u00a0", "");
	            		line = br.readLine();
	            		line = br.readLine();
	            		line = br.readLine();
	            		//System.out.println(attributeName+" : "+ attributeValue);
	            		results.put(attributeName, Arrays.asList(attributeValue));
	            	}
	            }
	        }
	    } catch (MalformedURLException mue) {
	         throw mue;
	    } catch (IOException ioe) {
	         throw ioe;
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    return results;
	}
	
	/**
	 * Does an initial pass over results from getProductInfo
	 * Will rename fields and group identical fields together.
	 * @param resultSet
	 * @return
	 * @throws Exception Thrown when conflicts within the same resultMap is found.
	 */
	public static HashMap<String,List<String>> initialClean(HashMap<String,List<String>> resultMap) throws Exception{
		HashMap<String,List<String>> cleanedResultMap = new HashMap<String,List<String>>();
		Iterator<Entry<String, List<String>>> resultSet = resultMap.entrySet().iterator();
		while(resultSet.hasNext()){
			Entry<String, List<String>> result = resultSet.next();
			String key = result.getKey();
			switch (key.replaceAll("\\s","")){
				case "DESC_EN" : key = "DESC2_EN"; break;
				case "d" : key = "Inside Diameter"; break;
				case "InsideDiad" : key = "Inside Diameter"; break;
				case "InsideDiameterd" : key = "Inside Diameter"; break;
				case "D" : key = "Outside Diameter"; break;
				case "OutsideDiameterD" : key = "Outside Diameter"; break;
				case "B" : key = "Width"; break;
				case "WidthB" : key = "Width"; break;
				case "WidthW" : key = "Width"; break;
				case "kg" : key = "Weight"; break;
				default: break;
			}
			String value = result.getValue().get(0);
			String valueAlpha = result.getValue().get(0).replaceAll("[^A-Za-z]", "");
			switch (valueAlpha.toLowerCase()){
			case "mm" : value = value.replaceAll("[A-Za-z ]", ""); key = key + " | " + "mm"; break;
			case "kn" : value = value.replaceAll("[A-Za-z ]", ""); key = key + " | " + "kN"; break;
			case "rpm" : value = value.replaceAll("[A-Za-z ]", ""); key = key + " | " + "rpm"; break;
			case "kg" : value = value.replaceAll("[A-Za-z ]", ""); key = key + " | " + "Kg"; break;
			case "g" : value = Double.toString((Double.parseDouble(value.replaceAll("[A-Za-z ]", ""))/1000)); key = key + " | " + "Kg"; break;
			default: break;
			}
			if(valueAlpha.isEmpty()){
			switch (key){
			//mm is default value
			case "Inside Diameter" : key = "Inside Diameter | mm"; break;
			case "Outside Diameter" : key = "Outside Diameter | mm"; break;
			case "Width" : key = "Width | mm"; break;
			default: break;
			}}
			//initial cleaning done attempt to add but check for conflict
			if(cleanedResultMap.get(key)==null){
				//key does not exist yet so add to map
				
				if(result.getValue().size()>1){
					cleanedResultMap.put(key,result.getValue());//only AlsoKnownAs generates an actual array with multiple values but they do need to be kept
				}
				else{
					cleanedResultMap.put(key, Arrays.asList(value));
				}
			}
				
			else if (!cleanedResultMap.get(key).get(0).equals(value)){
				//key exists but conflicts
				throw new Exception("Data Conflict: From resultMap with description "+resultMap.get("DESC_EN")+"\n"
						+ " key "+key+" expected value "+cleanedResultMap.get(key).get(0)+" but got "+ value);
			}
			//remaining else would be key exists but no conflict so ok.
		}
		return cleanedResultMap;
	}
	
}
