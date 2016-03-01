package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class RunEnvironmentStoryScenarioMetaStep extends TableBase{
	private final static String TABLE_NAME = "RunEnvironmentStoryScenarioMetaStep";
	private final static String PK_FIELD_NAME = "RunEnvironmentStoryScenarioMetaStepId";
	
	
	private RunEnvironmentStoryScenarioMetaStep()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public RunEnvironmentStoryScenarioMetaStep(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
