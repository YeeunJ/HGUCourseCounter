package edu.handong.analysis;

import edu.handong.analysis.utils.NotEnoughArgumentException;

public class Main {
	public static void main(String[] args) throws NotEnoughArgumentException {
		HGUCoursePatternAnalyzer analyzer = new HGUCoursePatternAnalyzer();
		analyzer.run(args);
	}
}