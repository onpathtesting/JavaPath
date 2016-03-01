package Framework.Database;

import Framework.Core.Log;
import Framework.Database.Base.TableBase;

public class Meta extends TableBase{
	private final static String TABLE_NAME = "Meta";
	private final static String PK_FIELD_NAME = "MetaId";
	
	
	private Meta()
	{ 
		super(null, TABLE_NAME, PK_FIELD_NAME);
	}
	
	public Meta(Log logger)
	{
		super(logger, TABLE_NAME, PK_FIELD_NAME);
	}
}