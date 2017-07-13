package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Set;

import utils.GeneralUtils;

/**
 * converts general file intro sparse matrix 
 * 
 * @author Ariana
 *
 */
public class SparseMatrixFileGenerator {

	public static void main(String[] args) throws Exception {

		Set<String> pos = GeneralUtils.getPOSFromFile();
		Set<String> chunks = GeneralUtils.getChunksFromFile();

		FileReader fr = new FileReader(GeneralUtils.DUMMY_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;

		FileWriter fw = new FileWriter(GeneralUtils.DUMMY_FILE_TRAIN, false);
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
				for (int i = 0; i < tokens.length; i++) {
					if(i == 0) { 
						Integer val = Integer.parseInt(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
						index++;
					} else if(i == 31) { 
						Integer val = Integer.parseInt(tokens[i]);
						if (val != 0) {
							row += (index + 1) + ":" + val + " ";
						}
						index++;
					} else if(i>0 && i <=20) {
						if (tokens[i].equals("true")) {
							row += (index + 1) + ":" + "1" + " ";
						}
						index++;
					} else if(i==21) {
						row = setPOSFeatures(row, tokens[i], pos, index);
						index = index + 44;
					} else if(i==22) {
						for (String ch : chunks) {
							if (tokens[i].toUpperCase().contains(ch.toUpperCase())) {
								row += (index + 1) + ":" + "1" + " ";
							}
							index++;
						}
					} else if(i>22 && i<=28) {
						row = setPOSFeatures(row, tokens[i], pos, index);
						index = index + 44;
					} else if(i == 29 || i == 30) {
						if (tokens[i].equals("true")) {
							row += (index + 1) + ":" + "1" + " ";
						}
						index++;
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
