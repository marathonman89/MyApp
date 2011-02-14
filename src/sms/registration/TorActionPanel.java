package sms.registration;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import sms.student.Student;

public class TorActionPanel extends Panel{

	public TorActionPanel(String id, final IModel model){
		super(id, model);
		add(new Link("select"){
            @Override
            public void onClick(){
            	Student stud = (Student)model.getObject();
            	setResponsePage(new TorContent(stud));
            }
		});
	}
}
