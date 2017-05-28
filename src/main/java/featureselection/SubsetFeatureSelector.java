package featureselection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;



import utils.GeneralUtils;
import utils.MapUtil;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.distance.PearsonCorrelationCoefficient;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.featureselection.scoring.GainRatio;
import net.sf.javaml.featureselection.subset.GreedyForwardSelection;
import net.sf.javaml.tools.data.FileHandler;

public class SubsetFeatureSelector {
	
	public static void main(String[] args) throws IOException{
		Dataset data = FileHandler.loadDataset(new File(utils.GeneralUtils.CSV_WITHOUT_LEMMA), 4, ",");
		//getFirstNFeatures(10, data);
		//getFeatureScoring(data);
		getFeatureRanking(data);

	}
	
	private static void getFirstNFeatures(int n, Dataset data) {
		GreedyForwardSelection ga = new GreedyForwardSelection(n, new PearsonCorrelationCoefficient());
		ga.build(data);
		System.out.println("Primele " + n + "trasaturi:");
		System.out.println(ga.selectedAttributes());
	}
	
	/**
	 * Score every feature and writes them in file in ascendent order
	 * @param data
	 * @throws IOException
	 */
	private static void getFeatureScoring(Dataset data) throws IOException {
		GainRatio ga = new GainRatio();
		ga.build(data);
		FileWriter fw = new FileWriter(GeneralUtils.FEATURE_SCORING, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		FileWriter fw2 = new FileWriter(GeneralUtils.FETURES_FILTERED, false);
		BufferedWriter bw2 = new BufferedWriter(fw2);
		PrintWriter out2 = new PrintWriter(bw2);
		
		HashMap<Integer,Double> hmap = new HashMap<Integer,Double>();

		for (int i = 0; i < ga.noAttributes(); i++) {
			hmap.put(i+1,ga.score(i));
		}
		
		hmap = (HashMap<Integer, Double>) MapUtil.sortByValue(hmap);

		for(Map.Entry<Integer, Double> entry : hmap.entrySet()) {
			out.println(entry.getKey() + " " + entry.getValue());
			if(entry.getValue() == 0.0) {
				out2.println(entry.getKey());
			}
        }
		
		out.close();
		out2.close(); 
		
		System.out.println("Feature Scoring done!");
		
	}
	
	private static void getFeatureRanking(Dataset data) throws IOException {
		RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
		svmrfe.build(data);
		FileWriter fw = new FileWriter(GeneralUtils.FEATURE_RANKING, false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		
		FileWriter fw2 = new FileWriter(GeneralUtils.FETURES_FILTERED, false);
		BufferedWriter bw2 = new BufferedWriter(fw2);
		PrintWriter out2 = new PrintWriter(bw2);
		
		HashMap<Integer,Integer> hmap = new HashMap<Integer,Integer>();

		for (int i = 0; i < svmrfe.noAttributes(); i++) {
			hmap.put(i+1,svmrfe.rank(i));
		}
		
		hmap = (HashMap<Integer, Integer>) MapUtil.sortByValue(hmap);

		for(Map.Entry<Integer, Integer> entry : hmap.entrySet()) {
			out.println(entry.getKey() + " " + entry.getValue());
			if(entry.getValue() == 0.0) {
				out2.println(entry.getKey());
			}
        }
		
		out.close();
		out2.close(); 
		
		System.out.println("Feature Ranking done!");
		
	}

}
