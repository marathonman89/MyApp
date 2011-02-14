package sms.college;

import java.io.Serializable;

public class College implements Serializable{

	private int college_code;
	private String college_name;
	
	public College(int collegeCode, String collegeName) {
		super();
		college_code = collegeCode;
		college_name = collegeName;
	}
	
	public College(){}

	public int getCollege_code() {
		return college_code;
	}

	public void setCollege_code(int collegeCode) {
		college_code = collegeCode;
	}

	public String getCollege_name() {
		return college_name;
	}

	public void setCollege_name(String collegeName) {
		college_name = collegeName;
	}

	
}
