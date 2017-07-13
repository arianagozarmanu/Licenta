package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import utils.GeneralUtils;

/**
 * convert general file into sparse matrix with undersampling
 * 
 * @author Ariana
 *
 */
public class SparseMatrixGeneratorWithUndersampling {

	public static void main(String[] args) throws Exception {

		Set<String> pos = GeneralUtils.getPOSFromFile();
		Set<String> chunks = GeneralUtils.getChunksFromFile();

		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(GeneralUtils.LIBLNR_TEST, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		//elimina POS
		HashSet<String> hs2POS = new HashSet<String>();
		hs2POS = readUnusedPOSFromFile(GeneralUtils.POS2_UNUSED_MED_CONCEPTS);
		HashSet<String> hs3POS = new HashSet<String>();
		hs3POS = readUnusedPOSFromFile(GeneralUtils.POS3_UNUSED_MED_CONCEPTS);
		HashSet<String> hs4POS = new HashSet<String>();
		hs4POS = readUnusedPOSFromFile(GeneralUtils.POS4_UNUSED_MED_CONCEPTS);
		HashSet<String> hs5POS = new HashSet<String>();
		hs5POS = readUnusedPOSFromFile(GeneralUtils.POS5_UNUSED_MED_CONCEPTS);
		
		//elimina Chunks
		HashSet<String> hs5CHNK = new HashSet<String>();
		hs5CHNK = readUnusedPOSFromFile(GeneralUtils.CHNK5_UNUSED_MED_CONCEPTS);
		
		int count = 0;
		int index = 0;
		String row = "";
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				row = "";
				index = 0; 
				String[] tokens = currentLine.split("\\s");
				if (haveFilteredPOS(tokens, hs2POS, hs3POS, hs4POS, hs5POS, hs5CHNK)) {
					for (int i = 0; i < tokens.length; i++) {
						if (i == 0) { 
							Integer val = Integer.parseInt(tokens[i]);
							if (val != 0) {
								row += (index + 1) + ":" + val + " ";
							}
							index++;
						} else if (i == 31) { 
							Integer val = Integer.parseInt(tokens[i]);
							if (val != 0) { 
								row += (index + 1) + ":" + val + " ";
							}
							index++;
						} else if (i > 0 && i <= 20) {
							if (tokens[i].equals("true")) {
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						} else if (i == 21) {
							row = setPOSFeatures(row, tokens[i], pos, index);
							index = index + 44;
						} else if (i == 22) {
							for (String ch : chunks) {
								if (tokens[i].toUpperCase().contains(
										ch.toUpperCase())) {
									row += (index + 1) + ":" + "1" + " ";
								}
								index++;
							}
						} else if (i > 22 && i <= 28) {
							row = setPOSFeatures(row, tokens[i], pos, index);
							index = index + 44;
						} else if (i == 29 || i == 30) {
							if (tokens[i].equals("true")) {
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						} else if (i == 32) {
							Double val = Double.parseDouble(tokens[i]);
							if (val != 0) {
								row += (index + 1) + ":" + val + " ";
							}
							index++;
						} else if (i == tokens.length - 1) {
							row = getClassIndex(tokens[i]) + " " + row;
						} else {
							if (tokens[i].equals("true")) {
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
		
		if(tokens[tokens.length - 1].equals("none") &&
				!chnkOfTokens[0].equals("B-NP") &&
				!chnkOfTokens[0].equals("B-VP") &&
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
