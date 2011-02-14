package sms.subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

public class Subject implements Serializable{

	
	private Integer units;
	private int subject_code;
	private String subject_name;
	private int offered_id;
	private int record_id;
	private String subject_section;
	private String time;
	private String day;
	private String room;
	private String semester;
	private int school_year;
	private int dept_code;
	private String dept_name;
	private String college_name;
	private String program_name;
	private int college_code;
	private int program_code;
	private int slot;
	private int employee_id;
	private String rating;

	
	public Subject(){}
	
	public Subject(Integer units, int subjectCode, String subjectName,
			int offeredId, String subjectSection, String time, String day,
			String room, String semester, int schoolYear, int deptCode,
			int college_code, int programCode, int slot, int employeeId) {
		super();
		this.units = units;
		subject_code = subjectCode;
		subject_name = subjectName;
		offered_id = offeredId;
		subject_section = subjectSection;
		this.time = time;
		this.day = day;
		this.room = room;
		this.semester = semester;
		school_year = schoolYear;
		dept_code = deptCode;
		this.college_code = college_code;
		program_code = programCode;
		this.slot = slot;
		employee_id = employeeId;
	}
	
	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public int getSubject_code() {
		return subject_code;
	}

	public void setSubject_code(int subjectCode) {
		subject_code = subjectCode;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subjectName) {
		subject_name = subjectName;
	}

	public int getOffered_id() {
		return offered_id;
	}

	public void setOffered_id(int offeredId) {
		offered_id = offeredId;
	}

	public String getSubject_section() {
		return subject_section;
	}

	public void setSubject_section(String subjectSection) {
		subject_section = subjectSection;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getSchool_year() {
		return school_year;
	}

	public void setSchool_year(int schoolYear) {
		school_year = schoolYear;
	}

	public int getDept_code() {
		return dept_code;
	}

	public void setDept_code(int deptCode) {
		dept_code = deptCode;
	}

	public int getCollege_code() {
		return college_code;
	}

	public void setCollege_code(int college_code) {
		this.college_code = college_code;
	}

	public int getProgram_code() {
		return program_code;
	}

	public void setProgram_code(int programCode) {
		program_code = programCode;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employeeId) {
		employee_id = employeeId;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String deptName) {
		dept_name = deptName;
	}

	public String getCollege_name() {
		return college_name;
	}

	public void setCollege_name(String collegeName) {
		college_name = collegeName;
	}

	public String getProgram_name() {
		return program_name;
	}

	public void setProgram_name(String programName) {
		program_name = programName;
	}

	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int recordId) {
		record_id = recordId;
	}
	public static String[] getCourseNumberAndDesc(String str){
		String[] tokens = str.split("\\-");
		return tokens;
	}
	/*
	public static List selectEntries(List list, int first, int count, final org.apache.wicket.extensions.markup.html.repeater.util.SortParam sortParam) {
		List sortedEntries = new ArrayList(list);
		if (sortParam != null) {
			if (sortParam.getProperty().equals("subject_name")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Subject entry1 = (Subject) arg0;
						Subject entry2 = (Subject) arg1;
						int result = entry1.getSubject_name().compareTo(entry2.getSubject_name());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("subject_code")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Subject entry1 = (Subject) arg0;
						Subject entry2 = (Subject) arg1;
						int result = ((Integer)entry1.getSubject_code()).compareTo((Integer)entry2.getSubject_code());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
		}
		return sortedEntries.subList(first, first + count);
		//return list.subList(first, first + count);
	} */
	
	
	public static List selectEntries(List list, int first, int count){
		return list.subList(first, first + count);
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	} 
	
}
