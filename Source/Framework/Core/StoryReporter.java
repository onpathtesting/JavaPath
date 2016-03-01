package Framework.Core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.GivenStories;
import org.jbehave.core.model.Lifecycle;
import org.jbehave.core.model.Meta;
import org.jbehave.core.model.Narrative;
import org.jbehave.core.model.OutcomesTable;
import org.jbehave.core.model.Scenario;
import org.jbehave.core.model.Story;
import org.jbehave.core.model.StoryDuration;

public class StoryReporter implements org.jbehave.core.reporters.StoryReporter {
	
private final static Integer SUCCESS = 2;
private final static Integer FAILED = 5;
private final static Integer PENDING = 3;
private final static Integer IGNORED = 1;
private final static Integer NOTPERFORMED = 4;

private Boolean _isValidStory;
private Log _logger;
private Integer _runId;
private Integer _environmentId;
private Integer _interStepDelay;

private Framework.Database.Story _tableStory;
private Framework.Database.Scenario _tableScenario;
private Framework.Database.Step _tableStep;
private Framework.Database.Meta _tableMeta;
private Framework.Database.RunEnvironmentStoryScenarioMetaStep _tableRunEnvironmentMetaStep;

private Integer _storyId;
private Integer _scenarioId;
private Integer _stepId;
private Integer _metaId;
private Boolean _givenStory;
private static Semaphore _reporterMutex;

public StoryReporter(Log logger, Integer runId, 
		Integer environmentId, Integer interStepDelay)
{
	_logger = logger;
	_runId = runId;
	_environmentId = environmentId;
	_interStepDelay = interStepDelay;
	_reporterMutex = new Semaphore(1);
	
	_tableStory = new Framework.Database.Story(_logger);
	_tableScenario = new Framework.Database.Scenario(_logger);
	_tableStep = new Framework.Database.Step(_logger);
	_tableMeta = new Framework.Database.Meta(_logger);
	_tableRunEnvironmentMetaStep = new Framework.Database.RunEnvironmentStoryScenarioMetaStep(_logger);
}

protected Log getLogger()
{
	return _logger;
}

public void beforeStory(Story story, boolean givenStory) {
	if(story.getName().equals("BeforeStories") || story.getName().equals("AfterStories"))
	{
		_isValidStory = false;
		return;
	}
	

	
	_isValidStory= true;
	_givenStory = givenStory;
	String storyTitle = story.getDescription().asString();
	
	_tableStory.set("Description", storyTitle);
	_tableStory.set("Narrative", story.getNarrative().toString());
	_storyId  = _tableStory.AddUnique();
	
	if(!_givenStory)
	{
		try {
			_reporterMutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		getLogger().fine("Story: " + storyTitle);
	}
}

public void beforeScenario(String scenarioTitle) {
	_tableScenario.set("StoryId", _storyId);
	_tableScenario.set("Description", scenarioTitle);
	_scenarioId = _tableScenario.AddUnique();
	
	getLogger().fine("\tScenario:" + scenarioTitle);
}


public void beforeStep(String step) 
{
	try {
		Thread.sleep(_interStepDelay);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	_tableStep.set("ScenarioId", _scenarioId);
	_tableStep.set("Description", step);
	_stepId = _tableStep.AddUnique();
}

public void storyNotAllowed(Story story, String filter) {
	getLogger().fine("Story: " + story.getDescription() + " : SKIPPED");
	getLogger().fine("\t Filtered by : " + filter);
}

public void storyCancelled(Story story, StoryDuration storyDuration) {
	getLogger().fine("Story: CANCELLED");
	getLogger().fine("------------------------------------------------------------------------------------------------------");
}

public void afterStory(boolean givenOrRestartingStory) {
	if(!_isValidStory || givenOrRestartingStory)
		return;
	_reporterMutex.release();	
}

public void narrative(Narrative narrative) {
	if(!_givenStory)
	{
		getLogger().fine("\tIn order to " + narrative.inOrderTo());
		getLogger().fine("\tAs a " + narrative.asA());
		getLogger().fine("\tI want to " + narrative.iWantTo());
		getLogger().fine("------------------------------------------------------------------------------------------------------");	}
}

public void lifecyle(Lifecycle lifecycle) {
}

public void scenarioNotAllowed(Scenario scenario, String filter) {
	getLogger().fine("<= SCENARIO NOT ALLOWED => ");
	getLogger().fine("\t" + scenario.getTitle());
	getLogger().fine("\t Filter:");
	getLogger().fine("\t " + filter);
}

public void scenarioMeta(Meta meta) {
	getLogger().fine("\tMeta:" + meta.toString());
}

public void afterScenario() {
	getLogger().fine("------------------------------------------------------------------------------------------------------");}

public void givenStories(GivenStories givenStories) {
}

public void givenStories(List<String> storyPaths) {
}

public void beforeExamples(List<String> steps, ExamplesTable table) {
}

public void example(Map<String, String> tableRow) {
	String metaTitle = tableRow.toString();

	_tableMeta.set("ScenarioId", _scenarioId);
	_tableMeta.set("Description", metaTitle);
	_metaId = _tableMeta.AddUnique();
	
	getLogger().fine("\t" + metaTitle);
}

public void afterExamples() {
}

public void successful(String step) {
	_tableRunEnvironmentMetaStep.set("RunId", _runId);
	_tableRunEnvironmentMetaStep.set("EnvironmentId", _environmentId);
	_tableRunEnvironmentMetaStep.set("StoryId", _storyId);
	_tableRunEnvironmentMetaStep.set("ScenarioId", _scenarioId);
	_tableRunEnvironmentMetaStep.set("MetaId", _metaId);
	_tableRunEnvironmentMetaStep.set("StepId", _stepId);
	_tableRunEnvironmentMetaStep.set("ResultId", SUCCESS);
	_tableRunEnvironmentMetaStep.AddUnique();
	
	getLogger().fine("\t" + step.toString() + " : SUCCESS" );
}

public void ignorable(String step) {
	_tableRunEnvironmentMetaStep.set("RunId", _runId);
	_tableRunEnvironmentMetaStep.set("EnvironmentId", _environmentId);
	_tableRunEnvironmentMetaStep.set("StoryId", _storyId);
	_tableRunEnvironmentMetaStep.set("ScenarioId", _scenarioId);
	_tableRunEnvironmentMetaStep.set("MetaId", _metaId);
	_tableRunEnvironmentMetaStep.set("StepId", _stepId);
	_tableRunEnvironmentMetaStep.set("ResultId", IGNORED);
	_tableRunEnvironmentMetaStep.AddUnique();
	
	getLogger().fine("\t" + step.toString() + " : IGNORED" );
}

public void pending(String step) {
	_tableRunEnvironmentMetaStep.set("RunId", _runId);
	_tableRunEnvironmentMetaStep.set("EnvironmentId", _environmentId);
	_tableRunEnvironmentMetaStep.set("StoryId", _storyId);
	_tableRunEnvironmentMetaStep.set("ScenarioId", _scenarioId);
	_tableRunEnvironmentMetaStep.set("MetaId", _metaId);
	_tableRunEnvironmentMetaStep.set("StepId", _stepId);
	_tableRunEnvironmentMetaStep.set("ResultId", PENDING);
	_tableRunEnvironmentMetaStep.AddUnique();
	
	getLogger().fine("\t" + step.toString() + " : PENDING" );
}

public void notPerformed(String step) {
	_tableRunEnvironmentMetaStep.set("RunId", _runId);
	_tableRunEnvironmentMetaStep.set("EnvironmentId", _environmentId);
	_tableRunEnvironmentMetaStep.set("StoryId", _storyId);
	_tableRunEnvironmentMetaStep.set("ScenarioId", _scenarioId);
	_tableRunEnvironmentMetaStep.set("MetaId", _metaId);
	_tableRunEnvironmentMetaStep.set("StepId", _stepId);
	_tableRunEnvironmentMetaStep.set("ResultId", NOTPERFORMED);
	_tableRunEnvironmentMetaStep.AddUnique();
	
	getLogger().fine("\t" + step.toString() + " : NOT PERFORMED" );
}

public void failed(String step, Throwable cause) {
	_tableRunEnvironmentMetaStep.set("RunId", _runId);
	_tableRunEnvironmentMetaStep.set("EnvironmentId", _environmentId);
	_tableRunEnvironmentMetaStep.set("StoryId", _storyId);
	_tableRunEnvironmentMetaStep.set("ScenarioId", _scenarioId);
	_tableRunEnvironmentMetaStep.set("MetaId", _metaId);
	_tableRunEnvironmentMetaStep.set("StepId", _stepId);
	_tableRunEnvironmentMetaStep.set("ResultId", FAILED);
	_tableRunEnvironmentMetaStep.AddUnique();
	
	
	getLogger().fine("\t" + step.toString() + " : FAILED" );
}

public void failedOutcomes(String step, OutcomesTable table) {
	getLogger().fine("\t" + step.toString() + " : FAILED STEP OUTCOME" );
}

public void restarted(String step, Throwable cause) {
	getLogger().fine("\t" + step.toString() + " : RESTARTED STEP" );
}

public void restartedStory(Story story, Throwable cause) {
	getLogger().fine("\tRESTARTED STORY: " + story.getDescription());
}

public void dryRun() {
	getLogger().fine("<= DRY RUN => ");
}

public void pendingMethods(List<String> methods) {
}
}