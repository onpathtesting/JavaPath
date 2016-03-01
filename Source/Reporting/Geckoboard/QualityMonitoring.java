package Reporting.Geckoboard;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import Framework.Core.Helper;
import Reporting.Geckoboard.Base.GeckoboardBase;
import nl.pvanassen.geckoboard.api.widget.Monitoring;

public class QualityMonitoring extends GeckoboardBase {
	private Monitoring _qualityMonitoring;
	
	public QualityMonitoring(){
		super("QualityMonitoring");
	}
	
	@Override
	public void startReport() throws IOException, InterruptedException, SQLException
	{
		_qualityMonitoring = new Monitoring(Helper.get("GeckoQualityMonitoring"));
	}
	
	@Override
	protected void endReport() throws InterruptedException, SQLException, IOException {
		Push(_qualityMonitoring);
	}

	@Override
	protected void startRowRead() {
	}

	@Override
	protected void endRowRead() {
	}

	@Override
	protected void nextColumn(String columnName, Object columnValue) {
		if(columnName.equals("Value"))
		{
			_qualityMonitoring.setStatus((Integer.parseInt(columnValue.toString())<0?"Down":"Up"));
		}
		if(columnName.equals("RunOn") && columnValue != null)
		{
			try {
				DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
				java.util.Date date;
				date = format.parse(columnValue.toString());
				DateFormat outputFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
				String displayDate = outputFormat.format(date);
				_qualityMonitoring.setDownTime(displayDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
