package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Convert Sparse Matrix into csv file
 * {info} need for {@link /medicalconcept/src/main/java/featureselection/FeatureSelection.java}
 * @author Ariana
 *
 */
public class CSVFromSparseMatrixFileGenerator {

	public static void main(String[] args) throws IOException {
		getCSVFileFromLiblinearFile();
	}

	public static void getCSVFileFromLiblinearFile() throws IOException {
		BufferedReader brLiblinear = new BufferedReader(new FileReader(utils.GeneralUtils.FEATURES_FILE));
		PrintWriter prOut = new PrintWriter( new BufferedWriter(new FileWriter(utils.GeneralUtils.CSV_FROM_LIBLINEAR, false)));

		//4763 instances for our training data set
		int nrInstances = utils.GeneralUtils.NR_OF_INSTANCES_IN_ROW;
		String curr;
		int count = 1;
		String row = "";
		while((curr = brLiblinear.readLine())!=null) {
			String[] tokens = curr.split("\\s");
			count = 1;
			row = "";
			for(int i=1; i<tokens.length; i++) {
				String[] indexValue = tokens[i].split(":");
				int index = Integer.parseInt(indexValue[0]);
				while(count < index) {
					row = row + "0,";
					count++;
				}
				if(index == nrInstances) {
					row = row + indexValue[1];
				} else {
					row = row + indexValue[1] + ",";
				}

				count++;
			}
			while(count < nrInstances) {
				row = row + "0,";
				count++;
			}
			if(count == nrInstances) {
				row = row + "0";
			}
			prOut.println(row);
		}
		brLiblinear.close();
		prOut.close();
		
		System.out.println("File is Converted!");
		
	}
}
