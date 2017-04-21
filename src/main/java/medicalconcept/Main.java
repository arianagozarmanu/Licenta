package medicalconcept;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.didion.jwnl.JWNLException;

import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;

import features.ContextualFeatures;
import features.SintacticFeatures;
import features.WordLevelFeatures;

public class Main {
	
	public static void main(String[] args) throws IOException, JWNLException {
		
		Processor proc = new CoreNLPProcessor(true, true, 0, 100);
		
		WordLevelFeatures wlf = new WordLevelFeatures();
		SintacticFeatures sf = new SintacticFeatures();
		ContextualFeatures cf = new ContextualFeatures();
		
		NGramGenAndFeatCalc ngrams = new NGramGenAndFeatCalc();
		
		//preluare LEMMA generate pentru toate documentele existente
		List<String> lemmaFeature = Util.getLemmaFromFile();
		
		//deschidere fisier pentru scriere trasaturi
		FileWriter fw = new FileWriter(Util.FEATURES_FILE, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		String currentLine;
		String features;
		
		//cod pentru citirea tuturor fisierelor si calcularea trasaturilor pentru cuvinte si ngrame
		File folderTextFiles = new File(Util.RAW_DOCS_PATH);
		for (final File fileEntry : folderTextFiles.listFiles()) {
            System.out.println(fileEntry.getName().toString());
			//creare buffer reader pentru fisierele text
            FileReader fr = new FileReader(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString());
            BufferedReader br = new BufferedReader(fr);
            
            //creare lista de obiecte din fisierele .con pentru preluare categorie
            List<Concept> conObjects = new ArrayList<Concept>();
            conObjects = Util.takeConObjectsIntoList(fileEntry);
            
            //creare doc cu fisierul intreg pentru TF
            Scanner scanner = new Scanner(new File(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString()));
            String text = scanner.useDelimiter("\\A").next();
            Document totalDOC = proc.annotate(text, false);
            scanner.close();
            
            while ((currentLine = br.readLine()) != null) {
            	Document doc = proc.annotate(currentLine, false); //unele linii din fisier au cate 2 sau mai multe propozitii
            	for (Sentence sentence : doc.sentences()) {	
            		String[] tokens = Util.mkString(sentence.words(), " ").split("\\s");
            		String[] pos = sentence.tags().get();
            		String[] lemmas = sentence.lemmas().get();
            		
            		//preluare trasaturi pentru n-grame
            		List<String> ngramFeatures = ngrams.generateNGramFeatures(sentence, totalDOC, conObjects, lemmaFeature);
            		//scriere in fisier trasaturi ngrame
            		for(String feature : ngramFeatures) {
            			out.println(feature);
            		}
            		
            		//preluare trasaturi pentru fiecare cuvant
            		for (int x=0; x<tokens.length; x++) {
            			if(tokenToTake(tokens[x], pos[x])) {
            				//pentru ca sentence.words() face ca ( si ) sa fie LRB si RRB
            				String word = Util.getWordWithLRBRRB(tokens, x);
            				String lemma = Util.getWordWithLRBRRB(lemmas, x);
            				if(word.contains("(")){	
            					x = x+3;
            				} 
	            			features = "";
	            			//adaugare features word level, contextuale si sintactice
		            		features += wlf.getWordLevelFeatures(x, word, sentence);
		            		features += sf.getSintacticFeatures(x, sentence);
		            		features += cf.getContextualFeatures(x, word, sentence, totalDOC);
		            		
		            		//adaugare features lemma cuvintelor
		            		features = Util.setLemmaFeature(lemmaFeature, features, lemma);
		            		
		            		//adaugare categorie
		            		features = Util.setCategory(conObjects, features, word);
		            		
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
	
	private static Boolean tokenToTake(String token, String pos) {
		
		//verificare daca tokenul e caracter special sau numar
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
		
		//verificare daca e format dintr-o singura litera
		if(token.length() == 1)
			return false;
		
		//verificare daca POS e valabil (not CC,CD,DT,EX,IN,-LRB-,LS,PDT,PP,PRPR$,PRP,PRP$,TO,UH,WDT,WP,WRB)
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

