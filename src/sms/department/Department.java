package sms.department;

import java.io.Serializable;

public class Department implements Serializable{
	
	private int dept_code;
	private int college_code;
	private String dept_name;
	
	public Department(){}
	
	public Department(int deptCode, int collegeCode, String deptName) {
		super();
		dept_code = deptCode;
		college_code = collegeCode;
		dept_name = deptName;
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
	public void setCollege_code(int collegeCode) {
		college_code = collegeCode;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String deptName) {
		dept_name = deptName;
	}
}
