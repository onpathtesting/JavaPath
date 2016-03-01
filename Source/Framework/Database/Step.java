package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Step extends TableBase{
	private final static String TABLE_NAME = "Step";
	private final static String PK_FIELD_NAME = "StepId";
	
	
	private Step()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Step(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}