package test;

import org.clulab.struct.CorefMention;
import org.clulab.struct.DirectedGraphEdgeIterator;
import org.clulab.processors.*;
import org.clulab.processors.corenlp.CoreNLPProcessor;


public class ClulabAppTest {
	public static void main(String[] args) {
		// create the processor
		Processor proc = new CoreNLPProcessor(true, true, 0, 100);
		// for much faster processing, use FastNLPProcessor
		// Processor proc = new FastNLPProcessor(true, false);
		
		 //"John Smith went to China. He visited Beijing, on January 10th, 2013."
		// the actual work is done here
		Document doc = proc.annotate(
				"67 y/o male with worsening shortness of breath. He did have burst of atrial fibrillation and was started on a Amiodarone gtt ."
				+ "On post-op day five he appeared to have left arm phlebitis and was started on antibiotics ."
				+ "The mitral valve leaflets are mildly thickened ." + "Report any fever greater than 101",
				false);

		// you are basically done. the rest of this code simply prints out the
		// annotations

		// let's print the sentence-level annotations
		int sentenceCount = 0;
		for (Sentence sentence : doc.sentences()) {
			System.out.println("Sentence #" + sentenceCount + ":");
			System.out.println("Tokens: " + mkString(sentence.words(), " "));
			System.out.println("Start character offsets: " + mkString(sentence.startOffsets(), " "));
			System.out.println("End character offsets: " + mkString(sentence.endOffsets(), " "));

			// these annotations are optional, so they are stored using Option
			// objects, hence the isDefined checks
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
					// note that we use offsets starting at 0 (unlike CoreNLP,
					// which uses offsets starting at 1)
					System.out.println(" head:" + dep._1() + " modifier:" + dep._2() + " label:" + dep._3());
				}
			}
			if (sentence.syntacticTree().isDefined()) {
				System.out.println("Constituent tree: " + sentence.syntacticTree().get());
				// see the org.clulab.struct.Tree class for more information
				// on syntactic trees, including access to head phrases/words
			}

			sentenceCount += 1;
			System.out.println("\n");
		}

		// let's print the coreference chains
		if (doc.coreferenceChains().isDefined()) {
			// these are scala.collection Iterator and Iterable (not Java!)
			scala.collection.Iterator<scala.collection.Iterable<CorefMention>> chains = doc.coreferenceChains().get()
					.getChains().iterator();
			while (chains.hasNext()) {
				scala.collection.Iterator<CorefMention> chain = chains.next().iterator();
				System.out.println("Found one coreference chain containing the following mentions:");
				while (chain.hasNext()) {
					CorefMention mention = chain.next();
					// note that all these offsets start at 0 too
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