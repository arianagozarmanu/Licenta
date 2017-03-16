package medicalconcept;

import org.clulab.processors.*;

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
	
	static final String specialCharacters = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~";
	
	public void upperCaseAtBeginning(String str) {
		
		Boolean isUp = Character.isUpperCase(str.charAt(0));
		System.out.print(isUp + "\t");
	}
	
	public void showWordLevelFeatures(Document doc) {
		
		for (Sentence sentence : doc.sentences()) {
			String token = Util.mkString(sentence.words(), " ");
			String[] result = token.split("\\s");
			for (int x=0; x<result.length; x++) {
				System.out.print(result[x] + "\t\t");
				upperCaseAtBeginning(result[x]);
				wordLength(result[x]);
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
				System.out.println();
			}
		}	
	}
	
	public void wordLength(String str) {

		System.out.print(str.length() + "\t");
	}
	
	public void isAllUpperCase(String str) {
		
				System.out.print(str.equals(str.toUpperCase()) + "\t");
	}
	
	public void isMixedCase(String str) {

		System.out.print(!(str.equals(str.toUpperCase()) 
							|| str.equals(str.toLowerCase()))
						    + "\t");
	}
	
	public void containsAmpersand(String str) {
		
		System.out.print(str.contains("&") + "\t");
	}
	
	public void containsMinus(String str) {

		System.out.print(str.contains("-") + "\t");
	}
	
	public void containsDots(String str) {

		System.out.print(str.contains(".") + "\t");
	}
	
	public void containsComma(String str) {
		
		System.out.print(str.contains(",") + "\t");
	}
	
	public void containsQuotedMark(String str) {

		System.out.print(str.contains("'") + "\t");
	}
	
	public void containsSlash(String str) {

		System.out.print(str.contains("/") + "\t");
	}
	
	public void containsParanthesis(String str) {

		System.out.print(str.contains("(") + "\t");
	}
	
	public void containsGreaterSign(String str) {
		
		System.out.print(str.contains(">") + "\t");
	}
	
	public void containsLessSign(String str) {

		System.out.print(str.contains("<") + "\t");
	}
	
	public void containsPercent(String str) {

		System.out.print(str.contains("%") + "\t");
	}
	
	public void containsDigits(String str) {
		
		System.out.print(str.matches(".*\\d+.*") + "\t");
	}
	
	public void containsSpecialCharBefore(int x, Sentence sentence) {
		Boolean notSC = true;
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == 0) {
			System.out.print("false" + "\t");
		} else {
			String splitToken[] = token[x-1].split("");
			for (int i = 0; i < splitToken.length && notSC; i++) {
				if (specialCharacters.contains(splitToken[i])) {
					System.out.print("true" + "\t");
					notSC = false;
				}
			}
			
			if( notSC ) {
				System.out.print("false" + "\t");
			}
		}
	}
	
	public void containsSpecialCharBefBef(int x, Sentence sentence) {
		Boolean notSC = true;
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == 0 || x == 1) {
			System.out.print("false" + "\t");
		} else {
			String splitToken[] = token[x-2].split("");
			for (int i = 0; i < splitToken.length && notSC; i++) {
				if (specialCharacters.contains(splitToken[i])) {
					System.out.print("true" + "\t");
					notSC = false;
				}
			}
			
			if( notSC ) {
				System.out.print("false" + "\t");
			}
		}
	}
	
	public void containsSpecialCharBefBefBef(int x, Sentence sentence) {
		Boolean notSC = true;
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == 0 || x == 1 || x == 2) {
			System.out.print("false" + "\t");
		} else {
			String splitToken[] = token[x-3].split("");
			for (int i = 0; i < splitToken.length && notSC; i++) {
				if (specialCharacters.contains(splitToken[i])) {
					System.out.print("true" + "\t");
					notSC = false;
				}
			}
			
			if( notSC ) {
				System.out.print("false" + "\t");
			}
		}
	}
	
	public void containsSpecialCharAfter(int x, Sentence sentence) {
		Boolean notSC = true;
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1) {
			System.out.print("false" + "\t");
		} else {
			String splitToken[] = token[x+1].split("");
			for (int i = 0; i < splitToken.length && notSC; i++) {
				if (specialCharacters.contains(splitToken[i])) {
					System.out.print("true" + "\t");
					notSC = false;
				}
			}
			
			if( notSC ) {
				System.out.print("false" + "\t");
			}
		}
	}
	
	public void containsSpecialCharAftAft(int x, Sentence sentence) {
		Boolean notSC = true;
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1 || x == token.length-2) {
			System.out.print("false" + "\t");
		} else {
			String splitToken[] = token[x+2].split("");
			for (int i = 0; i < splitToken.length && notSC; i++) {
				if (specialCharacters.contains(splitToken[i])) {
					System.out.print("true" + "\t");
					notSC = false;
				}
			}
			
			if( notSC ) {
				System.out.print("false" + "\t");
			}
		}
	}
	
	public void containsSpecialCharAftAftAft(int x, Sentence sentence) {
		Boolean notSC = true;
		String[] token = Util.mkString(sentence.words(), " ").split("\\s");
		if( x == token.length-1 || x == token.length-2 || x == token.length-3) {
			System.out.print("false" + "\t");
		} else {
			String splitToken[] = token[x+3].split("");
			for (int i = 0; i < splitToken.length && notSC; i++) {
				if (specialCharacters.contains(splitToken[i])) {
					System.out.print("true" + "\t");
					notSC = false;
				}
			}
			
			if( notSC ) {
				System.out.print("false" + "\t");
			}
		}
	}
	
	
	
	
	
}

