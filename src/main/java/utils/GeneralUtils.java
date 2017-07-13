package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medicalconcept.Concept;

import org.clulab.processors.Document;
import org.clulab.processors.Sentence;

/**
 * path for Files and general methods
 * @author Ariana
 *
 */
public final class GeneralUtils {
	// dummy constants
	public static final String DUMMY_STRING1 = "Alal bala Port3 IeSi gaD5a artera aorta . "
			+ "Mar-3fs ja & sd-pf sfa1 's assg . H/D/F 34 , g ad . No(2) >30% of marg . "
			+ "<< asd efd 4omg . In lov3 with 3 of mg and 4 mg artera aorta. The thoracic, artera aorta is broken x3 mg.";
	public static final String DUMMY_STRING2 = "The thoracic, artera aorta is broken x3 mg.";
	// it should be generated first
	public static final String DUMMY_FILE = "src/main/resources/TrainAndTestFiles/features170.txt"; 
	// it should be generated first
	public static final String DUMMY_FILE_TRAIN = "src/main/resources/TrainAndTestFiles/train256.txt"; 
	
	public static final String RAW_DOCS_PATH = "src/main/resources/ALL_TXT_FILES"; 
	public static final String RAW_CON_DOCS_PATH = "src/main/resources/ALL_CON_FILES"; 
	public static final String LEMMA_OUT_FILE = "src/main/resources/AnalysisFiles/lemma.txt"; 
	// it should be generated first
	public static final String ARFF_FILE = "src/main/resources/TrainAndTestFiles/medicalconcept10.arff"; 
	// it should be generated first
	public static final String REJECTED_WORDS = "src/main/resources/AnalysisFiles/rejected-lemma.txt";
	// general file computed in Main class. Its dimension is about GB. It should be generated first
	public static final String FEATURES_FILE = "src/main/resources/TrainAndTestFiles/generalFile.txt"; 
	// it should be generated first
	public static final String CON_PROCESSED_FILE = "src/main/resources/AnalysisFiles/con-procesat.txt"; 
	public static final String LIBLNR_TRAIN = "src/main/resources/TrainAndTestFiles/trainSet170LemmaPosChnkFilterNrWdsNoWN.txt"; 
	public static final String LIBLNR_TEST = "src/main/resources/TrainAndTestFiles/testSet256TOATEwCHNKSv2.txt"; 
	public static final String LIBLNR_OUT = "src/main/resources/TrainAndTestFiles/OUT/outDefault";
	public static final String POS_FILE = "src/main/resources/AnalysisFiles/POS.txt"; 
	public static final String CHUNKS_FILE = "src/main/resources/AnalysisFiles/CHUNKS.txt"; 
	// it should be generated first
	public static final String LIBLNR_NO_LEMMA_TRAIN = "src/main/resources/TrainAndTestFiles/trainSet170faraLemma.txt";
	// it should be generated first
	public static final String LIBLNR_NO_LEMMA_TEST = "src/main/resources/TrainAndTestFiles/testSet258faraLemma.txt"; 
	public static final String POS_USED_FOR_MEDICAL_CONCEPT = "src/main/resources/AnalysisFiles/4POSUsedForMedicalConcepts.txt";
	public static final String POS_USED_FOR_NONMEDICAL_CONCEPT = "src/main/resources/AnalysisFiles/4POSUsedForNonMedicalConcepts.txt";
	public static final String CHUNKS_USED_FOR_MED_CONC = "src/main/resources/AnalysisFiles/1ChunksUsedForMedicalConcepts.txt";
	public static final String CHUNKS_USED_FOR_NONMED_CONC = "src/main/resources/AnalysisFiles/1ChunksUsedForNonMedicalConcepts.txt";
	public static final String POS2_UNUSED_MED_CONCEPTS = "src/main/resources/AnalysisFiles/2POSUnusedForMedicalConcepts.txt";
	public static final String POS3_UNUSED_MED_CONCEPTS = "src/main/resources/AnalysisFiles/3POSUnusedForMedicalConcepts.txt";
	public static final String POS4_UNUSED_MED_CONCEPTS = "src/main/resources/AnalysisFiles/4POSUnusedForMedicalConcepts.txt";
	public static final String POS5_UNUSED_MED_CONCEPTS = "src/main/resources/AnalysisFiles/5POSUnusedForMedicalConcepts.txt";
	public static final String CHNK5_UNUSED_MED_CONCEPTS = "src/main/resources/AnalysisFiles/5ChunksUnusedForMedicalConcepts.txt";
	public static final String CHNK1_UNUSED_MED_CONCEPTS = "src/main/resources/AnalysisFiles/1ChunksUnusedForMedicalConcepts.txt";
	// it should be generated first
	public static final String SHORTER_FEATURES_FILE = "src/main/resources/TrainAndTestFiles/features170noPOS.txt"; 
	// it should be generated first
	public static final String LIBLNR_OUT_WITH_NR_OF_WORDS = "src/main/resources/TrainAndTestFiles/trainSet170withNrOfWords.txt"; 
	// it should be generated first
	public static final String CSV_FROM_LIBLINEAR = "src/main/resources/AnalysisFiles/forFeatureSelection.txt";
	// it should be generated first
	public static final String CSV_WITHOUT_LEMMA = "src/main/resources/AnalysisFiles/forFeatureSelectionNoLemma.txt";
	// it should be downloaded from the internet
	public static final String IRIS_DATA = "src/main/resources/AnalysisFiles/iris.data";
	public static final String FEATURE_SCORING = "src/main/resources/AnalysisFiles/featureScoring.txt";
	// it should be generated first
	public static final String FEATURE_RANKING = "src/main/resources/AnalysisFiles/featureRanking.txt";
	public static final String FETURES_FILTERED = "src/main/resources/AnalysisFiles/featuresNotToTake.txt";
	public static final String FETURES_FILTERED_TO_TAKE = "src/main/resources/AnalysisFiles/featuresToTake.txt";
	// it should be generated first
	public static final String TRAIN_FEATURES_FILTERED = "src/main/resources/TrainAndTestFiles/train170FeatureSelection.txt";
	// it should be generated first
	public static final String TEST_FEATURES_FILTERED = "src/main/resources/TrainAndTestFiles/test256FeatureSelection.txt";
	// Histogram of concept length 
	public static final String OUT_FILE = "src/main/resources/AnalysisFiles/ConceptLengthHistogram.txt";
	public static final String FINAL_RESULTS_FILEPATH = "src/main/resources/Results/FinalResults.xlsx";
	// nr of features for our training file
	public static final int NR_OF_INSTANCES_IN_ROW = 4763;
	// nr of static features
	public static final int NR_OF_INSTANCES_WITHOUT_LEMMA = 359;
	
