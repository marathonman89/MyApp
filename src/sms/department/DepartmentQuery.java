package sms.department;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import sms.user.Employee;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class DepartmentQuery implements Serializable{
	
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	
	public DepartmentQuery(){}
		
	public Department getData(Employee emp, String queryId){
		Department result = null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Department)sqlMap.queryForObject(queryId, emp);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Department> getListOfDataWithNoParam(String queryId){
		List<Department> result = null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Department>)sqlMap.queryForList(queryId);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
