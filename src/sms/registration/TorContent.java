package sms.registration;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;

import sms.index.Home;
import sms.student.Student;
import sms.subject.Subject;
import sms.subject.SubjectQuery;

public class TorContent extends Home{

	public TorContent(final Student stud){
		String id = Student.displayStudentId(stud.getStudent_id());
		String name = stud.getLname() + ", " + stud.getFname() + " " + stud.getMname();
		List<Subject> yearAndSem = getYearAndSemesterOfRecord(stud);
		ListView allYearAndSem = new ListView("YearAndSem", yearAndSem){
			protected void populateItem(ListItem item){
				Subject subj = (Subject) item.getModelObject();
				
				String year = Integer.toString(subj.getSchool_year()) + " - " + Integer.toString(subj.getSchool_year()+1);
				String sem = subj.getSemester();
				
				stud.setSchool_year(Integer.toString(subj.getSchool_year()));
				stud.setSemester(sem);
				
				List<Subject> subjectsTaken = getAllRecords(stud, "getStudentTor");
				ListView allrecord = new ListView("record", subjectsTaken){
					protected void populateItem(ListItem item){
						Subject subj = (Subject) item.getModelObject();
						String units = subj.getUnits() == null ? "n/a" : Integer.toString(subj.getUnits());
						String rating = subj.getRating() == null ? "n/a" : subj.getRating();
						
						item.setModel(new CompoundPropertyModel(subj));
						item.add(new Label("subject_name"));
						item.add(new Label("subject_section"));
						item.add(new Label("units", units));
						item.add(new Label("rating", rating));
					}
				};
				
				Label yearLabel = new Label("year", year);
				Label semLabel = new Label("sem", sem);
				Label gpaLabel = new Label("gpa", calculateGPA(subjectsTaken).toString());
				if(stud.getEduc_level().equals(Student.secondary)){
					semLabel = new Label("sem", "n/a");
				}
				
				item.add(yearLabel);
				item.add(semLabel);
				item.add(gpaLabel);
				item.add(allrecord);
			}
		};
		List<Subject> allSubject = getAllRecords(stud, "getStudentTorWithIDasParam"); 
		add(new Label("student_id", id));
		add(new Label("name", name));
		add(new Label("overallGPA", calculateGPA(allSubject).toString()));
		add(allYearAndSem);
	}
	public Double calculateGPA(List<Subject> subj){
		Double gpa = new Double(0.0);
		int excludingSubj = 0;
		int numOfSubj = subj.size();
		
		for(int i = 0; i < numOfSubj; i++){
			if(subj.get(i).getRating()==null || subj.get(i).getRating().equals("wdrw"))
				excludingSubj++;
			else{
				if(subj.get(i).getRating().equals("drp") || subj.get(i).getRating().equals("inc"))
					gpa+=5;
				else
					gpa+= Double.parseDouble(subj.get(i).getRating());
			}
		}
		if(numOfSubj-excludingSubj > 0)
			return gpa/(numOfSubj-excludingSubj);
		
		return gpa;
	}
	public List<Subject> getAllRecords(Student student, String queryId){
		SubjectQuery query = new SubjectQuery();
		return query.getData(student, queryId);
	}
	public List<Subject> getYearAndSemesterOfRecord(Student student){
		SubjectQuery query = new SubjectQuery();
		return query.getData(student, "getYearAndSemesterOfTor");
	}
}

