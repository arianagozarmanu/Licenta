package medicalconcept;

import java.io.*;

import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;

public class Main {

	public static final String DUMMY_STRING = "Alal bala Port3 IeSi gaD5a. "
			+ "Mar-3fs ja & sd-pf sfa1 's assg. H/D/F 34 ,g ad. No(2) >30% of marg. "
			+ "<< asd efd 4omg.";
	
	public static final String RAW_DOCS_PATH = "E:/An4/Licenta/DATASET/concept_assertion_relation_training_data.tar/concept_assertion_relation_training_data/beth/txt";
	 
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		
		Processor proc = new CoreNLPProcessor(true, false, 0, 100);
		Document doc = proc.annotate(DUMMY_STRING, false);
		
		WordLevelFeatures wlf = new WordLevelFeatures();
		wlf.showWordLevelFeatures(doc);
		
		//SintacticFeatures sf = new SintacticFeatures();
		//sf.showSintacticFeatures(doc);
		
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

