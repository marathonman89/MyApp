package sms.subject;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import sms.student.Student;
import sms.user.Employee;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SubjectQuery implements Serializable{
	
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	
	public SubjectQuery(){}
	
	public List<Subject> getData(Subject subject, String id){
		List<Subject> result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Subject>)sqlMap.queryForList(id, subject);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Subject> getData(Student stud, String id){
		List<Subject> result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Subject>)sqlMap.queryForList(id, stud);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Subject> getData(Employee emp, String id){
		List<Subject> result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Subject>)sqlMap.queryForList(id, emp);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getStringData(Subject subject, String id){
		String result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (String)sqlMap.queryForObject(id, subject);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Student> getListOfStudents(Subject subject, String id){
		List<Student> result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Student>)sqlMap.queryForList(id, subject);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public boolean getBooleanData(Subject subject, String id){
		boolean result = false;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Boolean)sqlMap.queryForObject(id, subject);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public void updateData(Subject subject, String id){
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			sqlMap.update(id, subject);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
