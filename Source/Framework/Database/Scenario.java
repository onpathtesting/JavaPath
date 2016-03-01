package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Scenario extends TableBase{
	private final static String TABLE_NAME = "Scenario";
	private final static String PK_FIELD_NAME = "ScenarioId";
	
	
	private Scenario()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Scenario(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
