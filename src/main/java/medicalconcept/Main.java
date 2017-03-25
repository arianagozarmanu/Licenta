package medicalconcept;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import net.didion.jwnl.JWNLException;

import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;

public class Main {
	
	public static void main(String[] args) throws IOException, JWNLException {
		
		Processor proc = new CoreNLPProcessor(true, true, 0, 100);;
		
		WordLevelFeatures wlf = new WordLevelFeatures();
		SintacticFeatures sf = new SintacticFeatures();
		ContextualFeatures cf = new ContextualFeatures();
		
		List<String> lemmaFeature = Util.getLemmaFromFile();
		
		FileWriter fw = new FileWriter(Util.FEATURES_FILE, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		String currentLine;
		String features;
		//cod pentru citirea tuturor fisierelor
		File folder = new File(Util.RAW_DOCS_PATH);
		for (final File fileEntry : folder.listFiles()) {
            //System.out.println(fileEntry.getName().toString());
            FileReader fr = new FileReader(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString());
            BufferedReader br = new BufferedReader(fr);
            
            //creare doc cu fisierul intreg pentru TF
            Scanner scanner = new Scanner(new File(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString()));
            String text = scanner.useDelimiter("\\A").next();
            Document totalDOC = proc.annotate(text, false);
            scanner.close();
            
            while ((currentLine = br.readLine()) != null) {
            	Document doc = proc.annotate(currentLine, false);
            	for (Sentence sentence : doc.sentences()) {
            		String[] tokens = Util.mkString(sentence.words(), " ").split("\\s");
            		String[] pos = sentence.tags().get();
            		//preluare trasaturi pentru fiecare cuvant
            		for (int x=0; x<tokens.length; x++) {
            			if(tokenToTake(tokens[x], pos[x])) {
	            			features = "";
		            		features += wlf.getWordLevelFeatures(x, tokens[x], sentence);
		            		features += sf.getSintacticFeatures(x, sentence);
		            		features += cf.getContextualFeatures(x, tokens[x], sentence, totalDOC);
		            		for(String str : lemmaFeature) {
		            			if(str.equals(tokens[x].toLowerCase())) {
		            				features += "\t" + "true";
		            			} else {
		            				features += "\t" + "false";
		            			}
		            		}
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
	
	public static Boolean tokenToTake(String token, String pos) {
		
		//verificare daca contine tokenul e caracter special sau numar
		Boolean notOK = true;
		for (int i = 0; i < token.length() && notOK; i++) {
			if(!Util.SPECIAL_CHARS.contains("" + token.charAt(i))) {
				notOK = false;
			}
		}
		if(notOK) return false;
		
		notOK = true;
		for (int i = 0; i < token.length() && notOK; i++) {
			if(!Util.NUMBERS.contains("" + token.charAt(i))) {
				notOK = false;
			}
		}
		if(notOK) return false;
		
		//verificare daca POS e valabil (not CC,CD,DT,EX,IN,-LRB-,LS,PDT,PP,PRPR$,PRP,PRP$,TO,UH)
		if(pos.toUpperCase().equals("CC") || pos.toUpperCase().equals("CD") || pos.toUpperCase().equals("DT") 
				|| pos.toUpperCase().equals("EX") || pos.toUpperCase().equals("IN") || pos.toUpperCase().equals("-LRB-") || pos.toUpperCase().equals("-RRB-")
				|| pos.toUpperCase().equals("LS") || pos.toUpperCase().equals("PDT") || pos.toUpperCase().equals("PP")
				|| pos.toUpperCase().equals("PRPR$") || pos.toUpperCase().equals("PRP") || pos.toUpperCase().equals("PRP$")
				|| pos.toUpperCase().equals("TO") || pos.toUpperCase().equals("UH")) {
			return false;
		}
		
	return true;
	}

}

