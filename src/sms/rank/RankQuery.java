package sms.rank;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class RankQuery implements Serializable{
	private Reader reader 			= null;
	private SqlMapClient sqlMap 	= null;
	
	public RankQuery(){}
	
	public List<Rank> getDataWithNoParam(String id){
		List<Rank> result = null;
		try{
			reader = Resources.getResourceAsReader("sqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			result = (List<Rank>)sqlMap.queryForList(id);
		}catch (IOException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
