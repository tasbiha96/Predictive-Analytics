import java.io.*;
import java.util.*;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import java.util.Properties;
import java.util.regex.*;
import java.util.stream.Collectors;

import edu.stanford.nlp.simple.*;

public class PreProcessing1 {
	List<String> stopwords;
	
	PreProcessing1(List<String> stopwords){
       this.stopwords = stopwords;
    }
    
     public String removeStopwords(String[] words){
        StringBuilder sb1 = new StringBuilder();
     	for(String word:words){
     		if(!stopwords.contains(word)){
     			sb1.append(word + " ");
     		}
        }
        return sb1.toString();
    }
    
    public List<String>  processFiles(String[] words){
        String output = removeStopwords(words);
        
        List<String> tokens = new ArrayList<String>();

//      Tokenization, Generating POS Tags, Lemma, NER Tags
        
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(output);
        
        pipeline.annotate(doc);
        
//        String tokensLemmaNERTags = doc.tokens().stream().map(token -> "("+token.word()+","+token.lemma()+","+token.ner()+")")
//       		.collect(Collectors.joining(" "));
//        System.out.println("tokensLemmaNERTags: " + tokensLemmaNERTags);
        
        // Applying NER
        String prevTag = doc.tokens().get(0).ner();
        String currTag = "O";
        String ner_pairs ="";
        String prevWord=doc.tokens().get(0).word(),currWord="";
        Pattern p = Pattern.compile("[\\p{Punct}\\p{IsPunctuation}]");
        
        for(int i=1; i <=doc.tokens().size();i++) {

        	CoreLabel tok;
        	if(i<doc.tokens().size()) {
        		tok = doc.tokens().get(i);
        		currTag = tok.ner();
        		currWord = tok.word();
        	}
        	else {
        		tok=null;
        		currTag = "O";
        		currWord = "";
        	}
        	CoreLabel prevTok = doc.tokens().get(i-1);
        	if (!prevTok.word().matches("[\\p{Punct}\\p{IsPunctuation}]")) {
        	
//        		System.out.println("Curr Tag: " +currTag);
        		if(prevTag.equals("O")) {
//        			System.out.println("tokensLemma: " + tok.lemma());
        			tokens.add(prevTok.lemma());
        			prevTag = currTag;
            		prevWord = currWord;
            		prevTok = tok;
        			continue;
        		}
        		if(prevTag.equals(currTag)) {
        			ner_pairs += prevWord + "_";
        			prevTag = currTag;
            		prevWord = currWord;
            		prevTok = tok;
        			continue;
        		}
        		ner_pairs +=prevWord;
        		tokens.add(ner_pairs);	
        		ner_pairs="";
        		prevTag = currTag;
        		prevWord = currWord;
        		prevTok = tok;
        		//}
        	}
        }
        return tokens;     
    }
    
    public void slidingWindow(Map<String, List<String>> map) {
    	Map<String,Integer> freq = new HashMap<>();
    	List<String> list = new ArrayList<>();
    	   getNGrams(map,freq,2);
    	   getNGrams(map,freq,3);
           for (Map.Entry<String, Integer> entry: freq.entrySet()) {
               if (entry.getValue()>=10) {
                   list.add(entry.getKey());
               }
           }
//           System.out.println("LIST: " + list);
           
           for (Map.Entry<String, List<String>> entry: map.entrySet()) {

               String modifiedTokens =  String.join(" ", entry.getValue());
               modifiedTokens.trim();
               for(String ngram : list){ 
            	    String ref = ngram.replace(" ","_");
            	    modifiedTokens = modifiedTokens.replace(ngram,ref);
               }

               List<String> tokensList = Arrays.asList(modifiedTokens.split(" "));
               map.put(entry.getKey(), tokensList);
           }
       }
    
    
    public void getNGrams(Map<String, List<String>> map,Map<String, Integer> freq, int n){
    	for (Map.Entry<String, List<String>> entry: map.entrySet()) {
 		   String[] array = entry.getValue().toArray(new String[0]);	
 	    	List<String> nGrams = new ArrayList<String>();
 	    	for (int i = 0; i< array.length - n + 1;i++) {
 			    StringBuilder sb = new StringBuilder();
 				sb.append(array[i]);
 				for(int j = i+1; j< i+n;j++) {
 					sb.append(" " + array[j]);
 				}
 				nGrams.add((sb.toString()).trim());
 	    	}
 				
 				
            for (String ngram: nGrams) {
            	Integer count = freq.getOrDefault(ngram, 0);
                freq.put(ngram, count + 1);
            	}
 	    	}
    	}
    }


	

    
    

        
        