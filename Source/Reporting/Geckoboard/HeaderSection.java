package Reporting.Geckoboard;

import java.io.IOException;
import java.sql.SQLException;

import Framework.Core.Helper;
import Reporting.Geckoboard.Base.GeckoboardBase;
import nl.pvanassen.geckoboard.api.json.text.TextItemType;
import nl.pvanassen.geckoboard.api.widget.Text;

public class HeaderSection  extends GeckoboardBase {
	private Text _textHeader;
	
	public HeaderSection() {
		super("HeaderSection");
	}

	@Override
	protected void startReport() throws InterruptedException, SQLException, IOException 
	{
		_textHeader = new Text(Helper.get("GeckoHeaderTextKey"));
		_textHeader.addText(Helper.get("BaseUrl"),TextItemType.NONE);
		_textHeader.addText(Helper.get("Language"),TextItemType.NONE);
		_textHeader.addText(Helper.get("Suite"),TextItemType.NONE);
		_textHeader.addText(Helper.get("Priority"),TextItemType.NONE);
	}

	@Override
	protected void endReport() throws InterruptedException, SQLException, IOException, Exception {
		Push(_textHeader);
	}

	@Override
	protected void startRowRead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void endRowRead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void nextColumn(String columnName, Object columnValue) {
		// TODO Auto-generated method stub
		
	}

}
