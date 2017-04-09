package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import medicalconcept.Util;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ArffFileGenerator {

	public static void main(String[] args) throws Exception {
		//testSimpleARFF();
		ArrayList<Attribute> atts;
		ArrayList<String> attValsBool;
		ArrayList<String> attValsCat;
		Instances data;
		double[] vals;
		int i;
		
		//nu punem si numele conceptului, nu? Numai trasaturile calculate
		
		//1. set up Attributes
		atts = new ArrayList<Attribute>();
		//string
		atts.add(new Attribute("concept", (ArrayList<String>) null));
		//numeric
		atts.add(new Attribute("length"));	
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
		//nominal	-- fiecare lemma unica e un atribut nominal
		List<String> lemmaFeature = Util.getLemmaFromFile();
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
		
		//3. fill with data
		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine;
		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if(count > 0) {
				String[] instances = currentLine.split("\\s");
				vals = new double[data.numAttributes()];
				for(int j=0 ; j<instances.length; j++) {
					if(j == 1) {
						vals[j] = Integer.parseInt(instances[j]);
					} else if(j == instances.length-1) {
						vals[j] = attValsCat.indexOf(instances[j]);
					} else if (j > 1 && j < 22) {
						vals[j] = attValsBool.indexOf(instances[j]);
					} else if (j == 30 || j == 31 || j > 33) {
						vals[j] = attValsBool.indexOf(instances[j]);
					} else if(j == 32) {
						vals[j] = Integer.parseInt(instances[j]);
					} else if(j == 33) {
						vals[j] = Double.parseDouble(instances[j]);
					} else {
						vals[j] = data.attribute(j).addStringValue(instances[j]);
					}
				}
				// add
				data.add(new DenseInstance(1.0, vals));
			}
			count++;
		}
		br.close();
		
		FileWriter fw = new FileWriter(Util.ARFF_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		out.println(data);
		out.close();
		
		System.out.println("Nr. of instances = "+(count-1));
	}
	
	private static void testSimpleARFF() throws Exception {
		ArrayList<Attribute> atts;
		ArrayList<String> attVals;
		Instances data;
		double[] vals;
		int i;

		// 1. set up attributes
		atts = new ArrayList<Attribute>();
		// - numeric
		atts.add(new Attribute("att1"));
		// - nominal
		attVals = new ArrayList<String>();
		for (i = 0; i < 5; i++)
			attVals.add("val" + (i + 1));
		atts.add(new Attribute("att2", attVals));
		// - string
		atts.add(new Attribute("att3", (ArrayList<String>) null));
		// - date
		atts.add(new Attribute("att4", "yyyy-MM-dd"));
		

		// 2. create Instances object
		data = new Instances("MyRelation", atts, 0);

		// 3. fill with data
		// first instance
		vals = new double[data.numAttributes()];
		// - numeric
		vals[0] = Math.PI;
		// - nominal
		vals[1] = attVals.indexOf("val3");
		// - string
		vals[2] = data.attribute(2).addStringValue("This is a string!");
		// - date
		vals[3] = data.attribute(3).parseDate("2001-11-09");
		
		// add
		data.add(new DenseInstance(1.0, vals));

		// second instance
		vals = new double[data.numAttributes()]; // important: needs NEW array!
		// - numeric
		vals[0] = Math.E;
		// - nominal
		vals[1] = attVals.indexOf("val1");
		// - string
		vals[2] = data.attribute(2).addStringValue("And another one!");
		// - date
		vals[3] = data.attribute(3).parseDate("2000-12-01");
		
		// add
		data.add(new DenseInstance(1.0, vals));

		// 4. output data
		System.out.println(data);
	}
}
