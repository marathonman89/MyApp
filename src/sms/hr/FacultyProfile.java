package sms.hr;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import sms.admin.UsersActionPanel;
import sms.session.MySession;
import sms.user.Employee;
import sms.user.EmployeeQuery;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;

@AuthorizeInstantiation("HR")
public class FacultyProfile extends Layout{
	private Form form;
	private List<Employee> faculties;
	private String searchname;
	
	public FacultyProfile(){
		form = new Form("facultysearch", new CompoundPropertyModel(this));
		form.add(new TextField("searchname"));
		
		if(((MySession)getSession()).getFacultySearch()==null){
			faculties = getFaculties();
			//allUsers = getUserAcct();
			//System.out.println("allUsers is null");
		}
		else{
			faculties = getSearchFaculty(((MySession)getSession()).getFacultySearch());
			//allUsers = getSearchUserAcct(((MySession)getSession()).getUserAcctSearch());
			//System.out.println("allUsers is not null");
		}
		
		SortableDataProvider provider = new SortableDataProvider() {
			public int size() {
				return faculties.size();
			}
			public IModel model(Object object) {
				Employee emp = (Employee) object;
				return new Model((Serializable) emp);
			}
			public Iterator iterator(int first, int count) {
				SortParam sortParam = getSort();
				return Employee.selectEntries(faculties, first, count, sortParam).iterator();
			}
		};
		
		final DataView dataView = new DataView("facultylist", provider){
            @Override
            protected void populateItem(final Item item){
                
                Employee emp = (Employee) item.getModelObject();

    			item.add(new Label("employee_id", Employee.displayAccountId(emp.getEmployee_id())));
    			item.add(new Label("name", emp.getLname() + ", " + emp.getFname() + " " + emp.getMname().charAt(0) + "."));
    			item.add(new Label("deptname", emp.getDept_name()));
    			item.add(new Label("collegename", emp.getCollege_name()));
    			item.add(new ProfileActionPanel("action", item.getModel()));
                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel(){
                    @Override
                    public String getObject(){
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        };
        
        Label numberOfResults = new Label("results", "Results Found: " + faculties.size());
        PagingNavigator pagination = new PagingNavigator("navigator", dataView);
        
        if(faculties.size() <= 40)
        	pagination.setVisible(false);
        
        dataView.setItemsPerPage(40);
        add(dataView);
        add(pagination);
        add(numberOfResults);
		add(form);
		add(new Link("addFaculty"){
			public void onClick(){
				setResponsePage(AddFaculty.class);
			}
		});
		add(new OrderByBorder("orderByEmployeeId", "employee_id", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
		add(new OrderByBorder("orderByName", "name", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
		add(new OrderByBorder("orderByDept", "department", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
		add(new OrderByBorder("orderByCol", "college", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
		form.add(new AjaxButton("searchbutton", form){
            @Override
            protected void onError(AjaxRequestTarget arg0, Form form){
            }
			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
				System.out.println(searchname);
				Employee tmp = new Employee();
				tmp.setLname(searchname);
				
				if(searchname == null){
					((MySession)getSession()).setFacultySearch(null);
					setResponsePage(sms.hr.FacultyProfile.class);
				}else{
					List facultyList = getSearchFaculty(tmp);
					if(facultyList != null && facultyList.size() > 0){
						((MySession)getSession()).setFacultySearch(tmp);
						setResponsePage(sms.hr.FacultyProfile.class);
						//((MySession)getSession()).setUserAcctSearch(tmp);
						//setResponsePage(sms.admin.Privileges.class);
					}else
						arg0.addJavascript("jAlert('Faculty not found')");
					
				}
				
			}
        });
	}
	private List<Employee> getFaculties(){
		EmployeeQuery query = new EmployeeQuery();
		return query.getNoParamData("getFaculties");
	}
	private List<Employee> getSearchFaculty(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		return query.fetchListOfData(emp, "getFacultiesWithParam");
	}
}
