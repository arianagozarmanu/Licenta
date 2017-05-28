package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class UtilFileManipulatorAndGenerator {

	// numarul randului pana unde sa se copieze
	private final static int LIMIT = 390472;

	public static void main(String[] args) throws IOException {
		// LIBLNR_TRAIN LIBLNR_TEST
		// getLemmaOutOfSparseMatrix(GeneralUtils.LIBLNR_TRAIN,
		// GeneralUtils.LIBLNR_NO_LEMMA_TRAIN);
		// getLemmaOutOfSparseMatrix(GeneralUtils.LIBLNR_TEST,
		// GeneralUtils.LIBLNR_NO_LEMMA_TEST);
		// getElementsFromAFileMinusElementsFromAnother();
		// getShorterFeatureFileWithoutPOS();
		// getFeaturesWithoutLemma();
		 deleteFeaturesSelected(GeneralUtils.LIBLNR_TRAIN, GeneralUtils.TRAIN_FEATURES_FILTERED, GeneralUtils.FETURES_FILTERED);
		// deleteFeaturesUnselected(GeneralUtils.LIBLNR_TEST, GeneralUtils.TEST_FEATURES_FILTERED, GeneralUtils.FETURES_FILTERED_TO_TAKE);
		// getFirstNFeatureIndex(100, GeneralUtils.FEATURE_SCORING, GeneralUtils.FETURES_FILTERED_TO_TAKE);
	}
	
	public static void getFirstNFeatureIndex(int n, String fileIN, String fileOUT) throws IOException {
		FileReader fr = new FileReader(fileIN);
		BufferedReader br = new BufferedReader(fr);
		String cl;
		
		FileWriter fw = new FileWriter(fileOUT, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		//read all feature index
		ArrayList<Integer> array = new ArrayList<Integer>();
		while ((cl = br.readLine()) != null) {
			String[] tokens = cl.split("\\s");
			//System.out.println(tokens[0]);
			array.add(Integer.parseInt(tokens[0]));
		}
		
		int size = array.size();
		//System.out.println(array);
		if(n < array.size()) {
			for(int i = 1; i<=n ; i++ ) {
				out.println(array.get(size - i));
			}
		} else {
			System.out.println("Number of features wanted is greater than number of features! Enter first paramenter smaller than " + array.size());
		}
		
		br.close();
		out.close();
		
		System.out.println("Execution end!");
	}
	
	public static void deleteFeaturesSelected(String fileIN, String fileOUT, String notToTakeFeatures) throws IOException {
		//read "not to take into account" features index 
		FileReader frf = new FileReader(notToTakeFeatures);
		BufferedReader brf = new BufferedReader(frf);
		String cl;
		//better performance for "contains" method than Array
		HashSet<Integer> features = new HashSet<Integer>();
		while ((cl = brf.readLine()) != null) {
			features.add(Integer.parseInt(cl));
		}
		brf.close();
		
		FileReader fr = new FileReader(fileIN);
		BufferedReader br = new BufferedReader(fr);
		
		FileWriter fw = new FileWriter(fileOUT,false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		StringBuilder row = new StringBuilder("");
		while ((cl = br.readLine()) != null) {
			row = new StringBuilder("");
			String[] tokens = cl.split("\\s");
			row.append(tokens[0]);
			row.append(" ");
			//take every line
			for(int i=1; i<tokens.length; i++) {
				//take every feature index-value
				String[] indexValue = tokens[i].split(":");
				if(!features.contains(Integer.parseInt(indexValue[0]))) {
					row.append(indexValue[0]);
					row.append(":");
					row.append(indexValue[1]);
					row.append(" ");
				}
			}
			out.println(row);
		}
		
		br.close();
		out.close();
		
		System.out.println("File Generated!");
	}
	
	public static void deleteFeaturesUnselected(String fileIN, String fileOUT, String toTakeFeatures) throws IOException {
		//read "to take into account" features index 
		FileReader frf = new FileReader(toTakeFeatures);
		BufferedReader brf = new BufferedReader(frf);
		String cl;
		//better performance for "contains" method than Array
		HashSet<Integer> features = new HashSet<Integer>();
		while ((cl = brf.readLine()) != null) {
			features.add(Integer.parseInt(cl));
		}
		brf.close();
		//System.out.println(features);
		FileReader fr = new FileReader(fileIN);
		BufferedReader br = new BufferedReader(fr);
		
		FileWriter fw = new FileWriter(fileOUT,false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		StringBuilder row = new StringBuilder("");
		while ((cl = br.readLine()) != null) {
			row = new StringBuilder("");
			String[] tokens = cl.split("\\s");
			row.append(tokens[0]);
			row.append(" ");
			//take every line
			for(int i=1; i<tokens.length; i++) {
				//take every feature index-value
				String[] indexValue = tokens[i].split(":");
				if(features.contains(Integer.parseInt(indexValue[0]))) {
					row.append(indexValue[0]);
					row.append(":");
					row.append(indexValue[1]);
					row.append(" ");
				}
			}
			out.println(row);
		}
		
		br.close();
		out.close();
		
		System.out.println("File Generated!");
	}
	
	public static void getShorterFeatureFileWithoutPOS() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(GeneralUtils.SHORTER_FEATURES_FILE,
				false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		HashSet<String> hs2POS = new HashSet<String>();
		hs2POS = readUnusedPOSFromFile(GeneralUtils.POS2_UNUSED_MED_CONCEPTS);

		HashSet<String> hs3POS = new HashSet<String>();
		hs3POS = readUnusedPOSFromFile(GeneralUtils.POS3_UNUSED_MED_CONCEPTS);

		HashSet<String> hs5POS = new HashSet<String>();
		hs5POS = readUnusedPOSFromFile(GeneralUtils.POS5_UNUSED_MED_CONCEPTS);

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

	public static void getElementsFromAFileMinusElementsFromAnother()
			throws IOException {
		HashSet<String> hs = new HashSet<String>();

		FileWriter fw = new FileWriter(GeneralUtils.CHNK1_UNUSED_MED_CONCEPTS,
				false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		FileReader fr1 = new FileReader(GeneralUtils.CHUNKS_USED_FOR_MED_CONC);
		BufferedReader br1 = new BufferedReader(fr1);
		String curr1;
		while ((curr1 = br1.readLine()) != null) {
			hs.add(curr1);
		}
		br1.close();

		FileReader fr2 = new FileReader(
				GeneralUtils.CHUNKS_USED_FOR_NONMED_CONC);
		BufferedReader br2 = new BufferedReader(fr2);
		String curr2;
		while ((curr2 = br2.readLine()) != null) {
			if (!hs.contains(curr2)) {
				out.println(curr2);
			}
		}
		br2.close();

		out.close();

		System.out.println("File A - File B end!");
	}

	public static void getLemmaOutOfSparseMatrix(String fileIN, String fileOUT)
			throws IOException {
		FileReader fr = new FileReader(fileIN);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine; // read line-by-line

		FileWriter fw = new FileWriter(fileOUT, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		while ((CurrentLine = br.readLine()) != null) {
			String[] tokens = CurrentLine.split("\\s");
			String row = tokens[0] + " ";
			for (int i = 1; i < tokens.length; i++) {
				int j = 0;
				String number = "";
				// System.out.println("Caracter="+tokens[i].charAt(j));
				while (tokens[i].charAt(j) != ':') {
					number = number + tokens[i].charAt(j);
					j++;
				}
				// System.out.println("Numar="+number);
				if (Integer.parseInt(number) <= 359) {
					row = row + tokens[i] + " ";
				}
			}
			out.println(row);
		}

		br.close();
		out.close();

		System.out.println("Lemma is out of file " + fileIN);

	}

	public static void copyFileToAnotherUntilLineLimit() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.FEATURES_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		int count = 1;
		while ((CurrentLine = br.readLine()) != null && count < LIMIT) {
			out.println(CurrentLine);
			count++;
		}
		br.close();
		out.close();
	}

	private static Boolean haveFilteredPOS(String[] tokens,
			HashSet<String> pos2, HashSet<String> pos3, HashSet<String> pos5) {
		String[] posOfTokens = tokens[21].split("_");
		// System.out.println(posOfTokens[0]);
		if (tokens[tokens.length - 1].equals("none")
				&& !posOfTokens[0].equals("JJ") && !posOfTokens[0].equals("NN")
				&& !posOfTokens[0].equals("DT")
				&& !posOfTokens[0].equals("NNP")
				&& !posOfTokens[0].equals("NNS")
				&& !posOfTokens[0].equals("VBG")
				&& !posOfTokens[0].equals("VBN")
				&& !posOfTokens[0].equals("SYM")
				&& !posOfTokens[0].equals("RB")
				&& !posOfTokens[0].equals("PRP$")) {
			return false;
		}

		if (posOfTokens.length > 1 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1];
			if (pos2.contains(pos)) {
				return false;
			}
		}

		if (posOfTokens.length > 2 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1] + "_"
					+ posOfTokens[2];
			if (pos3.contains(pos)) {
				return false;
			}
		}

		if (posOfTokens.length > 4 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1] + "_"
					+ posOfTokens[2] + "_" + posOfTokens[3] + "_"
					+ posOfTokens[4];
			if (pos5.contains(pos)) {
				return false;
			}
		}

		return true;
	}

	private static HashSet<String> readUnusedPOSFromFile(String file)
			throws IOException {
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

	private static void getFeaturesWithoutLemma() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.CSV_FROM_LIBLINEAR);
		BufferedReader br = new BufferedReader(fr);
		String cl; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.CSV_WITHOUT_LEMMA, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		// cl = br.readLine();
		// String[] tokens = cl.split("\\.");
		// System.out.println(tokens[0].split(",").length);
		StringBuilder row = new StringBuilder("");
		while ((cl = br.readLine()) != null) {
			String[] tokens = cl.split(",");
			row = new StringBuilder("");
			for (int i = 0; i <= GeneralUtils.NR_OF_INSTANCES_WITHOUT_LEMMA; i++) {
				if (i == GeneralUtils.NR_OF_INSTANCES_WITHOUT_LEMMA) {
					row.append(tokens[i]);
				} else {
					row.append(tokens[i]);
					row.append(",");
				}
			}
			out.println(row);
		}
		br.close();
		out.close();
		
		System.out.println("Manipulare terminata");
	}
}
