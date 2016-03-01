package Reporting.Spreadsheet;

import Framework.Core.Helper;
import Reporting.Spreadsheet.Base.GoogleSheetBase;

public class BrowserTrend extends GoogleSheetBase{

	public BrowserTrend() {
		super("BrowserTrend", Helper.get("GoogleSpreadSheetName") , "BrowserTrend", true);
	}
}
