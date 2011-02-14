package sms.advising;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import sms.program.Program;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.session.MySession;
import sms.index.Home;
import sms.lookup.Lookup;

public class SearchForSubject extends Home{
	
	private String search;
	private FeedbackPanel feedback;
	private Form form1;
	private Form form2;
	private Form form3;
	private Student student;
	
	public SearchForSubject(){
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		
		final CheckGroup group = new CheckGroup("group", new ArrayList());
		final Lookup lookup = Lookup.EnrollmentStatus();
		student = ((MySession)getSession()).getStudentToBeRegistered();
		Program program = Program.getProgramFromCode(student.getProgram_code());
		
		String sy = Integer.toString(lookup.getYear_started())+"-"+Integer.toString(lookup.getYear_started()+1);
		String sem = lookup.getSem_started();
		
		form1 = new Form("form1");
		form2 = new Form("form2", new CompoundPropertyModel(this));
		form3 = new Form("controlledForm");
		
		FormComponent fc;
		
		fc = new RequiredTextField("search");
		form2.add(fc);
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form2, "onsubmit", Duration.ONE_SECOND);
        form2.add(new AjaxButton("searchbutton", form2){
            @Override
            protected void onError(AjaxRequestTarget target, Form form){
                target.addComponent(feedback);
            }
			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
				Subject subj = new Subject();
				subj.setSubject_name(search);
				subj.setSemester(lookup.getSem_started());
				subj.setSchool_year(lookup.getYear_started());
				
				((MySession)getSession()).setSearchedSubject(subj);
				if(getSearchedSubj(subj) != null){
					setResponsePage(ControlSubject.class);
				}else
					error("Subject not available");
				/*
				SubjectQuery query1 = new SubjectQuery();
				List<Subject> offered = query1.getData(subj, "getOfferedSubject");
				if(offered.size()>0){ 
					for(int i = 0; i < offered.size(); i++){
						Subject tmp = new Subject();
						SubjectQuery query2 = new SubjectQuery();
						
						tmp.setOffered_id(offered.get(i).getOffered_id());
						
						
						String deptname = query2.getStringData(tmp, "getDeptFromDeptCode");
						String progname = query2.getStringData(tmp, "getProgFromProgCode");
						String colname  = query2.getStringData(tmp, "getColFromColCode");
						
						offered.get(i).setDept_name(deptname);
						offered.get(i).setProgram_name(progname);
						offered.get(i).setCollege_name(colname); 
					} 
					((MySession)getSession()).setSearchedSubject(offered); 
					setResponsePage(ControlSubject.class);
				}else{
					error("Subject not available!");
				} */
			}
        });
        
        List<Subject> record = getControlledSubj(student, lookup.getYear_started(), lookup.getSem_started());
		ListView allrecord = new ListView("record", record){
			protected void populateItem(ListItem item){
				Subject subj = (Subject) item.getModelObject();
				item.setModel(new CompoundPropertyModel(subj));

				item.add(new Label("subject_name"));
				item.add(new Label("subject_section"));
				item.add(new Label("time"));
				item.add(new Label("day"));
				item.add(new Label("room"));
				item.add(new Label("units"));
				item.add(new Check("checkbox", item.getModel()));
			}
		};
		
		//a javascript confirmation should be better here
		form1.add(new Button("done"){
			public void onSubmit(){
				((MySession)getSession()).setStudentToBeRegistered(null);
				((MySession)getSession()).setSearchedSubject(null);
				setResponsePage(Help.class);
			}
		});
		form3.add(new Button("remove"){
			public void onSubmit(){
				StudentQuery query = new StudentQuery();
				List<Subject> subject = (List<Subject>) group.getModelObject();
				
				for(int i = 0; i < subject.size(); i++){
					int recid = subject.get(i).getRecord_id();
					int offered_id = subject.get(i).getOffered_id();
					student.setRecord_id(recid);
					student.setOffered_id(offered_id);
					query.deleteData(student, "delRecord");
					query.updateData(student, "incSlot");
				}
				if(((MySession)getSession()).getSearchedSubject()==null)
					setResponsePage(SearchForSubject.class);
				else
					setResponsePage(ControlSubject.class);	
			}
		});
		
		recordVisibility(record.size());
		group.add(allrecord);
		form3.add(group);
		add(feedback);
		add(form1);
        add(form2);
        add(form3);
        add(new Label("sy", sy));
		add(new Label("sem", sem));
		add(new Label("student_id", Student.displayStudentId(student.getStudent_id())));
		add(new Label("fname", student.getFname()));
		add(new Label("mname", student.getMname()));
		add(new Label("lname", student.getLname()));
		add(new Label("program", program.getProgram_name()));
		add(new Label("year", Integer.toString(student.getYear_level())));
		form3.add(new Label("totalunits", Integer.toString(getNumberOfUnits(record))));
	}

	public FeedbackPanel getFeedbackPanel(){
		return feedback;
	}
	
	public List<Subject> getSearchedSubj(Subject subj){
		
		SubjectQuery query1 = new SubjectQuery();
		List<Subject> offered = query1.getData(subj, "getOfferedSubject");
		
		if(offered.size() > 0){
			for(int i = 0; i < offered.size(); i++){
				Subject tmp = new Subject();
				SubjectQuery query2 = new SubjectQuery();
				
				tmp.setOffered_id(offered.get(i).getOffered_id());
				
				String deptname = query2.getStringData(tmp, "getDeptFromDeptCode");
				String progname = query2.getStringData(tmp, "getProgFromProgCode");
				String colname  = query2.getStringData(tmp, "getColFromColCode");
				
				offered.get(i).setDept_name(deptname);
				offered.get(i).setProgram_name(progname);
				offered.get(i).setCollege_name(colname); 
			}
			return offered;
		}else
			return null;
	}
	private List<Subject> getControlledSubj(Student stud, int sy, String sem){
		SubjectQuery query = new SubjectQuery();
		stud.setSchool_year(Integer.toString(sy));
		stud.setSemester(sem);
		
		return query.getData(stud, "studentRecord");
	}
	
	private void recordVisibility(int size){
		if(size>0)
			form3.setVisible(true);
		else
			form3.setVisible(false);
	}
	
	private int getNumberOfUnits(List<Subject> subjects){
		int total = 0;
		for(int i = 0; i < subjects.size(); i++){
			total+=subjects.get(i).getUnits();
		}
		return total;
	}
	
}
