package medicalconcept;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;

import org.clulab.processors.Sentence;
import org.clulab.processors.Document;

public class ContextualFeatures {

	public void showContextualFeatures(Document doc, Document[] docs) throws FileNotFoundException, JWNLException {
		for (Sentence sentence : doc.sentences()) {
			String[] result = Util.mkString(sentence.words(), " ").split("\\s");
			for (int x = 0; x < result.length; x++) {
				System.out.print(result[x] + "\t\t"
						+ isSecondNeighborMG(x, result) + "\t\t"
						+ hasCommaBeforeOrAfter(x, result) + "\t\t"
						+ result[x].length()+ "\t\t"
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
		if (pos == result.length - 1 && result[pos - 1].equals(",")) {
			return true;
		} else if (pos == 0 && result[pos + 1].equals(",")) {
			return true;
		} else if (pos != result.length - 1 && pos != 0
				&& (result[pos + 1].equals(",") || result[pos - 1].equals(","))) {
			return true;
		} else {
			return false;
		}
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
		//System.out.println("Counter="+counter);
		//System.out.println("Doc length="+docLength);
		
		if(docLength > 0) {
			return (double)counter/docLength;
		} else return 0;

	}

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

	public int getNrOfDefinitionsInWordNet(String str)
			throws FileNotFoundException, JWNLException {
		JWNL.initialize(new FileInputStream(
				"E:/An4/Licenta/workspace/properties.xml"));
		Dictionary dictionary = Dictionary.getInstance();
		IndexWord word = dictionary.lookupIndexWord(POS.NOUN, str);
		if (word!=null) {
			Synset[] senses = word.getSenses();
			return senses.length;
		} else return 0;
	}

	
}
