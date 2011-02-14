package sms.student;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import sms.subject.Subject;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class StudentQuery implements Serializable{
	
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	
	public StudentQuery(){}
	
	public void insertData(Student student, String id)
	{
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			sqlMap.insert(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Student getData(Student student, String id)
	{
		Student result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Student)sqlMap.queryForObject(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Student> getDataWithNoParam(String id){
		List<Student> result = null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Student>)sqlMap.queryForList(id);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void updateData(Student student, String id){
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			sqlMap.update(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteData(Student student, String id){
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			sqlMap.delete(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Integer getIntegerDatum(Student student, String id)
	{
		Integer result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Integer)sqlMap.queryForObject(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Subject> getStudentData(Student student, String id)
	{
		List<Subject> result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Subject>)sqlMap.queryForList(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Student> fetchListOfData(Student student, String id)
	{
		List<Student> result = null;
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Student>)sqlMap.queryForList(id, student);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
