package sms.studscholarship;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
//import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
//import org.apache.wicket.model.Model;

import sms.index.Home;
import sms.scholarship.Scholarship;


public class Profile extends Home {
	//private Model statusModel;
	//private DropDownChoice status;
	@SuppressWarnings("serial")
	public Profile(final Scholarship studscholar){
		
		super();
		add(new Label("student_id", Scholarship.displayAccountId(studscholar.getStudent_id())));
		add(new Label("fname", studscholar.getFname()));
		add(new Label("mname", studscholar.getMname()));
		add(new Label("lname", studscholar.getLname()));
		add(new Label("scholarship", studscholar.getScholarship_name()));
		
		/*statusModel = new Model();
		List statusChoices = new ArrayList();
		statusChoices.add("scholar");
		statusChoices.add("not");
		
		status = new DropDownChoice("status", statusModel, statusChoices);
		status.setRequired(true);*/
		add(new Label("status", studscholar.getStatus()));
		add(new Link("editStudentScholarship"){

			@Override
			public void onClick() {
				setResponsePage(new EditStudentScholarship(studscholar.getStudent_id()));
				
			}
			
		});
		//add(noentry);
		
	}

}
