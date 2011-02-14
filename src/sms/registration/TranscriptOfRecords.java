package sms.registration;

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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import sms.admin.UsersActionPanel;
import sms.student.Student;
import sms.student.StudentQuery;
import sms.user.Employee;

public class TranscriptOfRecords extends Layout{
	private FeedbackPanel feedback;
	private Form form;
	private String name;
	private List<Student> allStudent;
	
	public TranscriptOfRecords(){
		feedback = new FeedbackPanel("error");
		form = new Form("torsearchform", new CompoundPropertyModel(this));
		
		AjaxFormValidatingBehavior.addToAllFormComponents(form, "onsubmit", Duration.ONE_SECOND);
		form.add(new TextField("name"));
        form.add(new AjaxButton("searchbutton", form){
            @Override
            protected void onError(AjaxRequestTarget arg0, Form form){
            }
			@Override
			protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
			}
        });
        
        allStudent = getStudentsWithRecord();
        
        SortableDataProvider provider = new SortableDataProvider() {
			public int size() {
				return allStudent.size();
			}
			public IModel model(Object object) {
				Student stud = (Student) object;
				return new Model((Serializable) stud);
			}
			public Iterator iterator(int first, int count) {
				SortParam sortParam = getSort();
				return Student.selectEntries(allStudent, first, count, sortParam).iterator();
			}
		};
		
		final DataView dataView = new DataView("records", provider){
            @Override
            protected void populateItem(final Item item){
                Student stud = (Student) item.getModelObject();
                item.setModel(new CompoundPropertyModel(stud));
                
                String student_id = Student.displayStudentId(stud.getStudent_id());
                String student_name = stud.getLname() + ", " + stud.getFname() + " " + stud.getMname(); 
                item.add(new Label("student_id", student_id));
                item.add(new Label("student_name", student_name));
                item.add(new TorActionPanel("action", item.getModel()));
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
        add(new OrderByBorder("orderByStudentId", "student_id", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
        add(new OrderByBorder("orderByLname", "lname", provider)
        {
            @Override
            protected void onSortChanged()
            {
                dataView.setCurrentPage(0);
            }
        });
		add(feedback);
		add(form);
		add(dataView);
	}
	
	private List<Student> getStudentsWithRecord(){
		StudentQuery query = new StudentQuery();
		return query.getDataWithNoParam("getStudentWithRecords");
	}
}
