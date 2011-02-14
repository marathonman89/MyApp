package sms.admission;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import sms.student.Student;

public class ProfileActionPanel extends Panel{

	public ProfileActionPanel(String id, final IModel model) {
		super(id, model);
		add(new Link("select") {
			@Override
			public void onClick() {
				Student stud = (Student)model.getObject();
				System.out.println(stud.getStudent_id());
				setResponsePage(new Profile(stud));
				
			}
			
		});
	}
	
	
}
