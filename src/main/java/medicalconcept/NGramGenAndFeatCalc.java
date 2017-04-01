package medicalconcept;

import java.util.ArrayList;
import java.util.List;

import org.clulab.processors.Document;
import org.clulab.processors.Processor;
import org.clulab.processors.Sentence;
import org.clulab.processors.corenlp.CoreNLPProcessor;

import features.NgramLevelFeatures;

public class NGramGenAndFeatCalc {

	  public static List<String> ngrams(int n, String str) {
		  List<String> ngrams = new ArrayList<String>();
		  String[] words = str.split(" ");
		  for (int i = 0; i < words.length - n + 1; i++)
			  ngrams.add(concat(words, i, i+n));
          return ngrams;
	  }

	    public static String concat(String[] words, int start, int end) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = start; i < end; i++)
	            sb.append((i > start ? " " : "") + words[i]);
	        return sb.toString();
	    }

	    public static String makeString(String[] str) {
	    	String res = "";
	    	for(String token: str) {
    			if(!token.equals(".")) {
    				res += token + " ";

    			}
    		}
	    	return res;
	    }
	    
	    //se primeste cate o propozitie din Main de fiecare data
	    public static void main(String[] args) {
	    	Processor proc = new CoreNLPProcessor(true, true, 0, 100);
	    	String str= "The descending thoracic aorta is broken x3.";
	    	String[] tokens = null;
	    	String[] pos = null;
	    	String[] lemmas = null;
	    	Document doc = proc.annotate(str, false); //unele linii din fisier au cate 2 sau mai multe propozitii
	    	for (Sentence sentence : doc.sentences()) {	
	    		tokens = Util.mkString(sentence.words(), " ").split("\\s");
        		pos = sentence.tags().get();
        		lemmas = sentence.lemmas().get();
	    	}
	    	//verificam daca se pot face n-grame mai mari ca 1, ADICA
	    	//trebuie sa  fie o propozitie macar din 4 cuvinte DEOARECE
	    	//daca e noun-verb-noun
        	if(tokens.length > 3) {	
        		NgramLevelFeatures nglf = new NgramLevelFeatures();
        		String features="";
            	List<String> ngramRez = new ArrayList<String>();
            	List<String> ngramFeature = new ArrayList<String>();
            	
	    		String tokensString = makeString(tokens);
	    		String posString = makeString(pos);
	    		String lemmaString = makeString(lemmas);
	    		  		
		        for (int n = 2; n <= tokens.length-2; n++) {
		        	List<String> ngrams = ngrams(n, tokensString);
		        	List<String> posgrams = ngrams(n, posString);
		        	List<String> lemgrams = ngrams(n, lemmaString);
		            for (int i=0; i<ngrams.size(); i++) {  
		            	//ngrams.get(i) e propozitia intreaga
		            	features = nglf.getNgramLevelFeatures(tokens, ngrams.get(i).split("\\s"), ngrams.get(i));
		            	ngramRez.add(ngrams.get(i));
		            	ngramFeature.add(features);
		            	System.out.println(ngrams.get(i)+"|"+features);
		            	//System.out.println(ngrams.get(i));
		                //System.out.println(posgrams.get(i));
		                //System.out.println(lemgrams.get(i));
		                
		            }
		            System.out.println();
		        }
        	}
        	
	    }
}
