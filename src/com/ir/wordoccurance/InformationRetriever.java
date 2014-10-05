package com.ir.wordoccurance;


/*
 * @author Dinesh Appavoo
 * HomeWork  1
 * CS 6322 : Information Retrieval 
 * Fall 2014
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InformationRetriever {
	static long startTime = System.currentTimeMillis();
	static int count = 0, count1=0;
	static HashMap<String, Integer> hm = new HashMap<String, Integer>();

	public static void main(String[] args) throws IOException {

		InformationRetriever hw = new InformationRetriever();
		File file = null;
		String[] paths;
		int cnt = 0;
		try {
			String filePath = "/users/dany/downloads/New";
			//String filePath = "C:/Users/Arunkumar/Desktop/Folder/";

			file = new File(filePath);

			paths = file.list();
			for (String path : paths) {
				cnt++;
				System.out.println(filePath+"/"+path);

				hw.processFile(filePath + "/" + path);

				//break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("1. Total tokens			: " + count);
		System.out.println("2. Total Unique Tokens		: " + hm.size());
		System.out.println("3. Words that occurs Only once	: " + hw.OnlyOnce());
		System.out.println("4. Average Tokens per CraneField: "
				+ (new Float(count / cnt)));
		System.out.println("5. Most Frequent 30 words ");

		Map<String, String> sortedHashMap = hw.sortByValue(hm);
		hw.print(sortedHashMap);
		
		System.out.println("Testing  "+hm.get("1958"));

		long endTime = System.currentTimeMillis();
		System.out.println("6. Run time :" + ((endTime - startTime)) + " ms");
	}

	// ========================================================================================
	
	public void runStemmerAlgorithm()
	{
		Stemmer stemmer=new Stemmer();
	}

	private Map sortByValue(Map unsortMap) {

		List l = new LinkedList(unsortMap.entrySet());

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

	// ============================================================================================

	public void print(Map<String, String> map) {
		int j = 0;
		int i = map.keySet().size();
		System.out.println("--------------------");
		System.out.println("| Word | Frequency |");
		System.out.println("--------------------");
		for (Map.Entry entry : map.entrySet()) {
			if (j == i - 30) {
				System.out.println(entry.getKey() + ":	" + entry.getValue()
						+ " ");
			} else {
				j++;
			}
		}
	}

	// ============================================================================================

	private int OnlyOnce() {
		int cnt = 0;
		for (String s : hm.keySet()) {
			if (hm.get(s) == 1) {
				cnt++;
			}
		}
		return cnt;
	}

	private void processFile(String string) throws IOException {
		
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader(string));

		while ((sCurrentLine = br.readLine()) != null) {

			// System.out.println(sCurrentLine);
			String[] temp = sCurrentLine.split(" ");

			for (int i = 0; i < temp.length; i++) {
				
				if ((temp[i].endsWith(">")) || (temp[i].startsWith("<"))
						|| InformationRetriever.hasNumber(temp[i]) || temp[i].equals(".")
						|| temp[i].equals(",") || temp[i].trim().equals("")) {
					continue;
				}

				else if (temp[i].startsWith(".") || temp[i].startsWith(",")
						|| temp[i].startsWith("/") || temp[i].startsWith(":")
						|| temp[i].startsWith(";") || temp[i].startsWith("(")
						|| temp[i].startsWith("\\")) {

					if (temp[i].endsWith(".") || temp[i].endsWith(",")
							|| temp[i].endsWith("/") || temp[i].endsWith(":")
							|| temp[i].endsWith(";") || temp[i].endsWith(")")
							|| temp[i].endsWith("\\")) {
						if (temp[i].length() > 1) {
							// System.out.print(temp[i].substring(1,
							// temp[i].length()-1)+" ");
							count++;
							InformationRetriever.add(temp[i].substring(1, temp[i].length() - 1)
									.toLowerCase());
						}
					} else {
						// System.out.print(temp[i].substring(1)+" ");
						count++;
						InformationRetriever.add(temp[i].substring(1).toLowerCase());
					}
				}

				else if (temp[i].endsWith(".") || temp[i].endsWith(",")
						|| temp[i].endsWith("/") || temp[i].endsWith(":")
						|| temp[i].endsWith(";") || temp[i].endsWith("(")
						|| temp[i].endsWith("\\")) {
					if (hasNumber(temp[i].substring(0, temp[i].length() - 1)) == true) {

					} else {
						// System.out.print(temp[i].substring(0,
						// temp[i].length()-1)+" ");
						count++;

						InformationRetriever.add(temp[i].substring(0, temp[i].length() - 1)
								.toLowerCase());
					}
				}

				else if(temp[i].length()>3){
					if(temp[i].charAt(temp[i].length()-2) == '\'' ){
						count++;
						//System.out.println(temp[i].charAt(temp[i].length()-2)+ " : "+count1);
						InformationRetriever.add(temp[i].substring(0, temp[i].length() - 2)
								.toLowerCase());
					}
					else{
						InformationRetriever.add(temp[i].toLowerCase());
						count++;
					}
				}

				else {
					// System.out.print(temp[i]+" ");
					count++;
					InformationRetriever.add(temp[i].toLowerCase());
				}
			}
			// System.out.println("");
		}

		// System.out.println(" ");

	}

	private static void add(String string) {
		int value = 1;

		if (hm.containsKey(string)) {
			int temp = hm.get(string) + 1;
			hm.put(string, temp);
		} else {
			hm.put(string, value);
		}

	}

	private static boolean hasNumber(String string) {
		float i = 0;
		try {
			i = Float.parseFloat(string);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}
