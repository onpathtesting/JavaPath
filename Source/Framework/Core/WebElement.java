package Framework.Core;

import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.core.filter.Filter;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;

public class WebElement extends FluentWebElement{

	public WebElement(org.openqa.selenium.WebElement webElement) {
		super(webElement);
		// TODO Auto-generated constructor stub
	}
	public WebElement(FluentWebElement fluentWebElement)
	{
		super(fluentWebElement.getElement());
	}
	
	public WebElement safeFindFirst(String name, final Filter... filters)
	{
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
		{
			Helper.getLogger().fine("WebElement safeFindFirst() did not find " + name);
		}

		return (fluentWebElement==null) ? null : new WebElement(fluentWebElement);
	}
	
	public WebElement safeFind(String name, Integer number, Filter... filters) 
	{
		int count = 0;
		Boolean elementFound = false;
		FluentWebElement fluentWebElement = null;
		
		while (count < 4 && !elementFound){
		    try {
		    	fluentWebElement = super.find(name, number, filters);
		    	elementFound = true;
		     } 
		    catch (StaleElementReferenceException e){
		       count = count+1;
		     }     
		}		
		if(!elementFound)
			Helper.getLogger().fine("WebElement safeFind() did not find " + name);
		
		return (fluentWebElement==null) ? null : new WebElement(fluentWebElement);
    }
	
	public WebElement parent()
	{
		return new WebElement(super.getElement().findElement(By.xpath("..")));
	}	
}
