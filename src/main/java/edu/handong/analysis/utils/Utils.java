package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class Utils {
	
	public static ArrayList<String> getLines(String file, boolean removeHeader) throws NotEnoughArgumentException{
		Scanner inputStream = null;
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		
	    try {
	    	File input_file = new File (file);
	    	if(!input_file.exists())
				throw new NotEnoughArgumentException("The file path does not exist. Please check your CLI argument!");
	    	inputStream = new Scanner (input_file);
	    } catch (FileNotFoundException e) {
	    	System.out.println(e.getMessage());
	    	System.exit (0);
	    }
	    
	    if(removeHeader == true) {
	    	line = inputStream.nextLine ();
	    }
	    while (inputStream.hasNextLine ()) {
			line = inputStream.nextLine ();
			lines.add(line);
		}
		inputStream.close ();
		
	    return lines;
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
