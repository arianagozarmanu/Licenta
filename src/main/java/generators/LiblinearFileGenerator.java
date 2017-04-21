package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;

import medicalconcept.Util;

/**
 * Clase: none = 0, test = 1, treatment = 2, problem = 3 
 * False = 0, True = 1
 * 
 * @author Ariana
 *
 */
public class LiblinearFileGenerator {

	public static void main(String[] args) throws Exception {

		Set<String> pos = Util.getPOSFromFile();
		Set<String> chunks = Util.getChunksFromFile();

		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(Util.LIBLNR_TEST, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		int count = 0;
		int index = 0;
		String row = "";
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				row = "";
				index = 0;
				String[] tokens = currentLine.split("\\s");
				// transforma un rand de date
				for (int i = 0; i < tokens.length; i++) {
					//System.out.println("Count for token " + tokens[i] + " is " + index);
					if (tokens[i].equals("true")) {
						//System.out.println("A intrat pe true");
						row += (index + 1) + ":" + "1" + " ";
					} else if (i == tokens.length - 1) {
						//System.out.println("A intrat pe Category");
						row = getClassIndex(tokens[i]) + " " + row;
					} else if (i == 32) {
						//System.out.println("A intrat pe Double");
						Double val = Double.parseDouble(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
					} else if (i == 0 || i == 31) {
						//System.out.println("A intrat pe Integer");
						Integer val = Integer.parseInt(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
					} else if (isTokenCHUNK(tokens[i], chunks) && !tokens[i].equals("false") && !tokens[i].equals("none")) {
						//System.out.println("A intrat pe CHUNKS");
						// parcurge CHUNK
						for (String ch : chunks) {
							// System.out.println(tokens[i]+"|"+ch);
							if (tokens[i].toUpperCase().contains(
									ch.toUpperCase())) {
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						}
						index--;
					} else if (isTokenPOS(tokens[i], pos) && !tokens[i].equals("false") && !tokens[i].equals("none")) {
						//System.out.println("A intrat pe POS");
						// parcurge POS
						row = setPOSFeatures(row, tokens[i], pos, index);
						index = index + 43;
					} else if (!tokens[i].equals("false") && !tokens[i].equals("none")) {
						//System.out.println("N-a intrat pe POS si Chunks si nu e false sau none");
						index = index + 43 + 24;
					}
				 // we have 25 CHUNKS and 44 POS

					index++;
				}
				out.println(row);
			}
			count++;
		}

		br.close();
		out.close();
		System.out.println("Conversion ended!");
	}

	private static String setPOSFeatures(String row, String str, Set<String> pos, int i) {
		String rez = row;
		int index = i;
		String[] tokens = str.split("_");
		Boolean notFound = true;
		for (String p : pos) {
			notFound = true;
			for (int j = 0; j < tokens.length && notFound; j++) {
				//System.out.println(tokens[j]+"|"+p);
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

	private static Boolean isTokenCHUNK(String str, Set<String> chunks) {

		for (String ch : chunks) {
			//System.out.println(str+"|"+ch);
			if (str.toUpperCase().contains(ch.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	private static Boolean isTokenPOS(String str, Set<String> pos) {
		
		for (String ps : pos) {
			if (str.toUpperCase().contains(ps.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

}
