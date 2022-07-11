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
    
     String removeStopwords(String[] words){
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

//        Tokenization, Generating POS Tags, Lemma, NER Tags
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument doc = new CoreDocument(output);
        
        pipeline.annotate(doc);
        
        String tokensLemmaNERTags = doc.tokens().stream().map(token -> "("+token.word()+","+token.lemma()+","+token.ner()+")")
       		.collect(Collectors.joining(" "));
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
        		tok= doc.tokens().get(i);
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
        System.out.println("After NER_tags: \n"+tokens+"\n\n");

        //tokens.removeIf(s -> s == null || "".equals(s));
        return tokens;
        
        }
    }

    
    

        
        