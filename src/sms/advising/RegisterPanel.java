package sms.advising;

import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;

import sms.lookup.Lookup;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.session.MySession;

public class RegisterPanel extends Panel{
	private Form form1;
	private Form form2;
	private FeedbackPanel feedback;
	private FormComponent[] fc;
	private String idpart1;
	private String idpart2;
	private Lookup lookup;
	
	public RegisterPanel(String id){
		super(id);
		
		lookup = Lookup.EnrollmentStatus();
		
		form1 = new Form("searchform", new CompoundPropertyModel(this));
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		
		fc = new FormComponent[2];
		fc[0] = new RequiredTextField("idpart1");
		fc[0].add(StringValidator.minimumLength(4));
		form1.add(fc[0]);
		
		fc[1] = new RequiredTextField("idpart2");
		fc[1].add(StringValidator.minimumLength(3));
		form1.add(fc[1]);
		
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form1, "onsubmit", Duration.ONE_SECOND);
        form1.add(new AjaxButton("search", form1){
            @Override
            protected void onError(AjaxRequestTarget target, Form form)
            {
                //target.addComponent(feedback);

				if(fc[0].hasErrorMessage() || fc[1].hasErrorMessage()){
					target.addJavascript("jAlert('Provide a valid input for the ID Number')");
				}
            }
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				Boolean enrollmentEnabled = lookup.isEnrollmentEnabled();
				if(!Pattern.matches("[0-9]+", idpart1) || !Pattern.matches("[0-9]+", idpart2)){
					target.addJavascript("jAlert('ID number must be numeric')");
					//error("ID number must be numeric");
				}else{
					if(enrollmentEnabled != null && enrollmentEnabled){
						StudentQuery query1 = new StudentQuery();
						Student student = new Student();
						Student tmp = new Student(); 
						
						student.setStudent_id(Integer.parseInt(idpart1+idpart2));
						
						if((tmp = query1.getData(student, "getStudentProfile"))!=null){
							
							String regSemester = tmp.getEduc_level().equals("tertiary") ? lookup.getSem_started() : "1st-2nd";
							String regSchoolYear = Integer.toString(lookup.getYear_started());
							
							student.setSchool_year(regSchoolYear);
							student.setSemester(regSemester);

							if((student = query1.getData(student, "registrationInfo"))!=null){
								String educlevel = tmp.getEduc_level();
								
								student.setSchool_year(regSchoolYear);
								student.setSemester(regSemester);
								student.setEduc_level(educlevel);
								
								((MySession)getSession()).setStudentToBeRegistered(student);
								if(educlevel.equals(Student.tertiary))
									setResponsePage(SearchForSubject.class);
								if(educlevel.equals(Student.secondary) && !student.isRegistered())
									setResponsePage(RegisterSecondaryStudent.class);
								else if(educlevel.equals(Student.secondary) && student.isRegistered()){
									target.addJavascript("jAlert('Student has already been registered!')");
									((MySession)getSession()).setStudentToBeRegistered(null);
								}
							}else{
								target.addJavascript("jAlert('Student has not been advised yet!')");
							}

						}else{
							//error("Student not found on the database!");
							target.addJavascript("jAlert('Student not found on the database!')");	
						} //end of student profile query block 
					}else{
						target.addJavascript("jAlert('Enrollemnt has been disabled')");
						//error("Enrollment has been disabled!");
					} //end of enrollment checking block
				} //end of onsubmit if else block
						
			}//end of onSubmit
        });
        form1.add(feedback);
        add(form1);
	}
}
