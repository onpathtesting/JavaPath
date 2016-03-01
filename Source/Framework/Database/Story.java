package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Story extends TableBase{
	private final static String TABLE_NAME = "Story";
	private final static String PK_FIELD_NAME = "StoryId";
	
	
	private Story()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Story(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}
