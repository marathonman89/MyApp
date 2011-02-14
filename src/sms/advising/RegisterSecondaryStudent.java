package sms.advising;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import sms.index.Home;
import sms.lookup.Lookup;
import sms.program.Program;
import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.subject.Subject;
import sms.subject.SubjectQuery;

public class RegisterSecondaryStudent extends Home{
	
	public RegisterSecondaryStudent(){
		Student student = ((MySession) getSession()).getStudentToBeRegistered();
		((MySession)getSession()).setStudentToBeRegistered(null);
		
		Form form = new Form("form");
		final Lookup lookup = Lookup.EnrollmentStatus();
		
		System.out.println("Student id: " + student.getStudent_id());
		System.out.println("student section: " + student.getSection());
		System.out.println("school year:" + student.getSchool_year());
		System.out.println("semester: " + student.getSemester());
		System.out.println("firstname: " + student.getFname());
		System.out.println("lastname: " + student.getLname());
		System.out.println("middle name: " + student.getMname());
		System.out.println("program code "+student.getProgram_code()); 
		System.out.println("student education level: "+ student.getEduc_level());
		
		Program program = Program.getProgramFromCode(student.getProgram_code());
		String sy = Integer.toString(lookup.getYear_started())+"-"+Integer.toString(lookup.getYear_started()+1);
		String sem = lookup.getTwoSems();
		
		form.add(new Button("done"){
			public void onSubmit(){
				setResponsePage(Help.class);
			}
		});
		
		
		if(!student.isRegistered()){
			register(student);
		}
		
		List<Subject> record = getSecondaryStudentRecord(student);
		ListView allrecord = new ListView("record", record){
			protected void populateItem(ListItem item){
				Subject subj = (Subject) item.getModelObject();
				item.add(new Label("subject_name", new PropertyModel(subj, "subject_name")));
				item.add(new Label("time", new PropertyModel(subj, "time")));
				item.add(new Label("day", new PropertyModel(subj, "day")));
				item.add(new Label("room", new PropertyModel(subj, "room")));
			}
		};
		
		add(allrecord);
		add(form);
		add(new Label("sy", sy));
		add(new Label("sem", sem));
		add(new Label("student_id", Student.displayStudentId(student.getStudent_id())));
		add(new Label("fname", student.getFname()));
		add(new Label("mname", student.getMname()));
		add(new Label("lname", student.getLname()));
		add(new Label("program", program.getProgram_name()));
		add(new Label("year", Integer.toString(student.getYear_level())));
		add(new Label("section", student.getSection()));
	}
	
	
	private void register(Student s){
		int i = 0;
		SubjectQuery query = new SubjectQuery();
		StudentQuery query1 = new StudentQuery();
		List<Subject> subjects = query.getData(s, "getControllableSubjects");
		while(subjects.size() > 0 && i < subjects.size() ){
			s.setOffered_id(subjects.get(i).getOffered_id());
			query1.insertData(s, "controlSubject");
			query1.updateData(s, "decSlot");
			i++;
		}
	} 
	
	/*
	private boolean isRegistered(Student s){
		StudentQuery query = new StudentQuery();
		Integer num = query.getIntegerDatum(s, "isRegistered");
		//System.out.println("enrolled subjects: " + num.toString());
		if(num != null && num > 0)
			return true;
		
		return false;
	}
	*/
	private List<Subject> getSecondaryStudentRecord(Student s){
		StudentQuery query = new StudentQuery();
		return query.getStudentData(s, "getSecondaryStudentRecord");
	}
}
