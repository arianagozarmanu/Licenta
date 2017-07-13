package generators;

import java.awt.Desktop;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import utils.GeneralUtils;

/**
 * compute metrics for performance validation
 * 
 * @author Ariana
 *
 */
public class FinalResultsGenerator {
	
	// metrics
	private static HashMap<Integer, Double> precision = new HashMap<Integer, Double>();
	private static HashMap<Integer, Double> recall = new HashMap<Integer, Double>();
	private static HashMap<Integer, Double> fscore = new HashMap<Integer, Double>();
	// true positives
	private static HashMap<Integer, Integer> tp = new HashMap<Integer, Integer>();
	// false positives
	private static HashMap<Integer, Integer> fp = new HashMap<Integer, Integer>();
	// false negatives
	private static HashMap<Integer, Integer> fn = new HashMap<Integer, Integer>();
	// fonts used in xlsx file
	private static CellStyle boldCell;
	private static CellStyle lemonCell;
	private static CellStyle blueCell;

	public static void main(String[] args) throws IOException {
		getResultsFromOutFile();
	}
	
	private static void getResultsFromOutFile() throws IOException {
		// initialize xlsx file
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		Sheet studentsSheet = workbook.createSheet("Metrics");
		createFonts(workbook);
		
		// writes confusion matrix header in xlsx file
		int rowIndex = 0;
		Row row = studentsSheet.createRow(rowIndex++);
		int cellIndex = 0;
		Cell cell = row.createCell(cellIndex++);
		cell.setCellValue("Matricea de confuzie:");
		cell.setCellStyle(boldCell);
		row = studentsSheet.createRow(rowIndex++);
		cellIndex = 0;
		row.createCell(cellIndex++).setCellValue("");
		Cell cell3 = row.createCell(cellIndex++);
		cell3.setCellValue("none");
		cell3.setCellStyle(blueCell);
		cell3 = row.createCell(cellIndex++);
		cell3.setCellValue("tratament");
		cell3.setCellStyle(blueCell);
		cell3 = row.createCell(cellIndex++);
		cell3.setCellValue("test");
		cell3.setCellStyle(blueCell);
		cell3 = row.createCell(cellIndex++);
		cell3.setCellValue("problema");
		cell3.setCellStyle(blueCell);
		
		// initialize tp, fp, fn metrics
		tp = populateIntegerMaps(tp);
		fp = populateIntegerMaps(fp);
		fn = populateIntegerMaps(fn);
		HashMap<Integer, Integer> hmap0 = new HashMap<Integer, Integer>();
		hmap0 = populateIntegerMaps(hmap0);
		HashMap<Integer, Integer> hmap1 = new HashMap<Integer, Integer>();
		hmap1 = populateIntegerMaps(hmap1);
		HashMap<Integer, Integer> hmap2 = new HashMap<Integer, Integer>();
		hmap2 = populateIntegerMaps(hmap2);
		HashMap<Integer, Integer> hmap3 = new HashMap<Integer, Integer>();
		hmap3 = populateIntegerMaps(hmap3);

		// initialize confusion matrix
		HashMap<Integer, HashMap<Integer, Integer>> confMatrix = new HashMap<Integer, HashMap<Integer, Integer>>();
		confMatrix.put(0, hmap0);
		confMatrix.put(1, hmap1);
		confMatrix.put(2, hmap2);
		confMatrix.put(3, hmap3);
		
		// read test file and out file
		BufferedReader brTest = new BufferedReader(new FileReader(
				utils.GeneralUtils.LIBLNR_TEST));
		BufferedReader brOut = new BufferedReader(new FileReader(
				utils.GeneralUtils.LIBLNR_OUT));
		
		// read from files and compare results
		String currentLineTest;
		String currentLineOut;
		// creeare marice de confuzie
		while ((currentLineTest = brTest.readLine()) != null
				&& (currentLineOut = brOut.readLine()) != null) {
			String[] tokensTest = currentLineTest.split("\\s");
			String[] tokensOut = currentLineOut.split("\\s");
			Integer valueTest = Integer.parseInt(tokensTest[0]);
			Integer valueOut = Integer.parseInt(tokensOut[0]);
			Integer currValue = confMatrix.get(valueTest).get(valueOut);
			currValue++;
			confMatrix.get(valueTest).put(valueOut, currValue);
		}
		brTest.close();
		brOut.close();

		// write CONFUSION MATRIX
		// columns
		System.out.print("*" + "\t");
		for (int i = 0; i <= 3; i++) {
			System.out.print(i + "\t");
		}
		System.out.println();

		// rows
		for (Integer key : confMatrix.keySet()) {
			row = studentsSheet.createRow(rowIndex++);
			System.out.print(key + "\t");
			cellIndex = 0;
			Cell cell2 = row.createCell(cellIndex++);
			cell2.setCellValue(getCategoryName(key));
			cell2.setCellStyle(blueCell);
			for (Integer k : confMatrix.get(key).keySet()) {
				Cell cellAux = row.createCell(cellIndex++);
				cellAux.setCellValue(confMatrix.get(key).get(k));
				if (key == k) {
					cellAux.setCellStyle(lemonCell);
				}
				System.out.print(confMatrix.get(key).get(k) + "\t");
			}
			System.out.println();
		}

		// compute Precision, Recall, F-score
		for (Integer key : tp.keySet()) {
			tp.put(key, confMatrix.get(key).get(key));
		}

		for (Integer key : fp.keySet()) {
			Integer sumCol = 0;
			for (Integer i : confMatrix.keySet()) {
				if (i != key) {
					sumCol = sumCol + confMatrix.get(i).get(key);
				}
			}
			fp.put(key, sumCol);
		}

		for (Integer key : fn.keySet()) {
			Integer sumRow = 0;
			for (Integer i : confMatrix.keySet()) {
				if (i != key) {
					sumRow = sumRow + confMatrix.get(key).get(i);
				}
			}
			fn.put(key, sumRow);
		}

		// writes metrics
		precision = populateDoubleMaps(precision);
		recall = populateDoubleMaps(recall);
		fscore = populateDoubleMaps(fscore);

		for (Integer key : tp.keySet()) {
			System.out.println("TP" + key + ":" + tp.get(key) + " ");
		}
		for (Integer key : fp.keySet()) {
			System.out.println("FP" + key + ":" + fp.get(key) + " ");
		}
		for (Integer key : fn.keySet()) {
			System.out.println("FN" + key + ":" + fn.get(key) + " ");
		}

		precision.put(0, (double) tp.get(0) / (tp.get(0) + fp.get(0)));
		precision.put(1, (double) tp.get(1) / (tp.get(1) + fp.get(1)));
		precision.put(2, (double) tp.get(2) / (tp.get(2) + fp.get(2)));
		precision.put(3, (double) tp.get(3) / (tp.get(3) + fp.get(3)));
		System.out.println("Precision0 = " + precision.get(0));
		System.out.println("Precision1 = " + precision.get(1));
		System.out.println("Precision2 = " + precision.get(2));
		System.out.println("Precision3 = " + precision.get(3));

		recall.put(0, (double) tp.get(0) / (tp.get(0) + fn.get(0)));
		recall.put(1, (double) tp.get(1) / (tp.get(1) + fn.get(1)));
		recall.put(2, (double) tp.get(2) / (tp.get(2) + fn.get(2)));
		recall.put(3, (double) tp.get(3) / (tp.get(3) + fn.get(3)));
		System.out.println("Recall0 = " + recall.get(0));
		System.out.println("Recall1 = " + recall.get(1));
		System.out.println("Recall2 = " + recall.get(2));
		System.out.println("Recall3 = " + recall.get(3));

		fscore.put(0, (double) ((precision.get(0) * recall.get(0)) * 2)
				/ (precision.get(0) + recall.get(0)));
		fscore.put(1, (double) ((precision.get(1) * recall.get(1)) * 2)
				/ (precision.get(1) + recall.get(1)));
		fscore.put(2, (double) ((precision.get(2) * recall.get(2)) * 2)
				/ (precision.get(2) + recall.get(2)));
		fscore.put(3, (double) ((precision.get(3) * recall.get(3)) * 2)
				/ (precision.get(3) + recall.get(3)));
		System.out.println("F-score0 = " + fscore.get(0));
		System.out.println("F-score1 = " + fscore.get(1));
		System.out.println("F-score2 = " + fscore.get(2));
		System.out.println("F-score3 = " + fscore.get(3));

		studentsSheet.createRow(rowIndex++);
		int rowIndexAux = createCategoryXLSXArea("TRATAMENT", 1, rowIndex,
				studentsSheet);
		rowIndexAux = createCategoryXLSXArea("TEST", 2, rowIndexAux,
				studentsSheet);
		rowIndexAux = createCategoryXLSXArea("PROBLEMA", 3, rowIndexAux,
				studentsSheet);
		rowIndex = createCategoryXLSXArea("NONE", 0, rowIndexAux, studentsSheet);

		try {
			FileOutputStream fos = new FileOutputStream(GeneralUtils.FINAL_RESULTS_FILEPATH);
			workbook.write(fos);
			fos.close();

			File file = new File(GeneralUtils.FINAL_RESULTS_FILEPATH);
			if (!Desktop.isDesktopSupported()) {
				System.out.println("Desktop is not supported");
			}

			Desktop desktop = Desktop.getDesktop();
			if (file.exists()) {
				desktop.open(file);
			}
		} catch (java.io.FileNotFoundException e) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Fisierul este utilizat in alt utilitar. Inchide si incearca din nou.",
							"Dialog", JOptionPane.WARNING_MESSAGE);
		}

	}
	
	/**
	 * writes metrics and their values in the xlsx file
	 * 
	 * @param catName [None, Treatment, Test, Problem]
	 * @param index	is the category index [0, 1, 2, 3]
	 * @param rowIndex is the row index in xlsx file
	 * @param studentsSheet is the xlsx file
	 * @return
	 */
	private static int createCategoryXLSXArea(String catName, int index,
			int rowIndex, Sheet studentsSheet) {
		Row row = studentsSheet.createRow(rowIndex++);
		int cellIndex = 0;
		Cell cell = row.createCell(cellIndex++);
		cell.setCellValue(catName);
		cell.setCellStyle(boldCell);
		row = studentsSheet.createRow(rowIndex++);
		cellIndex = 0;
		row.createCell(cellIndex++).setCellValue("TP");
		row.createCell(cellIndex++).setCellValue("FP");
		row.createCell(cellIndex++).setCellValue("FN");
		row = studentsSheet.createRow(rowIndex++);
		cellIndex = 0;
		row.createCell(cellIndex++).setCellValue(tp.get(index));
		row.createCell(cellIndex++).setCellValue(fp.get(index));
		row.createCell(cellIndex++).setCellValue(fn.get(index));
		row = studentsSheet.createRow(rowIndex++);
		cellIndex = 0;
		DecimalFormat df = new DecimalFormat("####0.00");
		row.createCell(cellIndex++).setCellValue("Precision");
		row.createCell(cellIndex++).setCellValue("Recall");
		row.createCell(cellIndex++).setCellValue("F-score");
		row = studentsSheet.createRow(rowIndex++);
		cellIndex = 0;
		row.createCell(cellIndex++).setCellValue(
				Double.parseDouble(df.format(precision.get(index) * 100)));
		row.createCell(cellIndex++).setCellValue(
				Double.parseDouble(df.format(recall.get(index) * 100)));
		row.createCell(cellIndex++).setCellValue(
				Double.parseDouble(df.format(fscore.get(index) * 100)));
		studentsSheet.createRow(rowIndex++);
		return rowIndex;
	}
	
	/**
	 * initialize hash map with 0 for every category index
	 * 
	 * @param hmap
	 * @return
	 */
	private static HashMap<Integer, Integer> populateIntegerMaps(
			HashMap<Integer, Integer> hmap) {
		hmap.put(0, 0);
		hmap.put(1, 0);
		hmap.put(2, 0);
		hmap.put(3, 0);
		return hmap;
	}
	
	/**
	 * initialize hash map with 0.0 for every category index
	 * 
	 * @param hmap
	 * @return
	 */
	private static HashMap<Integer, Double> populateDoubleMaps(
			HashMap<Integer, Double> hmap) {
		hmap.put(0, 0.0);
		hmap.put(1, 0.0);
		hmap.put(2, 0.0);
		hmap.put(3, 0.0);
		return hmap;
	}
	
	/**
	 * get the category name by index
	 * 
	 * @param key
	 * @return
	 */
	private static String getCategoryName(int key) {
		String category = "";
		switch (key) {
		case 0:
			category = "none";
			break;
		case 1:
			category = "tratament";
			break;
		case 2:
			category = "test";
			break;
		case 3:
			category = "problema";
			break;
		default:
			category = "";
		}

		return category;
	}
	
	/**
	 * create fonts used in xlsx file
	 * 
	 * @param workbook
	 */
	private static void createFonts(SXSSFWorkbook workbook) {
		lemonCell = workbook.createCellStyle();
		lemonCell.setFillForegroundColor(HSSFColor.BRIGHT_GREEN.index);
		lemonCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		blueCell = workbook.createCellStyle();
		blueCell.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
		blueCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		boldCell = workbook.createCellStyle();
		Font boldFont = workbook.createFont();
		boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		boldCell.setFont(boldFont);
	}
}
