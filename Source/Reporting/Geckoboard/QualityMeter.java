package Reporting.Geckoboard;

import java.io.IOException;
import java.sql.SQLException;

import Framework.Core.Helper;
import Reporting.Geckoboard.Base.GeckoboardBase;
import nl.pvanassen.geckoboard.api.json.common.GraphType;
import nl.pvanassen.geckoboard.api.widget.GeckOMeter;

public class QualityMeter extends GeckoboardBase {
	private GeckOMeter _qualityMeter;
	
	public QualityMeter(){
		super("QualityMeter");
	}
	
	@Override
	public void startReport() throws IOException, InterruptedException, SQLException
	{
		_qualityMeter = new GeckOMeter(Helper.get("GeckoQualityMeter"), GraphType.STANDARD);
		_qualityMeter.setMin("Poor", "0");
		_qualityMeter.setMax("Excellent", "100");
	}

	@Override
	protected void endReport() throws InterruptedException, SQLException, IOException {
		Push(_qualityMeter);
	}

	@Override
	protected void startRowRead() {
	}

	@Override
	protected void nextColumn(String columnName, Object columnValue) {
		if(columnName.equals("Value"))
		{
			_qualityMeter.setCurrent(columnValue.toString());
		}
	}

	@Override
	protected void endRowRead() {
	}
}
