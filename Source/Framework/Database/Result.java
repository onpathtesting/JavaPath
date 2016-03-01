package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Result extends TableBase{
	private final static String TABLE_NAME = "Result";
	private final static String PK_FIELD_NAME = "ResultId";
	
	
	private Result()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Result(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