	public static final String SPECIAL_CHARS = "!#$%&'*+,-./:;<=>?@[]^_`{|}~";
	public static final String NUMBERS = "0123456789";
	
	public static final String STATIC_FEATURES_HEADER = "Nr. of words" + "\t" + "Length" + "\t" + "Begin-UpperCase" + "\t" + "All-UpperCase" + "\t" 
			+ "MixedCase" + "\t" + "Ampersand" + "\t" + "Comma" + "\t" + "Period" + "\t" + "GreaterSign" + "\t" + "LessSign" + "\t" 
			+ "Minus" + "\t" + "Paranthesis" + "\t" + "QuoteMark" + "\t" + "Percent" + "\t" + "Slash" + "\t" + "Digits" + "\t" 
			+ "SpCharBef" + "\t" + "SpCharBBef" + "\t" +"SpCharBBBef" + "\t" + "SpCharAft" + "\t" + "SpCharAAft" + "\t" + "SpCharAAAft" + "\t" 
			+ "PartOfSpeech" + "\t" + "Chunking" + "\t" + "PosBef" + "\t" + "PosBBef" + "\t" + "PosBBBef" + "\t" + "PosAft" + "\t" 
			+ "PosAAft" + "\t" + "PosAAAft" + "\t" + "Is2NeighMG" + "\t" + "CommaBefAft" + "\t" + "NrDefWN" + "\t" + "TermFreq";
	
	public static String mkString(String[] sa, String sep) {
		StringBuilder os = new StringBuilder();
		for (int i = 0; i < sa.length; i++) {
			if (i > 0)
				os.append(sep);
			os.append(sa[i]);
		}
		return os.toString();
	}
	
	public static String concatenateString(String[] sa) {
		StringBuilder os = new StringBuilder();
		for (int i = 0; i < sa.length; i++) {
			os.append(sa[i]);
		}
		return os.toString();
	}
	
	public void getSentenceSplitting(Document doc) {
		for (Sentence sentence : doc.sentences()) {
			System.out.println(sentence);
		}
	}
	
