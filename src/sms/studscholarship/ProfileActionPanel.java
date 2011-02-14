package sms.studscholarship;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

//import sms.admission.Profile;
import sms.scholarship.Scholarship;

public class ProfileActionPanel extends Panel{
	
	public ProfileActionPanel(String id, final IModel model) {
		super(id, model);
		add(new Link("select") {
			@Override
			public void onClick() {
				Scholarship studscholar = (Scholarship)model.getObject();
				System.out.println(studscholar.getStudent_id());
				setResponsePage(new Profile(studscholar));
				
			}
			
		});
	}
}
