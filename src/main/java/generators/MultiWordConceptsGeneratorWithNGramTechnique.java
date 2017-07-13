package generators;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import medicalconcept.Concept;
import net.didion.jwnl.JWNLException;

import org.clulab.processors.Document;
import org.clulab.processors.Sentence;

import utils.GeneralUtils;
import features.MultiWordConceptContextualFeatures;
import features.MultiWordConceptLevelFeatures;
import features.MultiWordConceptSintacticFeatures;

/**
 * multiword concept generation and feature computation
 * @author Ariana
 *
 */
public class MultiWordConceptsGeneratorWithNGramTechnique {

	public static List<String> ngrams(int n, String str) {
		List<String> ngrams = new ArrayList<String>();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i + n));
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
		for (String token : str) {
			if (!token.equals(".")) {
				res += token + " ";

			}
		}
		return res;
	}

	public List<String> generateNGramFeatures(Sentence sentence, Document docWhole, List<Concept> conObjects, List<String> lemmaFeature)
			throws FileNotFoundException, JWNLException { 

		List<String> ngramFeature = new ArrayList<String>();
		
		String[] tokens = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		String[] pos = sentence.tags().get();
		String[] lemmas = sentence.lemmas().get();
		String[] chunks = sentence.chunks().get();

		if (tokens.length > 3) {
			MultiWordConceptLevelFeatures nglf = new MultiWordConceptLevelFeatures();
			MultiWordConceptSintacticFeatures ngsf = new MultiWordConceptSintacticFeatures();
			MultiWordConceptContextualFeatures ngcf = new MultiWordConceptContextualFeatures();

			String features = "";

			String tokensString = makeString(tokens);
			String posString = makeString(pos);
			String lemmaString = makeString(lemmas);
			String chunkString = makeString(chunks);
			
			int ngramMaxLength = tokens.length - 2;
			if(ngramMaxLength > 5) {
				ngramMaxLength = 5;
			}
			
			for (int n = 2; n <= ngramMaxLength; n++) {
				List<String> ngrams = ngrams(n, tokensString);
				List<String> posgrams = ngrams(n, posString);
				List<String> lemgrams = ngrams(n, lemmaString);
				List<String> chunkgrams = ngrams(n, chunkString);
				int ngramsize = ngrams.size();
				int possize = posgrams.size();
				if(ngramsize != possize) {
					System.out.println(ngrams+"|"+ngramsize+"|pos:"+possize);
					System.out.println(posgrams);
				    if (tokensString.charAt(tokensString.length()-2)=='?') {
				    	posString = posString + " ?";
				    }
				    if (tokensString.charAt(tokensString.length()-2)=='!') {
				    	posString = posString + " !";
				    }
				    posgrams = ngrams(n, posString);
				}
				
				for (int i = 0; i < ngramsize; i++) {
					if (ngramToTake(posgrams.get(i), ngrams.get(i))) {
						features = "";
						
						features = nglf.getNgramLevelFeatures(tokens, ngrams.get(i).split("\\s"), ngrams.get(i));
						features += ngsf.getNgramSintacticFeatures(tokens, pos,
								ngrams.get(i).split("\\s"), posgrams.get(i).split("\\s"),chunkgrams.get(i).split("\\s"));
						features += ngcf.getNgramContextualFeatures(tokens,ngrams.get(i).split("\\s"), ngrams.get(i),docWhole);
						
	            		//adaugare features lemma cuvintelor
	            		features = GeneralUtils.setLemmaFeatureForNgrams(lemmaFeature, features, lemgrams.get(i).split("\\s"));
	            		
	            		//adaugare categorie
	            		features = GeneralUtils.setCategory(conObjects, features, ngrams.get(i));
	            		
						ngramFeature.add(features);
					}

				}
			}
		}
		
		return ngramFeature;

	}
	
	/**
	 * concept identification rules
	 * 
	 * @param posgram
	 * @param ngrams
	 * @return
	 */
	private static Boolean ngramToTake(String posgram, String ngrams) {
		String[] pos = posgram.split("\\s");
		String[] ngram = ngrams.split("\\s");
		
		if (pos[0].toUpperCase().equals("CC")
				|| pos[0].toUpperCase().equals("CD")
				|| pos[0].toUpperCase().equals("EX")
				|| pos[0].toUpperCase().equals("IN")
				|| pos[0].toUpperCase().equals("-LRB-")
				|| pos[0].toUpperCase().equals("-RRB-")
				|| pos[0].toUpperCase().equals("LS")
				|| pos[0].toUpperCase().equals("PP")
				|| pos[0].toUpperCase().equals("WDT")
				|| pos[0].toUpperCase().equals("WP$")
				|| pos[0].toUpperCase().equals("WP")
				|| pos[0].toUpperCase().equals("WRB")
				|| pos[0].toUpperCase().equals("TO")
				|| pos[0].toUpperCase().equals("UH")
				|| GeneralUtils.SPECIAL_CHARS.contains(ngram[0])) {
			return false;
		}
		
		if (GeneralUtils.SPECIAL_CHARS.contains(ngram[ngram.length - 1])
				|| pos[pos.length - 1].toUpperCase().equals("-LRB-")
				|| pos[pos.length - 1].toUpperCase().equals("-RRB-")) {
			return false;
		}
		
		boolean lrb = false;
		boolean rrb = false;
		for(int i=0; i<pos.length; i++) {
			if(pos[i].toUpperCase().equals("-LRB-")){
				lrb = true;
			}
			if(pos[i].toUpperCase().equals("-RRB-")){
				rrb = true;
			}
		}
		if((lrb == true && rrb == false) || (rrb == true && lrb == false)) {
			return false;
		}
		
		for(int i=0; i<ngram.length; i++) {
			if(ngram[0].matches("\\d{4}-\\d{2}-\\d{2}")){
				return false;
			}
		}
		return true;
	}
}
