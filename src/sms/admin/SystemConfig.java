package sms.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import sms.lookup.Lookup;
import sms.lookup.LookupQuery;
import sms.session.MySession;
import sms.user.Employee;

@AuthorizeInstantiation("Administration")
public class SystemConfig extends Layout{
	
	private String result;
	private Model log_enabled_Model;
	private Model enr_enabled_Model;
	private Model year_started_Model;
	private Model sem_started_Model;
	
	public SystemConfig(){
		Form form = new Form("configForm");
		final ModalWindow modal;
		form.add(modal = new ModalWindow("modal"));
		modal.setPageMapName("modal-1");
        modal.setCookieName("modal-1");
        modal.setMinimalHeight(180);
        modal.setMinimalWidth(280);
        modal.setInitialWidth(280);
        modal.setInitialHeight(180);
        modal.setTitle("Create Log File");
        modal.setResizable(false);
        modal.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
        modal.setPageCreator(new ModalWindow.PageCreator()
        {
            public Page createPage()
            {
                return new ModalLogCreation(SystemConfig.this, modal);
            }
        });
        /** modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
        {
            public void onClose(AjaxRequestTarget target)
            {
                //target.addComponent(result);
            }
        });
       
        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
        {
            public boolean onCloseButtonClicked(AjaxRequestTarget target)
            {
                setResult("Modal window 1 - close button");
                return true;
            }
        }); **/

        form.add(new AjaxLink("modalCreate")
        {
            @Override
            public void onClick(AjaxRequestTarget target)
            {
                modal.show(target);
            }
        });
        
        final ModalWindow modal1;
        form.add(modal1 = new ModalWindow("modal1"));
        modal1.setPageMapName("modal1-1");
        modal1.setCookieName("modal1-1");
        modal1.setMinimalHeight(180);
        modal1.setMinimalWidth(280);
        modal1.setTitle("Select Log File");
        modal1.setResizable(false);
        modal1.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
        modal1.setPageCreator(new ModalWindow.PageCreator()
        {
            public Page createPage()
            {
                return new SelectLog(SystemConfig.this, modal1);
            }
        });
        /*
        modal1.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
        {
            public void onClose(AjaxRequestTarget target)
            {
                //target.addComponent(result);
            }
        }); */

        form.add(new AjaxLink("selectLogFile"){
            @Override
            public void onClick(AjaxRequestTarget target){
            	modal1.show(target);
            }
        });
        
        Lookup lookup = Lookup.AllLookupStatus();
        
        List choices = Arrays.asList(new String[] { "Yes", "No" });
        List semester = Arrays.asList(new String[] { "1st", "2nd", "summer" });
        
        List year = new ArrayList();
        Calendar cal = Calendar.getInstance();
        for(int i = 1990; i <= cal.get(Calendar.YEAR); i++) year.add(i);
        

        year_started_Model = lookup.getYear_started() != null ? new Model(lookup.getYear_started()) : new Model();
        DropDownChoice year_started = new DropDownChoice("year_started", year_started_Model, year);
        
        sem_started_Model = lookup.getSem_started() != null ? new Model(lookup.getSem_started()) : new Model();
        DropDownChoice sem_started = new DropDownChoice("sem_started", sem_started_Model, semester);
        
        //System.out.println("Enrollmet enabled: " + lookup.isEnr_on());
        enr_enabled_Model = lookup.isEnr_on() != null ? (lookup.isEnr_on() ? new Model("Yes") : new Model("No")): new Model();
        DropDownChoice enr_enabled = new DropDownChoice("enr_enabled", enr_enabled_Model, choices);
        
        //System.out.println("Log enabled: " + lookup.isLog_enabled());
        log_enabled_Model = lookup.isLog_enabled() != null ? (lookup.isLog_enabled() ? new Model("Yes") : new Model("No")): new Model();
        //log_enabled_Model = new Model();
        final DropDownChoice log_enabled = new DropDownChoice("log_enabled", log_enabled_Model, choices);
        
        form.add(year_started);
        form.add(sem_started);
        form.add(enr_enabled);
        form.add(log_enabled);
        
        AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("saveconfig", form){
        	protected void onError(AjaxRequestTarget arg0, Form form){
            }
			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
				String enr = (String)enr_enabled_Model.getObject();
				String log =  (String) log_enabled_Model.getObject();
				Integer year = (Integer) year_started_Model.getObject();
				String sem = (String) sem_started_Model.getObject();
				
				Boolean enr_on = enr != null ? (enr.equals("Yes") ? true : false) : null;
				Boolean log_on = log != null ? (log.equals("Yes") ? true : false) : null;
				
				System.out.println("enrollment enabled on save: " + enr_on);
				System.out.println("log enabled on save: " + log_on);
				System.out.println("year started: " + year);
				System.out.println("sem started: " + sem);

				Lookup lookup1 = new Lookup(enr_on, log_on, year, sem);
				LookupQuery query = new LookupQuery();
				query.updateData(lookup1, "updateAllLookupStatus");
				
				Lookup lookup2 = Lookup.AllLookupStatus();
    			Employee emp = ((MySession)getSession()).getLoggedEmployee();
    			String file = lookup2.getLog_file_used();
    			String logMessage = "SUCCESS! User with Account: " + Employee.displayAccountId(emp.getEmployee_id()) + " saved the configuration on System Config";
    			Log.loggedAction(lookup2.isLog_enabled(), file, logMessage);
    			
				arg0.addJavascript("jAlert('Configuration has been saved!')");
			}

        });
        add(form);
	}
}
