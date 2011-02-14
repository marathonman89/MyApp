package sms.studscholarship;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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

import sms.scholarship.Scholarship;
import sms.scholarship.ScholarshipQuery;
import sms.session.MySession;


public class StudentScholarshipProfile extends Layout{

	private Form form;
	private List<Scholarship> scholarships;
	private String searchname;
	
	public StudentScholarshipProfile() {
		form = new Form("studentscholarshipsearch", new CompoundPropertyModel(this));
		form.add(new TextField("searchname"));
		
		if(((MySession)getSession()).getStudentScholarshipSearch()==null){
			scholarships = getStudentScholarship();
			//allUsers = getUserAcct();
			//System.out.println("allUsers is null");
		}
		else{
			scholarships = getSearchStudentScholarship(((MySession)getSession()).getStudentScholarshipSearch());
			//allUsers = getSearchUserAcct(((MySession)getSession()).getUserAcctSearch());
			//System.out.println("allUsers is not null");
		}
		
		SortableDataProvider provider = new SortableDataProvider() {
			public int size() {
				return scholarships.size();
			}
			public IModel model(Object object) {
				Scholarship scholar = (Scholarship) object;
				return new Model((Serializable) scholar);
			}
			public Iterator iterator(int first, int count) {
				SortParam sortParam = getSort();
				return Scholarship.selectEntries(scholarships, first, count, sortParam).iterator();
			}
		};
		
		final DataView dataView = new DataView("scholarshiplist", provider){
            @Override
            protected void populateItem(final Item item){
                
                Scholarship scholar = (Scholarship) item.getModelObject();
                /*Student stud = new Student();
                stud.setStudent_id(scholar.getStudent_id());
                StudentQuery query = new StudentQuery();
                
                Student name = query.getData(stud, "getName");*/
                	
                
                item.add(new Label("student_id", Scholarship.displayAccountId(scholar.getStudent_id())));
                item.add(new Label("name", scholar.getLname() + ", " + scholar.getFname() + " " + scholar.getMname().charAt(0) + "."));
                item.add(new Label("scholarshipname", scholar.getScholarship_name()));
                item.add(new Label("status", scholar.getStatus()));
                item.add(new ProfileActionPanel("action", item.getModel()));
                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel(){
                    @Override
                    public String getObject(){
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
               
                }));
            }
        };
        
        Label numberOfResults = new Label("results", "Results Found: " + scholarships.size());
        PagingNavigator pagination = new PagingNavigator("navigator", dataView);
        
        if(scholarships.size() <= 40)
        	pagination.setVisible(false);
        
        dataView.setItemsPerPage(40);
        add(dataView);
        add(pagination);
        add(numberOfResults);
		add(form);
		add(new Link("addStudentScholarship"){
			public void onClick(){
				setResponsePage(AddStudentScholarship.class);
			}
		});
		add(new OrderByBorder("orderByStudentId", "student_id", provider)
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
		add(new OrderByBorder("orderByScholarship", "scholarshipname", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
		add(new OrderByBorder("orderByStatus", "status", provider)
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
				Scholarship tmp = new Scholarship();
				tmp.setLname(searchname);
				
				if(searchname == null){
					((MySession)getSession()).setStudentScholarshipSearch(null);
					setResponsePage(sms.studscholarship.StudentScholarshipProfile.class);
				}else{
					List studentScholarshipList = getSearchStudentScholarship(tmp);
					if(studentScholarshipList != null && studentScholarshipList.size() > 0){
						((MySession)getSession()).setStudentScholarshipSearch(tmp);
						setResponsePage(sms.studscholarship.StudentScholarshipProfile.class);
						//((MySession)getSession()).setUserAcctSearch(tmp);
						//setResponsePage(sms.admin.Privileges.class);
					}else
						arg0.addJavascript("jAlert('Student not found')");
					
				}
				
			}
        });
	}
	
	private List<Scholarship> getStudentScholarship() {
		ScholarshipQuery query = new ScholarshipQuery();
		return query.getDataWithNoParam("getStudentScholar");
	}
	
	private List<Scholarship> getSearchStudentScholarship(Scholarship scholarship) {
		ScholarshipQuery query = new ScholarshipQuery();
		return query.fetchListOfData(scholarship, "getStudentScholarshipWithParam");
	}
}
