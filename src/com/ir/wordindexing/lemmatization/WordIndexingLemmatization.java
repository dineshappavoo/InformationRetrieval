package com.ir.wordindexing.lemmatization;

/*

 * @author Logeshwaran Bhuvaneshwaran
 * HomeWork  2
 * CS 6322 : Information Retrieval 
 * Fall 2014
 */

import java.io.*;
import java.util.*;

public class WordIndexingLemmatization {

	public static void main(String[] args) {

		new WordIndexingLemmatization().processFiles();

	}


	public void processFiles()
	{

		try {
			long start = System.currentTimeMillis();
			final File folder = new File("/Users/Dany/Downloads/data1");

			//StanfordLemmatizer lemmatizer=new StanfordLemmatizer();

			Integer count = 0;			
			String stop = "";
			ArrayList<String> files = new ArrayList<String>();

			HashMap<String, Integer> hmap = new HashMap<String, Integer>();
			HashMap<String, str> posting = new HashMap<String, str>();

			Hashtable<String, Integer> hTabCnt = new Hashtable<String, Integer>();
			Hashtable<Integer, Hashtable> wordFreqDoc = new Hashtable<Integer, Hashtable>();
			Hashtable<String, Integer> documentlength = new Hashtable<String, Integer>();

			System.out.println("Reading stop words");
			//File file = new File(	"C:/Users/Arunkumar/Desktop/IR/resourcesIR/stopWords");

			File[] lFiles = folder.listFiles();


			//Stopwords file
			File file = new File("/Users/Dany/Downloads/stopwords.txt");
			//File file = new File(args[1]);

			int ch;
			StringBuffer Stringcontent = new StringBuffer("");
			FileInputStream fin = new FileInputStream(file);
			while ((ch = fin.read()) != -1){
				Stringcontent.append((char) ch);
			}
			fin.close();

			//final File folder = new File(args[0]);

			System.out.println("Test");

			System.out.println("Processing "+lFiles.length+" files...");
			int len = lFiles.length;
			for (int k = 0; k < len; k++) {
				files.add(lFiles[k].toString().trim());
			}

			System.out.println("Test1");

			for (int j = 0; j < lFiles.length; j++) {
				String line;

				BufferedReader buf = new BufferedReader(new FileReader(lFiles[j]));
				int tokencount = 0, number = 0;
				documentlength.put(lFiles[j].toString().trim(), number);
				stop = "";
				Hashtable<String, Integer> hashTWord = new Hashtable<String, Integer>();
				while ((line = buf.readLine()) != null) {

					line = line.replaceAll("<(.|\n)*?>", " ").trim();

					StringTokenizer token = new StringTokenizer(line, " ");

					while (token.hasMoreTokens()) {
						String temp = token.nextToken(" ").trim();
						temp = temp.toLowerCase();

						if (temp.equals(".") || temp.equals(";")|| temp.startsWith("<") || temp.equals(":") || temp.equals(",")) {
							continue;
						}

						if (temp.contains(")") || temp.contains("(") || temp.contains("/")) {
							temp = temp.replace("(", "");
							temp = temp.replace(")", "");
							temp = temp.replace("/", "");
						}

						if (temp.startsWith(",") || temp.endsWith(",") ||temp.contains(",") || temp.startsWith(".") || temp.endsWith(".")) {
							temp = temp.replace(",", "");
							temp = temp.replace(".", "");
						}



						if (temp.startsWith(";") || temp.endsWith(";") || temp.startsWith(":") || temp.endsWith(":") || temp.endsWith("\\")) {
							temp = temp.replace(";", "");
							temp = temp.replace("\\", "");
							temp = temp.replace(":", "");
						}


						if (Stringcontent.toString().contains(temp.toLowerCase())) {

							documentlength.put(lFiles[j].toString().trim(), documentlength
									.get(lFiles[j].toString().trim())
									.intValue() + 1);
							temp = "";
							continue;
						}
						documentlength.put(lFiles[j].toString().trim(), documentlength
								.get(lFiles[j].toString().trim())
								.intValue() + 1);


						//Stem stemobj = new Stem();
						LinkedList<String> lemmatizedWords=new LinkedList<String>();

						if (!(temp.equals(""))){
							System.out.println("WORD "+temp.toString());
							

							//lemmatizedWords = (LinkedList<String>) lemmatizer.lem(temp.toString());
						}

						for(String word : lemmatizedWords)
						{
							//temp=word;						

							if (word.length() <= 3 || word.equalsIgnoreCase(" ")|| word.equalsIgnoreCase("  "))
								continue;
							word = temp.trim();

							if (hashTWord.get(word) != null) {
								hashTWord.put(word, hashTWord.get(word) + 1);
							} else {
								hashTWord.put(word, 1);
							}

						count++;
						tokencount++;

						int indx = files.indexOf(lFiles[j].toString().trim());
						str st ;
						if (hmap.get(word) != null) {
							hmap.put(word, hmap.get(word) + 1);
							st = posting.get(word);
							st.totFreqency++;

							if (st.docId.contains(indx)) {
								int value = st.termFreq.get(st.docId
										.indexOf(indx));
								st.termFreq.set(st.docId.indexOf(indx),
										value + 1);
							}
							else {

								st.docId.add(indx);
								st.termFreq.add(st.docId.indexOf(indx), 1);
								st.totDoc++;
							}
							posting.put(word, st);

						} else {
							st = new str();
							hmap.put(word, 1);

							st.totFreqency++;
							st.totDoc++;

							st.docId.add(indx);
							st.termFreq.add(st.docId.indexOf(indx), 1);
							posting.put(word, st);
						}

						

						break;//Test
						}//lemmatized words end

						
					}
					
					break;//Test
				}

				if (hashTWord != null) {
					wordFreqDoc.put(files.indexOf(lFiles[j].toString().trim()),
							hashTWord);
				}
				hTabCnt.put(lFiles[j].toString().trim(), tokencount);
			}
			WordIndexingLemmatization obj = new WordIndexingLemmatization();
			obj.sizeOfInvertedindex(posting);

			String[] words = { "reynold", "nasa", "prandtl", "flow", "pressur",
					"boundari", "shock" };




			for (int j = 0; j < words.length; j++) {
				System.out.println();
				System.out.println("********************************");
				System.out.println("Term:"+words[j]);
				str sm = posting.get(words[j]);
				System.out.println("Total Number of documents (df):"+sm.totDoc);
				System.out.println("Total term frequency (tf):"+sm.totFreqency);
				System.out.println("Inverted List (df,tf):");
				File fileword = new File("word"+j);
				Writer fileword2 = new BufferedWriter(new FileWriter(fileword));

				double  b =0;
				for (int i = 0; i < sm.docId.size(); i++) {
					System.out.print("[");
					fileword2.write("[");
					System.out.print(sm.docId.get(i)+1);
					fileword2.write(Integer.toString(sm.docId.get(i)+1));
					System.out.print(",");
					fileword2.write(",");
					System.out.print(sm.termFreq.get(i));
					fileword2.write(Integer.toString(sm.termFreq.get(i)));
					System.out.print("] ");
					fileword2.write("]");
					fileword2.write(" ");

				}


				fileword2.close();

				File f = new File("word"+j);
				System.out.println("");
				System.out.println(" Size of the inverted list in bytes: "+f.length());

			}


			long end = System.currentTimeMillis();			
			System.out.println(" \n Run Time " + (end - start)/ 1000 + "sec");		

		}

		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}


	}
	String delta(int num) throws IOException {
		String binary = Integer.toBinaryString(num);
		int	len = binary.length();
		String offset = gamma(len);
		return (offset + binary.substring(1, len));
	}

	String gamma(int num) throws IOException {
		String binary = Integer.toBinaryString(num);
		int len = binary.length();
		String offset = binary.substring(1, len);
		String unary = "";
		for (int i = 0; i < offset.length(); i++) {
			unary = unary + "1";
		}
		unary = unary + "0";
		return ( unary + offset);
	}

	void sizeOfInvertedindex(HashMap<String, str> posting)
			throws IOException {
		{
			File file1 = new File("/Users/Dany/Downloads/comp.txt");
			File file2 = new File("/Users/Dany/Downloads/uncomp.txt");
			Writer uncomp = new BufferedWriter(new FileWriter(file2));
			FileOutputStream comp = new FileOutputStream(file1);
			int first = 0, sizeuncomp = 0, sizecomp = 0, count = 0, last = 0;
			String delta = null, gamma = null;

			List<String> e = new ArrayList<String>(posting.keySet());
			for (String s: e) {				
				str getList = posting.get(s);
				int i = 0;
				uncomp.write(s+" "+getList.docId.size());
				WordIndexingLemmatization p = new WordIndexingLemmatization();
				count++;

				for (i = 0; i < getList.docId.size(); i++) {
					int value = getList.docId.get(i).intValue();
					String val = getList.docId.get(i).toString();
					int freq = getList.termFreq.get(i).intValue();
					String fq = getList.termFreq.get(i).toString();
					uncomp.write("["+val + "," + fq+"]");

					sizeuncomp += val.getBytes().length + fq.getBytes().length;
					if (first == 0) {
						delta = p.delta(value);
					} else {

						delta = delta + p.delta(value - last);
					}
					last = value;
					gamma = gamma + p.gamma(freq);

				}

				comp.write(delta.getBytes().length);
				comp.write(gamma.getBytes().length);

				uncomp.write("\n");

			}

			uncomp.write("\n\n"+sizeuncomp);
			sizecomp = gamma.getBytes().length + delta.getBytes().length;
			comp.write(sizecomp);

			uncomp.close();
			comp.close();

			System.out.println("Size of inverted list:" + count+" \n Uncompressed size :" + sizeuncomp+"\n Compressed size :" + sizecomp);

		}
	}	
}

class str {
	List<Integer> docId=new ArrayList<Integer>();
	List<Integer> termFreq=new ArrayList<Integer>();
	int totFreqency=0, totDoc=0;		
}