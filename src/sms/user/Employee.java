package sms.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.authorization.strategies.role.Roles;

public class Employee implements Serializable{
	
	public static String ActiveAcct = "ACTIVE";
	public static String PendingAcct = "PENDING";
	public static String BlockAcct = "BLOCK";
	
	private int employee_id;
	private String fname;
	private String mname;
	private String lname;
	private String bdate;
	private String address;
	private String gender;
	private String work_type;
	private int rank;
	private int cost_center;
	private String email_address; 
	private String password;
	private int role_id;
	private String role_name;
	private int school_year;
	private String semester;
	private int numOfEntry;
	private String dept_name;
	private String college_name;
	private String rank_name;
	private String acct_status;
	
	private Roles roles;
	
	public Employee(){}
	
	
	public Employee(int employee_id){
		this.employee_id = employee_id;
	}
	
	
	public Employee(int employee_id, String[] smsRoles){
		this.employee_id = employee_id;
		roles = new Roles(smsRoles);
	}
	public Employee(int employee_id, String smsRoles){
		this.employee_id = employee_id;
		roles = new Roles(smsRoles);
	}
	public Employee(int employee_id, String fname, String mname, String lname, String bdate,
			        String address, String gender, String email_address, int rank, int cost_center, 
			        String work_type, String password, String acct_status, int role_id){
		super();
		this.employee_id = employee_id;
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		this.bdate = bdate;
		this.address = address;
		this.gender = gender;
		this.email_address = email_address;
		this.rank = rank;
		this.cost_center = cost_center;
		this.work_type = work_type;
		this.password = password;
		this.acct_status = acct_status;
		this.role_id = role_id;
	}
	/*
	public Employee(int employeeId, String fname, String mname, String lname,
			String bdate, String address, String gender, String workType,
			int rank, int costCenter, String password, int roleId, String role_name) {
		super();
		employee_id = employeeId;
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		this.bdate = bdate;
		this.address = address;
		this.gender = gender;
		work_type = workType;
		this.rank = rank;
		cost_center = costCenter;
		this.password = password;
		role_id = roleId;
		this.role_name = role_name;
	}
	*/
	/*
	public Employee(int emp_id, String fname, String lname, String mname, String bdate, String address, char gender, 
					String wt, int work_rank_code, int cost_center_id,	String email_address){
		employee_id = emp_id;
		this.fname = fname;
		this.lname = lname;
		this.mname = mname;
		this.bdate = bdate;
		this.address = address;
		this.gender = gender;
		work_type = wt;
		rank = work_rank_code;
		cost_center = cost_center_id;
		this.email_address = email_address;
	} */
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employeeId) {
		employee_id = employeeId;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getWork_type() {
		return work_type;
	}
	public void setWork_type(String workType) {
		work_type = workType;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getCost_center() {
		return cost_center;
	}
	public void setCost_center(int costCenter) {
		cost_center = costCenter;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int roleId) {
		role_id = roleId;
	}
	public String getRole_name(){
		return role_name;
	}
	public void setRole_name(String role_name){
		this.role_name = role_name;
	}

	public int getSchool_year() {
		return school_year;
	}

	public void setSchool_year(int schoolYear) {
		school_year = schoolYear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}
	public static String displayAccountId(int id){
		String str = Integer.toString(id);
		return str.substring(0,4)+"-"+str.substring(4, str.length());
	}
	/*
	public static List selectEntries(List list, int first, int count){
		return list.subList(first, first + count);
	} */
	public static List selectEntries(List list, int first, int count, final org.apache.wicket.extensions.markup.html.repeater.util.SortParam sortParam) {
		List sortedEntries = new ArrayList(list);
		if (sortParam != null) {
			if (sortParam.getProperty().equals("employee_id")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Employee entry1 = (Employee) arg0;
						Employee entry2 = (Employee) arg1;
						int result = ((Integer)entry1.getEmployee_id()).compareTo((Integer)entry2.getEmployee_id());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("name")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Employee entry1 = (Employee) arg0;
						Employee entry2 = (Employee) arg1;
						int result = entry1.getLname().compareTo(entry2.getLname());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("department")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Employee entry1 = (Employee) arg0;
						Employee entry2 = (Employee) arg1;
						int result = entry1.getDept_name().compareTo(entry2.getDept_name());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("college")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Employee entry1 = (Employee) arg0;
						Employee entry2 = (Employee) arg1;
						int result = entry1.getCollege_name().compareTo(entry2.getCollege_name());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
			if (sortParam.getProperty().equals("acct_status")) {
				Collections.sort(sortedEntries, new Comparator() {
					public int compare(Object arg0, Object arg1) {
						Employee entry1 = (Employee) arg0;
						Employee entry2 = (Employee) arg1;
						int result = entry1.getAcct_status().compareTo(entry2.getAcct_status());
					return sortParam.isAscending() ? result : -result;
				}
				});
			}
		}
		return sortedEntries.subList(first, first + count);
		//return list.subList(first, first + count);
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String emailAddress) {
		email_address = emailAddress;
	}

	public int getNumOfEntry() {
		return numOfEntry;
	}

	public void setNumOfEntry(int numOfEntry) {
		this.numOfEntry = numOfEntry;
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

	public String getRank_name() {
		return rank_name;
	}

	public void setRank_name(String rankName) {
		rank_name = rankName;
	}

	public String getAcct_status() {
		return acct_status;
	}

	public void setAcct_status(String acctStatus) {
		acct_status = acctStatus;
	}
	public boolean hasAnyRole(Roles roles)
    {
        return this.roles.hasAnyRole(roles);
    }
	
}
