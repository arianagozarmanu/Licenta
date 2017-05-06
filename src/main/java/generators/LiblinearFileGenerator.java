package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;

import Utils.Util;

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

		FileReader fr = new FileReader(Util.DUMMY_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(Util.DUMMY_FILE_TRAIN, false);
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
					if(i == 0) { //lungime cuvant
						//System.out.println("A intrat pe lungime cuvant");
						Integer val = Integer.parseInt(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
						index++;
					} else if(i == 31) { //nr. def. WN
						//System.out.println("A intrat pe nr. def. WN");
						Integer val = Integer.parseInt(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
						index++;
					} else if(i>0 && i <=20) {
						if (tokens[i].equals("true")) {
							//System.out.println("A intrat pe true");
							row += (index + 1) + ":" + "1" + " ";
						}
						index++;
					} else if(i==21) {
						//System.out.println("A intrat pe POS");
						// parcurge POS
						row = setPOSFeatures(row, tokens[i], pos, index);
						index = index + 44;
					} else if(i==22) {
						//System.out.println("A intrat pe CHUNKS");
						// parcurge CHUNK
						for (String ch : chunks) {
							// System.out.println(tokens[i]+"|"+ch);
							if (tokens[i].toUpperCase().contains(ch.toUpperCase())) {
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						}
					} else if(i>22 && i<=28) {
						//System.out.println("A intrat pe POS Before & After");
						row = setPOSFeatures(row, tokens[i], pos, index);
						index = index + 44;
					} else if(i == 29 || i == 30) {
						//System.out.println("A intrat pe is nrigh.MG sau comma before or after");
						if (tokens[i].equals("true")) {
							//System.out.println("A intrat pe true");
							row += (index + 1) + ":" + "1" + " ";
						}
						index++;
					} else if(i==32) {
						//System.out.println("A intrat pe TF");
						Double val = Double.parseDouble(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
						index++;
					} else if (i == tokens.length - 1) {
						//System.out.println("A intrat pe Category");
						row = getClassIndex(tokens[i]) + " " + row;
					} else {
						//System.out.println("A intrat pe lemma features");
						if (tokens[i].equals("true")) {
							//System.out.println("A intrat pe true");
							row += (index + 1) + ":" + "1" + " ";
						}
						index++;
					} 
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

}
