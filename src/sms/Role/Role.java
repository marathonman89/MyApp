package sms.Role;

import java.io.Serializable;

public class Role implements Serializable{
	
	public static String department = "Department";
	public static String administration = "Administration";
	public static String scholarship = "Scholarship";
	public static String admission = "Admission";
	public static String curriculum = "Curriculum Mngt";
	public static String college = "College";
	public static String faculty = "Faculty";
	public static String registration = "Registration";
	public static String hr = "HR";
	
	private String role_name;
	private int role_id;
	
	public Role(){}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String roleName) {
		role_name = roleName;
	}

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int roleId) {
		role_id = roleId;
	}
}
