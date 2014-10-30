package com.ir.wordindexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TermFreq {
	static BufferedReader br = null;	
	static String folderPathham=null;
	static String line = "";
	static String delimiter = " ";
	static String[] split=null;
	static HashMap<Integer, ArrayList<String>> dict=new HashMap<Integer, ArrayList<String>>();
	static HashMap<Integer, String[]> hashTF=new HashMap<Integer, String[]>();
	static ArrayList<String> termBag=new ArrayList<String>();
	static int count=0;
	static File[] listOfFiles;
		
	public static void createDictionary(String folderPath){
			File folder = new File(folderPath);
			listOfFiles = folder.listFiles();
			count=listOfFiles.length;
			String filename=null;
			for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			    	  filename=listOfFiles[i].toString();
			    	  parse(filename,i);
			      } 
			 }
			findTF();
			Iterator<Entry<Integer, String[]>> iterator = hashTF.entrySet().iterator() ;
	        while(iterator.hasNext()){
	        	Map.Entry<Integer, String[]> tokenhash = iterator.next();
	        	String[] values=new String[2];
	        	values=tokenhash.getValue();
	        	System.out.println(tokenhash.getKey()+" :"+values[0]+" :"+values[1]);
	        }
	}
		
	public static void parse(String filename,int docId){
			String token=null;
			termBag=new ArrayList<String>();
			try {
				br = new BufferedReader(new FileReader(filename));
				while ((line = br.readLine()) != null) {
					split = line.split(delimiter);
					for(int i=0;i<split.length;i++){
						token=split[i].trim();
						String token1="";
					for(int j=0;j<token.length();j++){
						if(token.charAt(j)=='\''){
							break;
						}
						else{
						token1=token1+token.charAt(j);
						}
					}
						token=token1;
						
						
					if(token.equalsIgnoreCase("-") ||token.equalsIgnoreCase(".")|| token.equalsIgnoreCase("@") || token.equalsIgnoreCase("/")
					|| token.equalsIgnoreCase("+")|| token.equalsIgnoreCase("*")|| token.equalsIgnoreCase(":") || token.equalsIgnoreCase(",") ||token.equalsIgnoreCase("%") || token.equalsIgnoreCase("!")|| token.equalsIgnoreCase("<")||
					token.equalsIgnoreCase("{") ||token.equalsIgnoreCase("") ||token.equalsIgnoreCase(" ")||token.equalsIgnoreCase("_") ||token.equalsIgnoreCase("\\") ||token.equalsIgnoreCase("=") ||token.equalsIgnoreCase(";")  ||token.equalsIgnoreCase(">") || token.equalsIgnoreCase("?") || token.equalsIgnoreCase("[")|| token.equalsIgnoreCase("]") || token.equalsIgnoreCase("!") || token.equalsIgnoreCase("'")){
						continue;
					}
					else if(token.startsWith("<") && token.endsWith(">")){
						continue;
					}
					else{
						if(token.startsWith("-") ||token.startsWith(".")|| token.startsWith("@") || token.startsWith("/")
						|| token.equalsIgnoreCase("+")|| token.equalsIgnoreCase("*")|| token.startsWith(":") || token.startsWith(",")||token.startsWith("(")||token.startsWith(")") ||token.startsWith("%") || token.startsWith("!")|| token.startsWith("<")||
						token.startsWith("\"") ||token.startsWith("{") ||token.startsWith("}") ||token.startsWith("_") ||token.startsWith("\\") ||token.startsWith("=") ||token.startsWith(";")  ||token.startsWith(">") || token.startsWith("?") || token.startsWith("[")|| token.startsWith("]") || token.startsWith("!") || token.startsWith("'")){
									token=token.substring(1);
									token=token.toLowerCase();
						}
						if(token.endsWith("-") ||token.endsWith(".")|| token.endsWith("@") || token.endsWith("/")
						|| token.equalsIgnoreCase("+")|| token.equalsIgnoreCase("*")|| token.endsWith(":") || token.endsWith(",") ||token.endsWith("%") || token.endsWith("!")|| token.endsWith("<")||
						token.startsWith("\"") ||token.endsWith("{") ||token.endsWith("}") ||token.endsWith("_") ||token.endsWith("\\") ||token.endsWith("=") ||token.endsWith(";")  ||token.endsWith(">") || token.endsWith("?") || token.endsWith("[")|| token.endsWith("]") || token.endsWith("!") || token.endsWith("'")){
									token=token.substring(0,token.length()-1);
									token=token.toLowerCase();
						}
					}
						termBag.add(token);

					}
				}
				dict.put(docId,termBag);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	public static void findTF(){
		int c=0;
		Iterator<Entry<Integer, ArrayList<String>>> iterator = dict.entrySet().iterator() ;
		HashMap<String, Integer> wordcount=new HashMap<String, Integer>();
		ArrayList<String> termBaglocal=new ArrayList<String>();
        while(iterator.hasNext()){
        	wordcount=new HashMap<String, Integer>();
            Map.Entry<Integer, ArrayList<String>> tokenhash = iterator.next();
            //System.out.println(totaltokens.getKey()+":"+totaltokens.getValue());
            termBaglocal=tokenhash.getValue();
            for (String s : termBaglocal) {
            	if(wordcount.containsKey(s)){
            		int count=wordcount.get(s);
            		count++;
            		wordcount.put(s, count);
            	}
            	else{
            		wordcount.put(s, 1);
            	}
            }
            //find max from wordcount
            Entry<String,Integer> maxEntry = null;
            for(Entry<String,Integer> entry : wordcount.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
            String[] maxKeyVal=new String[2];
            maxKeyVal[0]=maxEntry.getKey();
            maxKeyVal[1]=maxEntry.getValue().toString();
            hashTF.put(tokenhash.getKey(), maxKeyVal);
        }

		
	}
	
}
