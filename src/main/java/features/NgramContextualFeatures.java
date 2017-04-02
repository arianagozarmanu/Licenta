package features;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import medicalconcept.Util;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

import org.clulab.processors.Document;
import org.clulab.processors.Sentence;

public class NgramContextualFeatures {

	public String getNgramContextualFeatures(String[] wholeString, String[] ngrams, String ngram, Document doc) throws FileNotFoundException, JWNLException {
		String result = "";
		result = isSecondNeighborMG(wholeString, ngrams) + "\t"
				+ hasCommaBeforeOrAfter(wholeString, ngrams) + "\t"
				+ getNrOfDefinitionsInWordNet(ngrams) + "\t"
				+ getTermFrequency(ngram, doc, ngrams.length);
		return result;
	
	}
	
	public Boolean isSecondNeighborMG(String[] wholeString, String[] ngrams) {
		String mg = getWordBeforeOrAfter(wholeString, ngrams, 2, "after");
		if(mg.toLowerCase().equals("mg")) {
			return true;
		} else return false;
	}

	public Boolean hasCommaBeforeOrAfter(String[] wholeString, String[] ngrams) {
		String commaB = getWordBeforeOrAfter(wholeString, ngrams, 1, "before");
		String commaA = getWordBeforeOrAfter(wholeString, ngrams, 1, "after");
		if(commaB.equals(",") || commaA.equals(",")) {
			return true;
		} else return false;
	}
	
	//de cate ori apare n-gram-ul impartit la (nr total de cuvinte / lungime ngram in cuvinte)
	public double getTermFrequency(String word, Document doc, int ngramWordSize) {
		int counter = 0;
		int docLength = 0;
		String result = "";
		for (Sentence sentence : doc.sentences()) {
			result = Util.mkString(sentence.words(), " ");
			if (result.toLowerCase().contains(word)) {
				counter++;
			}
			docLength += result.split("\\s").length;
		}
		
		String resultDouble = String.format("%.5f", (double)counter/(docLength/ngramWordSize));
		return Double.valueOf(resultDouble);
	}

	public int getNrOfDefinitionsInWordNet(String[] ngrams)
			throws FileNotFoundException, JWNLException {
		JWNL.initialize(new FileInputStream(
				"E:/An4/Licenta/workspace/properties.xml"));
		Dictionary dictionary = Dictionary.getInstance();
		int sumDef = 0;
		for(int i=0; i<ngrams.length; i++) {
			IndexWord wordN = dictionary.lookupIndexWord(POS.NOUN, ngrams[i]);
			IndexWord wordV = dictionary.lookupIndexWord(POS.VERB, ngrams[i]);
			IndexWord wordAV = dictionary.lookupIndexWord(POS.ADVERB, ngrams[i]);
			IndexWord wordAJ = dictionary.lookupIndexWord(POS.ADJECTIVE, ngrams[i]);
			
			int total = 0;
			Synset[] senses;
			
			if (wordN!=null) {
				senses = wordN.getSenses();
				total += senses.length;
			}
			if (wordV!=null) {
				senses = wordV.getSenses();
				total += senses.length;
			}
			if (wordAV!=null) {
				senses = wordAV.getSenses();
				total += senses.length;
			}
			if (wordAJ!=null) {
				senses = wordAJ.getSenses();
				total += senses.length;
			} 
			
			sumDef += total;
		}
		return sumDef;
	}
	
	private String getWordBeforeOrAfter(String[] whole, String[] ngrams, int pos, String method) {
		String result = "";
		int count = 0;
		Boolean notFound = true;
		if(method.equals("after")) {
			String last = ngrams[ngrams.length-1];
			String prelast = ngrams[ngrams.length-2];
			notFound = true;
			count = 0;
			for(int i=1; i<whole.length && notFound; i++) {
				if(whole[i-1].equals(prelast) && whole[i].equals(last)){
					notFound = false;
				}
				count++;
			}
			if(count < (whole.length-1-pos))
				result = whole[count+pos];
			
		} else {
			String first = ngrams[0];
			String second = ngrams[1];
			notFound = true;
			count = -1;
			for(int i=0; i<whole.length-1 && notFound; i++) {
				if(whole[i].equals(first) && whole[i+1].equals(second)){
					notFound = false;
				}
				count++;
			}
			if(count > (0 + (pos-1))) {
				result = whole[count-pos];
			}
		}
		
		return result;
	}
}
