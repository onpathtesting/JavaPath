package Reporting.Spreadsheet;

import java.util.ArrayList;
import java.util.List;

import Framework.Core.Helper;
import Reporting.Spreadsheet.Base.GoogleSheetBase;

public class GoogleSheetManager {
	private static List<Class<? extends GoogleSheetBase>> _googleSheets = new ArrayList<Class<? extends GoogleSheetBase>>(); 

	static
	{
		_googleSheets.add(BrowserTrend.class);
		_googleSheets.add(FailureReport.class);
	}
	
	public static void update() throws Exception
	{
		Helper.getLogger().fine("-------------------- Updating Google Sheet -------------------");
		for(Class<? extends GoogleSheetBase> googleSheet : _googleSheets)
		{
			GoogleSheetBase googleSheetObject = googleSheet.getConstructor().newInstance();
			googleSheetObject.pushToGoogleSpreadSheet();
		}
		Helper.getLogger().fine("-------------------- Updating Complete -------------------");
	}	
}