	public static List<String> getLemmaFromFile() throws IOException {
		List<String> lemma = new ArrayList<String>();
		FileReader fr = new FileReader(LEMMA_OUT_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine;		//read line-by-line
        while ((CurrentLine = br.readLine()) != null) {
        	lemma.add(CurrentLine.toLowerCase());
        }
        br.close();
        return lemma;
	}
	
	public static Set<String> getPOSFromFile() throws IOException {
		Set<String> pos = new HashSet<String>();
		FileReader fr = new FileReader(POS_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine;		//read line-by-line
        while ((CurrentLine = br.readLine()) != null) {
        	pos.add(CurrentLine);
        }
        br.close();
        return pos;
	}
	
	public static Set<String> getChunksFromFile() throws IOException {
		Set<String> chunks = new HashSet<String>();
		FileReader fr = new FileReader(CHUNKS_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine;		//read line-by-line
        while ((CurrentLine = br.readLine()) != null) {
        	chunks.add(CurrentLine);
        }
        br.close();
        return chunks;
	}
	
	public static String getWordWithLRBRRB(String[] tokens, int x) {
		String word = null;
		if(tokens.length - x > 3 && tokens[x+1].toLowerCase().equals("-lrb-") && tokens[x+3].toLowerCase().equals("-rrb-")) {
			word = tokens[x] + "(" + tokens[x+2] + ")";
		}
		else word = tokens[x];
		return word;		
	}
	
	public static String setLemmaFeature(List<String> lemmaFeature, String features, String lemma) {
		for(String str : lemmaFeature) {
			if(str.equals(lemma.toLowerCase())) {
				features += "\t" + "true";
			} else {
				features += "\t" + "false";
			}
		}
		
		return features;
	}
	
	public static String setLemmaFeatureForNgrams(List<String> lemmaFeature, String features, String[] lemmas) {
		
		for (String str : lemmaFeature) {
			boolean notFound = true;
			for (String lemma : lemmas) {	
				if (str.equals(lemma.toLowerCase()) && notFound) {
					features += "\t" + "true";
					notFound = false;
				} 
			}
			if(notFound) {
				features += "\t" + "false";
			}
		}

		return features;
	}
	
	public static String setCategory(List<Concept> conObjects, String features, String word) {
		Boolean notMedicalConcept = true;
		
		for(int i = 0; i<conObjects.size() && notMedicalConcept; i++) {
			if(concatenateString(conObjects.get(i).getName().split("\\s")).toLowerCase().equals(concatenateString(word.split("\\s")).toLowerCase())) {
				notMedicalConcept = false;
				features += "\t" + conObjects.get(i).getCategory().toLowerCase();
			}
		}
		if(notMedicalConcept) {
			features += "\t" + "none";
		}
		
		return features;
	}
	
	public static String concatenateWithUnderscore(String[] str) {
		String result="";
		for(int i=0; i<str.length; i++) {
			if(i==str.length-1){
				result += str[i];
			} else {
				result += str[i] + "_";
			}
		}
		return result;
	}
	
	public static List<Concept> takeConObjectsIntoList(File fileEntry) throws IOException {
		FileWriter fw = new FileWriter(GeneralUtils.CON_PROCESSED_FILE, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		FileReader frCON = new FileReader(GeneralUtils.RAW_CON_DOCS_PATH + "/" + fileEntry.getName().toString().substring(0,fileEntry.getName().toString().length()-3) + "con");
		BufferedReader br = new BufferedReader(frCON);
		List<Concept> result = new ArrayList<Concept>();
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			String[] tokens = currentLine.split("\"|:");
			String concept = null;
			int line = 0;
			String category = null;
			for (int x=0; x<tokens.length; x++) {
				if(x==1) {
					if(tokens[x].length()>1 && tokens[x].substring(tokens[x].length()-1, tokens[x].length()).equals(".") ) {
						concept = tokens[x].substring(0,tokens[x].length()-1); 
					} else {
						concept = tokens[x];
					}
				}
				if(x==2){
					line = Integer.parseInt(tokens[x].substring(1,tokens[x].length()));
				}
				if(x==5)	category = tokens[x]; 
			}
			Concept conceptObj = new Concept();
			conceptObj.setName(concept);
			conceptObj.setLine(line);
			conceptObj.setCategory(category);
			result.add(conceptObj);
		}
		
		for(Concept con: result) {
			out.println(con.getName()+"|"+con.getLine()+"|"+con.getCategory());
		}
		
		out.close();
		br.close();
		return result;
	}
}
