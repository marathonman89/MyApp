package sms.faculty;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import sms.lookup.Lookup;
import sms.session.MySession;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.user.Employee;
import sms.user.EmployeeQuery;

public class GradingSheet extends Layout{
	
	private Form form;
	
	public GradingSheet(){
		Employee logged = ((MySession)getSession()).getLoggedEmployee();
		
		Lookup l = Lookup.EnrollmentStatus();
		int school_year = l.getYear_started();
		String semester = notInSecondary(logged) ? l.getSem_started() : l.getTwoSems();
		
		logged.setSemester(semester);
		logged.setSchool_year(school_year);

		form = new Form("subjectsHandled");
		final RadioGroup group = new RadioGroup("group", new Model());

		List subjects = getHandledSubjects(logged);
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
			item.add(new Radio("selected", item.getModel()));
		}}; 
		
		Label noentry = new Label("noentry","No Subjects Handled!");
		Button go = new Button("go"){
			public void onSubmit(){
				Subject subj = (Subject) group.getModelObject();
				if(subj != null){
					((MySession)getSession()).setSelectedSubjectInGradingSheet(subj);
					setResponsePage(sms.faculty.Actions.class);
				}
					
			}
		};
		

		if(subjects.size() > 0){
			noentry.setVisible(false);
			go.setVisible(true);
		}else{
			noentry.setVisible(true);
			go.setVisible(false);
		}
		
		group.add(eachSubject);
		form.add(noentry);
		form.add(go);
		form.add(group);
		add(form);
	}
	
	//to be decided on when to use
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
