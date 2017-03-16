package medicalconcept;

import org.clulab.processors.Document;
import org.clulab.processors.Sentence;

public final class Util {

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
}
