package generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import utils.GeneralUtils;
import medicalconcept.Concept;

/**
 * Concepts lenght analysis
 * 
 * @author Ariana
 *
 */
public class ConceptLengthHistogramGenerator {
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws IOException {
		File folderTextFiles = new File(GeneralUtils.RAW_CON_DOCS_PATH);
		HashMap<Integer,Integer> hmap = new HashMap<Integer,Integer>();
		for(int i=1; i<30; i++) {
			hmap.put(i,0);
		}
		
		FileWriter fw = new FileWriter(GeneralUtils.OUT_FILE, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		List<Concept> conObjects = new ArrayList<Concept>();
		
		int nrOfWords = 0;
		int value = 0;
		for (File fileEntry : folderTextFiles.listFiles()) {
			 conObjects = GeneralUtils.takeConObjectsIntoList(fileEntry);
			 for(Concept concept: conObjects) {
				 nrOfWords = concept.getName().split("\\s").length;
				 value = hmap.get(nrOfWords) + 1;
				 hmap.put(nrOfWords, value);
			 }
		}
		
		Set<Entry<Integer, Integer>> set = hmap.entrySet();
        Iterator<Entry<Integer, Integer>> iterator = set.iterator();
        out.println("Concept Length - Number of Concepts");
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            out.println(""+mentry.getKey()+"\t=\t"+mentry.getValue());
        }
        
        out.close();
        System.out.println("Concept Lengh Histogram finished!");
	}
}
