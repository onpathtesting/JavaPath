package Reporting.Geckoboard;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Framework.Core.Helper;
import Reporting.Geckoboard.Base.GeckoboardBase;
import nl.pvanassen.geckoboard.api.widget.HighChart;
import nl.pvanassen.highchart.api.ChartOptions;
import nl.pvanassen.highchart.api.Point;
import nl.pvanassen.highchart.api.Series;
import nl.pvanassen.highchart.api.shared.SeriesType;


public class OperatingSystemWiseChart extends GeckoboardBase {
	private HighChart _highChart;
	private Map<String, Series> _chartSeries;
	private Point _currentPoint;
	private Series _currentSeries;
	private List<String> _xLabels; 
	
    public OperatingSystemWiseChart() {
		super("OperatingSystemWise");
		_chartSeries = new HashMap<String, Series>();
		_xLabels = new ArrayList<String>();
	}

	@Override
	public void startReport() throws InterruptedException, SQLException, IOException
	{
        _highChart = new HighChart(Helper.get("GeckoOperatingSystemWiseWidgetKey"));
        ChartOptions chartOptions = _highChart.getChartOptions();
        chartOptions.getTitle().setText(" ");
        chartOptions.getChart().setDefaultSeriesType(SeriesType.COLUMN);
        chartOptions.getYAxis().setMax(100);
        chartOptions.getYAxis().getTitle().setText("Percentage");
  	}

	@Override
	protected void endReport() throws InterruptedException, SQLException, IOException {
		ChartOptions chartOptions = _highChart.getChartOptions();
		for(String label : _xLabels)
		{
			chartOptions.getXAxis().getCategories().add(label);
		}
		for(Entry<String, Series> series : _chartSeries.entrySet())
		{
			chartOptions.getSeries().pushElement(series.getValue());
		}
		
		Push(_highChart);
	}
	
	@Override
	protected void startRowRead() {
		_currentPoint = new Point();
	}

	@Override
	protected void endRowRead() {
		_currentSeries.getData().pushElement(_currentPoint);
	}

	@Override
	protected void nextColumn(String columnName, Object columnValue) {
		// IMPORTANT - The sequence of columns when creating database view determines the firing of this delegate.
		if(columnName.equals("Status"))
		{
			if(_chartSeries.get(columnValue) == null)
			{
				Series newSeries = new Series().setName(columnValue.toString());
				_chartSeries.put(columnValue.toString(), newSeries);
			}
			_currentSeries = _chartSeries.get(columnValue); 
		}
		else if(columnName.equals("OperatingSystem"))
		{
			if(!_xLabels.contains(columnValue.toString()))
			{
				_xLabels.add(columnValue.toString());
			}
			_currentPoint.setName(columnValue.toString());
		}
		else if(columnName.equals("Value"))
		{
			_currentPoint.setY(Double.parseDouble(columnValue.toString()));
		}
	}
}
