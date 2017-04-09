package generators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import medicalconcept.Util;

import org.clulab.processors.Document;
import org.clulab.processors.Processor;
import org.clulab.processors.Sentence;
import org.clulab.processors.corenlp.CoreNLPProcessor;

public class UniqueLemmaGenerator {
	
	public static void main(String[] args) throws IOException {
		
		Processor proc = new CoreNLPProcessor(true, true, 0, 100); 
		
		File folder = new File(Util.RAW_DOCS_PATH);
		PrintWriter writer = new PrintWriter(Util.LEMMA_OUT_FILE, "UTF-8");
		PrintWriter rejected = new PrintWriter(Util.REJECTED_WORDS, "UTF-8");
		
		//preluare lemma unic
		Set<String> uniqueLemma = new HashSet<String>();
		
		for (final File fileEntry : folder.listFiles()) {
            //System.out.println(fileEntry.getName().toString());
            FileReader fr = new FileReader(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString());
            BufferedReader br = new BufferedReader(fr);
            
            String CurrentLine;		//read line-by-line
            while ((CurrentLine = br.readLine()) != null) {
            	Document doc = proc.annotate(CurrentLine, false);
            	for (Sentence sentence : doc.sentences()) {
            		String[] lemma = sentence.lemmas().get();
            		String[] pos = sentence.tags().get();
            		for(int i=0 ; i < lemma.length ; i++) {
            			if(notSpCharAndNotNeededPos(lemma[i], pos[i])) {
            				String word = Util.getWordWithLRBRRB(lemma, i);
            				//pt ca sentence.words() face ca ( si ) sa fie LRB si RRB
            				if(word.contains("(")) {
            					i = i+3;
            				}
            				uniqueLemma.add(word.toLowerCase());
            			} else {
            				rejected.println(lemma[i] + "\t" + pos[i]);
            			}
            		}
            	}
			}
            br.close();
        }
		
		for(String str : uniqueLemma) {
			 writer.println(str);
		}
		writer.close();
		rejected.close();
		System.out.println("Lemma Printed In File!");
		
	}
	
	public static Boolean notSpCharAndNotNeededPos(String lemma, String pos) {
		
		//daca e formata dintr-un caracter nu poate fi concept
		if(lemma.length() == 1)
			return false;
		
		//verificare daca contine lemma caracter special sau numar
		for (int i = 0; i < lemma.length(); i++) {
			if(Util.SPECIAL_CHARS.contains(""+lemma.charAt(i))) {
				return false;
			}
			if(Util.NUMBERS.contains(""+lemma.charAt(i))) {
				return false;
			}
		}
		
		//verificare daca POS e valabil (not CC,CD,DT,EX,IN,-LRB-,LS,PDT,PP,PRPR$,PRP,PRP$,TO,UH)
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
