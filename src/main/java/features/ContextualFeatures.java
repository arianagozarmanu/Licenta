package features;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

import medicalconcept.Util;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

import org.clulab.processors.Sentence;
import org.clulab.processors.Document;

public class ContextualFeatures {

	public String getContextualFeatures(int index, String token, Sentence sentence, Document doc) throws FileNotFoundException, JWNLException {
		String result = "";
		String[] tokens = Util.mkString(sentence.words(), " ").split("\\s");
		result = isSecondNeighborMG(index, tokens) + "\t"
				+ hasCommaBeforeOrAfter(index, tokens) + "\t"
				+ getNrOfDefinitionsInWordNet(token) + "\t"
				+ getTermFrequency(token, doc);
		return result;
	
	}
	
	public void showContextualFeatures(Document doc, Document[] docs) throws FileNotFoundException, JWNLException {
		for (Sentence sentence : doc.sentences()) {
			String[] result = Util.mkString(sentence.words(), " ").split("\\s");
			for (int x = 0; x < result.length; x++) {
				System.out.print(result[x] + "\t\t"
						+ isSecondNeighborMG(x, result) + "\t\t"
						+ hasCommaBeforeOrAfter(x, result) + "\t\t"
						+ getNrOfDefinitionsInWordNet(result[x]) + "\t\t"
						+ getTFIDF(getTermFrequency(result[x], doc), getInverseDocFrequency(result[x], docs)) + "\t\t"
						+ getSmoothTFIDF(getTermFrequency(result[x], doc), getInverseDocFrequency(result[x], docs)));
				System.out.println();
			}
		}
	}

	public Boolean isSecondNeighborMG(int pos, String[] result) {
		if (pos == result.length - 1 || pos == result.length - 2) {
			return false;
		} else {
			String neigh = result[pos + 2];
			if (neigh.toLowerCase().equals("mg")) {
				return true;
			} else
				return false;
		}
	}

	public Boolean hasCommaBeforeOrAfter(int pos, String[] result) {
		if(result.length > 1) {
			if (pos == 0 && result[pos + 1].equals(",")) {
				return true;
			} else if (pos == result.length - 1 && result[pos - 1].equals(",")) {
				return true;
			} else if (pos != result.length - 1 && pos != 0
					&& (result[pos + 1].equals(",") || result[pos - 1].equals(","))) {
				return true;
			} else {
				return false;
			}
		} else return false;
	}

	public double getTermFrequency(String word, Document doc) {
		int counter = 0;
		int docLength = 0;
		String[] result = null;
		for (Sentence sentence : doc.sentences()) {
			result = Util.mkString(sentence.words(), " ").split("\\s");
			for (int x = 0; x < result.length; x++) {
				if (word.toLowerCase().equals(result[x].toLowerCase())) {
					counter++;
				}
			}
			docLength += result.length;
		}
		
		String resultDouble = String.format("%.5f", (double)counter/docLength);
		if(docLength > 0) {
			return Double.valueOf(resultDouble);
		} else return 0;

	}

	public int getNrOfDefinitionsInWordNet(String str)
			throws FileNotFoundException, JWNLException {
		JWNL.initialize(new FileInputStream(
				"E:/An4/Licenta/workspace/properties.xml"));
		Dictionary dictionary = Dictionary.getInstance();
		IndexWord wordN = dictionary.lookupIndexWord(POS.NOUN, str);
		IndexWord wordV = dictionary.lookupIndexWord(POS.VERB, str);
		IndexWord wordAV = dictionary.lookupIndexWord(POS.ADVERB, str);
		IndexWord wordAJ = dictionary.lookupIndexWord(POS.ADJECTIVE, str);
		
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
		
		return total;
	}
	
	//Unused methods - IDF si TFIDF
	public double getInverseDocFrequency(String word, Document[] docs) {
		int counter = 0;
		Boolean isNotInDoc = true;
		for (Document doc : docs) {
			isNotInDoc = true;
			for (Sentence sentence : doc.sentences()) {
				String[] result = Util.mkString(sentence.words(), " ").split(
						"\\s");
				for (int x = 0; x < result.length && isNotInDoc; x++) {
					if (word.toLowerCase().equals(result[x].toLowerCase())) {
						counter++;
						isNotInDoc = false;
					}
				}
				if (!isNotInDoc) {
					break;
				}
			}
		}
		//System.out.println("CounterB="+counter);
		//System.out.println("DocsLength="+docs.length);
		
		if(counter > 0) {
			return (double)docs.length / counter;
		} else return 0;

	}

	public double getTFIDF(double tf, double idf) {
		return tf * Math.log(idf);
	}

	public double getSmoothTFIDF(double tf, double idf) {
		return tf * Math.log(1 + idf);
	}

	
}
