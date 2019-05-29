package edu.handong.analysis.utils;

import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import edu.handong.analysis.datamodel.Course;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;


public class Utils {
	
	public static ArrayList<Course> getLines(String file, boolean removeHeader) throws NotEnoughArgumentException, IOException{
		ArrayList<Course> css = new ArrayList<Course>();
		
		try {
            Reader in = new FileReader(file);

            CSVParser parser = CSVFormat.EXCEL.parse(in);

            // Loop on all the records of the file
            for (CSVRecord record : parser) {
            	ArrayList<String> s = new ArrayList<String>();
	        	// Process the headers in the first line
	            if((record.getRecordNumber() == 1)&&(removeHeader == true)) {
	            	for(int i = 0; i <9; i++)
	            		s.add(record.get(i).trim());
	            	continue;
	            }
	        	for(int i = 0; i <9; i++)
	        		s.add(record.get(i).trim());
	        	Course cs = new Course(s);
	        	css.add(cs);
        	}
        } catch(FileNotFoundException e){
        	System.out.println("The file path does not exist. Please check your CLI argument!");
			System.exit(0);
        }
		
		return css;
	}
	
	
	public static void writeAFile(ArrayList<String> lines, String targetFileName) {
		PrintWriter outputStream = null;
		
		try {
			File file = new File(targetFileName);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			outputStream = new PrintWriter(targetFileName);
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		for (String line: lines) {
            outputStream.println (line);
		}
		
		outputStream.close();
	}

}
