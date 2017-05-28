package generators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import utils.GeneralUtils;
import net.didion.jwnl.JWNLException;

public class HeaderFileGenerator {

	public static void main(String[] args) throws IOException, JWNLException {
		
		List<String> lemmaFeature = GeneralUtils.getLemmaFromFile();
		String featureHeader = GeneralUtils.FEATURE_HEADER;
		for(String str: lemmaFeature) {
			featureHeader += "\t" + str;
		}
		featureHeader += "\t" + "Categorie";
		
		FileWriter fw = new FileWriter(GeneralUtils.FEATURES_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println(featureHeader);
		out.close();
		System.out.println("Header File Printed!");
	}
}
