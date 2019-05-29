package edu.handong.analysis;

import java.io.IOException;

import edu.handong.analysis.utils.NotEnoughArgumentException;

public class Main {
	public static void main(String[] args) throws NotEnoughArgumentException, IOException {
		HGUCoursePatternAnalyzer analyzer = new HGUCoursePatternAnalyzer();
		analyzer.run(args);
	}
}