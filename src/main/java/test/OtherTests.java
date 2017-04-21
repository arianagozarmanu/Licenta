package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import medicalconcept.Concept;
import medicalconcept.Util;

import org.clulab.processors.Document;
import org.clulab.processors.Processor;
import org.clulab.processors.Sentence;
import org.clulab.processors.corenlp.CoreNLPProcessor;

public class OtherTests {

	public static void main(String[] args) throws IOException {
		String RAW_DOCS_PATH = Util.RAW_DOCS_PATH;//"E:/An4/Licenta/sample_input_data";
		//readFromFile(RAW_DOCS_PATH);
		//readFileWithClulab(RAW_DOCS_PATH);
		//takeConObjectsInList();
		testFeaturesFile();
	}
	
	//preluare categorie pentru fiecare cuvant din .con files
	public static void takeConObjectsInList() throws IOException {

		File folderTextFiles = new File(Util.RAW_DOCS_PATH);
		List<Concept> result = new ArrayList<Concept>();
		String currentLine;
		for (final File fileEntry : folderTextFiles.listFiles()) {
			FileReader frCON = new FileReader(Util.RAW_CON_DOCS_PATH + "/" + fileEntry.getName().toString().substring(0,fileEntry.getName().toString().length()-3) + "con");
			BufferedReader brCON = new BufferedReader(frCON);
			 while ((currentLine = brCON.readLine()) != null) {
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
						 if(x==2)	line = Integer.parseInt(tokens[x].substring(1,tokens[x].length()));
						 if(x==5)	category = tokens[x]; 
					 }
					 System.out.println(concept + "|" + line + "|" + category);

			 }
		}
	}
	
	public static void readFromFile(String RAW_DOCS_PATH) throws IOException {

        FileReader fr = new FileReader(RAW_DOCS_PATH + "/" + "record-13.con");
        BufferedReader br = new BufferedReader(fr);
        
        String CurrentLine;		//read line-by-line
        String document = "";
        while ((CurrentLine = br.readLine()) != null) {
        	System.out.println(CurrentLine);
        	//Nu merge asa
        	//document += CurrentLine + System.lineSeparator();
        	//System.out.println(document);
		}
	}
	
	public static void readFileWithClulab(String RAW_DOCS_PATH) throws IOException {
		Processor proc = new CoreNLPProcessor(true, false, 0, 100);
		
		FileReader fr = new FileReader(RAW_DOCS_PATH + "/" + "record-13.txt");
        BufferedReader br = new BufferedReader(fr);
        
        String CurrentLine;		//read line-by-line
        while ((CurrentLine = br.readLine()) != null) {
			Document doc = proc.annotate(CurrentLine, false);
			for (Sentence sentence : doc.sentences()) {
        		String token = Util.mkString(sentence.words(), " ");
        		System.out.println(token);
    			String[] result = token.split("\\s");
    			for (int x=0; x<result.length; x++) {
    				String word=null;
    				//pentru ca sentence.words() face ca ( si ) sa fie LRB si RRB
    				if(result.length - x > 3 && result[x+1].equals("-LRB-") && result[x+3].equals("-RRB-")) {
    					word = result[x] + "(" + result[x+2] + ")";
    					x = x+3;
    				}
    				else word = result[x];
    				System.out.print(word+" ");
    			}
    			System.out.println();
        	}
		}
	}
	
	/**
	 * Testul preia fisierul de features si verifica daca 
	 * numarul elementelor de pe fiecare linie este acelasi 
	 * @throws IOException
	 */
	public static void testFeaturesFile() throws IOException {
		FileReader fr = new FileReader(Util.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String CurrentLine; // read line-by-line
		int count = 1;
		int first = 0, next = 0;
		Boolean isOK = true;
		while ((CurrentLine = br.readLine()) != null && isOK) {
			if (count == 1) {
				first = next = CurrentLine.split("\\s").length;
			} else {
				next = CurrentLine.split("\\s").length;
				if (first != next) {
					isOK = false;
				}
			}
			count++;
		}

		if (isOK) {
			System.out.println("Fisierul este valid! Un sir are " + next + " instante.");
		} else {
			System.out.println("Fisierul are " + next + " elemente in loc de "
					+ first + " la linia " + (count - 1));
		}
		br.close();
	}
}
