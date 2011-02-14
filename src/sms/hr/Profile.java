package sms.hr;

import java.util.List;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;

import sms.index.Home;
import sms.lookup.Lookup;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.user.Employee;
import sms.user.EmployeeQuery;

@AuthorizeInstantiation("HR")
public class Profile extends Home{
	
	public Profile(Employee emp){
		Lookup l = Lookup.EnrollmentStatus();
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
			noentry.setVisible(true);

		
		String gender = emp.getGender().equals("M") ? "Male" : "Female";
		add(new Label("employee_id", Employee.displayAccountId(emp.getEmployee_id())));
		add(new Label("fname", emp.getFname()));
		add(new Label("mname", emp.getMname()));
		add(new Label("lname", emp.getLname()));
		add(new Label("bdate", emp.getBdate()));
		add(new Label("gender", gender));
		add(new Label("address", emp.getAddress()));
		add(new Label("emailaddress", emp.getEmail_address()));
		add(new Label("rank", emp.getRank_name()));
		add(new Label("college", emp.getCollege_name()));
		add(new Label("department", emp.getDept_name()));
		add(eachSubject);
		add(noentry);
		
	}
	private boolean notInSecondary(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		String str = query.getStringOfData(emp, "getDeptOfEmployee");
		String ids = "IDS";
		if(str.contains(ids))
			return false;
		
		return true;
	}
	private List getHandledSubjects(Employee emp){
		SubjectQuery query = new SubjectQuery();
		return query.getData(emp, "getHandledSubjects");
	}
}
