package sms.advising;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.subject.Subject;
import sms.subject.SubjectQuery;

public class ControlSubject extends SearchForSubject{
	private Form form;
	
	public ControlSubject(){
		
		final CheckGroup group = new CheckGroup("group", new ArrayList());
		form = new Form("controlform"){
			public void onSubmit(){
				List<Subject> subjects = (List<Subject>) group.getModelObject();
				
				boolean hasDuplicateSubj = false;
			    for(int i = 0; i < subjects.size(); i++){
			      for(int j = 0; j < subjects.size(); j++){
			        if(i != j && subjects.get(i).getSubject_code()==subjects.get(j).getSubject_code()){
			        	hasDuplicateSubj = true;
			          break;
			        }
			      }
			    }
			    if(hasDuplicateSubj){
			    	error("You have selected subjects that are similar! Try again");
			    }else{
			    	SubjectQuery query1 = new SubjectQuery();
					StudentQuery query2 = new StudentQuery();
					Student stud = ((MySession)getSession()).getStudentToBeRegistered();
					
					List<Subject> record = query1.getData(stud, "studentRecord");
					
					for(int i = 0; i < subjects.size(); i++){
						int j = 0;
						for(; j < record.size(); j++){
							int offered_id = record.get(j).getOffered_id();
							int subject_code = record.get(j).getSubject_code();
							
							if(subjects.get(i).getOffered_id()== offered_id ||
							   subjects.get(i).getSubject_code() == subject_code)
								break;
						}
						if(!(j<record.size())){ //controllable
							stud.setOffered_id(subjects.get(i).getOffered_id());
							query2.insertData(stud, "controlSubject");
							query2.updateData(stud, "decSlot");
						}
					}
					setResponsePage(ControlSubject.class);
				}
			}
		};
		
		//List subjectsOffered = ((MySession)getSession()).getSearchedSubject();
		List subjectsOffered = getSearchedSubj(((MySession)getSession()).getSearchedSubject());
		ListView eachSubject = new ListView("eachSubject", subjectsOffered){
		protected void populateItem(ListItem item) {
			Subject subj = (Subject) item.getModelObject();
			item.setModel(new CompoundPropertyModel(subj));
			
			item.add(new Label("subject_name"));
			item.add(new Label("subject_section"));
			item.add(new Label("time"));
			item.add(new Label("day"));
			item.add(new Label("room"));
			item.add(new Label("slot"));
			
			String dept = getFirstToken(subj.getDept_name());
			String college = getFirstToken(subj.getCollege_name());
			String program = getFirstToken(subj.getProgram_name());
			
			item.add(new Label("dept_name", dept));
			item.add(new Label("college_name", college));
			item.add(new Label("program_name", program));
			item.add(new Check("checkbox", item.getModel()));
		}
		};
		group.add(eachSubject);
		form.add(group);
		add(form);
	}
	
	private String getFirstToken(String str){
		if(str != null){
			StringTokenizer token = new StringTokenizer(str);
			return token.nextToken();
		}
		return null;
	}

}
