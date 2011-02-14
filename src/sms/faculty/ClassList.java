package sms.faculty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import sms.advising.ControlSubject;
import sms.lookup.Lookup;
import sms.session.MySession;
import sms.subject.Subject;
import sms.subject.SubjectQuery;
import sms.user.Employee;
import sms.user.EmployeeQuery;

public class ClassList extends Layout{
	
	private List<Subject> allSubjects;
	private Form form;
	private FeedbackPanel feedback;
	private String searchedSubject;
	
	public ClassList(){
		Employee logged = ((MySession)getSession()).getLoggedEmployee();
		
		Lookup l = Lookup.EnrollmentStatus();
		final int schoolYear = l.getYear_started();
		final String semester = notInSecondary(logged) ? l.getSem_started() : l.getTwoSems();
		
		logged.setSemester(semester);
		logged.setSchool_year(schoolYear);
		

		if(((MySession)getSession()).getClassListSearch()==null){
			allSubjects = allSubjects(logged);
			System.out.println("allSubjects is null");
		}
		else{
			allSubjects = getSearchedSubj(((MySession)getSession()).getClassListSearch());
			System.out.println("allSubjects is not null");
		}
		
		
		SortableDataProvider provider = new SortableDataProvider() {
			public int size() {
				return allSubjects.size();
			}
			public IModel model(Object object) {
				Subject subj = (Subject) object;
				return new Model((Serializable) subj);
			}
			public Iterator iterator(int first, int count) {
				SortParam sortParam = getSort();
				//return Subject.selectEntries(allSubjects, first, count, sortParam).iterator();
				return Subject.selectEntries(allSubjects, first, count).iterator();
			}
		};
		
		final DataView dataView = new DataView("sorting", provider)
        {
            @Override
            protected void populateItem(final Item item)
            {
                
                Subject subj = (Subject) item.getModelObject();
    			item.setModel(new CompoundPropertyModel(subj));
    			
    			String[] tokens = Subject.getCourseNumberAndDesc(subj.getSubject_name());
    			
    			item.add(new Label("subject_section"));
    			item.add(new Label("subject_code", tokens[0]));
    			item.add(new Label("subject_name", tokens[1])); 
    			item.add(new Label("time"));
    			item.add(new Label("day"));
    			item.add(new ActionPanel("action", item.getModel()));
    			
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
        
        form = new Form("subjectSearchForm", new CompoundPropertyModel(this));
        FormComponent fc = new TextField("searchedSubject");
		feedback = new FeedbackPanel("error");
		feedback.setOutputMarkupId(true);
        
        AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
        form.add(new AjaxButton("searchButton", form){
            @Override
            protected void onError(AjaxRequestTarget arg0, Form form){
            }
			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
				Subject subj = new Subject();
				
				subj.setSubject_name(searchedSubject);
				subj.setSchool_year(schoolYear);
				subj.setSemester(semester);
				
				if(getSearchedSubj(subj) != null){
					((MySession)getSession()).setClassListSearch(subj);
					setResponsePage(sms.faculty.ClassList.class);
				}else
					arg0.addJavascript("jAlert('Subject not available')");
				
			}
        });
		
        Label numberOfResults = new Label("results", "Results Found: " + allSubjects.size());
        PagingNavigator pagination = new PagingNavigator("navigator", dataView);
        
        if(allSubjects.size() <= 40)
        	pagination.setVisible(false);
        
        dataView.setItemsPerPage(40);
        add(dataView);
        add(pagination);
        //add(new PagingNavigator("navigator", dataView));
        add(numberOfResults);
        add(form);
		add(feedback);
		form.add(fc);
	}
	public List<Subject> getSearchedSubj(Subject subj){
		
		SubjectQuery query1 = new SubjectQuery();
		List<Subject> offered = query1.getData(subj, "getOfferedSubject");
		
		if(offered.size() > 0){
			for(int i = 0; i < offered.size(); i++){
				Subject tmp = new Subject();
				SubjectQuery query2 = new SubjectQuery();
				
				tmp.setOffered_id(offered.get(i).getOffered_id());
				
				String deptname = query2.getStringData(tmp, "getDeptFromDeptCode");
				String progname = query2.getStringData(tmp, "getProgFromProgCode");
				String colname  = query2.getStringData(tmp, "getColFromColCode");
				
				offered.get(i).setDept_name(deptname);
				offered.get(i).setProgram_name(progname);
				offered.get(i).setCollege_name(colname); 
			}
			return offered;
		}else
			return null;
	}
	private boolean notInSecondary(Employee emp){
		EmployeeQuery query = new EmployeeQuery();
		String str = query.getStringOfData(emp, "getDeptOfEmployee");
		String ids = "IDS";
		if(str.contains(ids))
			return false;
		
		return true;
	}
	private List<Subject> allSubjects(Employee emp){
		SubjectQuery query = new SubjectQuery();
		return query.getData(emp, "subjectOffered");
	}
}
