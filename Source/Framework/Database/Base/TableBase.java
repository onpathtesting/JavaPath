package Framework.Database.Base;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import Framework.Core.Log;
import Framework.Database.Database;

public class TableBase {
	private Log _logger;
	private String _tableName;
	private String _pkField;
	private Map<String,Object> _attributes;

	@SuppressWarnings("unused")
	private TableBase(){}
	
	protected TableBase(Log logger, String tableName, String pkFieldName)
	{
		_tableName = tableName;
		_pkField = pkFieldName;
		_attributes = new HashMap<String,Object>();
	}
	
	public Object get(String fieldName)
	{
		return _attributes.get(fieldName);
	}

	public void set(String fieldName, Object fieldValue)
	{
		_attributes.put(fieldName, fieldValue);
	}

	public Integer getPrimaryKeyValue(String fieldName, Object fieldValue) throws SQLException, InterruptedException
	{
		Integer primaryKeyValue = 0;
		
		String sqlString = "SELECT %1$s FROM %2$s WHERE %3$s = \"%4$s\"";
		sqlString = String.format(sqlString, _pkField,_tableName,fieldName,fieldValue.toString());
		sqlString = StringEscapeUtils.escapeSql(sqlString);
		
		Database.lockDB();
		PreparedStatement sqlStatement = Database.getDbConnection().prepareStatement(sqlString);
		sqlStatement.execute();
		primaryKeyValue = sqlStatement.getResultSet().getInt(0);
		Database.unlockDB();
		
		return primaryKeyValue;
	}
	
	public Integer AddUnique()
	{
		Integer keyValue = 0;
		
		try {
			keyValue= Add();
		} 
		catch (SQLException e) 
		{
			try 
			{
				keyValue = Read();
			} catch (SQLException e1) {
				_logger.fine("Read Exception :" +e.getMessage());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (InterruptedException e) {
//			e.printStackTrace();
		}

		return keyValue;
	}
	
	public Integer Add() throws InterruptedException, SQLException
	{
		Integer keyValue = 0;
		String fieldNames = "";
		String fieldValues = "";
		
		for (Map.Entry<String,Object> entry : _attributes.entrySet()) {
			fieldNames += (fieldNames.isEmpty() ? "" : ",") + entry.getKey();
			fieldValues += (fieldValues.isEmpty() ? "\"" : ",\"") + entry.getValue().toString() + "\"";
		}

		String sqlString = "INSERT INTO %1$s(%2$s) VALUES(%3$s)";
		sqlString = String.format(sqlString, _tableName,fieldNames,fieldValues);
		sqlString = StringEscapeUtils.escapeSql(sqlString);
		
		try {
			Database.lockDB();
			PreparedStatement sqlStatement;
			sqlStatement = Database.getDbConnection().prepareStatement(sqlString);
			sqlStatement.executeUpdate();
			keyValue = sqlStatement.getGeneratedKeys().getInt(1);
		} 
		catch (SQLException e) 
		{
			throw new SQLException(e);
		} 
		finally
		{
			Database.unlockDB();
		}
		
		return keyValue;
	}
	
	public Integer Read() throws InterruptedException, SQLException
	{
		Integer keyValue = 0;
		String fieldCondition = "";
		
		for (Map.Entry<String,Object> entry : _attributes.entrySet()) {
			fieldCondition += (fieldCondition.isEmpty() ? "" : " AND ") + entry.getKey();
			fieldCondition += "=\"" + entry.getValue().toString() + "\"";
		}

		String sqlString = "SELECT %1$s FROM %2$s WHERE %3$s";
		sqlString = String.format(sqlString, _pkField, _tableName, fieldCondition);
		sqlString = StringEscapeUtils.escapeSql(sqlString);
		
		try 
		{
			Database.lockDB();
			PreparedStatement sqlStatement = Database.getDbConnection().prepareStatement(sqlString);
			sqlStatement.execute();
			keyValue = sqlStatement.getResultSet().getInt(1);
		}
		catch (SQLException e) 
		{
			throw new SQLException(e);
		} 
		finally
		{
			Database.unlockDB();
		}
		
		return keyValue;
	}
}
