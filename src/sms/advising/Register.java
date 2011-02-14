package sms.advising;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;

import sms.session.MySession;
import sms.student.Student;

public class Register extends Layout{

	public Register(){
		if(((MySession)getSession()).getStudentToBeRegistered()==null){
			add(new AjaxLazyLoadPanel("lazy"){
	            public Component getLazyLoadComponent(String id){
	                try{
	                    Thread.sleep(1000);
	                }
	                catch (InterruptedException e){
	                    throw new RuntimeException(e);
	                }
	                return new RegisterPanel(id);
	            }
	        });
		}else{
			Student student = ((MySession) getSession()).getStudentToBeRegistered();
			if(student.getEduc_level().equals(Student.tertiary)){
				if(((MySession)getSession()).getSearchedSubject()==null)
					setResponsePage(SearchForSubject.class);
				else
					setResponsePage(ControlSubject.class);
			}
		}
	}
}
