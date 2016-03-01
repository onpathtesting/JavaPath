package Reporting.Spreadsheet;

import Framework.Core.Helper;
import Reporting.Spreadsheet.Base.GoogleSheetBase;

public class FailureReport extends GoogleSheetBase{

	public FailureReport() {
		super("FailureReport", Helper.get("GoogleSpreadSheetName") , "FailureReport", true);
	}
}
