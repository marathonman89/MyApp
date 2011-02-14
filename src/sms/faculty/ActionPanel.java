package sms.faculty;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import sms.subject.Subject;

public class ActionPanel extends Panel {
	
	public ActionPanel(String id, final IModel model)
    {
        super(id, model);
        add(new Link("select")
        {
            @Override
            public void onClick()
            {
            	Subject subj = (Subject) model.getObject();
            	System.out.println("Subject code: " + subj.getSubject_code());
            	System.out.println("Subject Name: " + subj.getSubject_name());
            	System.out.println("Subject section:" + subj.getSubject_section());
            	System.out.println("Offered ID: " + subj.getOffered_id());
                setResponsePage(new ClassListAction(subj));
            }
        });
    }
}
