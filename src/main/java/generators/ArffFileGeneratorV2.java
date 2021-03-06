package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import utils.GeneralUtils;
import weka.core.Attribute;
import weka.core.Instances;

/**
 * ARFF file generator from general file computed in {@link /medicalconcept/src/main/java/medicalconcept/Main.java}
 * it transforms comma and quote mark characters in String because ARFF files not support these special char.
 * {warning} use weka library functions
 * @author Ariana
 *
 */
public class ArffFileGeneratorV2 {

	public static void main(String[] args) throws Exception {
		ArrayList<Attribute> atts;
		ArrayList<String> attValsBool;
		ArrayList<String> attValsCat;
		Instances data;
		
		//1. set up Attributes
		atts = new ArrayList<Attribute>();
		//numeric
		atts.add(new Attribute("lengthWord"));	
		//nominal
		attValsBool = new ArrayList<String>();
		attValsBool.add("true");
		attValsBool.add("false");
		atts.add(new Attribute("uppCaseBegin", attValsBool));
		atts.add(new Attribute("uppCaseAll", attValsBool));
		atts.add(new Attribute("mixedCase", attValsBool));
		atts.add(new Attribute("hasAmpersand", attValsBool));
		atts.add(new Attribute("hasComma", attValsBool));
		atts.add(new Attribute("hasPeriod", attValsBool));
		atts.add(new Attribute("hasGreaterSign", attValsBool));
		atts.add(new Attribute("hasLessSign", attValsBool));
		atts.add(new Attribute("hasMinus", attValsBool));
		atts.add(new Attribute("hasParanthesis", attValsBool));
		atts.add(new Attribute("hasQuoteMark", attValsBool));
		atts.add(new Attribute("hasPercent", attValsBool));
		atts.add(new Attribute("hasSlash", attValsBool));
		atts.add(new Attribute("hasDigits", attValsBool));
		atts.add(new Attribute("hasSpecChrBefore", attValsBool));
		atts.add(new Attribute("hasSpecChrBBefore", attValsBool));
		atts.add(new Attribute("hasSpecChrBBBefore", attValsBool));
		atts.add(new Attribute("hasSpecChrAfter", attValsBool));
		atts.add(new Attribute("hasSpecChrAAfter", attValsBool));
		atts.add(new Attribute("hasSpecChrAAAfter", attValsBool));
		//string
		atts.add(new Attribute("partOfSpeech", (ArrayList<String>) null));
		atts.add(new Attribute("chunks", (ArrayList<String>) null));
		atts.add(new Attribute("beforePOS", (ArrayList<String>) null));
		atts.add(new Attribute("bBeforePOS", (ArrayList<String>) null));
		atts.add(new Attribute("bBBeforePOS", (ArrayList<String>) null));
		atts.add(new Attribute("afterPOS", (ArrayList<String>) null));
		atts.add(new Attribute("aAfterPOS", (ArrayList<String>) null));
		atts.add(new Attribute("aAAfterPOS", (ArrayList<String>) null));
		//nominal
		atts.add(new Attribute("isSecondNeighbMG", attValsBool));
		atts.add(new Attribute("hasCommaBeforeOrAfter", attValsBool));
		//numeric
		atts.add(new Attribute("NrDefInWordNet"));
		atts.add(new Attribute("TermFrequency"));
		//nominal	
		List<String> lemmaFeature = GeneralUtils.getLemmaFromFile();
		for(String lemma : lemmaFeature) {
			atts.add(new Attribute(lemma, attValsBool));
		}
		attValsCat = new ArrayList<String>();
		attValsCat.add("none");
		attValsCat.add("test");
		attValsCat.add("treatment");
		attValsCat.add("problem");
		atts.add(new Attribute("class", attValsCat));
		
		//2. create Instances object
		data = new Instances("MedicalConcepts", atts, 0);
		
		FileWriter fw = new FileWriter(GeneralUtils.ARFF_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		out.println(data);
		
		//3. fill with data
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;
		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				int tokensLength = tokens.length;
				String instances = "";
				for (int i = 0; i < tokensLength; i++) {
					tokens[i] = changeSpCharsIntoString(tokens[i]);
					if (i == tokensLength - 1) {
						instances += tokens[i];
					} else {
						instances += tokens[i] + ",";
					}
				}
				out.println(instances);
			}
			count++;
		}
		
		br.close();
		out.close();
		
		System.out.println("Nr. of instances = "+(count-1));

	}
	
	private static String changeSpCharsIntoString(String str) {
		String rez = str;
		if(str.contains(",")) {
			rez = str.replace(",", "comma");
		}
		String next = rez;
		if(str.contains("''")) {
			rez = next.replace("''", "quotes");
		}
		return rez;
	}
	
}
