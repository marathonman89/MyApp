package sms.advising;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.NumberValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sms.department.Department;
import sms.department.DepartmentQuery;
import sms.lookup.Lookup;
import sms.lookup.LookupQuery;
import sms.program.Program;
import sms.program.ProgramQuery;
import sms.session.MySession;
import sms.student.Student;
import sms.user.Employee;
import sms.user.EmployeeQuery;

public class Advise extends Layout{

	public Advise(){
		
		add(new AjaxLazyLoadPanel("lazy"){
            public Component getLazyLoadComponent(String id){
                try{
                    Thread.sleep(1000);
                }
                catch (InterruptedException e){
                    throw new RuntimeException(e);
                }
                return new AdvisePanel(id);
            }
        });
	}
}