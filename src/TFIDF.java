import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class TFIDF {

    List<String> allTokens;
    List<String> documents;
    double[] IDF;
    double[][] TF;
    double[][] TFIDF;
    double[][] documentTermMatrix;
    HashMap<String, List<String>> map;
    


     public TFIDF(HashMap<String, List<String>> map) {
    	 this.map = map;
         HashSet<String> tokenSet = new HashSet<>();
         documents = new ArrayList<>();
         allTokens = new ArrayList<>();
         for (List<String> tokens : map.values())
             tokenSet.addAll(tokens);

         for (String docs : map.keySet())
             documents.add(docs);
         TF = new double[documents.size()][tokenSet.size()];
         IDF = new double[tokenSet.size()];
         allTokens.addAll(tokenSet);
         Collections.sort(documents);
         Collections.sort(allTokens);
	}
	double[][] computeTFIDF(HashMap<String, List<String>> map) {
		
		TF = constructDocumentTermMatrix();
        for (int i = 0; i < documents.size(); i++) {
            int count = 0;
            for (int j = 0; j < allTokens.size(); j++) {
                if (TF[i][j] != 0)
                    count++;
            }
            if(count!=0)
                IDF[i] = (Math.log(documents.size() / count));
        }
        
        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < allTokens.size(); j++) {
                TF[i][j] = ((TF[i][j])/(map.get(documents.get(i)).size())) * IDF[j];
            }
        }
        this.TFIDF = TF;
        return TFIDF;
    }
	
	public double[][] constructDocumentTermMatrix() {
        for (int i = 0; i < documents.size(); i++) {
            for (int j = 0; j < allTokens.size(); j++) {
               // double freq = Collections.frequency(map.get(documents.get(i)), allTokens.get(j));
               // double TFScore = ( freq)/ map.get(documents.get(i)).size();
                TF[i][j] = Collections.frequency(map.get(documents.get(i)), allTokens.get(j));
            }
        }
        return TF;
	}
    public HashMap<String,  SortedSet<String>> generateTopicsLists(){
        HashMap<String, ArrayList<Integer>> folderDocMap = new HashMap<>();
        HashMap<String,  SortedSet<String>> keywords = new HashMap<>();
        for(int i=0;i<documents.size();i++){
            String document = documents.get(i);
            String[] arr = document.split("/");
            String folderName = arr[0];
             ArrayList<Integer> list = folderDocMap.getOrDefault(folderName, new ArrayList<>());
             list.add(i);
             folderDocMap.put(folderName,list);
        }

        for (Map.Entry<String, ArrayList<Integer>> entry: folderDocMap.entrySet()) {
            List<Integer> folderDocs = entry.getValue();
            HashMap<String, Double> tokenScore = new HashMap<>();
            for(Integer i: folderDocs){
                String doc = documents.get(i);
                HashSet<String> docTokens = new HashSet<>(map.get(doc));
                for(int j=0;j< allTokens.size();j++){
                     if(docTokens.contains(allTokens.get(j)))
                        tokenScore.put(allTokens.get(j), tokenScore.getOrDefault(allTokens.get(j),0.0)+TFIDF[i][j]);
                }
            }


            Comparator<String> compare = (e1, e2) -> tokenScore.get(e1) < tokenScore.get(e2) ? -1 : tokenScore.get(e1) > tokenScore.get(e2)? 1
                    : e1.compareTo(e2);
            SortedSet<String> keys = new TreeSet<>(compare);
            for(String k:tokenScore.keySet())
                keys.add(k);

            keywords.put(entry.getKey(), keys);
        }
    return keywords;
    }
    
    public void createTopicsFile(HashMap<String,SortedSet<String>> topics) {
    	List<String> folderNames = new ArrayList<>();
    	TreeMap<String,SortedSet<String>> sortedTopics = new TreeMap<>();
    	sortedTopics.putAll(topics);
    	try {
			FileWriter fw = new FileWriter("topics.txt");
			for (Map.Entry<String, SortedSet<String>> entry : sortedTopics.entrySet()) {
				String topicString = String.join(", ", entry.getValue());
				String[] arr = entry.getKey().split("/");
	            if(!(folderNames.contains(arr[0]))) {
	            	fw.write("Folder Name: " + arr[0]+ "\n");
	            	folderNames.add(arr[0]);
	            }
	            fw.write("Topics: \n" + topicString + "\n\n");	
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
}