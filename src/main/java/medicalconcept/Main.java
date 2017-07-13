package medicalconcept;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.didion.jwnl.JWNLException;

import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;

import utils.GeneralUtils;
import features.UniWordConceptContextualFeatures;
import features.UniWordConceptSintacticFeatures;
import features.UniWordConceptLevelFeatures;
import generators.MultiWordConceptsGeneratorWithNGramTechnique;

/**
 * Main class which generates a general file filled with features vectors 
 * of every concept identified in unstructured docs
 * {warning} Run first {@link /medicalconcept/src/main/java/generators/HeaderFileGenerator.java}
 * @author Ariana
 *
 */
public class Main {
	
	public static void main(String[] args) throws IOException, JWNLException {
		
		Processor proc = new CoreNLPProcessor(true, true, 0, 100);
		
		UniWordConceptLevelFeatures wlf = new UniWordConceptLevelFeatures();
		UniWordConceptSintacticFeatures sf = new UniWordConceptSintacticFeatures();
		UniWordConceptContextualFeatures cf = new UniWordConceptContextualFeatures();
		
		MultiWordConceptsGeneratorWithNGramTechnique ngrams = new MultiWordConceptsGeneratorWithNGramTechnique();
		
		// read lemma features from file
		List<String> lemmaFeature = GeneralUtils.getLemmaFromFile();
		
		FileWriter fw = new FileWriter(GeneralUtils.FEATURES_FILE, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		String currentLine;
		String features;
		
		// identifies concepts with n-gram and compute features vector
		File folderTextFiles = new File(GeneralUtils.RAW_DOCS_PATH);
		for (final File fileEntry : folderTextFiles.listFiles()) {
            System.out.println(fileEntry.getName().toString());
            FileReader fr = new FileReader(GeneralUtils.RAW_DOCS_PATH + "/" + fileEntry.getName().toString());
            BufferedReader br = new BufferedReader(fr);
            
            // read adnotated objects
            List<Concept> conObjects = new ArrayList<Concept>();
            conObjects = GeneralUtils.takeConObjectsIntoList(fileEntry);
            
            Scanner scanner = new Scanner(new File(GeneralUtils.RAW_DOCS_PATH + "/" + fileEntry.getName().toString()));
            String text = scanner.useDelimiter("\\A").next();
            Document totalDOC = proc.annotate(text, false);
            scanner.close();
            
            while ((currentLine = br.readLine()) != null) {
            	Document doc = proc.annotate(currentLine, false); 
            	for (Sentence sentence : doc.sentences()) {	
            		String[] tokens = GeneralUtils.mkString(sentence.words(), " ").split("\\s");
            		String[] pos = sentence.tags().get();
            		String[] lemmas = sentence.lemmas().get();
            		
            		// compute multiword concepts features
            		List<String> ngramFeatures = ngrams.generateNGramFeatures(sentence, totalDOC, conObjects, lemmaFeature);
            		for(String feature : ngramFeatures) {
            			out.println(feature);
            		}
            		
            		// compute uniword concepts features
            		for (int x=0; x<tokens.length; x++) {
            			if(tokenToTake(tokens[x], pos[x])) {
            				String word = GeneralUtils.getWordWithLRBRRB(tokens, x);
            				String lemma = GeneralUtils.getWordWithLRBRRB(lemmas, x);
            				if(word.contains("(")){	
            					x = x+3;
            				} 
	            			features = "";
		            		features += wlf.getWordLevelFeatures(x, word, sentence);
		            		features += sf.getSintacticFeatures(x, sentence);
		            		features += cf.getContextualFeatures(x, word, sentence, totalDOC);
		            		
		            		features = GeneralUtils.setLemmaFeature(lemmaFeature, features, lemma);
		            		
		            		features = GeneralUtils.setCategory(conObjects, features, word);
		            		
		            		out.println(features);
            			} 
            		}
            	}
			}
            br.close();
        }

        out.close();
        System.out.println("Feature writing completed!");

	}
	
	/**
	 * concept identification rules
	 * 
	 * @param token
	 * @param pos
	 * @return
	 */
	private static Boolean tokenToTake(String token, String pos) {
		
		Boolean notOK = true;
		for (int i = 0; i < token.length() && notOK; i++) {
			if(!GeneralUtils.SPECIAL_CHARS.contains("" + token.charAt(i))) {
				notOK = false;
			}
		}
		if(notOK) return false;
		
		notOK = true;
		for (int i = 0; i < token.length() && notOK; i++) {
			if(!GeneralUtils.NUMBERS.contains("" + token.charAt(i))) {
				notOK = false;
			}
		}
		if(notOK) return false;
		
		if(token.length() == 1)
			return false;
		
		if (pos.toUpperCase().equals("CC") || pos.toUpperCase().equals("CD")
				|| pos.toUpperCase().equals("DT")
				|| pos.toUpperCase().equals("EX")
				|| pos.toUpperCase().equals("IN")
				|| pos.toUpperCase().equals("-LRB-")
				|| pos.toUpperCase().equals("-RRB-")
				|| pos.toUpperCase().equals("LS")
				|| pos.toUpperCase().equals("PDT")
				|| pos.toUpperCase().equals("PP")
				|| pos.toUpperCase().equals("PRPR$")
				|| pos.toUpperCase().equals("PRP")
				|| pos.toUpperCase().equals("PRP$")
				|| pos.toUpperCase().equals("TO")
				|| pos.toUpperCase().equals("UH")
				|| pos.toUpperCase().equals("WDT")
				|| pos.toUpperCase().equals("WP$")
				|| pos.toUpperCase().equals("WP")
				|| pos.toUpperCase().equals("WRB")) {
			return false;
		}
		
		return true;
	}
	
}

