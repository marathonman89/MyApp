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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import sms.index.Home;
import sms.scholarship.Scholarship;
import sms.scholarship.ScholarshipQuery;
import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;

public class EditStudentScholarship extends Home{
	private String part1;
	private String part2;
	private String scholarship;
	private String status;
	
	private Form form;
	private Model statusmodel;
	private Model scholarshipmodel;
	private Model part1model;
	private Model part2model;
	private FeedbackPanel feedback;
	//private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	
	public EditStudentScholarship() {}
	public EditStudentScholarship(int scholarId) {
		feedback = new FeedbackPanel("error");
		//super();
		form = new Form("editStudentScholarshipForm", new CompoundPropertyModel(this));
		
		feedback.setOutputMarkupId(true);
		
		final FormComponent[] fc = new FormComponent[4];
		
		//List<Scholarship> studScholar = getScholarData(scholar);
		//for(int i = 0; i < studScholar.size(); i++){*/
			Scholarship scholar = new Scholarship();
			String studId = Integer.toString(scholarId);
			part1model = new Model(studId.substring(0,4));
			part2model = new Model(studId.substring(4,studId.length()));
			fc[0] = new RequiredTextField("part1", part1model);
			fc[1] = new RequiredTextField("part2", part2model);
			
			scholar.setStudent_id(scholarId);
			List<Scholarship> studentScholarData = getScholarData(scholar);
			
			scholarshipmodel = new Model();
			final List<Scholarship> scholarshipChoices = getScholarship();
			List scholarshipnames = new ArrayList();
			for(int n=0; n < scholarshipChoices.size(); n++) scholarshipnames.add(scholarshipChoices.get(n).getScholarship_name());
			fc[2] = new DropDownChoice("scholarships", scholarshipmodel, scholarshipnames);
			fc[2].setRequired(true);
			fc[2].setModelObject(studentScholarData.get(0).getScholarship_name());
			
			statusmodel = new Model();
			List statusChoices = new ArrayList();
			statusChoices.add("scholar");
			statusChoices.add("not");
			
			fc[3] = new DropDownChoice("scholarshipStatus", statusmodel, statusChoices);
			fc[3].setRequired(true);
			fc[3].setModelObject(studentScholarData.get(0).getStatus());
		//}
			
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("edit", form){
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
        		System.out.println(part1model.getObject().toString()+part2model.getObject().toString()+"hey");
        		if(!Pattern.matches("[0-9]+", part1model.getObject().toString()) || !Pattern.matches("[0-9]+", part2model.getObject().toString())){
					//error("ID number must be numeric");
        			target.addJavascript("jAlert('ID number must be numeric')");
				}
        		else{
        			System.out.println(part1model.getObject().toString()+part2model.getObject().toString()+"hoho");      			
        			int studId =  Integer.parseInt(part1model.getObject().toString()+part2model.getObject().toString());
        			Student tmp = new Student(studId);
        			if(entryExist(tmp)){
        				int scholarshipCode = -1;
        				
        				for(int i=0; i < scholarshipChoices.size(); i++)
        					if(scholarshipmodel.getObject().equals(scholarshipChoices.get(i).getScholarship_name()))
        						scholarshipCode = scholarshipChoices.get(i).getScholarship_code();
        				
        				ScholarshipQuery insertQuery = new ScholarshipQuery();
	        			//List numberOfScholar = insertQuery.getDataWithNoParam("getStudentScholar");
	        			System.out.println(Integer.toString(scholarshipCode));
	        			System.out.println(statusmodel.getObject().toString());
	        			Scholarship editScholar = new Scholarship(studId,scholarshipCode,
	        					                       statusmodel.getObject().toString());
	        			insertQuery.updateData(editScholar, "updateScholarship");
	        			((MySession) getSession()).setSuccessOnEditScholar(true);
	        			setResponsePage(sms.studscholarship.StudentScholarshipProfile.class);
	        			/*  1. faculty must be added in the system users
	        			 *  2. faculty must be given a default user role which is the FACULTY ROLE
	        			 */
        			}else{
        				target.addJavascript("jAlert('Process Failed. Student with ID number: "+part1+"-"+part2+" does not exist')");
        			}
        		}
			}
        });
        
        /*AbstractBehavior success =  new AbstractBehavior() {
        	public void renderHead(IHeaderResponse response) {
            	super.renderHead(response);
            	response.renderOnLoadJavascript("jAlert('Student successfully added')");
        	}
        }; 
        
        if(((MySession)getSession()).isSuccessOnEditScholar()){
        	add(success);
        	((MySession) getSession()).setSuccessOnEditScholar(false);
        }*/
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
	private List<Scholarship> getScholarData(Scholarship scholar) {
		ScholarshipQuery query = new ScholarshipQuery();
		return query.fetchListOfData(scholar, "getStudentScholarshipWithParam");
	}

}
