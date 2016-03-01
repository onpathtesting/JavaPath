package Framework.Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.gson.stream.JsonReader;

import Framework.Database.Environment;
import Framework.Database.Run;
import Framework.Database.RunEnvironment;

public class Helper {
	private static Properties _propObject;
	private static Log _logger;
	
	static
	{
		_propObject = new Properties();
		try {
			_propObject.load(new FileInputStream("config/sos.properties"));
			_logger = new Log("Main");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static WebDriver createWebDriver(String operatingSystem,String  browserName,String  browserVersion,
			String  OSVersion,String  screenResolution, String storyTitle, Long pageLoadTimeOut, Long scriptLoadTimeOut, Long stepTimeOut)
	{
		WebDriver webDriver = null;
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		
		desiredCapabilities.setCapability("browser", browserName);
		desiredCapabilities.setCapability("browser_version", browserVersion);
		desiredCapabilities.setCapability("os", operatingSystem);
		desiredCapabilities.setCapability("os_version", OSVersion); 
		desiredCapabilities.setCapability("resolution", screenResolution);
		desiredCapabilities.setCapability("project", Helper.get("Project"));
		desiredCapabilities.setCapability("build", Helper.get("BuildName"));
		desiredCapabilities.setCapability("name", storyTitle);
		
		if(Helper.get("ServerLocation").equals("Local"))
		{
			if(browserName.equals("FireFox"))
			{
				webDriver = new FirefoxDriver();
			}
		}
		else if(Helper.get("ServerLocation").equals("BrowserStack"))
		{
			String bsUrl = "http://" + Helper.get("BSUserName") + ":" + Helper.get("BSAccessKey") + "@hub.browserstack.com/wd/hub";
			desiredCapabilities.setCapability("browserstack.debug", "true");

			try {
				webDriver = new RemoteWebDriver(new URL(bsUrl), desiredCapabilities);
			} catch (Exception e) {
				// TODO Code improvements need to be done here.
				e.printStackTrace();
			}
		}
		
		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(pageLoadTimeOut, TimeUnit.SECONDS);
		if(!browserName.equals("Safari"))
			webDriver.manage().timeouts().pageLoadTimeout(scriptLoadTimeOut, TimeUnit.SECONDS);
		webDriver.manage().timeouts().setScriptTimeout(stepTimeOut, TimeUnit.SECONDS);
		
		return webDriver;
	}
	
	public static Log getLogger()
	{
		return _logger;
	}
	
	
	public static String get(String propName)
	{
		return _propObject.getProperty(propName);
	}
	
	public static List<String> getMetaFilters()
	{
		List<String> metaFilters;
		
		metaFilters = Arrays.asList((get("Priority")+"," +get("Suite")).split(","));
		
		return metaFilters;
	}
	
	private static Boolean _testToggle = false;
	public static Boolean testToggle()
	{
		return(_testToggle = !_testToggle);
	}
	
	public static Integer getRunId()
	{
		Integer runId = 0;
		Run run = new Run(_logger);
		
		run.set("ServerLocation", Helper.get("ServerLocation"));
		run.set("BaseUrl", Helper.get("BaseUrl"));
		run.set("SuiteFilter", Helper.get("Suite"));
		run.set("PriorityFilter", Helper.get("Priority"));
		runId = run.AddUnique();
		
		return runId;
	}
	
	public static Integer getEnvironmentId(String browser, String browserVersion, String operatingSystem, String operatingSystemVersion)
	{
		Integer environmentId = 0;
		Environment environment = new Environment(_logger);
		
		environment.set("Browser", browser);
		environment.set("BrowserVersion", browserVersion);
		environment.set("OperatingSystem", operatingSystem);
		environment.set("OperatingSystemVersion", operatingSystemVersion);
		environmentId = environment.AddUnique();
		
		return environmentId;
	}
	
	public static void addRunEnvironmentInfo(Integer runId, Integer environmentId)
	{
		RunEnvironment runEnvironment = new RunEnvironment(_logger);
		
		runEnvironment.set("RunId", runId);
		runEnvironment.set("EnvironmentId", environmentId);
		runEnvironment.AddUnique();
	}
	
	private static InputStreamReader getBSPlanJson() throws IOException
	{
        String stringUrl = "https://www.browserstack.com/automate/plan.json";
        URL url = new URL(stringUrl);
        URLConnection uc = url.openConnection();

        uc.setRequestProperty("X-Requested-With", "Curl");

        String userpass = Helper.get("BSUserName").toString() + ":" +  Helper.get("BSAccessKey").toString();
        String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
        uc.setRequestProperty("Authorization", basicAuth);

        InputStreamReader inputStreamReader = new InputStreamReader(uc.getInputStream(),"UTF-8");
        return inputStreamReader;
	}
	
	public static Integer getMaxThreadCount() throws IOException
	{
		JsonReader reader = new JsonReader(getBSPlanJson());
		Integer allowedSessions = 0;
		Integer currentSessions = 0;
		
		reader.beginObject();

		while (reader.hasNext()) {

		  String name = reader.nextName();

		  if (name.equals("parallel_sessions_max_allowed")) {
						
			  allowedSessions = reader.nextInt();
						
		  } else if (name.equals("parallel_sessions_running")) {
						
			  currentSessions = reader.nextInt();
		  } else {
			reader.skipValue(); //avoid some unhandle events
		  }
        }

		reader.endObject();
		reader.close();

		_logger.fine("Max parallel sessions allowed :" + allowedSessions);
		_logger.fine("Parallel sessions running :" + currentSessions);
		allowedSessions = allowedSessions - currentSessions;
		allowedSessions = Integer.parseInt(Helper.get("MaxThreads").toString()) > allowedSessions ? allowedSessions : 
											Integer.parseInt(Helper.get("MaxThreads").toString());

		return allowedSessions;
	}	
}