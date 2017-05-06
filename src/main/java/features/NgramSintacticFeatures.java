package features;

import Utils.Util;

public class NgramSintacticFeatures {

	public String getNgramSintacticFeatures(String[] wholeString, String[] wholePOS, String[] ngram, String[] pos, String[] chunk) {
		String result = "";
		result = Util.concatenateWithUnderscore(pos) + "\t"
				+ Util.concatenateWithUnderscore(chunk) + "\t"
				+ getPosBeforeOrAfter(wholeString, wholePOS, ngram, 1, "before") + "\t"
				+ getPosBeforeOrAfter(wholeString, wholePOS, ngram, 2, "before") + "\t"
				+ getPosBeforeOrAfter(wholeString, wholePOS, ngram, 3, "before") + "\t"
				+ getPosBeforeOrAfter(wholeString, wholePOS, ngram, 1, "after") + "\t"
				+ getPosBeforeOrAfter(wholeString, wholePOS, ngram, 2, "after") + "\t"
				+ getPosBeforeOrAfter(wholeString, wholePOS, ngram, 3, "after") + "\t";
		return result;
	}
	
	private String getPosBeforeOrAfter(String[] whole, String[] wholePOS, String[] ngrams, int pos, String method) {
		String result = "none";
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
				result = wholePOS[count+pos];
			
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
				result = wholePOS[count-pos];
			}
		}
		
		return result;
	}
}
