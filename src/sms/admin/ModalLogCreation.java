package sms.admin;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import sms.lookup.Lookup;
import sms.session.MySession;
import sms.user.Employee;

@AuthorizeInstantiation("Administration")
public class ModalLogCreation extends WebPage{
	private Form form;
	private String fileName;
	
	public ModalLogCreation(final SystemConfig modalWindowPage, final ModalWindow window){
		form = new Form("form", new CompoundPropertyModel(this));

		form.add(new RequiredTextField("fileName"));
        form.add(new AjaxButton("createButton", form){
        	protected void onError(AjaxRequestTarget target, Form form){
        	
        	}
        	protected void onSubmit(AjaxRequestTarget target, Form form){
        		Lookup lookup = Lookup.AllLookupStatus();
    			Employee emp = ((MySession)getSession()).getLoggedEmployee();
    			String file = lookup.getLog_file_used();
    			
        		if (modalWindowPage != null ){
                	System.out.println(fileName);
                	
                	File f = new File("E:\\SmsSystem\\SMS\\logs\\" + fileName +".txt");
                	if(Pattern.matches("[a-zA-Z_0-9]+", fileName)){
	                    if(!f.exists()){
							try {
								f.createNewFile();
								
				    			String logMessage = "SUCCESS! User with Account: " + Employee.displayAccountId(emp.getEmployee_id()) + " created " + fileName + ".txt for the Log";
				    			Log.loggedAction(lookup.isLog_enabled(), file, logMessage);
				    			
								window.close(target);
							} catch (IOException e) {
								e.printStackTrace();
							}
	                    }else{
	                    	String logMessage = "ERRROR! User with Account: " + Employee.displayAccountId(emp.getEmployee_id()) + " attempted to create a file that already exists!";
			    			Log.loggedAction(lookup.isLog_enabled(), file, logMessage);
	                    	target.addJavascript("alert('The file name already exists!')");
	                    }
                	}else{
                		String logMessage = "ERRROR! User with Account: " + Employee.displayAccountId(emp.getEmployee_id()) + " attempted to create a file for the Log with an invalid filename!";
		    			Log.loggedAction(lookup.isLog_enabled(), file, logMessage);
                		target.addJavascript("alert('Please provide a valid name for the file')");
                	}
                }
        	}
        });
        add(form);
	}
}
