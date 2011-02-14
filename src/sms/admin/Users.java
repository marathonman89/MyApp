package sms.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import sms.lookup.Lookup;
import sms.session.MySession;
import sms.subject.Subject;
import sms.user.Employee;
import sms.user.EmployeeQuery;

@AuthorizeInstantiation("Administration")
public class Users extends Layout{

	private List<Employee> allUsers;
	private Form form;
	private FeedbackPanel feedback;
	private String part1;
	private String part2;
	
	
	public Users(){
		
		//allUsers = getUserAcct();
		
		if(((MySession)getSession()).getUserAcctSearch()==null){
			allUsers = getUserAcct();
			System.out.println("allUsers is null");
		}
		else{
			//allUsers = getSearchUserAcct(((MySession)getSession()).getUserAcctSearch());
			allUsers = ((MySession)getSession()).getUserAcctSearch();
			System.out.println("allUsers is not null");
		}
		
		SortableDataProvider provider = new SortableDataProvider() {
			public int size() {
				return allUsers.size();
			}
			public IModel model(Object object) {
				Employee emp = (Employee) object;
				return new Model((Serializable) emp);
			}
			public Iterator iterator(int first, int count) {
				SortParam sortParam = getSort();
				return Employee.selectEntries(allUsers, first, count, sortParam).iterator();
			}
		};
		
		
		final DataView dataView = new DataView("sorting", provider){
            @Override
            protected void populateItem(final Item item){
                
                Employee emp = (Employee) item.getModelObject();
    			item.setModel(new CompoundPropertyModel(emp));

    			item.add(new Label("employee_id", Employee.displayAccountId(emp.getEmployee_id())));
    			item.add(new Label("password", emp.getPassword()));
    			item.add(new Label("status", emp.getAcct_status()));
    			item.add(new UsersActionPanel("action", item.getModel()));
    			
                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel()
                {
                    @Override
                    public String getObject()
                    {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        };
        dataView.setOutputMarkupId(true);
        add(new OrderByBorder("orderByEmployeeId", "employee_id", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
        add(new OrderByBorder("orderByAcctStatus", "acct_status", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
        
        form = new Form("acctSearchForm", new CompoundPropertyModel(this));
        feedback = new FeedbackPanel("error");
		feedback.setOutputMarkupId(true);
		FormComponent[] fc = new FormComponent[2];
		fc[0] = new TextField("part1");
		fc[1] = new TextField("part2");
		
        AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("searchButton", form){
            @Override
            protected void onError(AjaxRequestTarget arg0, Form form){
            }
			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
				Employee tmp = new Employee();
				
				Lookup lookup2 = Lookup.AllLookupStatus();
    			Employee logged = ((MySession)getSession()).getLoggedEmployee();
    			String file = lookup2.getLog_file_used();
    			String logMessage = null;

					if(part1 == null && part2 == null){
						((MySession)getSession()).setUserAcctSearch(null);
						setResponsePage(sms.admin.Users.class);
					}else{
						if((part1 != null && !Pattern.matches("[0-9]+", part1)) || (part2 != null && !Pattern.matches("[0-9]+", part2))){
							logMessage = "ERROR! User with Account: " + Employee.displayAccountId(logged.getEmployee_id()) + " searched for an account with invalid account ID";
		    				Log.loggedAction(lookup2.isLog_enabled(), file, logMessage);
							arg0.addJavascript("jAlert('Account must be numeric')");
						}else{
							if(part1 == null && part2 != null)
								tmp.setEmployee_id(Integer.parseInt(part2));
							if(part1 != null && part2 == null)
								tmp.setEmployee_id(Integer.parseInt(part1));
							if(part1 != null && part2 != null)
								tmp.setEmployee_id(Integer.parseInt(part1+part2));
							
							List acctList = getSearchUserAcct(tmp);
							if(acctList != null && acctList.size() > 0){
								((MySession)getSession()).setUserAcctSearch(acctList);
								setResponsePage(sms.admin.Users.class);
							}else{
								logMessage = "ERROR! User with Account: " + Employee.displayAccountId(logged.getEmployee_id()) + " searched for a non existing account";
			    				Log.loggedAction(lookup2.isLog_enabled(), file, logMessage);
								arg0.addJavascript("jAlert('Account not found')");
							}
						}
					}
				
			}
        });
        Link allLink = new Link("allAcct"){
        	public void onClick(){
        		((MySession)getSession()).setUserAcctSearch(null);
        		setResponsePage(sms.admin.Users.class);
        	}};
        Link activeLink = new Link("filterActive"){
        	public void onClick(){
        		List tmp = new ArrayList();
        		for(int i = 0; i < allUsers.size(); i++)
        			if(allUsers.get(i).getAcct_status().equals(Employee.ActiveAcct))
        				tmp.add(allUsers.get(i));
        		((MySession)getSession()).setUserAcctSearch(tmp);
        		setResponsePage(sms.admin.Users.class);
        	}};
        Link blockLink = new Link("filterBlock"){
        	public void onClick(){
        		List tmp = new ArrayList();
        		for(int i = 0; i < allUsers.size(); i++)
        			if(allUsers.get(i).getAcct_status().equals(Employee.BlockAcct))
        				tmp.add(allUsers.get(i));
        		((MySession)getSession()).setUserAcctSearch(tmp);
        		setResponsePage(sms.admin.Users.class);
        	}};
    	Link pendingLink = new Link("filterPending"){
        	public void onClick(){
        		List tmp = new ArrayList();
        		for(int i = 0; i < allUsers.size(); i++)
        			if(allUsers.get(i).getAcct_status().equals(Employee.PendingAcct))
        				tmp.add(allUsers.get(i));
        		((MySession)getSession()).setUserAcctSearch(tmp);
        		setResponsePage(sms.admin.Users.class);
        	}};
        int active = getAcctByStatus(allUsers, Employee.ActiveAcct);
        int block = getAcctByStatus(allUsers, Employee.BlockAcct);
        int pending = getAcctByStatus(allUsers, Employee.PendingAcct);
        Label numberOfResults = new Label("results", "Results Found: " + allUsers.size());
        Label numberOfActive = new Label("active", Integer.toString(active));
        Label numberOfBlock = new Label("block", Integer.toString(block));
        Label numberOfPending = new Label("pending", Integer.toString(pending));
        PagingNavigator pagination = new PagingNavigator("navigator", dataView);
        
        if(allUsers.size() <= 40)
        	pagination.setVisible(false);
        
        if(active == 0){
        	activeLink.setVisible(false);
        	numberOfActive.setVisible(false);
        }
        if(block == 0){
        	blockLink.setVisible(false);
        	numberOfBlock.setVisible(false);
        }
        if(pending == 0){
        	pendingLink.setVisible(false);
        	numberOfPending.setVisible(false);
        }
        	
        dataView.setItemsPerPage(40);
        //add(choices);
        add(dataView);
        add(pagination);
        add(allLink);
        add(activeLink);
        add(blockLink);
        add(pendingLink);
        add(numberOfResults);
        activeLink.add(numberOfActive);
        blockLink.add(numberOfBlock);
        pendingLink.add(numberOfPending);
        add(form);
        add(feedback);
        form.add(fc[0]);
        form.add(fc[1]);
		
	}
	public int getAcctByStatus(List<Employee> accts, String acct_status){
		int result = 0;
		for(int i = 0; i < accts.size(); i++)
			if(accts.get(i).getAcct_status().equals(acct_status))
				result++;
		return result;
	}
	private List<Employee> getUserAcct(){
		EmployeeQuery query = new EmployeeQuery();
		return query.getNoParamData("SystemUsers");
	}
	private List<Employee> getSearchUserAcct(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		return query.fetchListOfData(emp, "SystemUsersWithParam");
	}
}
