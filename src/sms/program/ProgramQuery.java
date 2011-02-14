package sms.program;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import sms.user.Employee;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class ProgramQuery implements Serializable{
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	
	public ProgramQuery(){}
	
	public List<Program> getData(Employee emp, String queryId){
		List<Program> result			= null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Program>)sqlMap.queryForList(queryId, emp);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public Program getDatum(Program program, String queryId){
		Program result			= null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Program)sqlMap.queryForObject(queryId, program);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
