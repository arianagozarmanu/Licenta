package generators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import medicalconcept.Util;
import net.didion.jwnl.JWNLException;

public class HeaderFileGenerator {

	public static void main(String[] args) throws IOException, JWNLException {
		
		List<String> lemmaFeature = Util.getLemmaFromFile();
		String featureHeader = Util.FEATURE_HEADER;
		for(String str: lemmaFeature) {
			featureHeader += "\t" + str;
		}
		featureHeader += "\t" + "Categorie";
		
		FileWriter fw = new FileWriter(Util.FEATURES_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println(featureHeader);
		out.close();
		System.out.println("Header File Printed!");
	}
}
