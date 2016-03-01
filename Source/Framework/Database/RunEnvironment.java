package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class RunEnvironment extends TableBase{
	private final static String TABLE_NAME = "RunEnvironment";
	private final static String PK_FIELD_NAME = "RunEnvironmentId";
	
	
	private RunEnvironment()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public RunEnvironment(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
