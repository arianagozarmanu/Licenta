package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

public class UtilFileManipulatorAndFileGenerator {
	
	//numarul randului pana unde sa se copieze
	private final static int LIMIT = 390472;
	
	public static void main(String[] args) throws IOException {
		//LIBLNR_TRAIN LIBLNR_TEST
		//getLemmaOutOfSparseMatrix(Util.LIBLNR_TRAIN, Util.LIBLNR_NO_LEMMA_TRAIN);
		//getLemmaOutOfSparseMatrix(Util.LIBLNR_TEST, Util.LIBLNR_NO_LEMMA_TEST);
		getElementsFromAFileMinusElementsFromAnother();
		//getShorterFeatureFileWithoutPOS();
	}

	
	public static void getShorterFeatureFileWithoutPOS() throws IOException {
		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(Util.SHORTER_FEATURES_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		HashSet<String> hs2POS = new HashSet<String>();
		hs2POS = readUnusedPOSFromFile(Util.POS2_UNUSED_MED_CONCEPTS);
		
		HashSet<String> hs3POS = new HashSet<String>();
		hs3POS = readUnusedPOSFromFile(Util.POS3_UNUSED_MED_CONCEPTS);
		
		HashSet<String> hs5POS = new HashSet<String>();
		hs5POS = readUnusedPOSFromFile(Util.POS5_UNUSED_MED_CONCEPTS);
		
		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				if (haveFilteredPOS(tokens, hs2POS, hs3POS, hs5POS)) {
					out.println(currentLine);
				}
			} else {
				out.println(currentLine);
			}
			count++;
		}
		br.close();
		out.close();
	}
	
	public static void getElementsFromAFileMinusElementsFromAnother() throws IOException {
		HashSet<String> hs = new HashSet<String>();
		
		FileWriter fw = new FileWriter(Util.CHNK1_UNUSED_MED_CONCEPTS, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		FileReader fr1 = new FileReader(Util.CHUNKS_USED_FOR_MED_CONC);
		BufferedReader br1 = new BufferedReader(fr1);
		String curr1;
		while ((curr1 = br1.readLine()) != null) {
			hs.add(curr1);
		}
		br1.close();
		
		FileReader fr2 = new FileReader(Util.CHUNKS_USED_FOR_NONMED_CONC);
		BufferedReader br2 = new BufferedReader(fr2);
		String curr2;
		while ((curr2 = br2.readLine()) != null) {
			if(!hs.contains(curr2)) {
				out.println(curr2);
			}
		}
		br2.close();

		out.close();
		
		System.out.println("File A - File B end!");
	}
	
	public static void getLemmaOutOfSparseMatrix(String fileIN, String fileOUT) throws IOException {
		FileReader fr = new FileReader(fileIN);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine; // read line-by-line
		
		FileWriter fw = new FileWriter(fileOUT, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		while ((CurrentLine = br.readLine()) != null) {
			String[] tokens = CurrentLine.split("\\s");
			String row = tokens[0] + " ";
			for(int i=1; i<tokens.length; i++) {
				int j = 0;
				String number = "";
				//System.out.println("Caracter="+tokens[i].charAt(j));
				while(tokens[i].charAt(j)!=':') {
					number = number + tokens[i].charAt(j);
					j++;
				}
				//System.out.println("Numar="+number);
				if(Integer.parseInt(number)<=358){
					row = row + tokens[i] + " ";
				}
			}
			out.println(row);
		}
		
		br.close();
		out.close();
		
		System.out.println("Lemma is out of file "+fileIN);
		
	}
	
	public static void copyFileToAnotherUntilLineLimit() throws IOException {
		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine; // read line-by-line
		
		FileWriter fw = new FileWriter(Util.FEATURES_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		int count = 1;
		while ((CurrentLine = br.readLine()) != null && count < LIMIT ) {
			out.println(CurrentLine);
			count++;
		}
		br.close();
		out.close();
	}
	
	private static Boolean haveFilteredPOS(String[] tokens,
			HashSet<String> pos2, HashSet<String> pos3, HashSet<String> pos5) {
	String[] posOfTokens = tokens[21].split("_");
		//System.out.println(posOfTokens[0]);
		if(tokens[tokens.length - 1].equals("none") &&
				!posOfTokens[0].equals("JJ") &&
				!posOfTokens[0].equals("NN") &&
				!posOfTokens[0].equals("DT") &&
				!posOfTokens[0].equals("NNP") &&
				!posOfTokens[0].equals("NNS") &&
				!posOfTokens[0].equals("VBG") &&
				!posOfTokens[0].equals("VBN") &&
				!posOfTokens[0].equals("SYM") &&
				!posOfTokens[0].equals("RB") &&
				!posOfTokens[0].equals("PRP$") ) {
			return false;
		}
		
		if(posOfTokens.length > 1 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1];
			if(pos2.contains(pos)) {
				return false;
			}
		}
		
		if(posOfTokens.length > 2 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1] + "_" + posOfTokens[2];
			if(pos3.contains(pos)) {
				return false;
			}
		}
		
		if(posOfTokens.length > 4 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1] + "_"
					+ posOfTokens[2] + "_" + posOfTokens[3] + "_"
					+ posOfTokens[4];
			if (pos5.contains(pos)) {
				return false;
			}
		}
		
		return true;
	}

	private static HashSet<String> readUnusedPOSFromFile(String file) throws IOException {
		HashSet<String> hs = new HashSet<String>();
		
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String curr1;
		while ((curr1 = br.readLine()) != null) {
			hs.add(curr1);
		}
		br.close();
		
		return hs;
	} 
}
