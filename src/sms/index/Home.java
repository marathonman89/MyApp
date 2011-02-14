package sms.index;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import sms.user.Employee;
import sms.user.EmployeeQuery;
import sms.Role.Role;
import sms.session.*;

public class Home extends Index{
	
	private Form form;
	private Model roleModel;
	private Employee emp;
	private Link home;
	private Link account;
	private Link logout;
	
	public Home(){
		emp = ((MySession)getSession()).getLoggedEmployee();
		
		List list = getRoles();
		String arrayOfRoles[] = new String[list.size()]; 
		for(int i = 0; i < list.size(); i++) arrayOfRoles[i] = (String)list.get(i);
		((MySession)getSession()).setUserLoggedRoles(new Employee(emp.getEmployee_id(), arrayOfRoles));
		//((MySession)getSession()).setUserLoggedRoles(new Employee(emp.getEmployee_id(), "Admission"));
		
		form = new Form("jumpTo");

		roleModel = new Model();
		final DropDownChoice choices = new DropDownChoice("choices", roleModel, getRoles());
		form.add(choices);
		add(form);
		choices.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
            	String str = (String) roleModel.getObject();
                 if(str.equals(Role.administration)){
                	 System.out.println("admin test");
                	 setResponsePage(sms.admin.Help.class);
                 }
                 if(str.equals(Role.department)){
                	 System.out.println("adviser test");
                	 setResponsePage(sms.advising.Help.class);
                 }
                 if(str.equals(Role.college)){
                	 System.out.println("college test");
                	 setResponsePage(sms.college.Help.class);
                 }
                 if(str.equals(Role.faculty)){
                	 System.out.println("faculty test");
                	 setResponsePage(sms.faculty.Help.class);
                 }
                 if(str.equals(Role.hr)){
                	 System.out.println("hr test");
                	 setResponsePage(sms.hr.Help.class);
                 }
                 if(str.equals(Role.registration)){
                	 System.out.println("registration test");
                	 setResponsePage(sms.registration.Help.class);
                 }
                 if(str.equals(Role.admission)){
                	 System.out.println("admission test");
                	 setResponsePage(sms.admission.Help.class);
                 }
                 if(str.equals(Role.scholarship)){
                	 System.out.println("scholarship test");
                	 setResponsePage(sms.studscholarship.Help.class);
                 }
            }
        });
		add(new Link("home"){
			public void onClick(){
				setResponsePage(sms.index.Home.class);
			}
		});
		add(new Link("account"){
			public void onClick(){
				this.setAutoEnable(true);
			}
		});
		add(new Link("logout") {
			public void onClick() {
			getSession().invalidate();
			setResponsePage(sms.index.Login.class);
			}});
	}
	
	public List getRoles(){
		EmployeeQuery query = new EmployeeQuery();
		List<Employee> tmp = query.fetchListOfData(emp, "getUserRoles");
		List roles = new ArrayList();
		
		for(int i = 0; i < tmp.size(); i++){
			roles.add(((Employee)tmp.get(i)).getRole_name());
		}
		return roles;
	}
	
	public void downloadFile(String URL, String file) throws MalformedURLException, IOException{
		java.io.BufferedInputStream in = new java.io.BufferedInputStream(new
		java.net.URL(URL).openStream());
		java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
		java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
		byte[] data = new byte[1024];
		int x=0;
		while((x=in.read(data,0,1024))>=0)
		{
		bout.write(data,0,x);
		}
		bout.close();
		in.close();
	}
	
}