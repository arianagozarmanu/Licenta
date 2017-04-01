package features;

import medicalconcept.Util;

import org.clulab.processors.Document;
import org.clulab.processors.Sentence;
/**
 * Methods: 
 * - POS for current and 2 neighbours before and after
 * - Chunk
 * @author Ariana
 *
 */
public class SintacticFeatures {
	
	public String getSintacticFeatures(int index, Sentence sentence) {
		String result = "";
		String[] resPOS = getCurrentPOS(sentence).split("\\s");
		String[] resChunk = getChunk(sentence).split("\\s");
		result = resPOS[index] + "\t"
				+ resChunk[index] + "\t"
				+ getBeforePOS(index, sentence) + "\t"
				+ getBefBefPOS(index, sentence) + "\t"
				+ getBefBefBefPOS(index, sentence) + "\t"
				+ getAfterPOS(index, sentence) + "\t"
				+ getAftAftPOS(index, sentence) + "\t"
				+ getAftAftAftPOS(index, sentence) + "\t";
		return result;
	}
	
	public void showSintacticFeatures(Document doc) {
		
		for (Sentence sentence : doc.sentences()) {
			String token = Util.mkString(sentence.words(), " ");
			String[] result = token.split("\\s");
			String[] resPOS = getCurrentPOS(sentence).split("\\s");
			String[] resChunk = getChunk(sentence).split("\\s");		
			for (int x=0; x<result.length; x++) {
				System.out.print(result[x] + "\t"
								+ resPOS[x] + "\t"
								+ resChunk[x] + "\t\t"
								+ getBeforePOS(x, sentence) + "\t"
								+ getBefBefPOS(x, sentence) + "\t"
								+ getBefBefBefPOS(x, sentence) + "\t"
								+ getAfterPOS(x, sentence) + "\t"
								+ getAftAftPOS(x, sentence) + "\t"
								+ getAftAftAftPOS(x, sentence) + "\t");
			}
		}
	}
	
	public String getCurrentPOS(Sentence sentence) {
		return Util.mkString(sentence.tags().get()," ");
	}
	
	public String getChunk(Sentence sentence) {
		return Util.mkString(sentence.chunks().get(), " ");
	}
	
	//if is first element, POS is none
	public String getBeforePOS(int x, Sentence sentence) {
		String[] pos = Util.mkString(sentence.tags().get()," ").split("\\s");
		if(x == 0) {
			return "none";
		} else {
			return pos[x-1];
		}
	}
	
	//if is first element or second, POS is none
	public String getBefBefPOS(int x, Sentence sentence) {
		String[] pos = Util.mkString(sentence.tags().get()," ").split("\\s");
		if(x == 0 || x == 1) {
			return "none";
		} else {
			return pos[x-2];
		}
	}
	
	//if is first element, second or third, POS is none
	public String getBefBefBefPOS(int x, Sentence sentence) {
		String[] pos = Util.mkString(sentence.tags().get()," ").split("\\s");
		if(x == 0 || x == 1 || x == 2) {
			return "none";
		} else {
			return pos[x-3];
		}
	}
	
	//if is last element, POS is none
	public String getAfterPOS(int x, Sentence sentence) {
		String[] pos = Util.mkString(sentence.tags().get()," ").split("\\s");
		if(x == pos.length-1) {
			return "none";
		} else {
			return pos[x+1];
		}
	}
	
	//if is last element or pre-last, POS is none
	public String getAftAftPOS(int x, Sentence sentence) {
		String[] pos = Util.mkString(sentence.tags().get()," ").split("\\s");
		if(x == pos.length-1 || x == pos.length-2) {
			return "none";
		} else {
			return pos[x+2];
		}
	}
	
	//if is last element, pre-last or prepre-last, POS is none
	public String getAftAftAftPOS(int x, Sentence sentence) {
		String[] pos = Util.mkString(sentence.tags().get()," ").split("\\s");
		if(x == pos.length-1 || x == pos.length-2 || x == pos.length-3) {
			return "none";
		} else {
			return pos[x+3];
		}
	}
	
}

