package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import medicalconcept.Util;

public class UtilFileManipulatorAndGenerator {
	
	//numarul randului pana unde sa se copieze
	private final static int LIMIT = 390472;
	
	public static void main(String[] args) {
		
	}
	
	public static void copyFileToAnotherUntilLineLimit() throws IOException {
		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine; // read line-by-line
		
		FileWriter fw = new FileWriter(Util.FEATURES_FILE, true);
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

}
