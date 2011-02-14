package sms.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Student implements Serializable{
	
	private int student_id;
	private String fname;
	private String lname;
	private String mname;
	private String bdate;
	private String barangay;
	private String municipality;
	private String province;
	private String gender;
	private String educ_level;
	private int program_code;
	private String school_year;
	private int year_level;
	private String semester;
	private String section;
	private int offered_id;
	private int record_id;
	private String rating;
	private String college_name;
	private String dept_name;
	public static String tertiary = "tertiary";
	public static String secondary = "secondary";
	
	public Student(){}
	
	public Student(int studentId, String fname, String lname, String mname,
			String bdate, String barangay, String municipality,
			String province, String gender, String educLevel, int programCode,
			String schoolYear, int yearLevel, String semester, String section) {
		super();
		student_id = studentId;
		this.fname = fname;
		this.lname = lname;
		this.mname = mname;
		this.bdate = bdate;
		this.barangay = barangay;
		this.municipality = municipality;
		this.province = province;
		this.gender = gender;
		educ_level = educLevel;
		program_code = programCode;
		school_year = schoolYear;
		year_level = yearLevel;
		this.semester = semester;
		this.section = section;
	}
	
	public Student(int studId,String fname,String lname,String mname,
			       String bdate, String barangay, String municipality,
			       String province, String gender, String educlevel) {
		super();
		student_id = studId;
		this.fname = fname;
		this.lname = lname;
		this.mname = mname;
		this.bdate = bdate;
		this.barangay = barangay;
		this.municipality = municipality;
		this.province = province;
		this.gender = gender;
		educ_level = educlevel;
		
	}
	public Student(int studId) {
		student_id = studId;
	}

	public int getStudent_id() {
		return student_id;
	}
	public void setStudent_id(int studentId) {
		student_id = studentId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getBarangay() {
		return barangay;
	}
	public void setBarangay(String barangay) {
		this.barangay = barangay;
	}
	public String getMunicipality() {
		return municipality;
	}
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEduc_level() {
		return educ_level;
	}
	public void setEduc_level(String educLevel) {
		educ_level = educLevel;
	}
	public int getProgram_code() {
		return program_code;
	}
	public void setProgram_code(int programCode) {
		program_code = programCode;
	}
	public String getSchool_year() {
		return school_year;
	}
	public void setSchool_year(String schoolYear) {
		school_year = schoolYear;
	}
	public int getYear_level() {
		return year_level;
	}
	public void setYear_level(int yearLevel) {
		year_level = yearLevel;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}

	public int getOffered_id() {
		return offered_id;
	}

	public void setOffered_id(int offeredId) {
		offered_id = offeredId;
	} /*
	public String displayStudentId(int id){
		String str = Integer.toString(id);
		return str.substring(0,4)+"-"+str.substring(4, str.length());
	} */

	public static String displayStudentId(int id){
		String str = Integer.toString(id);
		return str.substring(0,4)+"-"+str.substring(4, str.length());
	}
	
	public int getRecord_id() {
		return record_id;
	}

	public void setRecord_id(int recordId) {
		record_id = recordId;
	}
	
	public boolean isRegistered(){
		StudentQuery query = new StudentQuery();
		Integer num = query.getIntegerDatum(this, "isRegistered");
		if(num != null && num > 0)
			return true;
		
		return false;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getCollege_name() {
		return college_name;
	}
	
	public void setCollege_name(String collegeName) {
		college_name = collegeName;
	}
	
	public String getDept_name() {
		return dept_name;
	}
	
	public static List selectEntries(List list, int first, int count, final org.apache.wicket.extensions.markup.html.repeater.util.SortParam sortParam) {
		List sortedEntries = new ArrayList(list);
		if (sortParam != null) {
			if (sortParam.getProperty().equals("student_id")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Student entry1 = (Student) arg0;
						Student entry2 = (Student) arg1;
						int result = ((Integer)entry1.getStudent_id()).compareTo((Integer)entry2.getStudent_id());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("lname")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Student entry1 = (Student) arg0;
						Student entry2 = (Student) arg1;
						int result = (entry1.getLname()).compareTo(entry2.getLname());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
		}
		return sortedEntries.subList(first, first + count);
	}

	public static List selectEntries1(List list, int first, int count, final org.apache.wicket.extensions.markup.html.repeater.util.SortParam sortParam) {
		List sortedEntries = new ArrayList(list);
		if (sortParam != null) {
			if (sortParam.getProperty().equals("student_id")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Student entry1 = (Student) arg0;
						Student entry2 = (Student) arg1;
						int result = ((Integer)entry1.getStudent_id()).compareTo((Integer)entry2.getStudent_id());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("name")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Student entry1 = (Student) arg0;
						Student entry2 = (Student) arg1;
						int result = (entry1.getLname()+" "+entry1.getFname()+" "+entry1.getMname().charAt(0)).compareTo(entry2.getLname()+" "+entry2.getFname()+" "+entry2.getMname().charAt(0));
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("department")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Student entry1 = (Student) arg0;
						Student entry2 = (Student) arg1;
						int result = (entry1.getDept_name()).compareTo(entry2.getDept_name());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("college")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Student entry1 = (Student) arg0;
						Student entry2 = (Student) arg1;
						int result = (entry1.getCollege_name()).compareTo(entry2.getCollege_name());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
		}
		return sortedEntries.subList(first, first + count);
	}
	
}
