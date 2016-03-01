package Framework.Core.Base;

import de.codecentric.jbehave.junit.monitoring.JUnitReportingRunner;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import Framework.Core.Log;
import Framework.Core.Helper;
import Framework.Core.StoryReporterBuilder;
import Framework.Core.StoryResolver;
import Framework.Core.ParameterConverter;
import Framework.Core.InjectorStoryParser;
import Framework.Core.ScenarioInjector;

@RunWith(JUnitReportingRunner.class)
public abstract class StoryBase extends JUnitStory {
 
	private StoryStepsBase _stepsObject;
	private InjectorStoryParser plusStoryParser;
	private Log _logger;
	
	protected Log getLogger()
	{
		return _logger;
	}
	
	protected StoryBase(StoryStepsBase stepsObject)
	{
		_stepsObject = stepsObject;
		configuredEmbedder().useMetaFilters(Helper.getMetaFilters());
		configuredEmbedder().embedderControls().useStoryTimeouts(Helper.get("StoryTimeOutInSeconds"));
		
		plusStoryParser = new InjectorStoryParser(new Framework.Core.StoryTable());
		plusStoryParser.registerKeywordInjector("@InjectSteps", new ScenarioInjector());
		configuredEmbedder().configuration().useStoryParser(plusStoryParser);
	}
	
    @Override 
    public Configuration configuration() {
    	MostUsefulConfiguration configuration = new MostUsefulConfiguration();
    	configuration.useStoryLoader(new LoadFromClasspath(this.getClass()));
    	org.jbehave.core.steps.ParameterConverters paramCoverters = new ParameterConverters();
    	paramCoverters.addConverters(new ParameterConverter());
    	configuration.useParameterConverters(paramCoverters);

    	return configuration;
    }
 
    @Override public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(),
                _stepsObject);
    }
    
	public void initialize(WebDriver webDriver, Log logger, StoryReporterBuilder storyReporterBuilder, StoryResolver storyResolver)
	{
		_logger = logger;
		super.configuration().useStoryReporterBuilder(storyReporterBuilder);
		super.configuration().useStoryPathResolver(storyResolver);
		webDriver.manage().deleteAllCookies();
		_stepsObject.initialize(webDriver, logger);
	}
}