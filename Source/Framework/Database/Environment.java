package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Environment extends TableBase{
	private final static String TABLE_NAME = "Environment";
	private final static String PK_FIELD_NAME = "EnvironmentId";
	
	
	private Environment()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Environment(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
