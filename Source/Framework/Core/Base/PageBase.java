package Framework.Core.Base;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.fluentlenium.core.Fluent;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.filter.Filter;

import Framework.Core.Log;
import Framework.Core.WebElement;
import Framework.Database.Database;
import Framework.Core.Helper;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

public abstract class PageBase extends FluentPage {
protected Log _logger;
private String _pageName;
private Integer _screenShotCounter;

protected Log getLogger()
{
	return _logger;
}

public PageBase(String pageName)
{
	_pageName = pageName;
	_screenShotCounter = 1;
}

public void initialize(WebDriver webDriver, Log logger)
{
	_logger = logger;
	super.initFluent(webDriver);
}

public Fluent takeScreenShot(String suffix)
{
	if(Helper.get("ServerLocation").equals("Local"))
		return null;
	
	Fluent fluentObject = this;
	
	File filePath = new File(Helper.get("OutputFolder") + "/ScreenShots");
	filePath.mkdirs();

	String fileFullPath = Helper.get("OutputFolder") + "/ScreenShots/"+getLogger().getLogName() + "-" + _pageName + "-" + suffix +  _screenShotCounter.toString() + ".png";
	_screenShotCounter++;
	if(Helper.get("ServerLocation").equals("BrowserStack"))
	{
		WebDriver browserStackDriver = new Augmenter().augment(getDriver());
		File scrFile = ((TakesScreenshot) browserStackDriver).getScreenshotAs(OutputType.FILE);
		try {
			Database.lockDB();
			FileUtils.copyFile(scrFile, new File(fileFullPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		finally
		{
			try {
				Database.unlockDB();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}
	else
	{
		fluentObject = takeScreenShot(fileFullPath);
	}
	return fluentObject;
}

public void quitWebDriver()
{
//	super.getDriver().quit();
}
public Boolean goToUrl(String webUrl)
{
	Boolean isPageLoaded = true;

	try {
//		getLogger().fine("Actual url - " + Parameters.get("BaseUrl") + webUrl);
		goTo(Helper.get("BaseUrl") + webUrl);
//		await().atMost(1).until(".small").withText().startsWith("qaqaqaqa").isPresent();
	} catch (Exception e) {
		isPageLoaded = true;
//		getLogger().fine("Loaded url - " + Parameters.get("BaseUrl") + webUrl);
	}
	
	return isPageLoaded;
}

public void scrollUpToShow(FluentWebElement webElement, int offsetPixels)
{
//	getLogger().fine("Scrolling up to show the element [" + webElement.getName() + "]");
	executeScript("window.scrollTo(0," + (webElement.getElement().getLocation().getY() + webElement.getElement().getSize().height - offsetPixels) + ")");
}

public void scrollDownToHide(FluentWebElement webElement, int offsetPixels)
{
//	getLogger().fine("Scrolling down to hide the element [" + webElement.getText() + "]");
	executeScript("window.scrollTo(0," + (webElement.getElement().getLocation().getY() + webElement.getSize().height + offsetPixels) + ")");
//	getLogger().fine("Scrolling complete");
}

public WebElement safeFindFirst(String name, final Filter... filters) {
	int count = 0;
	Boolean elementFound = false;
	FluentWebElement fluentWebElement = null;
	
	while (count < 4 && !elementFound){
	    try {
	    	fluentWebElement = super.findFirst(name, filters);
	    	elementFound = true;
	     } 
	    catch (StaleElementReferenceException e){
	       count = count+1;
	     }     
	}		
	if(!elementFound)
		Helper.getLogger().fine("PageBase safeFindFirst() did not find " + name);

	return (fluentWebElement==null) ? null : new WebElement(fluentWebElement);
}
}