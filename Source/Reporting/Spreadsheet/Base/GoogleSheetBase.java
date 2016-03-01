package Reporting.Spreadsheet.Base;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

import Framework.Core.Helper;
import Framework.Database.Base.ViewBase;

public class GoogleSheetBase extends ViewBase {
	private final String SPREADSHEET_SERVICE_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
	private final String _spreadSheetName;
	private final String _workSheetName;
	private List<ListEntry> _rowEntries;
	private ListEntry _currentEntry;
	private Boolean _purgeOldData;

	public GoogleSheetBase(String viewName, String spreadSheetName, String workSheetName, Boolean purgeOldData) {
		super(viewName);
		_spreadSheetName = spreadSheetName;
		_workSheetName = workSheetName;
		_purgeOldData = purgeOldData;
	}
	
	public void pushToGoogleSpreadSheet() throws Exception
	{
		super.readView();
	}
	
	@Override
	protected void startReport() throws InterruptedException, SQLException, IOException {
		_rowEntries = new ArrayList<ListEntry>();
		
	}

	@Override
	protected void endReport() throws Exception {
		SpreadsheetService spreadSheetService = new SpreadsheetService("SoS");
		Credential oauth2Credential = getCredentials();
		spreadSheetService.setOAuth2Credentials(oauth2Credential);
		
		WorksheetEntry workSheet  = getWorkSheet(spreadSheetService, _spreadSheetName, _workSheetName);
		URL sheetFeedUrl = workSheet.getListFeedUrl();
		
		if(_purgeOldData)
		{
			//TODO : delete older entries
		}
		
		for(ListEntry listEntry : _rowEntries)
		{
			spreadSheetService.insert(sheetFeedUrl, listEntry);
		}
	}

	@Override
	protected void startRowRead() {
		_currentEntry = new ListEntry();
	}

	@Override
	protected void endRowRead() {
		_rowEntries.add(_currentEntry);
	}

	@Override
	protected void nextColumn(String columnName, Object columnValue) {
		_currentEntry.getCustomElements().setValueLocal(columnName, columnValue.toString());
	}
	
	private Credential getCredentials() throws IOException 
	{
		    HttpTransport transport = new NetHttpTransport();
		    JacksonFactory jsonFactory = new JacksonFactory();
		    return new GoogleCredential.Builder().setClientSecrets(Helper.get("GoogleClientId"), Helper.get("GoogleClientSecret"))
		        .setJsonFactory(jsonFactory).setTransport(transport).build()
		     .setAccessToken(Helper.get("GoogleAccessToken")).setRefreshToken(Helper.get("GoogleRefreshToken"));
	}

	private WorksheetEntry getWorkSheet(SpreadsheetService spreadSheetService, String sheetName, String workSheetName) throws Exception 
	{
        SpreadsheetEntry spreadsheet = getSpreadSheet(spreadSheetService, sheetName);

        if (spreadsheet != null) 
        {
            WorksheetFeed worksheetFeed = spreadSheetService.getFeed(spreadsheet.getWorksheetFeedUrl(), 
            													WorksheetFeed.class);
            List<WorksheetEntry> worksheets = worksheetFeed.getEntries();

            for (WorksheetEntry worksheetEntry : worksheets) 
            {
                 String wktName = worksheetEntry.getTitle().getPlainText();
                 if (wktName.equals(workSheetName)) 
                 {
                     return worksheetEntry;
                 }
             }
         }
	    return null;
	}	
	
	private SpreadsheetEntry getSpreadSheet(SpreadsheetService spreadSheetService, String spreadSheetName) throws Exception
	{
		SpreadsheetQuery spreadsheetQuery = new SpreadsheetQuery(new URL(SPREADSHEET_SERVICE_URL));
		
		spreadsheetQuery.setTitleQuery(spreadSheetName);
        spreadsheetQuery.setTitleExact(true);
        
        SpreadsheetFeed spreadsheet;
		spreadsheet = spreadSheetService.getFeed(spreadsheetQuery,SpreadsheetFeed.class);
        if (spreadsheet.getEntries() != null && spreadsheet.getEntries().size() == 1) 
        {
        	return spreadsheet.getEntries().get(0);
        }
        else 
        {
        	return null;
        }
	}	
}
