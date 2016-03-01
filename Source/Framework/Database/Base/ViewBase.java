package Framework.Database.Base;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;

import Framework.Database.Database;

public abstract class ViewBase {
	private final String _viewName;
	
	public ViewBase(String viewName)
	{
		_viewName = viewName;
	}

	public void readView() throws Exception 
	{
		ResultSet resultSet = null;
		String sqlString = "SELECT * FROM %1$s";
		sqlString = String.format(sqlString, _viewName);
		sqlString = StringEscapeUtils.escapeSql(sqlString);

		startReport();
		
		try {
			Database.lockDB();
			PreparedStatement sqlStatement = Database.getDbConnection().prepareStatement(sqlString);
			resultSet = sqlStatement.executeQuery();
		} 
		catch (InterruptedException e) 
		{
			throw new InterruptedException(e.toString());
		} 
		catch (SQLException e) 
		{
			throw new SQLException(e.toString()); 
		}
		finally
		{
			Database.unlockDB();
		}
		
		Integer totalColumns = resultSet.getMetaData().getColumnCount();
		while(resultSet.next())
		{
			startRowRead();
			for(Integer columnIndex=1;columnIndex <= totalColumns;columnIndex++)
			{
				nextColumn(resultSet.getMetaData().getColumnName(columnIndex),
								resultSet.getObject(columnIndex));
			}
			endRowRead();
		}
		
		endReport();
	}
	
	protected abstract void startReport() throws InterruptedException, SQLException, IOException; 
	protected abstract void endReport() throws InterruptedException, SQLException, IOException, Exception; 
	protected abstract void startRowRead();
	protected abstract void endRowRead();
	protected abstract void nextColumn(String columnName, Object columnValue);
}
