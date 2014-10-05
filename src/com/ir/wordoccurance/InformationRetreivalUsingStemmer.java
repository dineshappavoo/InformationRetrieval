/**
 * 
 */
package com.ir.wordoccurance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dinesh Appavoo
 *
 */

public class InformationRetreivalUsingStemmer {

	/**
	 * @param args
	 */
	static final String folderPath="/users/dany/downloads/New";
	//static final String folderPath="/people/cs/s/sanda/cs6322/Cranfield";	

	static HashMap<String, Integer> wordMap=new HashMap<String, Integer>();
	static HashMap<String, Integer> stemmerWordMap=new HashMap<String, Integer>();

	private static final Pattern TAG_REGEX = Pattern.compile("<[^>]*>([^<]*)</[^>]*>");
	static int noOfTokens=0,noOfUniqueTokens=0,noOfWordOccursOnce=0,thirtyMostFrequent=0,averageTokens=0,totalFileCount=0;
	static int noOfStemmerTokens=0,noOfStemmerUniqueTokens=0,noOfStemmerWordOccursOnce=0,thirtyStemmerMostFrequent=0;


	public static void main(String[] args) throws FileNotFoundException {		

		long inTime=System.currentTimeMillis();

		callWordProcessFunctions();

		long pTime=System.currentTimeMillis();
		System.out.println("Time in Milli Secs "+(pTime-inTime));
	}	

	public static void callWordProcessFunctions()
	{
		calculateWordOccurance();
		processWordUsingStemmer();
		calculateStemmerWordOccurance();
		printWordOccurance();
	}

	public static void processWordUsingStemmer()
	{
		Stemmer stemmer=new Stemmer();

		for(String word : wordMap.keySet())
		{
			/* to test add(char ch) */
			for (int c = 0; c < word.length(); c++) 
				stemmer.add(word.charAt(c));

			stemmer.stem();
			{  String u;

			u = stemmer.toString();
			if(u.equals("approxim"))
			{
				// System.out.println("Word Testing        "+word);
			}
			if(stemmerWordMap.containsKey(u))
			{
				stemmerWordMap.put(u, stemmerWordMap.get(u)+1);
			}else
			{
				stemmerWordMap.put(u, 1);
			}
			//System.out.print(u);
			}
		}
	}

	public static void printWordOccurance()
	{
		Map<String, Integer> sortedWordMap = sortByValue(wordMap);

		System.out.println("1. Total No of Tokens		: " + noOfTokens);
		System.out.println("2. Total Unique Tokens		: " + noOfUniqueTokens);
		System.out.println("3. Words that occurs Only once	: " + noOfWordOccursOnce);
		System.out.println("4. Average Tokens per CraneField: "+ (new Float(noOfTokens / totalFileCount)));
		print(sortedWordMap);

		//System.out.println("Total files : "+totalFileCount);
		System.out.println("\n\n\n\n");
		System.out.println("===========================================");
		System.out.println("             STEMMER OUTPUT                ");
		System.out.println("===========================================");

		Map<String, Integer> sortedStemmerWordMap = sortByValue(stemmerWordMap);

		System.out.println("1. Total No of Tokens		: " + noOfStemmerTokens);
		System.out.println("2. Total Unique Tokens		: " + noOfStemmerUniqueTokens);
		System.out.println("3. Words that occurs Only once	: " + noOfStemmerWordOccursOnce);
		System.out.println("4. Average Tokens per CraneField: "+ (new Float(noOfStemmerTokens / totalFileCount)));
		print(sortedStemmerWordMap);
	}

	public static void calculateWordOccurance()
	{
		processInputFile();
		noOfUniqueTokens=wordMap.size();
		int wordCount=0;

		for(String word : wordMap.keySet())
		{
			wordCount=wordMap.get(word);
			if(wordCount==1)
				noOfWordOccursOnce++;
			noOfTokens+=wordCount;
			//System.out.println(word+"   "+wordMap.get(word));
		}
	}

	public static void calculateStemmerWordOccurance()
	{
		noOfStemmerUniqueTokens=stemmerWordMap.size();
		int wordCount=0;

		for(String word : stemmerWordMap.keySet())
		{
			wordCount=stemmerWordMap.get(word);
			if(wordCount==1)
				noOfStemmerWordOccursOnce++;
			noOfStemmerTokens+=wordCount;
			//System.out.println(word+"   "+wordMap.get(word));
		}
	}


