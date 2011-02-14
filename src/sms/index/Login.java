package sms.index;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.regex.Pattern;

import sms.session.*;
import sms.user.Employee;
import sms.user.EmployeeQuery;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;

public class Login extends Index{
	private Form form;
	private String idpart1;
	private String idpart2;
	private String password;
	private FeedbackPanel feedback;
	
	public Login(){
		if(((MySession)getSession()).isSessionSet()){
			setResponsePage(Home.class);
		}else{
			form = new Form("form", new CompoundPropertyModel(this));
			form.setOutputMarkupId(true);
			
			feedback = new FeedbackPanel("error");
			feedback.setOutputMarkupId(true);
			
			final FormComponent[] fc = new FormComponent[3]; 
			fc[0] = new RequiredTextField("idpart1"); 
			fc[0].add(StringValidator.minimumLength(4));
			form.add(fc[0]);
			
			fc[1] = new RequiredTextField("idpart2");
			fc[1].add(StringValidator.minimumLength(3));
			form.add(fc[1]);
			
			fc[2] = new PasswordTextField("password"); 
			form.add(fc[2]);
			
			add(feedback);
			add(form);
			
			AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
	
	        form.add(new AjaxButton("login", form)
	        {
	            @Override
	            protected void onError(AjaxRequestTarget target, Form form)
	            {
	            	boolean haserror = false;
	            	
	                if(fc[0].hasErrorMessage() || fc[1].hasErrorMessage()){
	                	target.addJavascript("$('#account').css('color','red')");
	                	haserror = true;
	                }else
	                	target.addJavascript("$('#account').css('color','#266493')");
	                
	                
	                if(fc[2].hasErrorMessage()){
	                	target.addJavascript("$('#password').css('color','red')");
	                	haserror = true;
	                }
	                else
	                	target.addJavascript("$('#password').css('color','#266493')");
	                
	                if(haserror)
	                	target.addJavascript("jAlert('Please Enter a valid detail!')");
	                
	            }

				@Override
				protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
					if(!Pattern.matches("[0-9]+", idpart1) || !Pattern.matches("[0-9]+", idpart2)){
						//error("Account must be numeric");
						arg0.addJavascript("$('#account').css('color','red')");
						arg0.addJavascript("jAlert('Account must be numeric')");
					}else{
						arg0.addJavascript("$('#account').css('color','#266493')");
						
						Employee emp = new Employee();
						emp.setEmployee_id(Integer.parseInt(idpart1+idpart2));
						emp.setPassword(encPassword(password));
						
						EmployeeQuery query = new EmployeeQuery();
						//Employee fetched =	query.fetchData(emp, "getUserInfo"); 
						//if(fetched != null){
						if((emp = query.fetchData(emp, "getUserInfo")) != null){
							if(emp.getAcct_status().equals(Employee.ActiveAcct)){
								((MySession)getSession()).setLoggedEmployee(emp);
								setResponsePage(Home.class);
							}
							if(emp.getAcct_status().equals(Employee.BlockAcct))
								arg0.addJavascript("jAlert('ACCOUNT BLOCKED! Contact the System Administrator')");
							if(emp.getAcct_status().equals(Employee.PendingAcct))
								arg0.addJavascript("jAlert('PENDING ACCOUNT! Contact the System Administrator')");
						}else{
							///error("Login failed. Try again!");
							arg0.addJavascript("$('#account').css('color','red')");
							arg0.addJavascript("$('#password').css('color','red')");
							arg0.addJavascript("jAlert('Account and Password did not matched!')");
							
						}
					}
				}
	        });
		}
	}
	
}
