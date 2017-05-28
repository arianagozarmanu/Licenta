package features;

import org.clulab.processors.*;

import utils.GeneralUtils;

/**
 * Methods:
 * UpperCase At Beginning, All, Mixed Case
 * Word length
 * Contains & , . > < - ( ' % / digits
 * 2 Neighbors before and after contains special character 
 * @author Ariana
 *
 */
public class WordLevelFeatures {
	
	public String getWordLevelFeatures(int index, String token, Sentence sentence) {
		String result = "";
		//result += token + "\t";
		result += wordLength(token);
		result += upperCaseAtBeginning(token);
		result += isAllUpperCase(token);
		result += isMixedCase(token);
		result += containsAmpersand(token);
		result += containsComma(token);
		result += containsDots(token);
		result += containsGreaterSign(token);
		result += containsLessSign(token);
		result += containsMinus(token);
		result += containsParanthesis(token);
		result += containsQuotedMark(token);
		result += containsPercent(token);
		result += containsSlash(token);
		result += containsDigits(token);
		result += containsSpecialCharBefore(index, sentence);
		result += containsSpecialCharBefBef(index, sentence);
		result += containsSpecialCharBefBefBef(index, sentence);
		result += containsSpecialCharAfter(index, sentence);
		result += containsSpecialCharAftAft(index, sentence);
		result += containsSpecialCharAftAftAft(index, sentence);
		return result;
	}
	
	public void showWordLevelFeatures(Document doc) {
		
		for (Sentence sentence : doc.sentences()) {
			String token = GeneralUtils.mkString(sentence.words(), " ");
			String[] result = token.split("\\s");
			for (int x=0; x<result.length; x++) {
				System.out.print(result[x] + "\t");
				wordLength(result[x]);
				upperCaseAtBeginning(result[x]);
				isAllUpperCase(result[x]);
				isMixedCase(result[x]);
				containsAmpersand(result[x]);
				containsComma(result[x]);
				containsDots(result[x]);
				containsGreaterSign(result[x]);
				containsLessSign(result[x]);
				containsMinus(result[x]);
				containsParanthesis(result[x]);
				containsQuotedMark(result[x]);
				containsPercent(result[x]);
				containsSlash(result[x]);
				containsDigits(result[x]);
				containsSpecialCharBefore(x, sentence);
				containsSpecialCharBefBef(x, sentence);
				containsSpecialCharBefBefBef(x, sentence);
				containsSpecialCharAfter(x, sentence);
				containsSpecialCharAftAft(x, sentence);
				containsSpecialCharAftAftAft(x, sentence);
			}
		}	
	}
	
	public String wordLength(String str) {

		return str.length() + "\t";
	}
	
	public String isAllUpperCase(String str) {
		
		return str.equals(str.toUpperCase()) + "\t";
	}
	
	public String upperCaseAtBeginning(String str) {
		
		Boolean isUp = Character.isUpperCase(str.charAt(0));
		return isUp + "\t";
	}
	
	public String isMixedCase(String str) {

		return !(str.equals(str.toUpperCase()) 
					|| str.equals(str.toLowerCase()))
						  + "\t";
	}
	
	public String containsAmpersand(String str) {
		
		return str.contains("&") + "\t";
	}
	
	public String containsMinus(String str) {

		return str.contains("-") + "\t";
	}
	
	public String containsDots(String str) {

		return str.contains(".") + "\t";
	}
	
	public String containsComma(String str) {
		
		return str.contains(",") + "\t";
	}
	
	public String containsQuotedMark(String str) {

		return str.contains("'") + "\t";
	}
	
	public String containsSlash(String str) {

		return str.contains("/") + "\t";
	}
	
	public String containsParanthesis(String str) {

		return str.contains("(") + "\t";
	}
	
	public String containsGreaterSign(String str) {
		
		return str.contains(">") + "\t";
	}
	
	public String containsLessSign(String str) {

		return str.contains("<") + "\t";
	}
	
	public String containsPercent(String str) {

		return str.contains("%") + "\t";
	}
	
	public String containsDigits(String str) {
		
		return str.matches(".*\\d+.*") + "\t";
	}
	
	public String containsSpecialCharBefore(int x, Sentence sentence) {

		String[] token = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		if( x == 0) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x-1].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (GeneralUtils.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	public String containsSpecialCharBefBef(int x, Sentence sentence) {
		String[] token = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		if( x == 0 || x == 1) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x-2].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (GeneralUtils.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";

		}
	}
	
	public String containsSpecialCharBefBefBef(int x, Sentence sentence) {

		String[] token = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		if( x == 0 || x == 1 || x == 2) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x-3].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (GeneralUtils.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	public String containsSpecialCharAfter(int x, Sentence sentence) {

		String[] token = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x+1].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (GeneralUtils.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	public String containsSpecialCharAftAft(int x, Sentence sentence) {

		String[] token = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1 || x == token.length-2) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x+2].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (GeneralUtils.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}
	
	public String containsSpecialCharAftAftAft(int x, Sentence sentence) {
		
		String[] token = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1 || x == token.length-2 || x == token.length-3) {
			return "false" + "\t";
		} else {
			String splitToken[] = token[x+3].split("");
			for (int i = 0; i < splitToken.length; i++) {
				if (GeneralUtils.SPECIAL_CHARS.contains(splitToken[i])) {
					return "true" + "\t";
				}
			}
			
			return "false" + "\t";
		}
	}	
	
}

