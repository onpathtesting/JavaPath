package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Run extends TableBase{
	private final static String TABLE_NAME = "Run";
	private final static String PK_FIELD_NAME = "RunId";
	
	
	private Run()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Run(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
