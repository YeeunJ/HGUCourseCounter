package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;


public class Student {
	private String studentId;
	private ArrayList<Course> coursesTaken; // 학생이 들은 수업 목록
	private HashMap<String,Integer> semestersByYearAndSemester;
	
	public Student(String studentId){
		// constructor
		this.studentId = studentId;
		this.coursesTaken = new ArrayList<Course>();
		this.semestersByYearAndSemester = new HashMap<String,Integer>();
	}
	
	public void addCourse(Course newRecord) {
		coursesTaken.add(newRecord);
		if(semestersByYearAndSemester.containsKey(newRecord.getterYearandSemester()) == false)
			semestersByYearAndSemester.put(newRecord.getterYearandSemester(), semestersByYearAndSemester.size()+1);
	}
	
	public HashMap<String,Integer> getSemestersByYearAndSemester(){
		return semestersByYearAndSemester;
		
	}
	public int getNumCourseInNthSementer(int semester) {
		int count = 0;
		String YnS = null;
		for(String s: semestersByYearAndSemester.keySet()) {
			if(semestersByYearAndSemester.get(s).equals(semester)) {
				YnS = s;
				break;
			}
		}
		for(Course course: coursesTaken) {
			if(course.getterYearandSemester().equals(YnS) == true)
				count++;
		}
		
		return count;
	}
	
	public String getterStudentId() {
		return studentId;
	}
	
	public int check_course(String year, String code) {
		for(Course cs: coursesTaken) {
			if((cs.gettercourseCode().equals(code)) && (cs.getterYearandSemester().equals(year)))
				return 1;
		}
		return 0;
	}
	

	public boolean check_year(String year) {
		for(Course cs: coursesTaken) {
			if(cs.getterYearandSemester().equals(year))
				return true;
		}
		return false;
	}
	
	public String findCourseName(String code) {
		for(Course cs: coursesTaken) {
			if(cs.gettercourseCode().equals(code))
					return cs.gettercourseName();
		}
		return null;
		
	}
}
