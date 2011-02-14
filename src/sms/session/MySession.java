package sms.session;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import sms.Role.Role;
import sms.program.Program;
import sms.scholarship.Scholarship;
import sms.student.Student;
import sms.subject.Subject;
import sms.user.Employee;

@SuppressWarnings("serial")
public class MySession extends WebSession{
	
	private Employee employee;
	private List<Program> program;
	private Student student;
	private List<Subject> subjects;
	private Subject selectedSubjectInGradingSheet;
	private Subject subject;
	private Subject classListSearch;
	private List<Employee> userAcctSearch;
	private Employee facultySearch;
	private Student studentSearch;
	private boolean successOnAddFaculty;
	private boolean successOnAdvise;
	private boolean successEditScholar;
	private boolean successAddScholar;
	private boolean successOnAddScholar;
	private boolean successOnAddStudent;
	//private List<Role> userLoggedRoles;
	private Employee userLoggedRoles;
	private Scholarship studentScholarshipSearch;
	public MySession(Request request) {
		super(request);
	}
	
	@SuppressWarnings("deprecation")
	public MySession(Application application, Request request) {
		super(application, request);
	}
	
	public void setLoggedEmployee(Employee employee){
		this.employee = employee;
	}
	
	public Employee getLoggedEmployee(){
		return employee;
	}
	
	public boolean isSessionSet(){
		if(employee == null)
			return false;
		return true;
	}

	public List<Program> getProgram() {
		return program;
	}

	public void setProgram(List<Program> program) {
		this.program = program;
	}
	
	public void setStudentToBeRegistered(Student student){
		this.student = student;
	}
	
	public Student getStudentToBeRegistered(){
		return student;
	}
	/*
	public void setSearchedSubject(List<Subject> subjects){
		this.subjects = subjects;
	}
	
	public List<Subject> getSearchedSubject(){
		return subjects;
	}
	*/
	public void setSearchedSubject(Subject subject){
		this.subject = subject;
	}
	
	public Subject getSearchedSubject(){
		return subject;
	}

	public Subject getSelectedSubjectInGradingSheet() {
		return selectedSubjectInGradingSheet;
	}

	public void setSelectedSubjectInGradingSheet(
			Subject selectedSubjectInGradingSheet) {
		this.selectedSubjectInGradingSheet = selectedSubjectInGradingSheet;
	}

	public Subject getClassListSearch() {
		return classListSearch;
	}
	
	public Scholarship getStudentScholarshipSearch() {
		return studentScholarshipSearch;
	}

	public void setClassListSearch(Subject classListSearch) {
		this.classListSearch = classListSearch;
	}

	public void setStudentScholarshipSearch(Scholarship studScholarshipSearch) {
		studentScholarshipSearch = studScholarshipSearch;
	}
	
	public void setStudentSearch(Student studentSearch) {
		this.studentSearch = studentSearch;
	}
	
	public List<Employee> getUserAcctSearch() {
		return userAcctSearch;
	}

	public void setUserAcctSearch(List<Employee> userAcctSearch) {
		this.userAcctSearch = userAcctSearch;
	}

	public boolean isSuccessOnAddFaculty() {
		return successOnAddFaculty;
	}

	public void setSuccessOnAddFaculty(boolean successOnAddFaculty) {
		this.successOnAddFaculty = successOnAddFaculty;
	}

	public Employee getFacultySearch() {
		return facultySearch;
	}
	
	public Student getStudentSearch() {
		return studentSearch;
	}

	public void setFacultySearch(Employee facultySearch) {
		this.facultySearch = facultySearch;
	}

	public boolean isSuccessOnAdvise() {
		return successOnAdvise;
	}

	public boolean isSuccessOnAddStudent() {
		return successOnAddStudent;
	}
	public void setSuccessOnAdvise(boolean successOnAdvise) {
		this.successOnAdvise = successOnAdvise;
	}

	public Employee getUserLoggedRoles() {
		return userLoggedRoles;
	}

	public void setUserLoggedRoles(Employee userLoggedRoles) {
		this.userLoggedRoles = userLoggedRoles;
	}
	
	public void setSuccessOnEditScholar(boolean successEditScholar) {
		this.successEditScholar = successEditScholar;
	}
	
	public void setSuccessOnAddScholar(boolean successAddScholar) {
		this.successAddScholar = successAddScholar;
	}
	
	public boolean isSuccessOnAddScholar() {
		return successOnAddScholar;
	}
	
	public void setSuccessOnAddStudent(boolean successOnAddStudent) {
		this.successOnAddStudent = successOnAddStudent;
	}
}
