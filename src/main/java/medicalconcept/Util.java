package medicalconcept;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.clulab.processors.Document;
import org.clulab.processors.Sentence;

public final class Util {

	public static final String DUMMY_STRING1 = "Alal bala Port3 IeSi gaD5a . "
			+ "Mar-3fs ja & sd-pf sfa1 's assg . H/D/F 34 , g ad . No(2) >30% of marg . "
			+ "<< asd efd 4omg . In lov3 with 3 of mg and 4 mg .";
	
	public static final String DUMMY_STRING2 = "Alal bala Port3 IeSi gaD5a . "
			+ "Mar-3fs ja & sd-pf sfa1 's assg . 11. Furosemide 20 mg Tablet Sig : One ( 1 ) Tablet PO once a day for 2 weeks ."
			+ "No creams , lotions , powders , or ointments to incisions . Dr Day in 4 weeks (( 469 ) 587-0462 ) please call for appointment";
	
	public static final String DUMMY_STRING3 = "Alal bala Port3 IeSi gaD5a . "
			+ "Coronary artery disease s/p Coronary Artery Bypass Graft x3 "
			+ "PMH : Carpal tunnel syndrome , Hypertension , Hyperlipidemia , Arthritis";
	
	public static final String RAW_DOCS_PATH = "E:/An4/Licenta/DATASET/DUMMY";
	public static final String LEMMA_OUT_FILE = "E:/An4/Licenta/DATASET/Lemma/lemma.txt";
	public static final String REJECTED_WORDS = "E:/An4/Licenta/DATASET/Lemma/rejected-lemma.txt";
	public static final String FEATURES_FILE = "E:/An4/Licenta/DATASET/Lemma/features.txt";
	
	public static final String SPECIAL_CHARS = "!#$%&'()*+,-./:;<=>?@[]^_`{|}~";
	public static final String NUMBERS = "0123456789";
	
	public static final String FEATURE_HEADER = "Concept" + "\t" + "Length" + "\t" + "Begin-UpperCase" + "\t" + "All-UpperCase" + "\t" 
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
        	lemma.add(CurrentLine);
        }
        br.close();
        return lemma;
	}
}
