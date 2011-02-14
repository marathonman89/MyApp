package sms.hr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.EmailAddressPatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

import sms.Role.Role;
import sms.Role.RoleQuery;
import sms.college.College;
import sms.college.CollegeQuery;
import sms.department.Department;
import sms.department.DepartmentQuery;
import sms.index.Home;
import sms.rank.Rank;
import sms.rank.RankQuery;
import sms.session.MySession;
import sms.user.Employee;
import sms.user.EmployeeQuery;

@AuthorizeInstantiation("HR")
public class AddFaculty extends Home{
	//private Form back;
	private Form form;
	private String part1;
	private String part2;
	private String fname;
	private String lname;
	private String mname;
	private String bdate;
	private String address;
	private String gender;
	private String emailaddress;
	private String rank;
	private String college;
	private String costcenter;
	private Model genderModel;
	private Model rankModel;
	private FeedbackPanel feedback;
	private final Map<String, List<String>> modelsMap = new HashMap<String, List<String>>();
	private String selectedCollege;
	private String selectedDepartment;
	
	public AddFaculty(){
		feedback = new FeedbackPanel("error");
		form = new Form("facultyForm", new CompoundPropertyModel(this));
		//back = new Form("backform");
		//back.add(new Button("back"){
			//public void onSubmit(){
				//setResponsePage(sms.hr.FacultyProfile.class);
			//}
		//});
		feedback.setOutputMarkupId(true);
		final FormComponent[] fc = new FormComponent[12];
		
		fc[0] = new RequiredTextField("part1");
		fc[1] = new RequiredTextField("part2");
		fc[2] = new RequiredTextField("fname");
		fc[3] = new RequiredTextField("mname");
		fc[4] = new RequiredTextField("lname");
		fc[5] = new RequiredTextField("bdate");
		fc[6] = new RequiredTextField("address");
		
		genderModel = new Model();
		List genderChoices = new ArrayList();
		genderChoices.add("Male");
		genderChoices.add("Female");
		fc[7] = new DropDownChoice("gender", genderModel, genderChoices);
		fc[7].setRequired(true);
		
		fc[8] = new RequiredTextField("emailaddress");
		fc[8].add(EmailAddressPatternValidator.getInstance());
		
		rankModel = new Model();
		final List<Rank> rankChoices = getRanks();
		List rankNames = new ArrayList();
		for(int i = 0; i < rankChoices.size(); i++) rankNames.add(rankChoices.get(i).getRank_name());
		fc[9] = new DropDownChoice("rank", rankModel, rankNames);
		fc[9].setRequired(true);
		
		final List<College> collegesList = getColleges();
		final List<Department> deptList = getDepartments();
		for(int i = 0; i < collegesList.size(); i++){
			List depts = new ArrayList();
			String colname = collegesList.get(i).getCollege_name();
			for(int j = 0; j < deptList.size(); j++){
				if(collegesList.get(i).getCollege_code() == deptList.get(j).getCollege_code())
					depts.add(deptList.get(j).getDept_name());
			}
			modelsMap.put(colname, depts);
		}

        final IModel collegeChoices = new AbstractReadOnlyModel(){
            @Override
            public List<String> getObject(){
                Set<String> keys = modelsMap.keySet();
                List<String> list = new ArrayList<String>(keys);
                return list;
            }

        };
        final IModel departmentChoices = new AbstractReadOnlyModel(){
            @Override
            public List<String> getObject(){
                List<String> models = modelsMap.get(selectedCollege);
                if (models == null){
                    models = Collections.emptyList();
                }
                return models;
            }
        };
        
        //final DropDownChoice colleges = new DropDownChoice("college", new PropertyModel(this, "selectedMake"), collegeChoices);
        //final DropDownChoice departments = new DropDownChoice("costcenter",new Model(), departmentChoices);
        //departments.setOutputMarkupId(true);
           
        fc[10] = new DropDownChoice("college", new PropertyModel(this, "selectedCollege"), collegeChoices);
        fc[11] = new DropDownChoice("costcenter",new PropertyModel(this, "selectedDepartment"), departmentChoices);
        fc[10].setRequired(true);
        fc[11].setRequired(true);
        fc[11].setOutputMarkupId(true);
        
        fc[10].add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                target.addComponent(fc[11]);
            }
        });
        
        AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("add", form){
        	protected void onError(AjaxRequestTarget target, Form form){
                //target.addComponent(feedback);
                //System.out.println(feedback.toString());
        		boolean haserror = false;
        		String errormsg = "<ul id='alerterror'>";
				if(fc[0].hasErrorMessage() || fc[1].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the ID Number</li>";
					haserror = true;
				}
				if(fc[2].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the First name</li>";
					haserror = true;
				}
				if(fc[3].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Middle name</li>";
					haserror = true;
				}
				if(fc[4].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Last name</li>";
					haserror = true;
				}
				if(fc[5].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Birthdate</li>";
					haserror = true;
				}
				if(fc[6].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Address</li>";
					haserror = true;
				}
				if(fc[7].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Gender</li>";
					haserror = true;
				}
				if(fc[8].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Email Address</li>";
					haserror = true;
				}
				if(fc[9].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Rank</li>";
					haserror = true;
				}
				if(fc[10].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the College</li>";
					haserror = true;
				}
				if(fc[11].hasErrorMessage()){
					errormsg += "<li>Provide a valid input for the Department</li>";
					haserror = true;
				}
				errormsg += "</ul>";
				if(haserror){
					target.addJavascript("jAlert("+errormsg+")");
				}
            }
        	protected void onSubmit(AjaxRequestTarget target, Form form){
        		if(!Pattern.matches("[0-9]+", part1) || !Pattern.matches("[0-9]+", part2)){
					//error("ID number must be numeric");
        			target.addJavascript("jAlert('ID number must be numeric')");
				}
        		else{
        			/*
        			System.out.println("Employee ID: " + part1+part2);
        			System.out.println("Fname: " + fname);
        			System.out.println("Mname: " + mname);
        			System.out.println("Lname: " + lname);
        			System.out.println("Bdate: " + bdate);
        			System.out.println("Address: " + address);
        			System.out.println("Gender: " + genderModel.getObject());
        			System.out.println("Email Address: " + emailaddress);
        			System.out.println("Rank: " + rankModel.getObject());
        			System.out.println("College: " + selectedCollege);
        			System.out.println("Department: " + selectedDepartment); */
        			
        			int empId =  Integer.parseInt(part1+part2);
        			Employee tmp = new Employee(empId);
        			if(!entryExist(tmp)){
	        			int rankCode = -1;
	        			int departmentCode = -1;
	        			
	        			for(int i = 0; i < rankChoices.size();i++)
	        				if(rankModel.getObject().equals(rankChoices.get(i).getRank_name()))
	        					rankCode = rankChoices.get(i).getRank_id();
	        			
	        			for(int i = 0; i < deptList.size(); i++)
	        				if(selectedDepartment.equals(deptList.get(i).getDept_name()))
	        					departmentCode = deptList.get(i).getDept_code();

	        			EmployeeQuery insertQuery = new EmployeeQuery();
	        			Employee addEmp = new Employee(empId,fname,mname,lname,bdate, 
	        					                       address,genderModel.getObject().toString().substring(0, 1),
	        					                       emailaddress,rankCode,departmentCode, "faculty", 
	        					                       encPassword(Integer.toString(empId)), Employee.PendingAcct, getRoleId(Role.faculty));
	        			insertQuery.insert(addEmp, "insertEmployee");
	        			insertQuery.insert(addEmp, "insertUser");
	        			insertQuery.insert(addEmp, "allowPrivilege");
	        			((MySession) getSession()).setSuccessOnAddFaculty(true);
	        			setResponsePage(sms.hr.AddFaculty.class);
	        			/*  1. faculty must be added in the system users
	        			 *  2. faculty must be given a default user role which is the FACULTY ROLE
	        			 */
        			}else{
        				target.addJavascript("jAlert('Process Failed. Faculty with ID number: "+part1+"-"+part2+" already exist')");
        			}
        		}
			}
        });
        
        AbstractBehavior success =  new AbstractBehavior() {
        	public void renderHead(IHeaderResponse response) {
            	super.renderHead(response);
            	response.renderOnLoadJavascript("jAlert('Faculty successfully added')");
        	}
        }; 
        
        if(((MySession)getSession()).isSuccessOnAddFaculty()){
        	add(success);
        	((MySession) getSession()).setSuccessOnAddFaculty(false);
        }
		for(int i = 0; i < fc.length; i++) form.add(fc[i]);
		//form.add(fc[10]);
        //form.add(fc[11]);
		//add(back);
		add(form);
		add(feedback);
	}
	private boolean entryExist(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		if(query.getIntegerData(emp, "numberOfEntries").intValue() > 0)
			return true;
		return false;
	}
	private List<College> getColleges(){
		CollegeQuery query = new CollegeQuery();
		return query.getDataWithNoParam("getColleges");
	}
	private List<Department> getDepartments(){
		DepartmentQuery query = new DepartmentQuery();
		return query.getListOfDataWithNoParam("getDepartments");
	}
	private int getRoleId(String roleId){
		Role role = new Role();
		RoleQuery query = new RoleQuery();
		role.setRole_name(roleId);
		return query.getData(role, "roleId");
	}
	private List<Rank> getRanks(){
		RankQuery query = new RankQuery();
		return query.getDataWithNoParam("getRanks");
		/*
		List<Rank> ranks = query.getDataWithNoParam("getRanks");
		List rankName = new ArrayList();
		for(int i = 0; i < ranks.size(); i++){
			rankName.add(ranks.get(i).getRank_name());
		}
		return rankName; */
	}
	/*
	public String getSelectedCollege()
    {
        return selectedCollege;
    }
	public void setSelectedCollege(String selectedCollege)
    {
        this.selectedCollege = selectedCollege;
    }
	public String getSelectedDepartment() {
		return selectedDepartment;
	}
	public void setSelectedDepartment(String selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	} */

}
