package features;

import Utils.Util;


public class NgramLevelFeatures {

	public String getNgramLevelFeatures(String[] wholeString, String[] ngram, String ngramNesplit) {
		String result = "";
		//result += Util.concatenateWithUnderscore(ngram) + "\t";
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
		result += containsSpecialCharAfter(wholeString, ngram);
		result += containsSpecialCharAftAft(wholeString, ngram);
		result += containsSpecialCharAftAftAft(wholeString, ngram);
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
		//System.out.println("Before:"+wordBefore);
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
		//System.out.println("BeforeB:"+wordBefore);
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
			//System.out.println("BeforeBB:"+wordBefore);
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
	
	private String containsSpecialCharAfter(String[] whole, String[] ngrams) {
		String wordBefore = getWordBeforeOrAfter(whole, ngrams, 1, "after");
		//System.out.println("After:"+wordBefore);
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
	
	private String containsSpecialCharAftAft(String[] whole, String[] ngrams) {
		String wordBefore = getWordBeforeOrAfter(whole, ngrams, 2, "after");
		//System.out.println("AfterB:"+wordBefore);
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
	
	private String containsSpecialCharAftAftAft(String[] whole, String[] ngrams) {
		if(whole.length > 4) {
			String wordBefore = getWordBeforeOrAfter(whole, ngrams, 3, "after");
			//System.out.println("AfterBB:"+wordBefore);
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
