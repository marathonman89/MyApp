package sms.studscholarship;

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

import sms.index.Home;
import sms.scholarship.Scholarship;
import sms.scholarship.ScholarshipQuery;
import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;


public class AddStudentScholarship extends Home {
	private String part1;
	private String part2;
	private String scholarship;
	private String status;
	
	private Form form;
	private Model statusmodel;
	private Model scholarshipmodel;
	private FeedbackPanel feedback;
	private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	
	public AddStudentScholarship() {
		feedback = new FeedbackPanel("error");
		form = new Form("scholarForm", new CompoundPropertyModel(this));
		
		feedback.setOutputMarkupId(true);
		final FormComponent[] fc = new FormComponent[4];
		
		fc[0] = new RequiredTextField("part1");
		fc[1] = new RequiredTextField("part2");
		
		scholarshipmodel = new Model();
		final List<Scholarship> scholarshipChoices = getScholarship();
		List scholarshipnames = new ArrayList();
		for(int i=0; i < scholarshipChoices.size(); i++) scholarshipnames.add(scholarshipChoices.get(i).getScholarship_name());
		fc[2] = new DropDownChoice("scholarship", scholarshipmodel, scholarshipnames);
		fc[2].setRequired(true);
		
		statusmodel = new Model();
		List statusChoices = new ArrayList();
		statusChoices.add("scholar");
		statusChoices.add("not");
		
		fc[3] = new DropDownChoice("status", statusmodel, statusChoices);
		fc[3].setRequired(true);
		
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
					errormsg += "<li>Provide a valid input for Scholarship Name</li>";
					haserror = true;
				}
				if(fc[3].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for Status name</li>";
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
        			if(entryExist(tmp)){
        				int scholarshipCode = -1;
        				
        				for(int i=0; i < scholarshipChoices.size(); i++)
        					if(scholarshipmodel.getObject().equals(scholarshipChoices.get(i).getScholarship_name()))
        						scholarshipCode = scholarshipChoices.get(i).getScholarship_code();
        				
        				ScholarshipQuery insertQuery = new ScholarshipQuery();
	        			int numberOfScholars = insertQuery.getIntegerData("countScholars");
	        			
	        			Scholarship addScholar = new Scholarship(numberOfScholars+1, studId,scholarshipCode,
	        					                       statusmodel.getObject().toString());
	        			insertQuery.insert(addScholar, "insertScholarship");
	        			((MySession) getSession()).setSuccessOnAddScholar(true);
	        			setResponsePage(sms.studscholarship.AddStudentScholarship.class);
	        			/*  1. faculty must be added in the system users
	        			 *  2. faculty must be given a default user role which is the FACULTY ROLE
	        			 */
        			}else{
        				target.addJavascript("jAlert('Process Failed. Student with ID number: "+part1+"-"+part2+" does not exist')");
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
        
        if(((MySession)getSession()).isSuccessOnAddScholar()){
        	add(success);
        	((MySession) getSession()).setSuccessOnAddScholar(false);
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
		if(query.getIntegerDatum(stud, "numberOfStudentEntries").intValue() > 0)
			return true;
		return false;
	}
	private List<Scholarship> getScholarship() {
		ScholarshipQuery query = new ScholarshipQuery();
		return query.getDataWithNoParam("getScholarship");
	}
	
	
}
