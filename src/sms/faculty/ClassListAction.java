package sms.faculty;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;

import sms.index.Home;
import sms.session.MySession;
import sms.student.Student;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.user.Employee;

public class ClassListAction extends Home{

	private Form form;
	//private Form back;
	private ListView enrolled;
	
	public ClassListAction(final Subject subj){
		form = new Form("enrolledstudents");
		//back = new Form("backform");
		//back.add(new Button("back"){
			//public void onSubmit(){
				//setResponsePage(sms.faculty.ClassList.class);
			//}
		//});
		List<Student> students = getEnrolledStudents(subj);
		enrolled = new ListView("enrolled", students){
			protected void populateItem(ListItem item){
				Student stud = (Student) item.getModelObject();
				item.setModel(new CompoundPropertyModel(stud));
				
				String name = stud.getLname() + ", " + stud.getFname() + " " + stud.getMname();
				
				item.add(new Label("student_id", Student.displayStudentId(stud.getStudent_id())));
				item.add(new Label("name", name));
			}
		};
		AjaxButton export = new AjaxButton("export", form){
			protected void onError(AjaxRequestTarget target, Form form){
			}
			protected void onSubmit(AjaxRequestTarget target, Form form){
				List<Student> list = enrolled.getList(); 
				ExportClassListToCsv(list, subj);
				target.addJavascript("jAlert('CSV has been exported in your C: directory')");
			}
		};
		Label noentry = new Label("noentry", "No Enrolled Students!");
		
		if(students.size() > 0)
			noentry.setVisible(false);
		else
			export.setVisible(false);
		
		String[] subCodeAndName = Subject.getCourseNumberAndDesc(subj.getSubject_name());
		add(new Label("subject_code", subCodeAndName[0]));
		add(new Label("subject_name", subCodeAndName[1]));
		add(new Label("subject_section", subj.getSubject_section()));
		add(new Label("day", subj.getDay()));
		add(new Label("time", subj.getTime()));
		add(new Label("room", subj.getRoom()));
		add(new Label("school_year", Integer.toString(subj.getSchool_year())));
		add(new Label("semester", subj.getSemester()));
		form.add(enrolled);
		form.add(noentry);
		form.add(export);
		//add(back);
		add(form);
	}
	private List<Student> getEnrolledStudents(Subject subj){
		SubjectQuery query = new SubjectQuery();
		return query.getListOfStudents(subj, "getEnrolledStudents");
	}
	private void ExportClassListToCsv(List<Student> list, Subject subj){
		try
		{
			String[] subCodeAndName = Subject.getCourseNumberAndDesc(subj.getSubject_name());
			String fileName = "C:\\Enrollees_For_"+subCodeAndName[0]+"_"+subj.getSubject_section()+".csv";
			
			FileWriter writer = new FileWriter(fileName);
		 
			writer.append("Subject Code");
			writer.append(',');
			writer.append(subCodeAndName[0]);
			writer.append('\n');
		 
			writer.append("Subject Description");
			writer.append(',');
			writer.append(subCodeAndName[1]);
			writer.append('\n');
		 
			writer.append("Section");
			writer.append(',');
			writer.append(subj.getSubject_section());
			writer.append('\n');
		   
			writer.append("Day");
			writer.append(',');
			writer.append(subj.getDay());
			writer.append('\n');
		 
			writer.append("Time");
			writer.append(',');
			writer.append(subj.getTime());
			writer.append('\n');

			writer.append("Room");
			writer.append(',');
			writer.append(subj.getRoom());
			writer.append('\n');
			
			writer.append('\n');
			writer.append('\n');
			
			writer.append("Student ID");
			writer.append(',');
			writer.append("First Name");
			writer.append(',');
			writer.append("Middle Name");
			writer.append(',');
			writer.append("Last Name");
			writer.append(',');
			writer.append('\n');
			
			for(int i = 0; i < list.size(); i++){
				Student tmp = list.get(i);
				writer.append(Student.displayStudentId(tmp.getStudent_id()));
				writer.append(',');
				writer.append(tmp.getFname());
				writer.append(',');
				writer.append(tmp.getMname());
				writer.append(',');
				writer.append(tmp.getLname());
				writer.append('\n');
			}
			
			writer.flush();
			writer.close();
		  }
		  catch(IOException e)
		  {
		   e.printStackTrace();
		  }
	}
}
