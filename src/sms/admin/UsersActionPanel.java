package sms.admin;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import sms.faculty.ClassListAction;
import sms.user.Employee;

@AuthorizeInstantiation("Administration")
public class UsersActionPanel extends Panel {
	public UsersActionPanel(String id, final IModel model){
		super(id, model);
        add(new Link("select")
        {
            @Override
            public void onClick()
            {
            	Employee emp = (Employee) model.getObject();
            	System.out.println("Employee ID: " + emp.getEmployee_id());
            	setResponsePage(new EditUsers(emp));
            }
        });
	}
}
