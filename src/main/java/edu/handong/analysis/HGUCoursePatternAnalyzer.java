package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 * @throws NotEnoughArgumentException 
	 */
	public void run(String[] args) throws NotEnoughArgumentException {
		
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);
		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
		
	}
	
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		String pre_Id = null;
		Student st = null;
		HashMap<String, Student> student = new HashMap<String, Student>();
		
		for(String line: lines) {
			Course cs = new Course(line);
			if(pre_Id == null) {
				st = new Student(cs.getterStudentId());
			}
			else if(pre_Id.equals(cs.getterStudentId())) {
				st.addCourse(cs);
			}
			else {
				System.out.println();
				student.put(cs.getterStudentId(), st);
				st = new Student(cs.getterStudentId());
				st.addCourse(cs);
			}
			pre_Id = cs.getterStudentId();
		}
		return student; // do not forget to return a proper variable.
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> all_lines = new ArrayList<String>();
		String first_Data,second_Data;
		all_lines.add("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
		for(Student st: sortedStudents.values()) {
			first_Data = st.getterStudentId();
			
			first_Data += "," + Integer.toString(st.getSemestersByYearAndSemester().size());
			for(int i = 1; i <= st.getSemestersByYearAndSemester().size(); i++) {
				second_Data = ",";
				second_Data += Integer.toString(i);
				second_Data += ",";
				second_Data += Integer.toString(st.getNumCourseInNthSementer(i));
				all_lines.add(first_Data+second_Data);
			}
		}
		return all_lines; // do not forget to return a proper variable.
	}
}
