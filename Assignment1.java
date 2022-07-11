import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Assignment1 {

	public static void main(String[] args) throws Exception {
		String stopwords_file = "stopwords.txt";
		List<String> stopwords = Files.readAllLines(Paths.get(stopwords_file));
		
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
                 String words = "";
                 String[] split;
                 while((line = br2.readLine()) != null){
                 	words+=line + " ";
                 }
                 String[] splitted = words.toLowerCase().split("\\s+");
		         List<String> tokens = pp.processFiles(splitted);
		         

            }
        }
     }
}