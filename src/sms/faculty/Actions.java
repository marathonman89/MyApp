package sms.faculty;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;

import sms.index.Home;
import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.user.Employee;
import sms.user.EmployeeQuery;

public class Actions extends Home{
	
	private Form form;
	//private Form back;
	private ListView enrolled;
	private Subject subj;
	private boolean gradingsheetstatus;
	private static final List<String> ratingChoices1 = Arrays.asList(new String[] { "1.0", "1.25", "1.50",
			"1.75", "2.0", "2.25", "2.50", "2.75", "3.0", "5.0", "drp", "inc", "wdrw"});
	
	public Actions(){
		subj = ((MySession) getSession()).getSelectedSubjectInGradingSheet();
		form = new Form("gradingsheetform");
		//back = new Form("backform");
		//back.add(new Button("back"){
			//public void onSubmit(){
				//setResponsePage(sms.faculty.GradingSheet.class);
			//}
		//});
		
		gradingsheetstatus = isGradingSheetLock(subj);
		final FeedbackPanel feedback = new FeedbackPanel("error");
		List<Student> students = getEnrolledStudents(subj);
		enrolled = new ListView("enrolled", students){
			protected void populateItem(ListItem item){
				Employee logged = ((MySession)getSession()).getLoggedEmployee(); 
				Student stud = (Student) item.getModelObject();
				item.setModel(new CompoundPropertyModel(stud));
				
				String name = stud.getLname() + ", " + stud.getFname() + " " + stud.getMname();
				
				item.add(new Label("student_id", Student.displayStudentId(stud.getStudent_id())));
				item.add(new Label("name", name));
				if(notInSecondary(logged))
					item.add(new AjaxEditableChoiceLabel("rating", ratingChoices1));
				else{
					List ratingChoices2 = new ArrayList();
					for(int i = 70; i < 100; i++){
						ratingChoices2.add(Integer.toString(i));
					}
					item.add(new AjaxEditableChoiceLabel("rating", ratingChoices2));
				}
			}
		};
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
		AjaxButton save = new AjaxButton("save", form){
			protected void onError(AjaxRequestTarget target, Form form){
				target.addComponent(feedback);
			}
			protected void onSubmit(AjaxRequestTarget target, Form form){
				List<Student> list = enrolled.getList(); 
				for(int i = 0; i < list.size(); i++){
					Student tmp = list.get(i);
					
					if(!gradingsheetstatus){
						StudentQuery query = new StudentQuery();
						query.updateData(tmp, "updateGrade");
						setResponsePage(sms.faculty.Actions.class);
					}
				}
			}
		};
		AjaxButton export = new AjaxButton("export", form){
			protected void onError(AjaxRequestTarget target, Form form){
				target.addComponent(feedback);
			}
			protected void onSubmit(AjaxRequestTarget target, Form form){
				List<Student> list = enrolled.getList(); 
				ExportToCsv(list, subj);
				target.addJavascript("jAlert('CSV has been exported in your C: directory')");
			}
		};
		
		Button lock = new Button("lock"){
			public void onSubmit(){
				lockGradingSheet(subj);
				setResponsePage(sms.faculty.Actions.class);
			}
		};
		lock.setVisible(false); //to be decided
		
		Label status = new Label("status", "GRADING SHEET IS LOCK!");
		Label noentry = new Label("noentry", "No Enrolled Students!");
		if(students.size() > 0){
			noentry.setVisible(false);
			save.setVisible(true);
			export.setVisible(true);

			if(allStudentHasGrades(students))
				lock.setVisible(true);
			/* else{
				lock.setVisible(false);
			} */
			if(gradingsheetstatus){
				lock.setVisible(false);
				save.setEnabled(false);
				status.setVisible(true);
			}else{
				status.setVisible(false);
			}
		}else{
			noentry.setVisible(true);
			save.setVisible(false);
			export.setVisible(false);
			lock.setVisible(false);
			status.setVisible(false);
		}
		
		System.out.println("does all students have grades? " + allStudentHasGrades(students));
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
		form.add(save);
		form.add(export);
		form.add(lock);
		//add(back);
		add(form);
		add(feedback);
		add(status);
	}
	private List<Student> getEnrolledStudents(Subject subj){
		SubjectQuery query = new SubjectQuery();
		return query.getListOfStudents(subj, "getEnrolledStudents");
	}
	private boolean allStudentHasGrades(List<Student> student){
		for(int i = 0; i < student.size(); i++){
			if(student.get(i).getRating()==null || student.get(i).getRating().length() <= 0)
				return false;
		}
		return true;
	}
	private boolean isGradingSheetLock(Subject subj){
		SubjectQuery query = new SubjectQuery();
		return query.getBooleanData(subj, "isGradingSheetLocked");
	}
	private void lockGradingSheet(Subject subj){
		SubjectQuery query = new SubjectQuery();
		query.updateData(subj, "lockGradingSheet");
	}
	private boolean notInSecondary(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		String str = query.getStringOfData(emp, "getDeptOfEmployee");
		String ids = "IDS";
		if(str.contains(ids))
			return false;
		
		return true;
	}
	private void ExportToCsv(List<Student> list, Subject subj){
		try
		{
			String[] subCodeAndName = Subject.getCourseNumberAndDesc(subj.getSubject_name());
			String fileName = "C:\\Grading_Sheet_For_"+subCodeAndName[0]+"_"+subj.getSubject_section()+".csv";
			
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
			writer.append("Rating");
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
				writer.append(',');
				writer.append(tmp.getRating());
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
