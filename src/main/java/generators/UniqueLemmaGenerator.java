package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import medicalconcept.MapUtil;
import medicalconcept.Util;

import org.clulab.processors.Document;
import org.clulab.processors.Processor;
import org.clulab.processors.Sentence;
import org.clulab.processors.corenlp.CoreNLPProcessor;

import features.ContextualFeatures;

public class UniqueLemmaGenerator {
	
	public static final String LEMMA_TF_ORDERED = "E:/An4/Licenta/DATASET/OTHERS_analiza/LemmaTF";
	
	public static void main(String[] args) throws IOException {
		
		Processor proc = new CoreNLPProcessor(true, false, 0, 100); 
		
		//creare fisier cu lemma
		File folder = new File(Util.RAW_DOCS_PATH);
		PrintWriter writer = new PrintWriter(Util.LEMMA_OUT_FILE, "UTF-8");
		PrintWriter rejected = new PrintWriter(Util.REJECTED_WORDS, "UTF-8");
		
		//preluare lemma unic
		Set<String> uniqueLemma = new HashSet<String>();
		
		for (final File fileEntry : folder.listFiles()) {
            //System.out.println(fileEntry.getName().toString());
            FileReader fr = new FileReader(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString());
            BufferedReader br = new BufferedReader(fr);
            
            //creare fisier analiza lemma conform TF ------------------------ start declaratii analiza
//    		PrintWriter outLemmaTF = new PrintWriter(LEMMA_TF_ORDERED + "/" + "lemmaTF" + fileEntry.getName().toString(), "UTF-8");
//    		
//            //creare doc cu fisierul intreg pentru TF
//            Scanner scanner = new Scanner(new File(Util.RAW_DOCS_PATH + "/" + fileEntry.getName().toString()));
//            String text = scanner.useDelimiter("\\A").next();
//            Document totalDOC = proc.annotate(text, false);
//            scanner.close();
//            
//            ContextualFeatures cf = new ContextualFeatures();
//            Double tf = 0.0;
//            HashMap<String,Double> hmap = new HashMap<String,Double>();
            //-------------------------------------- end declaratii analiza
            
            String CurrentLine;		//read line-by-line
            while ((CurrentLine = br.readLine()) != null) {
            	Document doc = proc.annotate(CurrentLine, false);
            	for (Sentence sentence : doc.sentences()) {
            		String[] token = sentence.words();
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
            				//tf = cf.getTermFrequency(token[i], totalDOC);
            				//System.out.println("TF="+tf+"; word="+word);
            				//hmap.put(token[i],tf);
            			} else {
            				rejected.println(lemma[i] + "\t" + pos[i]);
            			}
            		}
            	}
			}
            br.close();
//			  Analiza TF pentru lemma           
//            hmap = (HashMap<String, Double>) MapUtil.sortByValue(hmap);
//            Set<Entry<String, Double>> set = hmap.entrySet();
//            Iterator<Entry<String, Double>> iterator = set.iterator();
//            while(iterator.hasNext()) {
//                Map.Entry mentry = (Map.Entry)iterator.next();
//                outLemmaTF.println(""+mentry.getKey()+"\t\t=\t"+mentry.getValue());
//            }
//            outLemmaTF.close();
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
		
		if(lemma.toUpperCase().equals("-LRB-") || lemma.toUpperCase().equals("-RRB-"))
			return false;
		
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
