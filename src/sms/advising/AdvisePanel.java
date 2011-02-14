package sms.advising;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.NumberValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sms.department.Department;
import sms.department.DepartmentQuery;
import sms.lookup.Lookup;
import sms.lookup.LookupQuery;
import sms.program.ProgramQuery;
import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.user.Employee;
import sms.user.EmployeeQuery;

public class AdvisePanel extends Panel{
	private Employee logged;
	private Form form;
	private FeedbackPanel feedback;
	private String idpart1;
	private String idpart2;
	private String fname;
	private String mname;
	private String lname;
	private String section;
	private boolean sectVisible;
	private Model program_name;
	private Model year_level;
	private Model school_year;
	private Model semester;
	private TextField semesterField;
	private TextField schoolyearField;
	private Label semester_label;
	private Label section_label;
	private FormComponent[] fc;
	private Lookup lookup;
	
	public AdvisePanel(String id){
		super(id);
		
		lookup = Lookup.EnrollmentStatus();
		
		setLoggedEmployee();
		queryPrograms();
		checkSectionSemVisibility();
		//status = lookup.queryForEnrollmentStatus();
		
		form = new Form("adviseform", new CompoundPropertyModel(this));
		feedback = new FeedbackPanel("error");
		form.setOutputMarkupId(true);
		feedback.setOutputMarkupId(true);
		
		fc = new FormComponent[8];
		
		fc[0] = new RequiredTextField("idpart1");
		fc[0].add(StringValidator.minimumLength(4));
		form.add(fc[0]);
		
		fc[1] = new RequiredTextField("idpart2");
		fc[1].add(StringValidator.minimumLength(3));
		form.add(fc[1]);
		
		fc[2] = new RequiredTextField("fname");
		form.add(fc[2]);
		
		fc[3] = new RequiredTextField("mname");
		form.add(fc[3]);
		
		fc[4] = new RequiredTextField("lname");
		form.add(fc[4]);
		
		program_name = new Model();
		List program_list = new ArrayList();
		for(int i = 0; i < ((MySession)getSession()).getProgram().size(); i++){
			program_list.add(((MySession)getSession()).getProgram().get(i).getProgram_name());
		}
		
		fc[5] = new DropDownChoice("program_name", program_name, program_list);
		fc[5].setRequired(true);
		form.add(fc[5]);
		
		
		year_level = new Model();
		List year = new ArrayList();
		for(int i = 1; i<=5; i++){
			year.add(Integer.toString(i));
		}
		
		fc[6] = new DropDownChoice("year_level", year_level, year);
		fc[6].setRequired(true);
		form.add(fc[6]); 

		String sy = Integer.toString(lookup.getYear_started()) + "-" + Integer.toString(lookup.getYear_started()+1);
		school_year = new Model(sy);
		schoolyearField = new TextField("school_year", school_year);
		schoolyearField.setEnabled(false);
		form.add(schoolyearField);
		
		section_label = new Label("section_label", "Section");
		fc[7]  = new TextField("section");
		if(!sectVisible){
			fc[7].setVisible(false);
			section_label.setVisible(false);
		}
		form.add(fc[7]);

		semester_label = new Label("semester_label", "Semester");
		semester = new Model(lookup.getSem_started());
		semesterField = new TextField("semester", semester);
		semesterField.setEnabled(false);
		form.add(semesterField);
		if(sectVisible){
			semesterField.setVisible(false);
			semester_label.setVisible(false);
		}

		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("advise", form){
            @Override
            protected void onError(AjaxRequestTarget target, Form form)
            {
                //target.addComponent(feedback);
            	boolean haserror = false;
        		String errormsg = "<ul id='alerterror'>";
				if(fc[0].hasErrorMessage() || fc[1].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the ID Number</li>";
					haserror = true;
				}
				if(fc[2].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the First name</li>";
					haserror = true;
				}
				if(fc[3].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Middle name</li>";
					haserror = true;
				}
				if(fc[4].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Last name</li>";
					haserror = true;
				}
				if(fc[5].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Program</li>";
					haserror = true;
				}
				if(fc[6].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Year Level</li>";
					haserror = true;
				}
				if(fc[7].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Section</li>";
					haserror = true;
				}
				errormsg += "</ul>";
				if(haserror){
					target.addJavascript("jAlert("+errormsg+")");
				}
            }
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form){
				Boolean enrollmentEnabled = lookup.isEnrollmentEnabled();
				if(!Pattern.matches("[0-9]+", idpart1) || !Pattern.matches("[0-9]+", idpart2)){
					target.addJavascript("jAlert('ID number must be numeric')");
					//error("ID number must be numeric");
				}else{
					if(enrollmentEnabled != null && enrollmentEnabled){
						String sem = (String)semester.getObject();
						sem = sectVisible ? sem+"-2nd" : sem;
						
						Student student = new Student();
						student.setStudent_id(Integer.parseInt(idpart1+idpart2));
						student.setProgram_code(getCode());
						student.setYear_level(Integer.parseInt((String)year_level.getObject()));
						//student.setSchool_year((String)school_year.getObject());
						student.setSchool_year(Integer.toString(lookup.getYear_started()));
						student.setSemester(sem);
						student.setSection(section);
						
						StudentQuery query = new StudentQuery();
						
						//PROCESS:
						//(1) proceed if the student is found on the database, error otherwise
						//(2) proceed if the student is still not advised, error otherwise
						//    if lastname changed, update it on the database -- not implemented
						//(3) Alert if the advising is successful, error otherwise
						if(query.getData(student, "getStudentProfile")!=null){
							if(query.getData(student,"getAdvisedData")==null){
								query.insertData(student, "adviseStudent");
								
								if(query.getData(student,"getAdvisedData")!=null){
									((MySession)getSession()).setSuccessOnAdvise(true);
									setResponsePage(sms.advising.Advise.class);
									//target.addJavascript("jAlert('Student has been successfully advised!')");									
								}
								else
									target.addJavascript("jAlert('Failed to advised')");
									//error("Failed To Advised!");
								
							}else
								target.addJavascript("jAlert('Student has already been advised')");
								//error("Student Has Already Been Advised!");
						}else
							target.addJavascript("jAlert('Student not found on the database')");
							//error("Student not found on the database");
						
						
						
					}else
						target.addJavascript("jAlert('Enrollment has been disabled')");
						//error("Enrollment has been disabled!");//check if enrollment status is on
				}}});
        
        AbstractBehavior success =  new AbstractBehavior() {
        	public void renderHead(IHeaderResponse response) {
            	super.renderHead(response);
            	response.renderOnLoadJavascript("jAlert('Student has been successfully advised!')");
        	}
        }; 
        
        if(((MySession)getSession()).isSuccessOnAdvise()){
        	add(success);
        	((MySession) getSession()).setSuccessOnAdvise(false);
        }
        
        add(form);
		form.add(feedback);
		form.add(semester_label);
		form.add(section_label);
		
	}
	/*
	public Form getForm(){
		return form;
	} */
	private void checkSectionSemVisibility(){
		DepartmentQuery query = new DepartmentQuery();
		Department dept = query.getData(logged, "getBelongedDept");
		String str = "IDS";
		sectVisible = dept.getDept_name().contains(str) ? true: false;
	}
	
	private void queryPrograms(){
		EmployeeQuery query1 = new EmployeeQuery();
		ProgramQuery query2 = new ProgramQuery();
		
		logged = query1.fetchData(logged, "getCostCenter"); 
		((MySession)getSession()).setProgram(query2.getData(logged, "getPrograms"));
	}
	
	private void setLoggedEmployee(){
		logged = ((MySession)getSession()).getLoggedEmployee();
	}
	
	private int getCode(){
		for(int i = 0; i < ((MySession)getSession()).getProgram().size(); i++){
			String name = ((MySession)getSession()).getProgram().get(i).getProgram_name();
			int code = ((MySession)getSession()).getProgram().get(i).getProgram_code(); 
			
			if(name.equals((String)program_name.getObject()))
				return code;
		}
		return -1;
	}
}
