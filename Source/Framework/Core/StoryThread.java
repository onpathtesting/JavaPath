package Framework.Core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import Framework.Core.Base.StoryBase;
import Stories.StoryManager;

public class StoryThread implements Runnable {
	private Log _log;
	Integer _runId;
	Integer _environmentId;
	private Properties _propObject;

	public StoryThread( Integer runId, Integer environmentId, String logName, Properties propObject) throws InstantiationException, IllegalAccessException, 
						SecurityException, IOException, IllegalArgumentException,
						InvocationTargetException, NoSuchMethodException
	{
		_log = new Log(logName);
		_runId = runId;
		_environmentId = environmentId;
		_propObject = propObject;
	}
	
	public void run() {
		StoryReporterBuilder storyReportBuilder = new StoryReporterBuilder(_log, _runId, _environmentId, Integer.parseInt(_propObject.get("InterStepDelay").toString()));
		StoryResolver storyResolver = new StoryResolver(); 
		_log.fine("-------------------- Environment Details ------------------------------------");
		_log.fine("Browser : " + _propObject.get("BrowserName").toString() + "(" + _propObject.get("BrowserVersion").toString()  +")");
		_log.fine("OS : " + _propObject.get("OperatingSystem").toString() + "(" + _propObject.get("OSVersion").toString() +")");
		_log.fine("BaseUrl : " + Helper.get("BaseUrl"));
		_log.fine("Language : " + Helper.get("Language"));
		_log.fine("Suite : " + Helper.get("Suite"));
		_log.fine("Priority : " + Helper.get("Priority"));
		_log.fine("-----------------------------------------------------------------------------");
		
		for (Class<? extends StoryBase> currentStory : StoryManager.getStories()) 
		{
			StoryBase storyBase = null;
			WebDriver webDriver = Helper.createWebDriver(_propObject.get("OperatingSystem").toString(), 
					_propObject.get("BrowserName").toString(), 
					_propObject.get("BrowserVersion").toString(), 
					_propObject.get("OSVersion").toString(),
					_propObject.get("ScreenResolution").toString(),
					storyResolver.getStoryTitle(currentStory),
					Long.parseLong(_propObject.get("PageLoadTimeOutInSeconds").toString()),
					Long.parseLong(_propObject.get("ScriptTimeOutInSeconds").toString()),
					Long.parseLong(_propObject.get("StepTimeOutInSeconds").toString()));
			try 
			{
				storyBase = currentStory.getConstructor()
						.newInstance();
				storyBase.initialize(webDriver, _log, storyReportBuilder, storyResolver);
				storyBase.run();
			} catch (Exception e) {
			} catch (Throwable e) {
			}
			finally
			{
				webDriver.quit();		
			}
		}
		
	}
}
