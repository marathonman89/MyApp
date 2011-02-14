package sms.admission;

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

import sms.session.MySession;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.user.Employee;


public class StudentProfile extends Layout{
	private Form form;
	private List<Student> students;
	private String searchname;
	
	public StudentProfile(){
		form = new Form("studentsearch", new CompoundPropertyModel(this));
		form.add(new TextField("searchname"));
		
		if(((MySession)getSession()).getStudentSearch()==null){
			students = getStudents();
			//allUsers = getUserAcct();
			//System.out.println("allUsers is null");
		}
		else{
			students = getSearchStudent(((MySession)getSession()).getStudentSearch());
			//allUsers = getSearchUserAcct(((MySession)getSession()).getUserAcctSearch());
			//System.out.println("allUsers is not null");
		}
		
		SortableDataProvider provider = new SortableDataProvider() {
			public int size() {
				return students.size();
			}
			public IModel model(Object object) {
				Student stud = (Student) object;
				return new Model((Serializable) stud);
			}
			public Iterator iterator(int first, int count) {
				SortParam sortParam = getSort();
				return Student.selectEntries1(students, first, count, sortParam).iterator();
			}
		};
		
		final DataView dataView = new DataView("studentlist", provider){
            @Override
            protected void populateItem(final Item item){
                
            	Student stud = (Student) item.getModelObject();

    			item.add(new Label("student_id", Employee.displayAccountId(stud.getStudent_id())));
    			item.add(new Label("name", stud.getLname() + ", " + stud.getFname() + " " + stud.getMname().charAt(0) + "."));
    			item.add(new Label("deptname", stud.getDept_name()));
    			item.add(new Label("collegename", stud.getCollege_name()));
    			item.add(new ProfileActionPanel("action", item.getModel()));
                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel(){
                    @Override
                    public String getObject(){
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        };
        
        Label numberOfResults = new Label("results", "Results Found: " + students.size());
        PagingNavigator pagination = new PagingNavigator("navigator", dataView);
        
        if(students.size() <= 40)
        	pagination.setVisible(false);
        
        dataView.setItemsPerPage(40);
        add(dataView);
        add(pagination);
        add(numberOfResults);
		add(form);
		add(new Link("addStudent"){
			public void onClick(){
				setResponsePage(AddStudent.class);
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
				Student tmp = new Student();
				tmp.setLname(searchname);
				
				if(searchname == null){
					((MySession)getSession()).setStudentSearch(null);
					setResponsePage(sms.admission.StudentProfile.class);
				}else{
					List studentList = getSearchStudent(tmp);
					if(studentList != null && studentList.size() > 0){
						((MySession)getSession()).setStudentSearch(tmp);
						setResponsePage(sms.admission.StudentProfile.class);
						//((MySession)getSession()).setUserAcctSearch(tmp);
						//setResponsePage(sms.admin.Privileges.class);
					}else
						arg0.addJavascript("jAlert('Student not found')");
					
				}
				
			}
        });
	}
	private List<Student> getStudents(){
		StudentQuery query = new StudentQuery();
		return query.getDataWithNoParam("getStudents");
	}
	private List<Student> getSearchStudent(Student stud){
		StudentQuery query = new StudentQuery();
		return query.fetchListOfData(stud, "getStudentsWithParam");
	}

}
