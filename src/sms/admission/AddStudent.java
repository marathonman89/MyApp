package sms.admission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;

import sms.index.Home;

public class AddStudent extends Home{
	private String part1;
	private String part2;
	private String fname;
	private String lname;
	private String mname;
	private String bdate;
	private String barangay;
	private String municipality;
	private String province;
	private String gender;
	private String educ_level;
	
	private Form form;
	private Model genderModel;
	private Model educlevelModel;
	//private Model provinceModel;
	private FeedbackPanel feedback;
	private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	
	public AddStudent(){
		feedback = new FeedbackPanel("error");
		form = new Form("studentForm", new CompoundPropertyModel(this));
		
		feedback.setOutputMarkupId(true);
		final FormComponent[] fc = new FormComponent[11];
		
		fc[0] = new RequiredTextField("part1");
		fc[1] = new RequiredTextField("part2");
		fc[2] = new RequiredTextField("fname");
		fc[3] = new RequiredTextField("lname");
		fc[4] = new RequiredTextField("mname");
		fc[5] = new RequiredTextField("bdate");
		fc[6] = new RequiredTextField("barangay");
		fc[7] = new RequiredTextField("municipality");
		fc[8] = new RequiredTextField("province");
		
		genderModel = new Model();
		List genderChoices = new ArrayList();
		genderChoices.add("Male");
		genderChoices.add("Female");
		
		fc[9] = new DropDownChoice("gender", genderModel, genderChoices);
		fc[9].setRequired(true);
		
		educlevelModel = new Model();
		List educlevelChoices = new ArrayList();
		educlevelChoices.add("High School");
		educlevelChoices.add("College");
		
		fc[10] = new DropDownChoice("educ_level", educlevelModel, educlevelChoices);
		fc[10].setRequired(true);
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("add", form){
        	protected void onError(AjaxRequestTarget target, Form form){
                //target.addComponent(feedback);
                //System.out.println(feedback.toString());
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
					errormsg += "<li>Provide a valid input for the Last name</li>";
					haserror = true;
				}
				if(fc[4].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Middle name</li>";
					haserror = true;
				}
				if(fc[5].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Birthdate</li>";
					haserror = true;
				}
				if(fc[6].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Barangay</li>";
					haserror = true;
				}
				if(fc[7].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Municipality</li>";
					haserror = true;
				}
				if(fc[8].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Province</li>";
					haserror = true;
				}
				if(fc[9].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Gender</li>";
					haserror = true;
				}
				if(fc[10].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Education Level</li>";
					haserror = true;
				}
				errormsg += "</ul>";
				if(haserror){
					target.addJavascript("jAlert("+errormsg+")");
				}
            }
        	protected void onSubmit(AjaxRequestTarget target, Form form){
        		if(!Pattern.matches("[0-9]+", part1) || !Pattern.matches("[0-9]+", part2)){
					//error("ID number must be numeric");
        			target.addJavascript("jAlert('ID number must be numeric')");
				}
        		else{
        			     			
        			int studId =  Integer.parseInt(part1+part2);
        			Student tmp = new Student(studId);
        			if(!entryExist(tmp)){
	        			        			
	        			StudentQuery insertQuery = new StudentQuery();
	        			Student addStud = new Student(studId,fname,lname,mname,bdate, 
	        					                       barangay,municipality,province,
	        					                       genderModel.getObject().toString().substring(0, 1),
	        					                       (educlevelModel.getObject().toString().equals("College")) ? "tertiary" : "secondary");
	        			insertQuery.insertData(addStud, "insertStudent");
	        			((MySession) getSession()).setSuccessOnAddStudent(true);
	        			setResponsePage(sms.admission.AddStudent.class);
	        			/*  1. faculty must be added in the system users
	        			 *  2. faculty must be given a default user role which is the FACULTY ROLE
	        			 */
        			}else{
        				target.addJavascript("jAlert('Process Failed. Student with ID number: "+part1+"-"+part2+" already exist')");
        			}
        		}
			}
        });
		
        AbstractBehavior success =  new AbstractBehavior() {
        	public void renderHead(IHeaderResponse response) {
            	super.renderHead(response);
            	response.renderOnLoadJavascript("jAlert('Student successfully added')");
        	}
        }; 
        
        if(((MySession)getSession()).isSuccessOnAddStudent()){
        	add(success);
        	((MySession) getSession()).setSuccessOnAddStudent(false);
        }
		for(int i = 0; i < fc.length; i++) form.add(fc[i]);
		//form.add(fc[10]);
        //form.add(fc[11]);
		//add(back);
		add(form);
		add(feedback);
	}
	
	private boolean entryExist(Student stud){
		StudentQuery query = new StudentQuery();
		if(query.getIntegerDatum(stud, "numberOfEntry").intValue() > 0)
			return true;
		return false;
	}
}
