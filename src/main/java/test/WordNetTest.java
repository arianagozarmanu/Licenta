package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;

/**
 * WordNet library functionalities test
 * @author Ariana
 *
 */
public class WordNetTest {
	
	public static void main(String[] args) throws FileNotFoundException, JWNLException {
		playWithWordNet();
	}

	public static void playWithWordNet() throws FileNotFoundException, JWNLException {
		JWNL.initialize(new FileInputStream(
				"E:/An4/Licenta/workspace/properties.xml"));
		Dictionary dictionary = Dictionary.getInstance();
		IndexWord word = dictionary.lookupIndexWord(POS.NOUN, "Coronary Artery Bypass Graft"); 
		System.out
				.println("Senses of the word 'Coronary Artery Bypass Graft':");
		if (word!=null) {
			Synset[] senses = word.getSenses();
			for (int i = 0; i < senses.length; i++) {
				Synset sense = senses[i];
				System.out.println((i + 1) + ". " + sense.getGloss());
				Pointer[] holo = sense.getPointers(PointerType.PART_HOLONYM);
				for (int j = 0; j < holo.length; j++) {
					Synset synset = (Synset) (holo[j].getTarget());
					Word synsetWord = synset.getWord(0);
					System.out.print("  -part-of-> " + synsetWord.getLemma());
					System.out.println(" = " + synset.getGloss());
				}
			}
		}
	}
}
