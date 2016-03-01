package Framework.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Semaphore;

import Framework.Core.Log;
import Framework.Core.Helper;

public class Database {
	@SuppressWarnings("unused")
	private static Log _logger;
	private static Connection _dbConnection;
	private static Semaphore _dbMutex;
	
	static
	{
		_dbMutex = new Semaphore(1);
		_logger = Helper.getLogger();
		setupConnection();
		purgeOlderRuns();
	}
	
	public static void setLogger(Log logger)
	{
		_logger = logger;
	}
	
	public static Connection getDbConnection()
	{
		return _dbConnection;
	}
	
	public  static void close() throws SQLException
	{
		_dbConnection.close();
	}

	public static void lockDB() throws InterruptedException
	{
		_dbMutex.acquire();
	}

	public static void unlockDB() throws InterruptedException
	{
		_dbMutex.release();
	}
	
	private static void setupConnection()
	{
		    try 
		    {
		    	Class.forName("org.sqlite.JDBC");
		    	_dbConnection = DriverManager.getConnection("jdbc:sqlite:"+ Helper.get("OutputFolder").toString() + "/QaAutomation.sqlite");
	
		    }
		    catch ( Exception e ) 
		    {
		      // TO DO
		    }
	}
	
	private static void purgeOlderRuns()
	{
		String limitRunsToLast = Helper.get("limitRunsToLast");
	    Statement sqlStatement = null;
	    String sql;
	    
	    try 
	    {
	    	@SuppressWarnings("resource")
			BufferedReader bufferReader = new BufferedReader(new FileReader(Helper.get("ConfigFolder").toString() + "/purge.run")); 
	    	sqlStatement = _dbConnection.createStatement();
      
	    	for(; (sql = bufferReader.readLine()) != null; ) 
	    	{
	    		sql = sql.replace("LIMIT", "LIMIT " + limitRunsToLast);
	    		sqlStatement.executeUpdate(sql);
	    	}
	    	sqlStatement.close();
	    }
	    catch ( Exception e ) 
	    {
	      // TO DO
	    }		
	}

}