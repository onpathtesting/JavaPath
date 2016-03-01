package Reporting.Geckoboard;

import java.util.ArrayList;
import java.util.List;

import Framework.Core.Helper;
import Framework.Core.Geckoboard.TextItemTypeAdapter;
import Framework.Database.Base.ViewBase;
import nl.pvanassen.geckoboard.api.gson.SoSGsonFactory;
import nl.pvanassen.geckoboard.api.json.text.TextItemType;

public class DashboardManager {
	private static List<Class<? extends ViewBase>> _geckoWidgets = new ArrayList<Class<? extends ViewBase>>(); 
	
	static 
	{
		_geckoWidgets.add(BrowserTrendChart.class);
		_geckoWidgets.add(BrowserWiseChart.class);
		_geckoWidgets.add(OperatingSystemTrendChart.class);
		_geckoWidgets.add(OperatingSystemWiseChart.class);
		_geckoWidgets.add(QualityMeter.class);
		_geckoWidgets.add(QualityMonitoring.class);
		_geckoWidgets.add(HeaderSection.class);
	}
	
	public static void update() throws Exception
	{
		addCustomAdapters();
		Helper.getLogger().fine("-------------------- Updating Geckoboard -------------------");
		for(Class<? extends ViewBase> widgetClass : _geckoWidgets)
		{
			ViewBase widgetObject = widgetClass.getConstructor().newInstance();
			widgetObject.readView();
		}
		Helper.getLogger().fine("-------------------- Updating Complete -------------------");
	}
	
	public static void addCustomAdapters()
	{
		SoSGsonFactory.getGsonBuilder().registerTypeAdapter(TextItemType.class, new TextItemTypeAdapter());
	}

}
