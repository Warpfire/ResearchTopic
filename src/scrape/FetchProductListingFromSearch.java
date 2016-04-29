package scrape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FetchProductListingFromSearch {

	public static void main(String[] args) {

		Iterator<String> results  = FetchSearch("6304Z").iterator();
		while(results.hasNext()){
			String url = results.next();
			System.out.println(url);
            getProductInfo(url);
		}
	}
	
	/**
	 * Collect the set of product info URLs found on simplybearings.co.uk from a single keyword.
	 * 
	 * TODO check number of results
	 */
	public static HashSet<String> FetchSearch(String keyword) {
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;
	    HashSet<String> foundURLs = new HashSet<String>();
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
	            			foundURLs.add(words[i]);
	            			
	            	}
	            	
	            }
	        }
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
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
	 */
	public static HashMap<String,String> getProductInfo(String url){
		HashMap<String,String> results = new HashMap<String,String>();
		

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
	            		
	            		System.out.println("DESC_EN : "+ line.substring(0, line.indexOf("Also Known As:")));
	            		results.put("DESC_EN", line.substring(0, line.indexOf("Also Known As:")));
	            		System.out.println("AlsoKnownAs : "+ line.substring(line.indexOf("Also Known As:")+16));
	            		results.put("AlsoKnownAs", line.substring(line.indexOf("Also Known As:")+16));
	            	}else{
	            		//only contains title so remainder of line should be the title/description
	            		System.out.println("DESC_EN : "+ line);
	            		results.put("DESC_EN", line);
	            	}
	            		
	            }
	            else if(line.contains("productDataTable-Cell-key")){
	            	//technical data
	            	String attributeName = Jsoup.parse(line).text();
	            	//read 2 lines to get data value
	            	br.readLine();
	            	line= br.readLine();
	            	String attributeValue = Jsoup.parse(line).text();
	            	System.out.println(attributeName+" : "+ attributeValue);
	            	results.put(attributeName, attributeValue);
	            }
	            else if(line.contains("<td class=\"main\"><table border=\"0\" cellspacing=\"1\" cellpadding=\"2\">")){
	            	//general product information
	            	line = br.readLine(); //read <tr>
	            	while (line.contains("<tr>")){
	            		line = br.readLine();
	            		String attributeName = Jsoup.parse(line).text();
	            		line = br.readLine();
	            		String attributeValue = Jsoup.parse(line).text();
	            		line = br.readLine();
	            		line = br.readLine();
	            		line = br.readLine();
	            		System.out.println(attributeName+" : "+ attributeValue);
	            		results.put(attributeName, attributeValue);
	            	}
	            }
	        }
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    return results;
	}
	
}
