package sms.admin;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sms.Role.Role;
import sms.Role.RoleQuery;
import sms.index.Home;
import sms.user.Employee;
import sms.user.EmployeeQuery;

@AuthorizeInstantiation("Administration")
public class EditUsers extends Home{
	private Form done;
	private Form form;
	private boolean department;
	private boolean administration;
	private boolean scholarship;
	private Model statusModel;
	
	public EditUsers(final Employee emp){
		form = new Form("privilegesform", new CompoundPropertyModel(this));
		done = new Form("doneform");
		done.add(new Button("done"){
			public void onSubmit(){
				setResponsePage(sms.admin.Users.class);
			}
		});
		
		System.out.println(emp.getEmployee_id());
		final FormComponent[] fc = new FormComponent[9];
		//Model allow = new Model(false);
		fc[0] = new CheckBox("department", new Model(false));
		fc[1] = new CheckBox("administration", new Model(false));
		fc[2] = new CheckBox("scholarship", new Model(false));
		fc[3] = new CheckBox("admission", new Model(false));
		fc[4] = new CheckBox("curriculum", new Model(false));
		fc[5] = new CheckBox("college", new Model(false));
		fc[6] = new CheckBox("faculty", new Model(false));
		fc[7] = new CheckBox("registrar", new Model(false));
		fc[8] = new CheckBox("hr", new Model(false));
		
		List<Employee> roles = getUserRoles(emp);
		for(int i = 0; i < roles.size(); i++){
			
			if(roles.get(i).getRole_name().equals(Role.department))
				fc[0] = new CheckBox("department",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.administration))
				fc[1] = new CheckBox("administration",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.scholarship))
				fc[2] = new CheckBox("scholarship",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.admission))
				fc[3] = new CheckBox("admission",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.curriculum))
				fc[4] = new CheckBox("curriculum",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.college))
				fc[5] = new CheckBox("college",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.faculty))
				fc[6] = new CheckBox("faculty",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.registration))
				fc[7] = new CheckBox("registrar",new Model(true));
			
			if(roles.get(i).getRole_name().equals(Role.hr))
				fc[8] = new CheckBox("hr",new Model(true));
			
		}
		
		form.add(new AjaxButton("edit", form){
            @Override
            protected void onError(AjaxRequestTarget target, Form form)
            {
                //target.addComponent(feedback);
            }
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				System.out.println("department: " + fc[0].getModelObject());
				
				if((Boolean)fc[0].getModelObject()){ 	// allow department privilege
					emp.setRole_id(getRoleId(Role.department));
					if(getData(emp)==null) allowPrivilege(emp, "allowPrivilege");
				}else{ 									// disallow department privilege
					emp.setRole_id(getRoleId(Role.department));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[1].getModelObject()){	//allow administration privilege
					emp.setRole_id(getRoleId(Role.administration));
					if(getData(emp)==null) allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow administration privilege
					emp.setRole_id(getRoleId(Role.administration));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[2].getModelObject()){ 	//allow scholarship privilege
					emp.setRole_id(getRoleId(Role.scholarship));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow scholarship privilege
					emp.setRole_id(getRoleId(Role.scholarship));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[3].getModelObject()){ 	//allow admission privilege
					emp.setRole_id(getRoleId(Role.admission));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow admission privilege
					emp.setRole_id(getRoleId(Role.admission));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[4].getModelObject()){ 	//allow curriculum privilege
					emp.setRole_id(getRoleId(Role.curriculum));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow curriculum privilege
					emp.setRole_id(getRoleId(Role.curriculum));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[5].getModelObject()){ 	//allow college privilege
					emp.setRole_id(getRoleId(Role.college));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow college privilege
					emp.setRole_id(getRoleId(Role.college));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[6].getModelObject()){ 	//allow faculty privilege
					emp.setRole_id(getRoleId(Role.faculty));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow faculty privilege
					emp.setRole_id(getRoleId(Role.faculty));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[7].getModelObject()){ 	//allow registration privilege
					emp.setRole_id(getRoleId(Role.registration));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow registration privilege
					emp.setRole_id(getRoleId(Role.registration));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				if((Boolean)fc[8].getModelObject()){ 	//allow hr privilege
					emp.setRole_id(getRoleId(Role.hr));
					if(getData(emp)==null)
						allowPrivilege(emp, "allowPrivilege");
				}else{ 									//disallow hr privilege
					emp.setRole_id(getRoleId(Role.hr));
					disallowPrivilege(emp, "disallowPrivilege");
				}
				
				
				emp.setAcct_status((String)statusModel.getObject());
				updateStatus(emp);
				
				target.addJavascript("jAlert('Privileges has been saved')");
			}
		});
		/**
		add(new AjaxFallbackLink("check"){
			public void onClick(AjaxRequestTarget target){
				fc[7] = new CheckBox("registrar", new Model(true));
				 System.out.println("check test");
			}
		});
		add(new AjaxLink("uncheck"){
			public void onClick(AjaxRequestTarget target){
			}
		}); **/
		
		List status = Arrays.asList(new String[] {Employee.ActiveAcct,Employee.BlockAcct,Employee.PendingAcct});
		statusModel = new Model(originalAcctStatus(emp));
		DropDownChoice choice = new DropDownChoice("status", statusModel, status);
		
		for(int i = 0; i < fc.length; i++)
			form.add(fc[i]);
		form.add(new Label("employee_id", Employee.displayAccountId(emp.getEmployee_id())));
		add(done);
		form.add(choice);
		add(form);
	}
	
	private List<Employee> getUserRoles(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		return query.fetchListOfData(emp, "userRoles");
	}
	private void allowPrivilege(Employee emp, String queryId){
		EmployeeQuery query = new EmployeeQuery();
		query.insert(emp, queryId);
	}
	private void disallowPrivilege(Employee emp, String queryId){
		EmployeeQuery query = new EmployeeQuery();
		query.remove(emp, queryId);
	}
	private Employee getData(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		return query.fetchData(emp, "getSpecificRoleId");
	}
	private int getRoleId(String roleId){
		Role role = new Role();
		RoleQuery query = new RoleQuery();
		
		role.setRole_name(roleId);
		
		return query.getData(role, "roleId");
	}
	private String originalAcctStatus(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		return query.getStringOfData(emp, "accountStatus");
	}
	private void updateStatus(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		query.update(emp, "updateAccountStatus");
	}
}
