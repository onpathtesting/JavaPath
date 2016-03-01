package Main;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Framework.Core.Helper;
import Framework.Core.StoryThread;
import Framework.Database.DatabaseMigrator;
import Reporting.Geckoboard.DashboardManager;
import Reporting.Spreadsheet.GoogleSheetManager;


public class Main {

	
	public static void main(String[] args) throws Exception 
	{
		DatabaseMigrator databaseMigrator = new DatabaseMigrator();
		databaseMigrator.doMigration();
		 
		Integer runId = Helper.getRunId();
		String runCounter = runId.toString();
		Integer allowedMaxThreads = Helper.getMaxThreadCount();

		if(allowedMaxThreads == 0)
		{
			Helper.getLogger().fine("Exiting application, no threads available in browser stack!");
			// TODO Return value to be decided based on Jenkins integration
			return;
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(allowedMaxThreads);
		
		for(String currentEnvironment : Helper.get("Environments").toString().split(","))
		{
			Properties propObject = new Properties();
			Integer environmentId = 0;
			try {
				propObject.load(new FileInputStream(Helper.get("ConfigFolder") + "/Environment-" + currentEnvironment +".properties"));
				
				environmentId= Helper.getEnvironmentId(propObject.get("BrowserName").toString(), 
						propObject.get("BrowserVersion").toString(),
						propObject.get("OperatingSystem").toString(), 
						propObject.get("OSVersion").toString());
				
				Helper.addRunEnvironmentInfo(runId, environmentId);
				
		        Runnable storyThread = new StoryThread(runId, environmentId, 
		        		runCounter + "-" + propObject.get("LogName").toString(), 
		        		propObject);
		        executorService.execute(storyThread);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		Helper.getLogger().fine("Executor service will be told to shutdown");
		executorService.shutdown();
		Helper.getLogger().fine("Executor service told to shutdown, awaiting time out");
		executorService.awaitTermination(60, TimeUnit.MINUTES);
		Helper.getLogger().fine("Executor service timed out");
		
		DashboardManager.update();
		GoogleSheetManager.update();
	}
}
