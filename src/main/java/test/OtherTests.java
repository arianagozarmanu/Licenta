package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import medicalconcept.Util;

import org.clulab.processors.Document;
import org.clulab.processors.Processor;
import org.clulab.processors.Sentence;
import org.clulab.processors.corenlp.CoreNLPProcessor;

public class OtherTests {

	public static void main(String[] args) throws IOException {
		String RAW_DOCS_PATH = "E:/An4/Licenta/sample_input_data";
		readFromFile(RAW_DOCS_PATH);
		//readFileWithClulab(RAW_DOCS_PATH);
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
    			String[] result = token.split("\\s");
    			for (int x=0; x<result.length; x++) {
    				System.out.print(result[x]);
    			}
    			System.out.println();
        	}
		}
	}
}
