package sms.scholarship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sms.student.Student;

public class Scholarship implements Serializable {
	private int scholarship_code;
	private int student_id;
	private int stud_scholarship_id;
	private String scholarship_name;
	private String scholarship_status;
	private String lname;
	private String fname;
	private String mname;
	
	public Scholarship() {}
	
	public Scholarship(int studScholarshipId, int studId, int scode, String status) {
		super();
		this.stud_scholarship_id = studScholarshipId;
		student_id = studId;
		scholarship_code = scode;
		this.scholarship_status = status;
	}
	
	public Scholarship(int studId, int scode, String status) {
		super();
		student_id = studId;
		scholarship_code = scode;
		this.scholarship_status = status;
	}
	
	public int getStudScholarshipId() {
		return stud_scholarship_id;
	}
	
	public int getScholarship_code() {
		return scholarship_code;
	}
	
	public String getScholarship_name() {
		return scholarship_name;
	}
	
	public int getStudent_id() {
		return student_id;
	}
	
	public String getStatus() {
		return scholarship_status;
	}
	
	public String getLname() {
		return lname;
	}
	
	public String getFname() {
		return fname;
	}
	
	public String getMname() {
		return mname;
	}
	
	public void setStudScholarshipId(int studId) {
		stud_scholarship_id = studId;
	}
	
	public void setScholarship_code(int code) {
		scholarship_code = code;
	}
	
	public void setScholarship_name(String name) {
		scholarship_name = name;
	}
	
	public void setStudent_id(int studId) {
		student_id = studId;
	}
	
	public void setStatus(String status) {
		this.scholarship_status = status;
	}
	
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public void setMname(String mname) {
		this.mname = mname;
	}
	
	public static String displayAccountId(int id){
		String str = Integer.toString(id);
		return str.substring(0,4)+"-"+str.substring(4, str.length());
	}
	
	public static List selectEntries(List list, int first, int count, final org.apache.wicket.extensions.markup.html.repeater.util.SortParam sortParam) {
		List sortedEntries = new ArrayList(list);
		if (sortParam != null) {
			if (sortParam.getProperty().equals("student_id")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Scholarship entry1 = (Scholarship) arg0;
						Scholarship entry2 = (Scholarship) arg1;
						int result = ((Integer)entry1.getStudent_id()).compareTo((Integer)entry2.getStudent_id());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("name")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Scholarship entry1 = (Scholarship) arg0;
						Scholarship entry2 = (Scholarship) arg1;
						int result = entry1.getLname().compareTo(entry2.getLname());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("scholarshipname")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Scholarship entry1 = (Scholarship) arg0;
						Scholarship entry2 = (Scholarship) arg1;
						int result = entry1.getScholarship_name().compareTo(entry2.getScholarship_name());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("status")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Scholarship entry1 = (Scholarship) arg0;
						Scholarship entry2 = (Scholarship) arg1;
						int result = entry1.getStatus().compareTo(entry2.getStatus());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
		}
		return sortedEntries.subList(first, first + count);
		//return list.subList(first, first + count);
	}

}
