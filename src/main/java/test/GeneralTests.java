package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import org.clulab.processors.Document;
import org.clulab.processors.Processor;
import org.clulab.processors.Sentence;
import org.clulab.processors.corenlp.CoreNLPProcessor;

import utils.GeneralUtils;

public class GeneralTests {

	public static void main(String[] args) throws IOException {
		// String RAW_DOCS_PATH =
		// Util.RAW_DOCS_PATH;//"E:/An4/Licenta/sample_input_data";
		// readFromFile(RAW_DOCS_PATH);
		// readFileWithClulab(RAW_DOCS_PATH);
		// takeConObjectsInList();
		// testFeaturesFile();
		// testPOSUsedForConcepts();
		// testPOSUsedForNonConcepts();
		// testNPOSUsedForConcepts(4);
		// testNPOSUsedForNonConcepts(4);
		// testNChunksUsedForNonConcepts(5);
		// testNChunksUsedForConcepts(5);
		// testCunksUsedForConcepts();
		// testCunksUsedForNonConcepts();
		getNrOfTokensSplitBySpaceOnRow(GeneralUtils.CSV_WITHOUT_LEMMA, ",");
	}

	public static void testCunksUsedForConcepts() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.CHUNKS_USED_FOR_MED_CONC, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		// preluare Chunks in HashMap
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		FileReader frchks = new FileReader(GeneralUtils.CHUNKS_FILE);
		BufferedReader brchks = new BufferedReader(frchks);
		String line; // read line-by-line
		int count0 = 1;
		while ((line = brchks.readLine()) != null && count0 <= 22) {
			hmap.put(line, 0);
			count0++;
		}
		hmap.put("O", 0);
		brchks.close();
		// System.out.println(hmap);
		// System.out.println(hmap.size());

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] chnkOfTokens = tokens[22].split("_");
				// System.out.println(posOfTokens[0]);
				if (!tokens[tokens.length - 1].equals("none")
						&& !chnkOfTokens[0].equals("''")
						&& !chnkOfTokens[0].equals(":")) {
					Integer nr = hmap.get(chnkOfTokens[0]);
					nr++;
					hmap.put(chnkOfTokens[0], nr);
				}
			}
			count++;
		}
		br.close();

		for (String key : hmap.keySet()) {
			out.println(key + ":" + hmap.get(key));
		}
		out.close();
		System.out.println("Analiza chunks terminata");
	}

	public static void testCunksUsedForNonConcepts() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.CHUNKS_USED_FOR_NONMED_CONC, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		// preluare POS in HashMap
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		FileReader frchks = new FileReader(GeneralUtils.CHUNKS_FILE);
		BufferedReader brchks = new BufferedReader(frchks);
		String line; // read line-by-line
		int count0 = 1;
		while ((line = brchks.readLine()) != null && count0 <= 22) {
			hmap.put(line, 0);
			count0++;
		}
		hmap.put("O", 0);
		brchks.close();
		// System.out.println(hmap);
		// System.out.println(hmap.size());

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] chnkOfTokens = tokens[22].split("_");
				// System.out.println(posOfTokens[0]);
				if (tokens[tokens.length - 1].equals("none")
						&& !chnkOfTokens[0].equals("''")
						&& !chnkOfTokens[0].equals(":")) {
					Integer nr = hmap.get(chnkOfTokens[0]);
					nr++;
					hmap.put(chnkOfTokens[0], nr);
				}
			}
			count++;
		}
		br.close();

		for (String key : hmap.keySet()) {
			out.println(key + ":" + hmap.get(key));
		}
		out.close();
		System.out.println("Analiza chunks terminata");
	}

	public static void testNChunksUsedForNonConcepts(int n) throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.CHUNKS_USED_FOR_NONMED_CONC, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		HashSet<String> hs = new HashSet<String>();

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] chnkOfTokens = tokens[22].split("_");
				// System.out.println(posOfTokens[0]);
				if (chnkOfTokens.length >= n
						&& tokens[tokens.length - 1].equals("none")
						&& !chnkOfTokens[0].equals("''")
						&& !chnkOfTokens[0].equals(":")) {
					String chnk = "";
					for (int k = 0; k < n; k++) {
						if (k == (n - 1)) {
							chnk += chnkOfTokens[k];
						} else {
							chnk += chnkOfTokens[k] + "_";
						}
					}
					hs.add(chnk);
				}
			}
			count++;
		}
		br.close();

		for (String s : hs) {
			out.println(s);
		}

		out.close();
		System.out.println("n-Chunks Analysis end!");
	}

	public static void testNChunksUsedForConcepts(int n) throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.CHUNKS_USED_FOR_MED_CONC, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		HashSet<String> hs = new HashSet<String>();

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] chnkOfTokens = tokens[22].split("_");
				// System.out.println(posOfTokens[0]);
				if (chnkOfTokens.length >= n
						&& !tokens[tokens.length - 1].equals("none")
						&& !chnkOfTokens[0].equals("''")
						&& !chnkOfTokens[0].equals(":")) {
					String chnk = "";
					for (int k = 0; k < n; k++) {
						if (k == (n - 1)) {
							chnk += chnkOfTokens[k];
						} else {
							chnk += chnkOfTokens[k] + "_";
						}
					}
					hs.add(chnk);
				}
			}
			count++;
		}
		br.close();

		for (String s : hs) {
			out.println(s);
		}

		out.close();
		System.out.println("n-Chunks Analysis end!");
	}

	public static void testNPOSUsedForNonConcepts(int n) throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.POS_USED_FOR_NONMEDICAL_CONCEPT,
				false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		HashSet<String> hs = new HashSet<String>();

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] posOfTokens = tokens[21].split("_");
				// System.out.println(posOfTokens[0]);
				if (posOfTokens.length >= n
						&& tokens[tokens.length - 1].equals("none")
						&& !posOfTokens[0].equals("''")
						&& !posOfTokens[0].equals(":")) {
					String pos = "";
					for (int k = 0; k < n; k++) {
						if (k == (n - 1)) {
							pos += posOfTokens[k];
						} else {
							pos += posOfTokens[k] + "_";
						}
					}
					hs.add(pos);
				}
			}
			count++;
		}
		br.close();

		for (String s : hs) {
			out.println(s);
		}

		out.close();
		System.out.println("n-POS Analysis end!");
	}

	public static void testNPOSUsedForConcepts(int n) throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.POS_USED_FOR_MEDICAL_CONCEPT, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		HashSet<String> hs = new HashSet<String>();

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] posOfTokens = tokens[21].split("_");
				// System.out.println(posOfTokens[0]);
				if (posOfTokens.length >= n
						&& !tokens[tokens.length - 1].equals("none")
						&& !posOfTokens[0].equals("''")
						&& !posOfTokens[0].equals(":")) {
					String pos = "";
					for (int k = 0; k < n; k++) {
						if (k == (n - 1)) {
							pos += posOfTokens[k];
						} else {
							pos += posOfTokens[k] + "_";
						}
					}
					hs.add(pos);
				}
			}
			count++;
		}
		br.close();

		for (String s : hs) {
			out.println(s);
		}

		out.close();
		System.out.println("n-POS Analysis ended!");
	}

	public static void testPOSUsedForConcepts() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.POS_USED_FOR_MEDICAL_CONCEPT, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		// preluare POS in HashMap
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		FileReader frPOS = new FileReader(GeneralUtils.POS_FILE);
		BufferedReader brPOS = new BufferedReader(frPOS);
		String line; // read line-by-line
		while ((line = brPOS.readLine()) != null) {
			hmap.put(line, 0);
		}
		brPOS.close();
		// System.out.println(hmap);
		// System.out.println(hmap.size());

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] posOfTokens = tokens[21].split("_");
				// System.out.println(posOfTokens[0]);
				if (!tokens[tokens.length - 1].equals("none")
						&& !posOfTokens[0].equals("''")
						&& !posOfTokens[0].equals(":")) {
					Integer nr = hmap.get(posOfTokens[0]);
					nr++;
					hmap.put(posOfTokens[0], nr);
				}
			}
			count++;
		}
		br.close();

		for (String key : hmap.keySet()) {
			out.println(key + ":" + hmap.get(key));
		}
		out.close();
		System.out.println("Analiza POS terminata");
	}

	public static void testPOSUsedForNonConcepts() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
		BufferedReader br = new BufferedReader(fr);
		String currentLine; // read line-by-line

		FileWriter fw = new FileWriter(GeneralUtils.POS_USED_FOR_NONMEDICAL_CONCEPT,
				false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		// preluare POS in HashMap
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		FileReader frPOS = new FileReader(GeneralUtils.POS_FILE);
		BufferedReader brPOS = new BufferedReader(frPOS);
		String line; // read line-by-line
		while ((line = brPOS.readLine()) != null) {
			hmap.put(line, 0);
		}
		brPOS.close();
		// System.out.println(hmap);
		// System.out.println(hmap.size());

		int count = 0;
		while ((currentLine = br.readLine()) != null) {
			if (count > 0) {
				String[] tokens = currentLine.split("\\s");
				String[] posOfTokens = tokens[21].split("_");
				// System.out.println(posOfTokens[0]);
				if (tokens[tokens.length - 1].equals("none")
						&& !posOfTokens[0].equals("''")
						&& !posOfTokens[0].equals(":")
						&& !posOfTokens[0].equals("``")) {
					Integer nr = hmap.get(posOfTokens[0]);
					nr++;
					hmap.put(posOfTokens[0], nr);
				}
			}
			count++;
		}
		br.close();

		for (String key : hmap.keySet()) {
			out.println(key + ":" + hmap.get(key));
		}
		out.close();
		System.out.println("Analiza POS terminata");
	}

	// preluare categorie pentru fiecare cuvant din .con files
	public static void takeConObjectsInList() throws IOException {

		File folderTextFiles = new File(GeneralUtils.RAW_DOCS_PATH);

		String currentLine;
		for (final File fileEntry : folderTextFiles.listFiles()) {
			FileReader frCON = new FileReader(
					GeneralUtils.RAW_CON_DOCS_PATH
							+ "/"
							+ fileEntry
									.getName()
									.toString()
									.substring(
											0,
											fileEntry.getName().toString()
													.length() - 3) + "con");
			BufferedReader brCON = new BufferedReader(frCON);
			while ((currentLine = brCON.readLine()) != null) {
				String[] tokens = currentLine.split("\"|:");
				String concept = null;
				int line = 0;
				String category = null;
				for (int x = 0; x < tokens.length; x++) {
					if (x == 1) {
						if (tokens[x].length() > 1
								&& tokens[x].substring(tokens[x].length() - 1,
										tokens[x].length()).equals(".")) {
							concept = tokens[x].substring(0,
									tokens[x].length() - 1);
						} else {
							concept = tokens[x];
						}
					}
					if (x == 2)
						line = Integer.parseInt(tokens[x].substring(1,
								tokens[x].length()));
					if (x == 5)
						category = tokens[x];
				}
				System.out.println(concept + "|" + line + "|" + category);

			}
			brCON.close();
		}
	}

	public static void readFromFile(String RAW_DOCS_PATH) throws IOException {

		FileReader fr = new FileReader(RAW_DOCS_PATH + "/" + "record-13.con");
		BufferedReader br = new BufferedReader(fr);

		String CurrentLine; // read line-by-line
		// String document = "";
		while ((CurrentLine = br.readLine()) != null) {
			System.out.println(CurrentLine);
			// Nu merge asa
			// document += CurrentLine + System.lineSeparator();
			// System.out.println(document);
		}
		br.close();
	}

	public static void readFileWithClulab(String RAW_DOCS_PATH)
			throws IOException {
		Processor proc = new CoreNLPProcessor(true, false, 0, 100);

		FileReader fr = new FileReader(RAW_DOCS_PATH + "/" + "record-13.txt");
		BufferedReader br = new BufferedReader(fr);

		String CurrentLine; // read line-by-line
		while ((CurrentLine = br.readLine()) != null) {
			Document doc = proc.annotate(CurrentLine, false);
			for (Sentence sentence : doc.sentences()) {
				String token = GeneralUtils.mkString(sentence.words(), " ");
				System.out.println(token);
				String[] result = token.split("\\s");
				for (int x = 0; x < result.length; x++) {
					String word = null;
					// pentru ca sentence.words() face ca ( si ) sa fie LRB si
					// RRB
					if (result.length - x > 3 && result[x + 1].equals("-LRB-")
							&& result[x + 3].equals("-RRB-")) {
						word = result[x] + "(" + result[x + 2] + ")";
						x = x + 3;
					} else
						word = result[x];
					System.out.print(word + " ");
				}
				System.out.println();
			}
		}
		br.close();
	}

	/**
	 * Testul preia fisierul de features si verifica daca numarul elementelor de
	 * pe fiecare linie este acelasi
	 * 
	 * @throws IOException
	 */
	public static void testFeaturesFile() throws IOException {
		FileReader fr = new FileReader(GeneralUtils.FEATURES_FILE);
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
			System.out.println("Fisierul este valid! Un sir are " + next
					+ " instante.");
		} else {
			System.out.println("Fisierul are " + next + " elemente in loc de "
					+ first + " la linia " + (count - 1));
		}
		br.close();
	}

	public static void getNrOfTokensSplitBySpaceOnRow(String file, String splitter)
			throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(file));
		String CurrentLine = br.readLine(); // read line-by-line
		System.out.println("Number of instances = "
				+ CurrentLine.split(splitter).length);
		br.close();
	}

}
