package edu.handong.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {
	private String dataPath, resultPath, coursecode = null;
	private int analysis, startyear, endyear;
	boolean help;
	private HashMap<String,Student> students;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 * @throws NotEnoughArgumentException 
	 */
	public void run(String[] args) throws NotEnoughArgumentException {
		Options options = createOptions();
		if(parseOptions(options, args)){
			if (help){
				printHelp(options);
				return;
			}
			ArrayList<Course> lines = Utils.getLines(dataPath, true);
			lines = removeCheck(lines);
			
			students = loadStudentCourseRecords(lines);
			Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
			if(analysis == 1) {
				// Generate result lines to be saved.
				ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
				// Write a file (named like the value of resultPath) with linesTobeSaved.
				Utils.writeAFile(linesToBeSaved, resultPath);
			}
			else if((analysis == 2)&&(coursecode != null)) {
				ArrayList<String> linesToBeSaved = countNumberOfStudentsInEachSemester(sortedStudents);
				Utils.writeAFile(linesToBeSaved, resultPath);
			}
			else {
				printHelp(options);
				return;
			}
		}
	}
	
	
	private ArrayList<Course> removeCheck(ArrayList<Course> lines){
		for(int i = 0; i < lines.size(); i++) {
			if((lines.get(i).getteryearTaken() <= startyear) || (lines.get(i).getteryearTaken() >= endyear)) {
				lines.remove(i);
			}
		}
		
		return lines;
	}
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<Course> css) {
		String pre_Id = null;
		Student st = null;
		HashMap<String, Student> student = new HashMap<String, Student>();
		
		for(Course cs: css) {
				if(pre_Id == null) {
					st = new Student(cs.getterStudentId());
				}
				else if(pre_Id.equals(cs.getterStudentId())) {
					st.addCourse(cs);
				}
				else {
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
		return all_lines;
	}
	
	
	private ArrayList<String> countNumberOfStudentsInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> all_lines = new ArrayList<String>();
		String line, pre_year = null, coursename = null;
		int all_count = 0;
		int count = 0;
		all_lines.add("Year,Semester,CouseCode,CourseName,TotalStudents,StudentsTaken,Rate");
		
		//먼저 시작년도부터 끝나는 년도까지 반복
		for(int i = startyear; i <= endyear; i++) {
			for(int j = 1; j <= 4; j++) {
				String year = Integer.toString(i) + "-" +  Integer.toString(j);
				all_count = 0;
				count = 0;
				for(Student st: sortedStudents.values()) {
					if(st.check_year(year)) {
						all_count++;
						count += st.check_course(year, coursecode);
						if(coursename == null) {
							coursename = st.findCourseName(coursecode);
						}
					}
				}
				if(count != 0) {
					
					float rate = (float)all_count/(float)count;
					double rate_format = Math.round(rate*10)/10.0;
					line = year.split("-")[0] + "," + year.split("-")[1] + "," + coursecode + "," + coursename + "," + Integer.toString(all_count) + "," + Integer.toString(count) + "," + Double.toString(rate_format)+"%";
					all_lines.add(line);
				}
			}
		}
		return all_lines; // do not forget to return a proper variable.
	}
	
	
	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			
			dataPath = cmd.getOptionValue("i");
			resultPath = cmd.getOptionValue("o");
			analysis = Integer.parseInt(cmd.getOptionValue("a"));
			coursecode = cmd.getOptionValue("c");
			startyear = Integer.parseInt(cmd.getOptionValue("s"));
			endyear = Integer.parseInt(cmd.getOptionValue("e"));
			help = cmd.hasOption("h");
		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}


	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());

		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an output file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());
		
		options.addOption(Option.builder("a").longOpt("analysis")
				.desc("1: Count courses per semester, 2: Count per course name and year")
				.hasArg() 
				.argName("Analysis option")
				.required()
				.build());
		
		options.addOption(Option.builder("c").longOpt("coursecode")
				.desc("Course code for '-a 2' option")
				.hasArg() 
				.argName("course code")
				.build());
		
		options.addOption(Option.builder("s").longOpt("startyear")
				.desc("Set the start year for analysis e.g., -s 2002")
				.hasArg() 
				.argName("Start year for analysis")
				.required()
				.build());
		
		options.addOption(Option.builder("e").longOpt("endyear")
				.desc("End year for analysis")
				.hasArg() 
				.argName("Set the end year for analysis e.g., -e 2005")
				.required()
				.build());
		
		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Show a Help page")
		        .argName("Help")
		        .build());
		
		return options;
	}
	
	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Course Analyzer";
		String footer ="";
		formatter.printHelp("HGUCourseCounter", header, options, footer, true);
	}

}
