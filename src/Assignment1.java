import java.io.*;
import java.util.*;

import edu.stanford.nlp.io.EncodingPrintWriter.out;

import java.nio.file.*;

public class Assignment1 {

	public static void main(String[] args) throws Exception {
		String stopwords_file = "stopwords.txt";
		List<String> stopwords = Files.readAllLines(Paths.get(stopwords_file));
		
		Map<String,List<String>> map = new HashMap<>();
		Map<Integer,Integer> documentCount = new HashMap<>();
		int dc=0;
		String folder_list = "data.txt";
		int k = 3;
		String similarityMethod = "cosine";
		//String folder_list = args[0];
		//int k = Integer.parseInt(args[1]);
		//String similarityMethod = args[2];
		
		File data_file = new File(folder_list);
		BufferedReader br1 = new BufferedReader(new FileReader(data_file));
		String folder_name;
		
		//Preprocessing  
		PreProcessing1 pp= new PreProcessing1(stopwords);
		while ((folder_name = br1.readLine()) != null) {
			documentCount.put(folder_name.charAt(1)-'0',dc );
			File folder = new File("data/" + folder_name);
            for (File file : folder.listFiles()) {
            	 BufferedReader br2 = new BufferedReader(new FileReader(file));
                 String line;
                 StringBuilder words = new StringBuilder();
                 while((line = br2.readLine()) != null){
                 	words.append(line.toLowerCase() + " ");
                 }
                 String[] splitted = words.toString().split(" ");
		         List<String> tokens = pp.processFiles(splitted);
		         map.put(folder_name + "/" + file.getName() , tokens);
		         br2.close();
            }
            dc++;
        }
		br1.close();
		pp.slidingWindow(map);
		
		//TFIDF
		TFIDF tfidf = new TFIDF((HashMap<String, List<String>>) map);
		double[][] documentTermMatrix = tfidf.constructDocumentTermMatrix();
        double[][] TFIDF  = tfidf.computeTFIDF((HashMap<String, List<String>>) map);
        HashMap<String,  SortedSet<String>> topics = tfidf.generateTopicsLists();
        tfidf.createTopicsFile(topics);
        //System.out.println(topics);
        
        int[] actualClass = new int[map.keySet().size()];
        int x = 0;
        List<String> sortedKeys = new ArrayList<>();
        sortedKeys.addAll(map.keySet());
        Collections.sort(sortedKeys);

        for(String folderName : sortedKeys)  {
        	String[] name = folderName.split("/");
        	actualClass[x] = documentCount.get(name[0].charAt(1) - '0');
        	x++;
        }
//        for(int y=0;y<actualClass.length;y++)
//        	System.out.println("Folder Name " + sortedKeys.get(y) + "  " + actualClass[y]);
     }
}