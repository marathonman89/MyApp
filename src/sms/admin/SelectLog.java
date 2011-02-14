package sms.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import sms.lookup.Lookup;
import sms.lookup.LookupQuery;
import sms.session.MySession;
import sms.user.Employee;

@AuthorizeInstantiation("Administration")
public class SelectLog extends WebPage{
	private Form form;
	
	public SelectLog(final SystemConfig modalWindowPage, final ModalWindow window){
		form = new Form("form");
		final RadioGroup group = new RadioGroup("group", new Model());
		
		List files = getFiles();
		ListView eachFile = new ListView("eachFile", files){
			protected void populateItem(ListItem item) {
				String fileName = (String) item.getModelObject();
				item.add(new Label("fileName", fileName));
				item.add(new Radio("selected", item.getModel()));
			}
		};
		
		AjaxButton select = new AjaxButton("select", form){
        	protected void onError(AjaxRequestTarget target, Form form){
        	
        	}
        	protected void onSubmit(AjaxRequestTarget target, Form form){
        		if (modalWindowPage != null ){
        			String fileSelected = (String)group.getModelObject();
        			System.out.println(fileSelected);	
        			if(fileSelected != null){
	        			updateLogFileToBeUsed(fileSelected);
	        			
	        			Lookup lookup = Lookup.AllLookupStatus();
	        			Employee emp = ((MySession)getSession()).getLoggedEmployee();
	        			String file = lookup.getLog_file_used();
	        			String logMessage = "SUCCESS! User with Account: " + Employee.displayAccountId(emp.getEmployee_id()) + " selected " + file + " to be used for the Log";
	        			Log.loggedAction(lookup.isLog_enabled(), file, logMessage);
	        			window.close(target);
        			}
        		}
        	}
		};
		
		String usedFile = Lookup.LogFile();
		Label noentry = new Label("noentry","No Log File Available!");
		Label usedLogFileLabel = new Label("usedLogFileLabel", "Currently used log file: " + (usedFile != null ? usedFile : "none"));
		
		if(files.size() > 0){
			noentry.setVisible(false);
			usedLogFileLabel.setVisible(true);
			select.setVisible(true);
		}else{
			noentry.setVisible(true);
			usedLogFileLabel.setVisible(false);
			select.setVisible(false);
		}
		
		
		group.add(eachFile);
		form.add(select);
		form.add(noentry);
		form.add(group);
		add(form);
		add(usedLogFileLabel);
	}
	
	private List getFiles(){
		 File ArrayOfFiles[] = new File("E:\\SmsSystem\\SMS\\logs").listFiles();
		 List fileList = new ArrayList();
		 for(int i = 0; i < ArrayOfFiles.length; i++){
			 fileList.add(ArrayOfFiles[i].getName());
		 }
		 return fileList;
	 }
	
	 private void updateLogFileToBeUsed(String str){
		 LookupQuery query = new LookupQuery();
		 Lookup l = new Lookup();
		 l.setLog_file_used(str);
		 
		 query.updateData(l, "logFileToBeUsed");
	 }
}
