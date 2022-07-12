import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Assignment1 {

	public static void main(String[] args) throws Exception {
		String stopwords_file = "stopwords.txt";
		List<String> stopwords = Files.readAllLines(Paths.get(stopwords_file));
		
		Map<String,List<String>> map = new HashMap<>();
        PreProcessing1 pp= new PreProcessing1(stopwords);
		
		String folder_list = "folderList.txt";
		File data_file = new File(folder_list);
		BufferedReader br1 = new BufferedReader(new FileReader(data_file));
		String folder_name;
		      
		while ((folder_name = br1.readLine()) != null) {
			File folder = new File("data/" + folder_name);
            for (File file : folder.listFiles()) {
            	 BufferedReader br2 = new BufferedReader(new FileReader(file));
                 String line;
                 StringBuilder words = new StringBuilder();
                 while((line = br2.readLine()) != null){
                 	words.append(line.toLowerCase() + " ");
                 }
                 String[] splitted = words.toString().split("\\s+");
		         List<String> tokens = pp.processFiles(splitted);
		         map.put(folder_name + "/" + file.getName() , tokens);
		         br2.close();
            }
        }
		pp.slidingWindow(map);
		System.out.println(map);
		br1.close();
     }
}