	public static void print(Map<String, Integer> map) {
		int j = 0;
		int i = map.keySet().size();
		System.out.println("------------------------------------");
		System.out.println("| Word 		|	Frequency |");
		System.out.println("------------------------------------");
		for (Map.Entry entry : map.entrySet()) {
			if (j == i - 30) {
				System.out.println(entry.getKey() + "		:		" + entry.getValue()
						+ " ");
			} else {
				j++;
			}
		}
	}

	public static  Map<String,Integer> sortByValue(Map<String,Integer> unsortMap) {

		List<String> l = new LinkedList(unsortMap.entrySet());

		Collections.sort(l, new Comparator() {
			public int compare(Object Obj1, Object Obj2) {
				return ((Comparable) ((Map.Entry) (Obj1)).getValue())
						.compareTo(((Map.Entry) (Obj2)).getValue());
			}
		});

		Map mapSorted = new LinkedHashMap();
		for (Iterator itrerator = l.iterator(); itrerator.hasNext();) {
			Map.Entry ent = (Map.Entry) itrerator.next();
			mapSorted.put(ent.getKey(), ent.getValue());
		}
		return mapSorted;
	}

	public static void processInputFile() 
	{
		File file=new File(folderPath);
		String[] filePaths=file.list();
		String filePath="";
		//String testPath="/users/dany/downloads/New/cranfield0001";
		for(String path : filePaths)
		{
			totalFileCount++;
			filePath=folderPath+"/"+path;

			File fileObj = new File(filePath);
			FileInputStream fis = null;
			String str = "";
			try {
				fis = new FileInputStream(fileObj);
				int content;
				while ((content = fis.read()) != -1) {
					// convert to char and display it
					str += (char) content;
				}
				processWords(str);				
				//System.out.println(str);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}			
			}
		}
	}

	public static void processWords(String str) throws FileNotFoundException
	{
		//String str="<DOC><DOCNO>1</DOCNO><TITLE>experimental investigation of the aerodynamics of awing in a slipstream .</TITLE><AUTHOR>brenckman,m.</AUTHOR><BIBLIO>j. ae. scs. 25, 1958, 324.</BIBLIO><TEXT>  an experimental study of a wing in a propeller slipstream was made in order to determine the spanwise distribution of the lift increase due to slipstream at different angles of attack of the wing and at different free stream to slipstream velocity ratios .  the results were intended in part as an evaluation basis for different theoretical treatments of this problem .   the comparative span loading curves, together with supporting evidence, showed that a substantial part of the lift increment produced by the slipstream was due to a /destalling/ or boundary-layer-control effect .  the integrated remaining lift increment, after subtracting this destalling lift, was found to agree well with a potential flow theory .  an empirical evaluation of the destalling effects was made for the specific configuration of the experiment . </TEXT> </DOC>";
		//String str="<>an experimental study of a wing in a propeller slipstream was</TEXT>";
		ArrayList<String> tagValues=getTagValues(str);

		for(String s : tagValues)
		{
			String string=s;			
			//System.out.println("Test "+string);
			string=string.replaceAll("[-+.^:,\\/()]", "");
			//str1=str1.replaceAll("[^a-zA-Z0-9]", "");

			String[] splited = string.split("\\s+");
			for(String word : splited)
			{			
				if(!isNumeric(word))
				{
					if (!(word.matches(".*[0-9].*")) && word.length()>1)
					{
						if(word.contains("'"))
						{
							word=word.substring(0,word.indexOf("'"));
						}
						word=word.toLowerCase();
						//System.out.println(word.toLowerCase());
						if(wordMap.containsKey(word))
						{
							wordMap.put(word, wordMap.get(word)+1);
						}else
						{
							wordMap.put(word, 1);
						}
					}
				}
			}
		}
	}

	private static ArrayList<String> getTagValues(final String str) {
		final ArrayList<String> tagValues = new ArrayList<String>();
		final Matcher matcher = TAG_REGEX.matcher(str);
		while (matcher.find()) {
			tagValues.add(matcher.group(1));
		}
		return tagValues;
	}


	private static boolean isNumeric(String str)  
	{  
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}

}
