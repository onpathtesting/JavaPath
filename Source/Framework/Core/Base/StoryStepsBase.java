package Framework.Core.Base;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.ScenarioType;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import Framework.Core.Log;

public abstract class StoryStepsBase extends Assertions {
	protected Log _logger;
	private List<PageBase> _pages;
	private WebDriver _webDriver;
	
	protected Log getLogger()
	{
		return _logger;
	}

	public StoryStepsBase()
	{
		_pages = new ArrayList<PageBase>();
	}

	@BeforeStory
    public void beforeStory() {
	}
	 
	@AfterStory
	public void afterStory() {
	}
	
	public void registerPage(PageBase pageBase)
	{
		_pages.add(pageBase);
	}
	
	public void initialize(WebDriver webDriver, Log logger)
	{
		_webDriver = webDriver;
		_logger  = logger;
		for(PageBase page : _pages)
		{
			page.initialize(webDriver, logger);
		}
	}
	
    @BeforeScenario(uponType=ScenarioType.EXAMPLE)
    public void beforeEachExampleScenario() {
		_webDriver.manage().deleteAllCookies();
    }
}
