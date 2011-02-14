package sms.Role;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;

import sms.user.Employee;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class RoleQuery implements Serializable{
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	
	public RoleQuery(){}
	
	public int getData(Role role, String id){
		Integer result = null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (Integer)sqlMap.queryForObject(id, role);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	} 
}
