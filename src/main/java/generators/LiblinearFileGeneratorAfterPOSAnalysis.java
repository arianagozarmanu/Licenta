package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import Utils.Util;

/**
 * Clase: none = 0, test = 1, treatment = 2, problem = 3 False = 0, True = 1
 * 
 * @author Ariana
 *
 */
public class LiblinearFileGeneratorAfterPOSAnalysis {

	public static void main(String[] args) throws Exception {

		Set<String> pos = Util.getPOSFromFile();
		Set<String> chunks = Util.getChunksFromFile();

		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(Util.LIBLNR_TEST, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		//elimina POS
		HashSet<String> hs2POS = new HashSet<String>();
		hs2POS = readUnusedPOSFromFile(Util.POS2_UNUSED_MED_CONCEPTS);
		HashSet<String> hs3POS = new HashSet<String>();
		hs3POS = readUnusedPOSFromFile(Util.POS3_UNUSED_MED_CONCEPTS);
		HashSet<String> hs4POS = new HashSet<String>();
		hs4POS = readUnusedPOSFromFile(Util.POS4_UNUSED_MED_CONCEPTS);
		HashSet<String> hs5POS = new HashSet<String>();
		hs5POS = readUnusedPOSFromFile(Util.POS5_UNUSED_MED_CONCEPTS);
		
		//elimina Chunks
		HashSet<String> hs5CHNK = new HashSet<String>();
		hs5CHNK = readUnusedPOSFromFile(Util.CHNK5_UNUSED_MED_CONCEPTS);
		
		int count = 0;
		int index = 0;
		String row = "";
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				row = "";
				index = 1;
				//add feature nr-of-words-in-concept
				String[] tokens = currentLine.split("\\s");
				row += index + ":" + tokens[21].split("_").length + " ";
				// transforma un rand de date
				if (haveFilteredPOS(tokens, hs2POS, hs3POS, hs4POS, hs5POS, hs5CHNK)) {
					for (int i = 0; i < tokens.length; i++) {
						if (i == 0) { // lungime cuvant
							// System.out.println("A intrat pe lungime cuvant");
							Integer val = Integer.parseInt(tokens[i]);
							if (val != 0) {
								row += (index + 1) + ":" + val + " ";
							}
							index++;
						} else if (i == 31) { // nr. def. WN
							// System.out.println("A intrat pe nr. def. WN");
							Integer val = Integer.parseInt(tokens[i]);
							if (val != 0 && tokens[21].split("_").length == 1) { // && tokens[21].split("_").length == 1  // WN definition just for One-word concepts
								row += (index + 1) + ":" + val + " ";
							}
							index++;
						} else if (i > 0 && i <= 20) {
							if (tokens[i].equals("true")) {
								// System.out.println("A intrat pe true");
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						} else if (i == 21) {
							// System.out.println("A intrat pe POS");
							// parcurge POS
							row = setPOSFeatures(row, tokens[i], pos, index);
							index = index + 44;
						} else if (i == 22) {
							// System.out.println("A intrat pe CHUNKS");
							// parcurge CHUNK
							for (String ch : chunks) {
								// System.out.println(tokens[i]+"|"+ch);
								if (tokens[i].toUpperCase().contains(
										ch.toUpperCase())) {
									row += (index + 1) + ":" + "1" + " ";
								}
								index++;
							}
						} else if (i > 22 && i <= 28) {
							// System.out.println("A intrat pe POS Before & After");
							row = setPOSFeatures(row, tokens[i], pos, index);
							index = index + 44;
						} else if (i == 29 || i == 30) {
							// System.out.println("A intrat pe is nrigh.MG sau comma before or after");
							if (tokens[i].equals("true")) {
								// System.out.println("A intrat pe true");
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						} else if (i == 32) {
							// System.out.println("A intrat pe TF");
							Double val = Double.parseDouble(tokens[i]);
							if (val != 0) {
								row += (index + 1) + ":" + val + " ";
							}
							index++;
						} else if (i == tokens.length - 1) {
							// System.out.println("A intrat pe Category");
							row = getClassIndex(tokens[i]) + " " + row;
						} else {
							// System.out.println("A intrat pe lemma features");
							if (tokens[i].equals("true")) {
								// System.out.println("A intrat pe true");
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						}
					}
					out.println(row);

				}
			}
			count++;
		}

		br.close();
		out.close();
		System.out.println("Conversion ended!");
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
	
	private static Boolean haveFilteredPOS(String[] tokens,
			HashSet<String> pos2, HashSet<String> pos3, HashSet<String> pos4,
			HashSet<String> pos5, HashSet<String> chnk5) {
		String[] posOfTokens = tokens[21].split("_");
		String[] chnkOfTokens = tokens[22].split("_");
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
		
		//VEZI SI CU I-NP SI B-ADJP
		if(tokens[tokens.length - 1].equals("none") &&
				//!chnkOfTokens[0].equals("B-ADJP") &&
				!chnkOfTokens[0].equals("B-NP") &&
				!chnkOfTokens[0].equals("B-VP") &&
				//!chnkOfTokens[0].equals("I-NP") &&
				!chnkOfTokens[0].equals("I-ADJP") &&
				!chnkOfTokens[0].equals("I-INTJ") &&
				!chnkOfTokens[0].equals("I-VP") &&
				!chnkOfTokens[0].equals("O") ) {
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
		
		if(posOfTokens.length > 3 && tokens[tokens.length - 1].equals("none")) {
			String pos = posOfTokens[0] + "_" + posOfTokens[1] + "_" + posOfTokens[2] + "_" + posOfTokens[3];
			if(pos4.contains(pos)) {
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
		
		if(chnkOfTokens.length > 4 && tokens[tokens.length - 1].equals("none")) {
			String chnk = chnkOfTokens[0] + "_" + chnkOfTokens[1] + "_"
					+ chnkOfTokens[2] + "_" + chnkOfTokens[3] + "_"
					+ chnkOfTokens[4];
			if (chnk5.contains(chnk)) {
				return false;
			}
		}
		
		return true;
	}
	
	private static String setPOSFeatures(String row, String str,
			Set<String> pos, int i) {
		String rez = row;
		int index = i;
		String[] tokens = str.split("_");
		Boolean notFound = true;
		for (String p : pos) {
			notFound = true;
			for (int j = 0; j < tokens.length && notFound; j++) {
				// System.out.println(tokens[j]+"|"+p);
				if (p.toUpperCase().equals(tokens[j].toUpperCase())) {
					notFound = false;
					rez += (index + 1) + ":" + "1" + " ";
				}
			}
			index++;
		}
		return rez;
	}

	private static String getClassIndex(String str) {
		if (str.equals("none"))
			return "0";
		if (str.equals("test"))
			return "1";
		if (str.equals("treatment"))
			return "2";
		if (str.equals("problem"))
			return "3";

		return "0";
	}

}
