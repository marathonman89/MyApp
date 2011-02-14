package sms.hr;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import sms.admin.EditUsers;
import sms.user.Employee;

@AuthorizeInstantiation("HR")
public class ProfileActionPanel extends Panel{
	
	public ProfileActionPanel(String id, final IModel model){
		super(id, model);
		add(new Link("select"){
            @Override
            public void onClick(){
            	Employee emp = (Employee)model.getObject();
            	System.out.println(emp.getEmployee_id());
            	//System.out.println(emp.getFname());
            	//System.out.println(emp.getLname());
            	//System.out.println(emp.getMname());
            	setResponsePage(new Profile(emp));
            }
        });
	}

}
