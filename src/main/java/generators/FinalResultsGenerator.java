package generators;

import java.io.*;
import java.util.*;

public class FinalResultsGenerator {

	public static void main(String[] args) throws IOException {
		getResultsFromOutFile();
	}
	
	private static void getResultsFromOutFile() throws IOException {

		//true positives
		HashMap<Integer, Integer> tp = new HashMap<Integer, Integer>();
		tp = populateIntegerMaps(tp);
		//false positives
		HashMap<Integer, Integer> fp = new HashMap<Integer, Integer>();
		fp = populateIntegerMaps(fp);
		//false negatives
		HashMap<Integer, Integer> fn = new HashMap<Integer, Integer>();
		fn = populateIntegerMaps(fn);
		
		//fiecare clasa cu HashMap-ul propriu
		HashMap<Integer, Integer> hmap0 = new HashMap<Integer, Integer>();
		hmap0 =	populateIntegerMaps(hmap0);
		HashMap<Integer, Integer> hmap1 = new HashMap<Integer, Integer>();
		hmap1 =	populateIntegerMaps(hmap1);
		HashMap<Integer, Integer> hmap2 = new HashMap<Integer, Integer>();
		hmap2 =	populateIntegerMaps(hmap2);
		HashMap<Integer, Integer> hmap3 = new HashMap<Integer, Integer>();
		hmap3 =	populateIntegerMaps(hmap3);
		
		//matricea de confuzie
		HashMap<Integer, HashMap<Integer,Integer>> confMatrix = new HashMap<Integer, HashMap<Integer,Integer>>();
		confMatrix.put(0, hmap0);
		confMatrix.put(1, hmap1);
		confMatrix.put(2, hmap2);
		confMatrix.put(3, hmap3);
		
		BufferedReader brTest = new BufferedReader(new FileReader(utils.GeneralUtils.LIBLNR_TEST));
		BufferedReader brOut = new BufferedReader(new FileReader(utils.GeneralUtils.LIBLNR_OUT));
		
		String currentLineTest;
		String currentLineOut;
		while ((currentLineTest = brTest.readLine()) != null && (currentLineOut = brOut.readLine()) != null) {
			String[] tokensTest = currentLineTest.split("\\s");
			String[] tokensOut = currentLineOut.split("\\s");
			Integer valueTest = Integer.parseInt(tokensTest[0]);
			Integer valueOut = Integer.parseInt(tokensOut[0]);
			
			Integer currValue = confMatrix.get(valueTest).get(valueOut);
			currValue++;
			confMatrix.get(valueTest).put(valueOut,currValue);
			//System.out.println(valueTest+ " "+valueOut);
		}
		brTest.close();
		brOut.close();
		
		//AFISARE CONFUSION MATRIX
		//coloanele matricei
		System.out.print("*"+"\t");
		for(int i=0; i<=3; i++) {
			System.out.print(i+"\t");
		}
		System.out.println();
		
		//randurile matricei
		for(Integer key: confMatrix.keySet()) {
			System.out.print(key+"\t");
			for(Integer k: confMatrix.get(key).keySet()) {
				System.out.print(confMatrix.get(key).get(k)+"\t");
			}
			System.out.println();
		}
		
		//CALCUL Precision, Recall, F-score
		for(Integer key: tp.keySet()) {
			tp.put(key,confMatrix.get(key).get(key));
		}
		
		for(Integer key: fp.keySet()) {
			Integer sumCol = 0;
			for(Integer i: confMatrix.keySet()) {
				if(i!=key) {
					sumCol = sumCol + confMatrix.get(i).get(key);
				}
			}
			fp.put(key,sumCol);
		}
		
		for(Integer key: fn.keySet()) {
			Integer sumRow = 0;
			for(Integer i: confMatrix.keySet()) {
				if(i!=key) {
					sumRow = sumRow + confMatrix.get(key).get(i);
				}
			}
			fn.put(key,sumRow);
		}
		
		//Afisare TP, FP, FN pentru fiecare clasa
		HashMap<Integer,Double> precision = new HashMap<Integer,Double>();
		precision = populateDoubleMaps(precision);
		HashMap<Integer,Double> recall = new HashMap<Integer,Double>();
		recall = populateDoubleMaps(recall);
		HashMap<Integer,Double> fscore = new HashMap<Integer,Double>();
		fscore = populateDoubleMaps(fscore);
		
		for(Integer key: tp.keySet()) {
			System.out.println("TP"+key+":"+tp.get(key)+" ");
		}
		for(Integer key: fp.keySet()) {
			System.out.println("FP"+key+":"+fp.get(key)+" ");
		}
		for(Integer key: fn.keySet()) {
			System.out.println("FN"+key+":"+fn.get(key)+" ");
		}
		
		precision.put(0,(double)tp.get(0)/(tp.get(0)+fp.get(0)));
		precision.put(1,(double)tp.get(1)/(tp.get(1)+fp.get(1)));
		precision.put(2,(double)tp.get(2)/(tp.get(2)+fp.get(2)));
		precision.put(3,(double)tp.get(3)/(tp.get(3)+fp.get(3)));
		System.out.println("Precision0 = " + precision.get(0));
		System.out.println("Precision1 = " + precision.get(1));
		System.out.println("Precision2 = " + precision.get(2));
		System.out.println("Precision3 = " + precision.get(3));
		
		recall.put(0, (double)tp.get(0)/(tp.get(0)+fn.get(0)));
		recall.put(1, (double)tp.get(1)/(tp.get(1)+fn.get(1)));
		recall.put(2, (double)tp.get(2)/(tp.get(2)+fn.get(2)));
		recall.put(3, (double)tp.get(3)/(tp.get(3)+fn.get(3)));
		System.out.println("Recall0 = " + recall.get(0));
		System.out.println("Recall1 = " + recall.get(1));
		System.out.println("Recall2 = " + recall.get(2));
		System.out.println("Recall3 = " + recall.get(3));
		
		fscore.put(0, (double)((precision.get(0)*recall.get(0))*2)/(precision.get(0)+recall.get(0)));
		fscore.put(1, (double)((precision.get(1)*recall.get(1))*2)/(precision.get(1)+recall.get(1)));
		fscore.put(2, (double)((precision.get(2)*recall.get(2))*2)/(precision.get(2)+recall.get(2)));
		fscore.put(3, (double)((precision.get(3)*recall.get(3))*2)/(precision.get(3)+recall.get(3)));
		System.out.println("F-score0 = " + fscore.get(0));
		System.out.println("F-score1 = " + fscore.get(1));
		System.out.println("F-score2 = " + fscore.get(2));
		System.out.println("F-score3 = " + fscore.get(3));
		
	}
	
	private static HashMap<Integer,Integer> populateIntegerMaps(HashMap<Integer,Integer> hmap) {
		hmap.put(0, 0);
		hmap.put(1, 0);
		hmap.put(2, 0);
		hmap.put(3, 0);
		return hmap;
	}
	
	private static HashMap<Integer,Double> populateDoubleMaps(HashMap<Integer,Double> hmap) {
		hmap.put(0, 0.0);
		hmap.put(1, 0.0);
		hmap.put(2, 0.0);
		hmap.put(3, 0.0);
		return hmap;
	}
}
