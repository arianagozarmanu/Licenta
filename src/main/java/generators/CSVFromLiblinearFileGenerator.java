package generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVFromLiblinearFileGenerator {

	public static void main(String[] args) throws IOException {
		getCSVFileFromLiblinearFile();
	}

	public static void getCSVFileFromLiblinearFile() throws IOException {
		BufferedReader brLiblinear = new BufferedReader(new FileReader(utils.GeneralUtils.FEATURES_FILE));
		PrintWriter prOut = new PrintWriter( new BufferedWriter(new FileWriter(utils.GeneralUtils.CSV_FROM_LIBLINEAR, false)));

		//4763 instances
		int nrInstances = utils.GeneralUtils.NR_OF_INSTANCES_IN_ROW;
		String curr;
		int count = 1;
		String row = "";
		while((curr = brLiblinear.readLine())!=null) {
			String[] tokens = curr.split("\\s");
			count = 1;
			row = "";
			//preia fiecare grupare de pe o linie
			for(int i=1; i<tokens.length; i++) {
				String[] indexValue = tokens[i].split(":");
				//System.out.println(indexValue[0]+" "+indexValue[1]);
				int index = Integer.parseInt(indexValue[0]);
				//System.out.println(index+"|"+i+"|"+(tokens.length-1));
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
			//scrie in fisier noul rand creat
			prOut.println(row);
		}
		brLiblinear.close();
		prOut.close();
		
		System.out.println("Conversie terminata!");
		
	}
}
