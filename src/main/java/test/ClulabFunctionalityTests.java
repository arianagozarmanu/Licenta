package test;

import org.clulab.struct.CorefMention;
import org.clulab.struct.DirectedGraphEdgeIterator;
import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;

/**
 * CLULab library functionalities test
 * @author Ariana
 *
 */
public class ClulabFunctionalityTests {
	public static void main(String[] args) {
		// create the processor
		Processor proc = new CoreNLPProcessor(true, true, 0, 100);

		Document doc = proc.annotate(
				"67 y/o male with worsening shortness of breath. He did have burst of atrial fibrillation and was started on a Amiodarone gtt ."
				+ "On post-op day five he appeared to have left arm phlebitis and was started on antibiotics ."
				+ "The mitral valve leaflets are mildly thickened ." + "Report any fever greater than 101",
				false);

		int sentenceCount = 0;
		for (Sentence sentence : doc.sentences()) {
			System.out.println("Sentence #" + sentenceCount + ":");
			System.out.println("Tokens: " + mkString(sentence.words(), " "));
			System.out.println("Start character offsets: " + mkString(sentence.startOffsets(), " "));
			System.out.println("End character offsets: " + mkString(sentence.endOffsets(), " "));

			if (sentence.lemmas().isDefined()) {
				System.out.println("Lemmas: " + mkString(sentence.lemmas().get(), " "));
			}
			if (sentence.tags().isDefined()) {
				System.out.println("POS tags: " + mkString(sentence.tags().get(), " "));
			}
			if (sentence.chunks().isDefined()) {
				System.out.println("Chunks: " + mkString(sentence.chunks().get(), " "));
			}
			if (sentence.entities().isDefined()) {
				System.out.println("Named entities: " + mkString(sentence.entities().get(), " "));
			}
			if (sentence.norms().isDefined()) {
				System.out.println("Normalized entities: " + mkString(sentence.norms().get(), " "));
			}
			if (sentence.dependencies().isDefined()) {
				System.out.println("Syntactic dependencies:");
				DirectedGraphEdgeIterator<String> iterator = new DirectedGraphEdgeIterator<String>(
						sentence.dependencies().get());
				while (iterator.hasNext()) {
					scala.Tuple3<Object, Object, String> dep = iterator.next();
					System.out.println(" head:" + dep._1() + " modifier:" + dep._2() + " label:" + dep._3());
				}
			}
			if (sentence.syntacticTree().isDefined()) {
				System.out.println("Constituent tree: " + sentence.syntacticTree().get());
			}

			sentenceCount += 1;
			System.out.println("\n");
		}

		if (doc.coreferenceChains().isDefined()) {
			scala.collection.Iterator<scala.collection.Iterable<CorefMention>> chains = doc.coreferenceChains().get()
					.getChains().iterator();
			while (chains.hasNext()) {
				scala.collection.Iterator<CorefMention> chain = chains.next().iterator();
				System.out.println("Found one coreference chain containing the following mentions:");
				while (chain.hasNext()) {
					CorefMention mention = chain.next();
					System.out.println("\tsentenceIndex:" + mention.sentenceIndex() + " headIndex:"
							+ mention.headIndex() + " startTokenOffset:" + mention.startOffset() + " endTokenOffset:"
							+ mention.endOffset());
				}
			}
		}
	}

	public static String mkString(String[] sa, String sep) {
		StringBuilder os = new StringBuilder();
		for (int i = 0; i < sa.length; i++) {
			if (i > 0)
				os.append(sep);
			os.append(sa[i]);
		}
		return os.toString();
	}

	public static String mkString(int[] sa, String sep) {
		StringBuilder os = new StringBuilder();
		for (int i = 0; i < sa.length; i++) {
			if (i > 0)
				os.append(sep);
			os.append(Integer.toString(sa[i]));
		}
		return os.toString();

	}
}
