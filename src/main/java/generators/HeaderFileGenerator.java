package generators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import utils.GeneralUtils;
import net.didion.jwnl.JWNLException;

/**
 * generates header for general file computed in {@link /medicalconcept/src/main/java/medicalconcept/Main.java}
 * {warning} it should be run before Main class.
 * 
 * @author Ariana
 *
 */
public class HeaderFileGenerator {

	public static void main(String[] args) throws IOException, JWNLException {
		
		List<String> lemmaFeature = GeneralUtils.getLemmaFromFile();
		String featureHeader = GeneralUtils.STATIC_FEATURES_HEADER;
		for(String str: lemmaFeature) {
			featureHeader += "\t" + str;
		}
		featureHeader += "\t" + "Category";
		
		FileWriter fw = new FileWriter(GeneralUtils.FEATURES_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println(featureHeader);
		out.close();
		System.out.println("Header File Printed!");
	}
}
