package sms.lookup;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;

import sms.student.Student;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;


public class LookupQuery implements Serializable{
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	public LookupQuery() {}
	
	public Lookup fetchData(String queryId){
		Lookup result			= null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Lookup)sqlMap.queryForObject(queryId);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String fetchDatum(String queryId){
		String result			= null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (String)sqlMap.queryForObject(queryId);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void updateData(Lookup lookup, String id){
		try 
		{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			sqlMap.update(id, lookup);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
