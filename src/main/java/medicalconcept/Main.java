package medicalconcept;

import java.io.*;

import net.didion.jwnl.JWNLException;

import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;

public class Main {

	public static final String DUMMY_STRING1 = "Alal bala Port3 IeSi gaD5a . "
			+ "Mar-3fs ja & sd-pf sfa1 's assg . H/D/F 34 , g ad . No(2) >30% of marg . "
			+ "<< asd efd 4omg . In lov3 with 3 of mg and 4 mg .";
	
	public static final String DUMMY_STRING2 = "Alal bala Port3 IeSi gaD5a . "
			+ "Mar-3fs ja & sd-pf sfa1 's assg . 11. Furosemide 20 mg Tablet Sig : One ( 1 ) Tablet PO once a day for 2 weeks ."
			+ "No creams , lotions , powders , or ointments to incisions . Dr Day in 4 weeks (( 469 ) 587-0462 ) please call for appointment";
	
	public static final String DUMMY_STRING3 = "Alal bala Port3 IeSi gaD5a . "
			+ "Coronary artery disease s/p Coronary Artery Bypass Graft x3 "
			+ "PMH : Carpal tunnel syndrome , Hypertension , Hyperlipidemia , Arthritis";
	
	public static final String RAW_DOCS_PATH = "E:/An4/Licenta/sample_input_data";
	 
	public static void main(String[] args) throws IOException, JWNLException {
		
		Processor proc = new CoreNLPProcessor(true, true, 0, 100);
		Document doc = proc.annotate(DUMMY_STRING1, false);
		
		//WordLevelFeatures wlf = new WordLevelFeatures();
		//wlf.showWordLevelFeatures(doc);
		
		//SintacticFeatures sf = new SintacticFeatures();
		//sf.showSintacticFeatures(doc);
		
		ContextualFeatures cf = new ContextualFeatures();
		Document doc1 = proc.annotate(DUMMY_STRING3, false);
		Document doc2 = proc.annotate(DUMMY_STRING2, false);
		Document[] docs={doc, doc1, doc2};
		cf.showContextualFeatures(doc, docs);
		
		//Testare diferenta TFIDF normal si smooth
/*		double tf = cf.getTermFrequency("bala", doc);
		System.out.println("TF="+tf);
		double idf = cf.getInverseDocFrequency("bala", docs);
		System.out.println("IDF=");
		System.out.println("TF*IDF="+cf.getTFIDF(tf, idf)+" Smooth="+cf.getSmoothTFIDF(tf, idf));
		*/
		
		//cod pentru citirea tuturor fisierelor
		/*
		File folder = new File(RAW_DOCS_PATH);
		for (final File fileEntry : folder.listFiles()) {
            //System.out.println(fileEntry.getName().toString());
            FileReader fr = new FileReader(RAW_DOCS_PATH + "/" + fileEntry.getName().toString());
            BufferedReader br = new BufferedReader(fr);
            
            String CurrentLine;		//read line-by-line
            while ((CurrentLine = br.readLine()) != null) {
				//System.out.println(CurrentLine);
			}
        }
		*/
        	

	}

}

