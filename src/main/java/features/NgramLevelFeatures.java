package features;

import medicalconcept.Util;

import org.clulab.processors.Sentence;

public class NgramLevelFeatures {

	public String getNgramLevelFeatures(String[] wholeString, String[] ngram, String ngramNesplit) {
		String result = "";
		result += wordLength(ngramNesplit);
		result += upperCaseAtBeginning(ngramNesplit);
		result += isAllUpperCase(ngramNesplit);
		result += isMixedCase(ngramNesplit);
		result += containsAmpersand(ngramNesplit);
		result += containsComma(ngramNesplit);
		result += containsDots(ngramNesplit);
		result += containsGreaterSign(ngramNesplit);
		result += containsLessSign(ngramNesplit);
		result += containsMinus(ngramNesplit);
		result += containsParanthesis(ngramNesplit);
		result += containsQuotedMark(ngramNesplit);
		result += containsPercent(ngramNesplit);
		result += containsSlash(ngramNesplit);
		result += containsDigits(ngramNesplit);
		result += containsSpecialCharBefore(wholeString, ngram);
		result += containsSpecialCharBefBef(wholeString, ngram);
		result += containsSpecialCharBefBefBef(wholeString, ngram);
		//result += containsSpecialCharAfter(index, sentence);
		//result += containsSpecialCharAftAft(index, sentence);
		//result += containsSpecialCharAftAftAft(index, sentence);
		return result;
	}
	
	private String wordLength(String str) {

		return str.length() + "\t";
	}
	
	private String isAllUpperCase(String str) {
		
		return str.equals(str.toUpperCase()) + "\t";
	}
	
	private String upperCaseAtBeginning(String str) {
		
		Boolean isUp = Character.isUpperCase(str.charAt(0));
		return isUp + "\t";
	}
	
	private String isMixedCase(String str) {

		return !(str.equals(str.toUpperCase()) 
					|| str.equals(str.toLowerCase()))
						  + "\t";
	}
	
	private String containsAmpersand(String str) {
		
		return str.contains("&") + "\t";
	}
	
	private String containsMinus(String str) {

		return str.contains("-") + "\t";
	}
	
	private String containsDots(String str) {

		return str.contains(".") + "\t";
	}
	
	private String containsComma(String str) {
		
		return str.contains(",") + "\t";
	}
	
	private String containsQuotedMark(String str) {

		return str.contains("'") + "\t";
	}
	
	private String containsSlash(String str) {

		return str.contains("/") + "\t";
	}
	
	private String containsParanthesis(String str) {

		return str.contains("(") + "\t";
	}
	
	private String containsGreaterSign(String str) {
		
		return str.contains(">") + "\t";
	}
	
	private String containsLessSign(String str) {

		return str.contains("<") + "\t";
	}
	
	private String containsPercent(String str) {

		return str.contains("%") + "\t";
	}
	
	private String containsDigits(String str) {
		
		return str.matches(".*\\d+.*") + "\t";
	}
	
	private String containsSpecialCharBefore(String[] whole, String[] ngrams) {
		String wordBefore = getWordBeforeOrAfter(whole, ngrams, 1, "before");
		
		if(wordBefore.equals("")) {
			return "false" + "\t";
		} else {
			String splitToken[] = wordBefore.split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (Util.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
		}
		
		return "false" + "\t";
	}
	
	private String containsSpecialCharBefBef(String[] whole, String[] ngrams) {
		String wordBefore = getWordBeforeOrAfter(whole, ngrams, 2, "before");
		
		if(wordBefore.equals("")) {
			return "false" + "\t";
		} else {
			String splitToken[] = wordBefore.split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (Util.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
		}
		
		return "false" + "\t";
	}
	
	private String containsSpecialCharBefBefBef(String[] whole, String[] ngrams) {
		if(whole.length > 4) {
			String wordBefore = getWordBeforeOrAfter(whole, ngrams, 3, "before");
			
			if(wordBefore.equals("")) {
				return "false" + "\t";
			} else {
				String splitToken[] = wordBefore.split("");
				for (int i = 0; i < splitToken.length; i++) {
					if (Util.SPECIAL_CHARS.contains(splitToken[i])) {
						return "true" + "\t";
					}
				}
			}
		} 
		return "false" + "\t";
	}
	
	private String containsSpecialCharAfter(int x, Sentence sentence) {

		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x+1].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (Util.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	private String containsSpecialCharAftAft(int x, Sentence sentence) {

		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1 || x == token.length-2) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x+2].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (Util.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	private String containsSpecialCharAftAftAft(int x, Sentence sentence) {
		
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1 || x == token.length-2 || x == token.length-3) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x+3].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (Util.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	private String getWordBeforeOrAfter(String[] whole, String[] ngrams, int pos, String method) {
		String result = "";
		int count = 0;
		Boolean notFound = true;
		if(method.equals("after")) {
			String last = ngrams[ngrams.length-1];
			String prelast = ngrams[ngrams.length-2];
			notFound = true;
			count = -1;
			for(int i=0; i<whole.length-1 && notFound; i++) {
				if(whole[i].equals(prelast) && whole[i+1].equals(last)){
					notFound = false;
				}
				count++;
			}
			if(count < (whole.length-2 - (pos-1)))
				result = whole[count+2];
			
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
				result = whole[count-1];
			}
		}
		
		return result;
	}
	
}
