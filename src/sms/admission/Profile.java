package sms.admission;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import sms.index.Home;
import sms.lookup.Lookup;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.user.Employee;


public class Profile extends Home {
	public Profile(Student stud){
		/*Lookup l = Lookup.queryForEnrollmentStatus();
		int school_year = l.getYear_started();
		String semester = notInSecondary(emp) ? l.getSem_started() : l.getTwoSems();
		
		emp.setSchool_year(school_year);
		emp.setSemester(semester);
		
		List subjects = getHandledSubjects(emp);
		Label noentry = new Label("noentry","No Subjects Handled!");
		ListView eachSubject = new ListView("eachSubject", subjects){
		protected void populateItem(ListItem item) {
			Subject subj = (Subject) item.getModelObject();
			item.setModel(new CompoundPropertyModel(subj));
			
			String[] tokens = Subject.getCourseNumberAndDesc(subj.getSubject_name());
			item.add(new Label("subject_section"));
			item.add(new Label("subject_code", tokens[0]));
			item.add(new Label("subject_name", tokens[1])); 
			item.add(new Label("time"));
			item.add(new Label("day"));
			item.add(new Label("room"));
		}}; 
		
		if(subjects.size() > 0)
			noentry.setVisible(false);
		else
			noentry.setVisible(true);*/
		
		
		String gender = stud.getGender().equals("M") ? "Male" : "Female";
		add(new Label("student_id", Employee.displayAccountId(stud.getStudent_id())));
		add(new Label("fname", stud.getFname()));
		add(new Label("mname", stud.getMname()));
		add(new Label("lname", stud.getLname()));
		add(new Label("bdate", stud.getBdate()));
		add(new Label("gender", gender));
		add(new Label("barangay", stud.getBarangay()));
		add(new Label("municipality", stud.getMunicipality()));
		add(new Label("province", stud.getProvince()));
		add(new Label("college", stud.getCollege_name()));
		add(new Label("department", stud.getDept_name()));
		
		if(getEnrolledStudent(stud).size() > 0)
			add(new Label("enrolled", new Model("Yes")));
		else
			add(new Label("enrolled", new Model("No")));
		//add(noentry);
		
	}
	
	private List<Student> getEnrolledStudent(Student studId) {
		StudentQuery query = new StudentQuery();
		return query.fetchListOfData(studId, "getEnrolledStudent");
	}
	
}
